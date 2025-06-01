package top.ticho.rainbow.application.executor;

import lombok.RequiredArgsConstructor;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.infrastructure.common.util.BeetlUtil;

import java.util.Map;

/**
 * 模版渲染
 *
 * @author zhajianjun
 * @date 2025-06-01 13:07
 */
@RequiredArgsConstructor
@Component
public class TemplateExecutor {
    private final GroupTemplate groupTemplate = BeetlUtil.getGroupTemplate(true);

    public String render(String path) {
        Template template = groupTemplate.getTemplate(path);
        return template.render();
    }

    public String render(String path, Map<String, Object> map) {
        Template template = groupTemplate.getTemplate(path);
        template.binding(map);
        return template.render();
    }

}
