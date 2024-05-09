package top.ticho.rainbow.infrastructure.core.component.excel;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import top.ticho.boot.json.util.JsonUtil;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.exception.BizException;
import top.ticho.boot.web.util.valid.ValidUtil;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
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

    /** 最大校验错误数 */
    private final long maxErrorSize;

    /** 读取数量 */
    @Getter
    private long total = 0;

    /** 是否需要默认校验逻辑 */
    private final boolean defaultValid;

    /** 是否忽略错误，true-错误数据不缓存在内存里 */
    private final boolean ignoreError;

    /** 是否忽略统计，true-读取的数据不缓存在内存里 */
    private final boolean ignoreStats;

    /** 错误信息，拼接字符串 */
    private final String delimiter;

    /** 执行逻辑 */
    private final BiConsumer<List<M>, Consumer<M>> consumer;

    /** 默认校验通过缓存的数据 */
    @Getter
    private List<M> successCacheDatas = ListUtils.newArrayListWithExpectedSize(batchSize);

    /** 缓存的校验成功的所有数据 */
    @Getter
    private final List<M> successAllDatas = new ArrayList<>();

    /** 缓存的校验失败的所有数据 */
    @Getter
    private final List<M> errorAllDatas = new ArrayList<>();

    @Getter
    private final List<M> allDatas = new ArrayList<>();

    public ExcelListener(BiConsumer<List<M>, Consumer<M>> consumer) {
        this(50, Long.MAX_VALUE, true, false, false, ";", consumer);
    }

    public ExcelListener(int batchSize, long maxErrorSize, boolean defaultValid, boolean ignoreError, boolean ignoreStats, String delimiter, BiConsumer<List<M>, Consumer<M>> consumer) {
        log.info("excel解析开始, 编号={}", id);
        this.batchSize = batchSize;
        this.maxErrorSize = maxErrorSize;
        this.consumer = consumer;
        this.delimiter = delimiter;
        this.defaultValid = defaultValid;
        this.ignoreError = ignoreError;
        this.ignoreStats = ignoreStats;
    }

    /**
     * 每一条数据解析都会来调用
     */
    @Override
    public void invoke(M data, AnalysisContext analysisContext) {
        // 读取数量累加
        total++;
        // 初始化数据，错误信息先置为空
        setData(data, null, false);
        // 存入所有数据
        saveToAllData(data);
        // 参数校验
        String errorMsg = validData(data);
        // 校验成功时的逻辑
        if (StrUtil.isNotBlank(errorMsg)) {
            errorHandle(data, errorMsg);
            return;
        }
        successHandle(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 最后遗留的数据也要进行存储
        if (!successCacheDatas.isEmpty()) {
            consumer.accept(successCacheDatas, x -> errorHandle(x, x.getMessage()));
        }
        log.info("excel解析完成, 编号{}, 共{}条数据", id, total);
    }

    private void successHandle(M data) {
        setData(data, "读取成功，待操作", false);
        successCacheDatas.add(data);
        if (successCacheDatas.size() >= batchSize) {
            consumer.accept(successCacheDatas, x -> errorHandle(x, x.getMessage()));
            // 存储完成清理 list
            successCacheDatas = ListUtils.newArrayListWithExpectedSize(batchSize);
        }
        if (ignoreStats) {
            return;
        }
        successAllDatas.add(data);
    }

    private void errorHandle(M data, String errorMsg) {
        error++;
        setData(data, errorMsg, true);
        // 校验失败的逻辑
        if (log.isDebugEnabled()) {
            log.debug("校验失败，错误信息: {}, 数据{}", errorMsg, JsonUtil.toJsonString(data));
        }
        if (ignoreError) {
            return;
        }
        if (error > maxErrorSize) {
            throw new BizException(BizErrCode.FAIL, String.format("错误数超出%s", maxErrorSize));
        }
        errorAllDatas.add(data);
    }

    private void setData(M data, String message, boolean isError) {
        data.setMessage(message);
        data.setIsError(isError);
    }

    private void saveToAllData(M data) {
        if (ignoreStats) {
            return;
        }
        allDatas.add(data);
    }

    /**
     * 校验数据
     */
    private String validData(M data) {
        // @formatter:off
        // 默认校验
        if (!defaultValid) {
            return null;
        }
        return defaultValid(data, delimiter);
        // @formatter:on
    }

    /**
     * 参数校验
     *
     * @param data      数据
     * @param delimiter 拼接字符串
     * @return {@link String}
     */
    private String defaultValid(Object data, String delimiter) {
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