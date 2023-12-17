package top.ticho.intranet.server;

import com.ticho.boot.security.annotation.EnableOauth2AuthServer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@EnableOauth2AuthServer
@SpringBootApplication
@MapperScan("top.ticho.intranet.server.infrastructure.mapper")
@EnableScheduling
@Slf4j
public class IntranetApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntranetApplication.class, args);
    }

}
