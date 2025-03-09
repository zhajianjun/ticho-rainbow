package top.ticho.rainbow.infrastructure.core.component;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.ticho.rainbow.application.dto.PermDTO;
import top.ticho.rainbow.infrastructure.core.constant.SecurityConst;
import top.ticho.starter.web.util.TiSpringUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 权限编码缓存处理
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Component
public class PermCacheHandle {

    private final Map<String, Map<String, String>> MAP = new HashMap<>();

    public void pushCurrentAppPerms() {
        List<PermDTO> perms = listCurrentAppPerms();
        if (CollUtil.isEmpty(perms)) {
            return;
        }
        Map<String, String> map = perms
            .stream()
            .collect(Collectors.toMap(PermDTO::getCode, PermDTO::getName, (v1, v2) -> v1, LinkedHashMap::new));
        MAP.put(SecurityConst.MICRO_REDIS_ALL_PERMS, map);
    }

    public List<PermDTO> listAllAppPerms() {
        Map<String, String> permsMap = getAllPermsMap();
        PermDTO json = new PermDTO();
        json.setName("");
        json.setCode("");
        json.setChildren(new ArrayList<>());
        for (Map.Entry<String, String> entry : permsMap.entrySet()) {
            String entryKey = entry.getKey();
            String[] keys = entryKey.split(":");
            PermDTO perm = json;
            for (String key : keys) {
                if (Objects.isNull(perm.getChildren())) {
                    perm.setChildren(new ArrayList<>());
                }
                List<PermDTO> children = perm.getChildren();
                Optional<PermDTO> childOpt = children.stream().filter(x -> Objects.equals(x.getCode(), key)).findFirst();
                PermDTO child;
                if (!childOpt.isPresent()) {
                    child = new PermDTO();
                    child.setName(key);
                    child.setCode(key);
                    child.setChildren(new ArrayList<>());
                    children.add(child);
                } else {
                    child = childOpt.get();
                }
                perm = child;
            }
        }
        return json.getChildren();
    }

    public Map<String, String> getAllPermsMap() {
        return MAP.get(SecurityConst.MICRO_REDIS_ALL_PERMS);
    }

    /**
     * 获取当前应用的全部权限标识
     *
     * @return {@link List}<{@link PermDTO}>
     */
    public List<PermDTO> listCurrentAppPerms() {
        RequestMappingHandlerMapping mapping = TiSpringUtil.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        return map.values()
            .stream()
            .map(this::getFunc)
            .filter(Objects::nonNull)
            .sorted(Comparator.comparing(PermDTO::getSort))
            .collect(Collectors.toList());
    }

    private PermDTO getFunc(HandlerMethod handlerMethod) {
        PreAuthorize preAuthorize = handlerMethod.getMethodAnnotation(PreAuthorize.class);
        if (preAuthorize == null) {
            return null;
        }
        String value = preAuthorize.value();
        if (StrUtil.isBlank(value)) {
            return null;
        }
        int start = value.indexOf("'") + 1;
        int end = value.lastIndexOf("'");
        value = value.substring(start, end);
        ApiOperation apiOperation = handlerMethod.getMethodAnnotation(ApiOperation.class);
        ApiOperationSupport support = handlerMethod.getMethodAnnotation(ApiOperationSupport.class);
        String name = handlerMethod.getBeanType().toString();
        PermDTO perm = new PermDTO();
        perm.setCode(value);
        perm.setName(Optional.ofNullable(apiOperation).map(ApiOperation::value).orElse(name));
        perm.setSort(Optional.ofNullable(support).map(ApiOperationSupport::order).orElse(Integer.MAX_VALUE));
        return perm;
    }

}
