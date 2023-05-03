package com.timeless;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author timeless
 * @create 2022-12-05 13:43
 */

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@MapperScan("com.timeless.mapper")
public class TimelessBlogAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(TimelessBlogAdminApplication.class, args);
    }
}
