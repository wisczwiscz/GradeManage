package com.wtu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 适配Spring Security 6.1+，关闭CSRF
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll() // 所有请求都允许匿名访问
            );
        return http.build();
    }
} 