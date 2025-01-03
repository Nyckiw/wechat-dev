package com.self;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/11/26
 */
@SpringBootApplication
@EnableDiscoveryClient  //开启服务的注册和发现功能
public class MainServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainServiceApplication.class,args);
    }
}
