package com.wtu.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtu.entity.User;
import com.wtu.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * 教师角色权限拦截器
 */
@Component
public class TeacherRoleInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    public TeacherRoleInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 已禁用权限检查，直接放行所有请求
        return true;
        
        // 原权限检查代码（已注释）
        /*
        // 获取当前登录用户
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        
        // 未登录
        if (loginUser == null) {
            handleUnauthorized(response, "请先登录");
            return false;
        }
        
        // 验证是否有教师权限
        if (!"TEACHER".equals(loginUser.getRole())) {
            handleUnauthorized(response, "权限不足，仅教师可操作");
            return false;
        }
        
        return true;
        */
    }
    
    /**
     * 处理未授权的请求
     *
     * @param response HttpServletResponse
     * @param message  错误消息
     * @throws IOException IO异常
     */
    private void handleUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        Result<Object> result = Result.fail(401, message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
} 