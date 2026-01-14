package com.zxsc.config;

import com.zxsc.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/login", "/register", "/user/login", "/user/register", 
                        "/css/**", "/js/**", "/images/**", "/error", "/api/**", "/upload/**");
    }
}
