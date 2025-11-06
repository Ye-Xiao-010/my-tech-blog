package com.xiaofei.my_tech_blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 暂时禁用CSRF保护，便于测试
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll() // 允许公开访问注册和登录
                        .requestMatchers(
                                "/api/users/register",
                                "/api/users/login",
                                "/api/articles/published",  // 允许公开访问已发布文章
                                "/api/articles/{id}"        // 允许公开访问单篇文章
                        ).permitAll()
                        .anyRequest().authenticated()
                );


        return http.build();
    }
}