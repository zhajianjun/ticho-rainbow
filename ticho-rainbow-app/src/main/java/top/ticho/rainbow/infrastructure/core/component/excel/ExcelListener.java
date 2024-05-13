package top.ticho.rainbow.infrastructure.core.component.excel;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import top.ticho.boot.json.util.JsonUtil;
import top.ticho.boot.web.util.valid.ValidUtil;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * excel读取监听
 *
 * @author zhajianjun
 * @date 2024-05-09 18:41
 */
@Slf4j
public class ExcelListener<M extends ExcelBaseImp> implements ReadListener<M> {

    /** 执行id */
    private final String id = IdUtil.fastSimpleUUID();

    /** 批量数量 */
    private int batchSize;

    /** 校验错误数 */
    @Getter
    private long error = 0;

    /** 读取数量 */
    @Getter
    private long total = 0;

    /** 是否需要默认校验逻辑 */
    private final boolean defaultValid;

    /** 错误信息，拼接字符串 */
    private final String delimiter;

    /** 执行逻辑 */
    private final BiConsumer<List<M>, Consumer<M>> consumer;

    /** 缓存的数据 */
    @Getter
    private List<M> cacheDatas = ListUtils.newArrayListWithExpectedSize(batchSize);

    public ExcelListener(BiConsumer<List<M>, Consumer<M>> consumer) {
        this(50, consumer);
    }

    public ExcelListener(int batchSize, BiConsumer<List<M>, Consumer<M>> consumer) {
        this(batchSize, true, ";", consumer);
    }

    public ExcelListener(int batchSize, boolean defaultValid, String delimiter, BiConsumer<List<M>, Consumer<M>> consumer) {
        log.info("excel解析开始, 编号={}", id);
        this.batchSize = batchSize;
        this.consumer = consumer;
        this.delimiter = delimiter;
        this.defaultValid = defaultValid;
    }

    /**
     * 每一条数据解析都会来调用
     */
    @Override
    public void invoke(M data, AnalysisContext analysisContext) {
        // 读取数量累加
        total++;
        // 参数校验
        String errorMsg = validData(data);
        if (StrUtil.isNotBlank(errorMsg)) {
            errorHandle(data, errorMsg);
        } else {
            setData(data, "读取成功，待操作", false);
        }
        cacheDatas.add(data);
        if (cacheDatas.size() >= batchSize) {
            handle();
            // 存储完成清理 list
            cacheDatas = ListUtils.newArrayListWithExpectedSize(batchSize);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 最后遗留的数据也要进行处理
        if (!cacheDatas.isEmpty()) {
            handle();
        }
        log.info("excel解析完成, 编号{}, 共{}条数据", id, total);
    }

    private void handle() {
        consumer.accept(cacheDatas, x -> errorHandle(x, x.getMessage()));
    }

    private void errorHandle(M data, String errorMsg) {
        error++;
        setData(data, errorMsg, true);
        // 校验失败的逻辑
        if (log.isDebugEnabled()) {
            log.debug("校验失败，错误信息: {}, 数据{}", errorMsg, JsonUtil.toJsonString(data));
        }
    }

    private void setData(M data, String message, boolean isError) {
        data.setMessage(message);
        data.setIsError(isError);
    }

    /**
     * 校验数据
     */
    public String validData(M data) {
        // @formatter:off
        // 默认校验
        if (!defaultValid) {
            return null;
        }
        return valid(data, delimiter);
        // @formatter:on
    }

    /**
     * 参数校验
     *
     * @param data      数据
     * @param delimiter 拼接字符串
     * @return {@link String}
     */
    public String valid(Object data, String delimiter) {
        // @formatter:off
        Set<ConstraintViolation<Object>> validate = ValidUtil.VALIDATOR_DEFAULT.validate(data);
        return validate
            .stream()
            .map(ConstraintViolation::getMessage)
            .sorted()
            .collect(Collectors.joining(delimiter));
        // @formatter:on
    }

}