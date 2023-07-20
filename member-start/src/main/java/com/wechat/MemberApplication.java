package com.wechat;

import com.wechat.jwt.JwtAuthenticationTokenFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = { "com.wechat"}, exclude={RedisAutoConfiguration.class})
@MapperScan("com.wechat.mapper")
@EnableScheduling
public class MemberApplication {

    public static void main(String[] args) {
        // 设置系统属性
        System.setProperty("java.awt.headless", "true");

        SpringApplication.run(MemberApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean registration(JwtAuthenticationTokenFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
}
