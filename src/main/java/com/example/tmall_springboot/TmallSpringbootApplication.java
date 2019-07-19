package com.example.tmall_springboot;

import com.example.tmall_springboot.utils.PortUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TmallSpringbootApplication {

    private static final int REDIS_PORT = 6379;

    static {
        System.out.println("Redis Port check: " + PortUtil.checkPort(REDIS_PORT));
    }

    public static void main(String[] args) {
        SpringApplication.run(TmallSpringbootApplication.class, args);
    }

}
