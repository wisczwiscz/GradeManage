package com.wtu.backend.whitebox;

import com.wtu.entity.User;
import com.wtu.mapper.UserMapper;
import com.wtu.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest {
    private UserServiceImpl userService;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() throws Exception {
        userMapper = Mockito.mock(UserMapper.class);
        userService = new UserServiceImpl();
        // 反射注入 baseMapper
        java.lang.reflect.Field baseMapperField = userService.getClass().getSuperclass().getDeclaredField("baseMapper");
        baseMapperField.setAccessible(true);
        baseMapperField.set(userService, userMapper);
    }

    @Test
    void 测试登录成功() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123456");
        user.setRole("TEACHER");
        Mockito.when(userMapper.selectOne(Mockito.any())).thenReturn(user);
        User result = userService.login("admin", "123456");
        assertNotNull(result, "登录成功时，返回用户对象不应为null");
        assertEquals("admin", result.getUsername(), "用户名应为admin");
        assertEquals("TEACHER", result.getRole(), "角色应为TEACHER");
    }

    @Test
    void 测试登录失败() {
        Mockito.when(userMapper.selectOne(Mockito.any())).thenReturn(null);
        User result = userService.login("admin", "wrong");
        assertNull(result, "登录失败时，返回结果应为null");
    }

    @Test
    void 测试添加用户() {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("pwd");
        user.setRole("STUDENT");
        Mockito.doNothing().when(userMapper).insert(user);
        User result = userService.addUser(user);
        assertEquals("newuser", result.getUsername(), "添加用户后用户名应为newuser");
        assertEquals("STUDENT", result.getRole(), "添加用户后角色应为STUDENT");
    }

    @Test
    void 测试按ID查找用户_存在() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("u1");
        Mockito.when(userMapper.selectById(1L)).thenReturn(user);
        User result = userService.getUserById(1L);
        assertNotNull(result, "查找存在的用户时，结果不应为null");
        assertEquals(1L, result.getUserId(), "用户ID应为1");
        assertEquals("u1", result.getUsername(), "用户名应为u1");
    }

    @Test
    void 测试按ID查找用户_不存在() {
        Mockito.when(userMapper.selectById(2L)).thenReturn(null);
        User result = userService.getUserById(2L);
        assertNull(result, "查找不存在的用户时，结果应为null");
    }

    @Test
    void 测试按用户名查找用户_存在() {
        User user = new User();
        user.setUsername("test");
        user.setRole("STUDENT");
        Mockito.when(userMapper.selectOne(Mockito.any())).thenReturn(user);
        User result = userService.getUserByUsername("test");
        assertNotNull(result, "查找存在用户名时，结果不应为null");
        assertEquals("test", result.getUsername(), "用户名应为test");
        assertEquals("STUDENT", result.getRole(), "角色应为STUDENT");
    }

    @Test
    void 测试按用户名查找用户_不存在() {
        Mockito.when(userMapper.selectOne(Mockito.any())).thenReturn(null);
        User result = userService.getUserByUsername("notfound");
        assertNull(result, "查找不存在用户名时，结果应为null");
    }
} 