package com.example.onehealthrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.onehealthrest", "com.example.onehealthcommon"})
@EntityScan("com.example.onehealthcommon")
@EnableJpaRepositories(basePackages = "com.example.onehealthcommon.repository")
@EnableAsync
@EnableAspectJAutoProxy
public class OneHealthRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneHealthRestApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
