package com.sparta.springboot_basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringbootBasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootBasicApplication.class, args);
    }

}
