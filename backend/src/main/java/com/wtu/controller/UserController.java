package com.wtu.controller;

import com.wtu.entity.User;
import com.wtu.result.Result;
import com.wtu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户接口", description = "提供用户登录和注册功能")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录
     *
     * @param loginMap 登录信息（username, password）
     * @return 登录结果
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public Result<User> login(@RequestBody Map<String, String> loginMap) {
        String username = loginMap.get("username");
        String password = loginMap.get("password");
        
        User user = userService.login(username, password);
        if (user == null) {
            return Result.fail("用户名或密码错误");
        }
        
        // 隐藏密码
        user.setPassword(null);
        
        return Result.success(user);
    }

    /**
     * 用户登出
     *
     * @return 登出结果
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出接口")
    public Result<String> logout() {
        return Result.success("登出成功");
    }

    /**
     * 获取当前登录用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/current")
    @Operation(summary = "获取用户信息", description = "获取用户信息")
    public Result<User> getCurrentUser(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        
        // 隐藏密码
        user.setPassword(null);
        
        return Result.success(user);
    }

    /**
     * 注册新用户
     *
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户")
    public Result<User> register(@RequestBody User user) {
        // 检查用户名是否已存在
        User existingUser = userService.getUserByUsername(user.getUsername());
        if (existingUser != null) {
            return Result.fail("用户名已存在");
        }
        
        // 设置默认角色（如果未指定）
        if (user.getRole() == null) {
            user.setRole("STUDENT"); // 默认为学生角色
        }
        
        // 保存用户
        User savedUser = userService.addUser(user);
        
        // 隐藏密码
        savedUser.setPassword(null);
        
        return Result.success(savedUser);
    }
} 