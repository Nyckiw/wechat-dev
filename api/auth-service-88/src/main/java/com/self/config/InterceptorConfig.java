package com.self.config;

import com.self.interceptor.SMSInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/3
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    /**
     * 在springboot容器中放入拦截器，托管给springboot
     */
    @Bean
    public SMSInterceptor smsInterceptor() {
        return new SMSInterceptor();
    }

    /**
     * 添加拦截器 拦截指定路由路径
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(smsInterceptor())
                .addPathPatterns("/passport/getSMSCode");
    }
}
