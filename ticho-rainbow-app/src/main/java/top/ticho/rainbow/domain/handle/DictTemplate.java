package top.ticho.rainbow.domain.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.application.service.DictService;
import top.ticho.rainbow.interfaces.dto.DictDTO;
import top.ticho.rainbow.interfaces.dto.DictLabelDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 字典工具
 *
 * @author zhajianjun
 * @date 2024-05-10 18:46
 */
@Component
public class DictTemplate {

    @Autowired
    private DictService dictService;

    /**
     * 查询字典标签map
     * 例如: code:yesOrNo;1-是,0-否
     * 结果: { 1:是, 2:否 }
     *
     * @param code 字典编码
     * @return {@link Map }<{@link String }, {@link String }>
     */
    public Map<String, String> getLabelMap(String code) {
        return getLabelMap(code, Function.identity());
    }

    /**
     * 查询字典标签map
     * 例如: code:yesOrNo;1-是,0-否
     * 结果: { 1:是, 2:否 }
     *
     * @param code 字典编码
     * @return {@link Map }<{@link String }, {@link String }>
     */
    public <T> Map<T, String> getLabelMap(String code, Function<String, T> keyConvert) {
        if (StrUtil.isBlank(code)) {
            return Collections.emptyMap();
        }
        List<DictDTO> list = dictService.list();
        return convertMap(list, x-> Objects.equals(x.getCode(), code), x-> keyConvert.apply(x.getValue()), DictLabelDTO::getLabel);
    }

    /**
     * (批量)查询字典标签map
     * 例如: code:yesOrNo;1-是,0-否
     * 结果: { yesOrNo1:是, yesOrNo2:否 }
     *
     * @param codes 编码列表
     * @return {@link Map }<{@link String }, {@link String }>
     */
    public Map<String, String> getLabelMapBatch(String... codes) {
        List<String> codeList = Arrays.stream(codes).collect(Collectors.toList());
        return getLabelMapBatch(codeList);
    }

    /**
     * (批量)查询字典标签map
     * 例如: code:yesOrNo;1-是,0-否
     * 结果: { yesOrNo1:是, yesOrNo2:否 }
     *
     * @param codes 编码列表
     * @return {@link Map }<{@link String }, {@link String }>
     */
    public Map<String, String> getLabelMapBatch(List<String> codes) {
        if (CollUtil.isEmpty(codes)) {
            return Collections.emptyMap();
        }
        List<DictDTO> list = dictService.list();
        return convertMap(list, x-> codes.contains(x.getCode()),x -> x.getCode() + x.getValue(), DictLabelDTO::getLabel);
    }

    /**
     * 查询字典值map
     * 例如: code:yesOrNo;1-是,0-否
     * 结果: { 是:1, 否:1 }
     *
     * @param code 字典编码
     * @return {@link Map }<{@link String }, {@link String }>
     */
    public Map<String, String> getValueMap(String code) {
        if (StrUtil.isBlank(code)) {
            return Collections.emptyMap();
        }
        return getValueMap(code, Function.identity());
    }

    /**
     * 查询字典值map
     * 例如: code:yesOrNo;1-是,0-否
     * 结果: { 是:1, 否:1 }
     *
     * @param code 字典编码
     * @return {@link Map }<{@link String }, {@link String }>
     */
    public <T> Map<String, T> getValueMap(String code, Function<String, T> valueConvert) {
        if (StrUtil.isBlank(code)) {
            return Collections.emptyMap();
        }
        List<DictDTO> list = dictService.list();
        return convertMap(list, x-> Objects.equals(x.getCode(), code), DictLabelDTO::getLabel, x-> valueConvert.apply(x.getValue()));
    }

    /**
     * (批量)查询字典值map
     * 例如: code:yesOrNo;1-是,0-否
     * 结果: { yesOrNo是:1, yesOrNo否:0 }
     *
     * @param codes 编码列表
     * @return {@link Map }<{@link String }, {@link String }>
     */
    public Map<String, String> getValueMapBatch(String... codes) {
        List<String> codeList = Arrays.stream(codes).collect(Collectors.toList());
        return getValueMapBatch(codeList);
    }

    /**
     * (批量)查询字典值map
     * 例如: code:yesOrNo;1-是,0-否
     * 结果: { yesOrNo是:1, yesOrNo否:0 }
     *
     * @param codes 编码列表
     * @return {@link Map }<{@link String }, {@link String }>
     */
    public Map<String, String> getValueMapBatch(List<String> codes) {
        return getValueMapBatch(codes, Function.identity());
    }

    /**
     * (批量)查询字典值map
     * 例如: code:yesOrNo;1-是,0-否
     * 结果: { yesOrNo是:1, yesOrNo否:0 }
     *
     * @param codes 编码列表
     * @return {@link Map }<{@link String }, {@link String }>
     */
    public <T> Map<String, T> getValueMapBatch(List<String> codes, Function<String, T> valueConvert) {
        if (CollUtil.isEmpty(codes)) {
            return Collections.emptyMap();
        }
        List<DictDTO> list = dictService.list();
        return convertMap(list, x-> codes.contains(x.getCode()),x -> x.getCode() + x.getLabel(), x-> valueConvert.apply(x.getValue()));
    }

    private <T, K> Map<T, K> convertMap(List<DictDTO> list, Predicate<DictDTO> filter, Function<DictLabelDTO, T> keyFunc, Function<DictLabelDTO, K> valueFunc) {
        return list
            .stream()
            .filter(filter)
            .flatMap(x -> x.getDetails()
                .stream()
                .peek(y -> y.setCode(x.getCode()))
                .sorted(Comparator.nullsLast(Comparator.comparing(DictLabelDTO::getSort)))
            )
            .collect(Collectors.toMap(keyFunc, valueFunc));
    }

}
