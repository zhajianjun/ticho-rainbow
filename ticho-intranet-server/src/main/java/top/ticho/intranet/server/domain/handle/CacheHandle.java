package top.ticho.intranet.server.domain.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.ticho.boot.web.util.SpringContext;
import top.ticho.intranet.server.infrastructure.core.constant.SecurityConst;
import top.ticho.intranet.server.interfaces.dto.PermDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 缓存处理
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public class CacheHandle {

    private Map<String, Map<String, String>> MAP = new HashMap<>();

    public List<PermDTO> pushCurrentAppPerms() {
        List<PermDTO> perms = listCurrentAppPerms();
        if (CollUtil.isEmpty(perms)) {
            return Collections.emptyList();
        }
        Map<String, String> map = perms.stream().collect(Collectors.toMap(PermDTO::getCode, PermDTO::getName));
        MAP.put(SecurityConst.MICRO_REDIS_ALL_PERMS, map);
        return perms;
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
            // String value = entry.getValue();
            PermDTO perm = json;
            int length = keys.length;
            for (int i = 0; i < length; i++) {
                String key = keys[i];
                boolean isLastKey = i == length - 1;
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
                // if (isLastKey) {
                //     child.setValue(entryKey);
                // }
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
        // @formatter:off
        RequestMappingHandlerMapping mapping = SpringContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        return map.values()
            .stream()
            .map(this::getFunc)
            .filter(Objects::nonNull)
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
        String name = handlerMethod.getBeanType().toString();
        PermDTO perm = new PermDTO();
        perm.setCode(value);
        perm.setName(apiOperation == null ? name : apiOperation.value());
        return perm;
    }

}
