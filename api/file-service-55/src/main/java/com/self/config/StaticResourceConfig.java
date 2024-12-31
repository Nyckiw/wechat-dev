package com.self.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/31
 */
@Configuration
public class StaticResourceConfig extends WebMvcConfigurationSupport {
    //添加静态资源映射路径 图片视频 都放在classpath的static下
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * addResourceHandler 指对外暴露的访问路径映射
         * addResourceLocations 指的本地文件所在的目录
         */
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:F:\\自研\\SpringCloud+Netty集群实战千万级 IM系统\\temp\\");
        //http://127.0.0.1:1000/static/face/555386991177367552.jpg
        super.addResourceHandlers(registry);
    }
}


