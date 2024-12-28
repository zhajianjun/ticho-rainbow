package top.ticho.rainbow.infrastructure.core.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import top.ticho.boot.security.util.BaseUserUtil;

import java.time.LocalDateTime;


/**
 * sql数据自动填充处理
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Component
public class MateMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createBy", BaseUserUtil.getCurrentUsername(), metaObject);
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("isDelete", 0, metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 针对非主键的字段,只有该表注解了fill 并且 字段名和字段属性 能匹配到才会进行填充(就算有值，也会被覆盖)
        this.setFieldValByName("updateBy", BaseUserUtil.getCurrentUsername(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

}
