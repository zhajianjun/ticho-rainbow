package top.ticho.intranet.client;

import ch.qos.logback.classic.util.ContextInitializer;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.dialect.Props;
import cn.hutool.setting.dialect.PropsUtil;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.client.handler.ClientHander;
import top.ticho.intranet.core.prop.ClientProperty;

import java.io.File;
import java.util.Objects;


/**
 * 客户端启动器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class ClientStart {

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        String filePath = projectPath + File.separator + "/conf/init.properties";
        Props props = PropsUtil.get(filePath);
        if (Objects.isNull(props)) {
            throw new RuntimeException("配置文件不存在");
        }
        // logback.xml放在config文件夹里，其它地方调用不会生效config下的logback.xml配置了
        System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "conf/logback.xml");
        ClientProperty clientProperty = props.toBean(ClientProperty.class);
        log.info("配置信息：{}", JSONUtil.toJsonStr(clientProperty));
        ClientHander clientHander = new ClientHander(clientProperty);
        clientHander.start();
    }

}