package com.wtu.backend.whitebox;

import com.wtu.controller.UserController;
import com.wtu.entity.User;
import com.wtu.result.Result;
import com.wtu.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerLoginWhiteBoxTest {
    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    @DisplayName("TC1: 有效数据，登录成功")
    void testLoginSuccess() {
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("username", "user11");
        loginMap.put("password", "pass12");
        User user = new User();
        user.setUsername("user11");
        user.setPassword("pass12");
        when(userService.login("user11", "pass12")).thenReturn(user);

        Result<User> result = userController.login(loginMap);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("user11", result.getData().getUsername());
    }

    @Test
    @DisplayName("TC2: 用户名为空")
    void testLoginUsernameNull() {
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("username", null);
        loginMap.put("password", "pass12");

        Result<User> result = userController.login(loginMap);
        assertNotEquals(200, result.getCode());
        assertEquals("用户名格式不正确，需5-10位且包含字母和数字", result.getMessage());
    }

    @Test
    @DisplayName("TC3: 密码为空")
    void testLoginPasswordNull() {
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("username", "user11");
        loginMap.put("password", null);

        Result<User> result = userController.login(loginMap);
        assertNotEquals(200, result.getCode());
        assertEquals("密码格式不正确，需5-10位且包含字母和数字", result.getMessage());
    }

    @Test
    @DisplayName("TC4: 用户名密码不匹配")
    void testLoginUserNotMatch() {
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("username", "user11");
        loginMap.put("password", "wrong12");
        when(userService.login("user11", "wrong12")).thenReturn(null);

        Result<User> result = userController.login(loginMap);
        assertNotEquals(200, result.getCode());
        assertEquals("用户名或密码错误", result.getMessage());
    }

    @Test
    @DisplayName("TC5: 用户名格式错误-只含字母")
    void testLoginUsernameFormatError() {
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("username", "useronly");
        loginMap.put("password", "pass12");

        Result<User> result = userController.login(loginMap);
        assertNotEquals(200, result.getCode());
        assertEquals("用户名格式不正确，需5-10位且包含字母和数字", result.getMessage());
    }

    @Test
    @DisplayName("TC6: 密码格式错误-只含数字")
    void testLoginPasswordFormatError() {
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("username", "user11");
        loginMap.put("password", "12345678");

        Result<User> result = userController.login(loginMap);
        assertNotEquals(200, result.getCode());
        assertEquals("密码格式不正确，需5-10位且包含字母和数字", result.getMessage());
    }
} 