package com.timeless;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author timeless
 * @create 2022-12-05 13:43
 */

@SpringBootApplication
@EnableScheduling
@EnableSwagger2
@MapperScan("com.timeless.mapper")
public class TimelessBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(TimelessBlogApplication.class, args);
    }
}
