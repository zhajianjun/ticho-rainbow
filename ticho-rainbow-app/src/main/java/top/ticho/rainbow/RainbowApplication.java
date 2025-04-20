package top.ticho.rainbow;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@SpringBootApplication
@MapperScan("top.ticho.rainbow.infrastructure.persistence.mapper")
@EnableScheduling
@Slf4j
@EnableAsync
public class RainbowApplication {

    public static void main(String[] args) {
        SpringApplication.run(RainbowApplication.class, args);
    }

}
