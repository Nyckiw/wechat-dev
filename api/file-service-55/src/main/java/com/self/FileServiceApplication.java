package com.self;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/11/26
 */

@SpringBootApplication
@EnableDiscoveryClient  //开启服务的注册和发现功能
@EnableFeignClients("com.self.api.feign")//扫包路径（服务提供者的接口的interface的包路径）
public class FileServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileServiceApplication.class,args);
    }
}

