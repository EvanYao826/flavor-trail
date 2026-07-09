package com.flavor.trail;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.flavor.trail.mapper")
@EnableCaching
public class FlavorTrailApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlavorTrailApplication.class, args);
    }
}