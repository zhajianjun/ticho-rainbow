package top.ticho.rainbow.application.service;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.ticho.rainbow.interfaces.dto.response.PermissionDTO;
import top.ticho.starter.web.util.TiSpringUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zhajianjun
 * @date 2025-04-06 13:40
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionQueryService {
    private final Map<String, String> MAP = new LinkedHashMap<>();

    public List<PermissionDTO> tree() {
        Map<String, String> permsMap = getMap();
        PermissionDTO json = new PermissionDTO();
        json.setName("");
        json.setCode("");
        json.setChildren(new ArrayList<>());
        for (Map.Entry<String, String> entry : permsMap.entrySet()) {
            String entryKey = entry.getKey();
            String[] keys = entryKey.split(":");
            PermissionDTO perm = json;
            for (String key : keys) {
                if (Objects.isNull(perm.getChildren())) {
                    perm.setChildren(new ArrayList<>());
                }
                List<PermissionDTO> children = perm.getChildren();
                Optional<PermissionDTO> childOpt = children.stream().filter(x -> Objects.equals(x.getCode(), key)).findFirst();
                PermissionDTO child;
                if (childOpt.isEmpty()) {
                    child = new PermissionDTO();
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

    public Map<String, String> getMap() {
        return MAP;
    }

    public void cache() {
        List<PermissionDTO> perms = all();
        Map<String, String> map = perms
            .stream()
            .filter(Objects::nonNull)
            .filter(item -> StrUtil.isNotBlank(item.getCode()))
            .filter(item -> StrUtil.isNotBlank(item.getName()))
            .sorted(Comparator.comparing(PermissionDTO::getCode, Comparator.nullsLast(Comparator.naturalOrder())))
            .collect(Collectors.toMap(PermissionDTO::getCode, PermissionDTO::getName, (v1, v2) -> v1, LinkedHashMap::new));
        MAP.putAll(map);
    }

    /**
     * 获取当前应用的全部权限标识
     */
    public List<PermissionDTO> all() {
        RequestMappingHandlerMapping mapping = TiSpringUtil.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        return map.values()
            .stream()
            .map(this::toPermission)
            .filter(Objects::nonNull)
            .sorted(Comparator.comparing(PermissionDTO::getSort, Comparator.nullsLast(Comparator.naturalOrder())))
            .collect(Collectors.toList());
    }

    private PermissionDTO toPermission(HandlerMethod handlerMethod) {
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
        PermissionDTO perm = new PermissionDTO();
        perm.setCode(value);
        perm.setName(value);
        perm.setSort(10);
        return perm;
    }

}
