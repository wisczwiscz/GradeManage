package com.wtu.config;

import com.wtu.interceptor.TeacherRoleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final TeacherRoleInterceptor teacherRoleInterceptor;

    public WebMvcConfig(TeacherRoleInterceptor teacherRoleInterceptor) {
        this.teacherRoleInterceptor = teacherRoleInterceptor;
    }

    /**
     * 添加拦截器
     *
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 暂时注释掉拦截器，以便测试
        /*
        registry.addInterceptor(teacherRoleInterceptor)
                .addPathPatterns("/api/scores/add", "/api/scores/update", "/api/scores/*")
                .order(1);
        */
    }

    // 已禁用全局CORS配置，改为使用@CrossOrigin注解实现，避免冲突
    /* 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 指定前端开发服务器地址
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true) // 允许携带凭证
                .maxAge(3600);
    }
    */
} 