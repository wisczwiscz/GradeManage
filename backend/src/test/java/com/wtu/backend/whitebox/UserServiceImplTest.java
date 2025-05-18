package com.wtu.backend.whitebox;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wtu.entity.User;
import com.wtu.mapper.UserMapper;
import com.wtu.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;

public class UserServiceImplTest {
    private UserServiceImpl userService;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        userMapper = Mockito.mock(UserMapper.class);
        userService = new UserServiceImpl();
        
        // 反射注入 baseMapper
        java.lang.reflect.Field baseMapperField = com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.class.getDeclaredField("baseMapper");
        baseMapperField.setAccessible(true);
        baseMapperField.set(userService, userMapper);
    }

    @Test
    void 测试登录成功() {
        // 准备测试数据
        User user = new User();
        user.setUsername("teacher1");
        user.setPassword("123456");
        user.setRole("TEACHER");
        
        // 设置mock行为 - 这里使用双参数版本的selectOne
        Mockito.when(userMapper.selectOne(any(), anyBoolean())).thenReturn(user);
        
        System.out.println("【测试登录成功】输入: username=teacher1, password=123456");
        System.out.println("【测试登录成功】期望: 返回User对象，用户名为teacher1，角色为TEACHER");
        
        // 调用被测试方法
        User result = userService.login("teacher1", "123456");
        
        System.out.println("【测试登录成功】响应结果: " + result);
        assertNotNull(result, "登录成功时，返回用户对象不应为null");
        assertEquals("teacher1", result.getUsername(), "用户名应为teacher1");
        assertEquals("TEACHER", result.getRole(), "角色应为TEACHER");
        System.out.println("【测试登录成功】断言通过\n");
        
        // 验证查询条件
        ArgumentCaptor<LambdaQueryWrapper> queryCaptor = ArgumentCaptor.forClass(LambdaQueryWrapper.class);
        Mockito.verify(userMapper).selectOne(queryCaptor.capture(), Mockito.eq(true));
    }

    @Test
    void 测试登录失败() {
        // 设置mock行为 - 使用双参数版本的selectOne
        Mockito.when(userMapper.selectOne(any(), anyBoolean())).thenReturn(null);
        
        System.out.println("【测试登录失败】输入: username=admin, password=wrong");
        System.out.println("【测试登录失败】期望: 返回null");
        
        // 调用被测试方法
        User result = userService.login("admin", "wrong");
        
        System.out.println("【测试登录失败】响应结果: " + result);
        assertNull(result, "登录失败时，返回结果应为null");
        System.out.println("【测试登录失败】断言通过\n");
        
        // 验证查询参数
        Mockito.verify(userMapper).selectOne(any(), Mockito.eq(true));
    }

    @Test
    void 测试添加用户() {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("pwd");
        user.setRole("STUDENT");
        
        // 设置insert方法返回1，表示插入成功
        Mockito.when(userMapper.insert(user)).thenReturn(1);
        
        System.out.println("【测试添加用户】输入: " + user);
        System.out.println("【测试添加用户】期望: 返回User对象，用户名为newuser，角色为STUDENT");
        
        // 调用被测试方法
        User result = userService.addUser(user);
        
        System.out.println("【测试添加用户】响应结果: " + result);
        assertEquals("newuser", result.getUsername(), "添加用户后用户名应为newuser");
        assertEquals("STUDENT", result.getRole(), "添加用户后角色应为STUDENT");
        System.out.println("【测试添加用户】断言通过\n");
        
        // 验证insert方法被调用
        Mockito.verify(userMapper).insert(user);
    }

    @Test
    void 测试按ID查找用户_存在() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("u1");
        
        // 设置selectById返回值
        Mockito.when(userMapper.selectById(1L)).thenReturn(user);
        
        System.out.println("【测试按ID查找用户_存在】输入: userId=1L");
        System.out.println("【测试按ID查找用户_存在】期望: 返回User对象，userId=1L，用户名为u1");
        
        // 调用被测试方法
        User result = userService.getUserById(1L);
        
        System.out.println("【测试按ID查找用户_存在】响应结果: " + result);
        assertNotNull(result, "查找存在的用户时，结果不应为null");
        assertEquals(1L, result.getUserId(), "用户ID应为1");
        assertEquals("u1", result.getUsername(), "用户名应为u1");
        System.out.println("【测试按ID查找用户_存在】断言通过\n");
        
        // 验证selectById方法被调用
        Mockito.verify(userMapper).selectById(1L);
    }

    @Test
    void 测试按ID查找用户_不存在() {
        // 设置selectById返回null
        Mockito.when(userMapper.selectById(2L)).thenReturn(null);
        
        System.out.println("【测试按ID查找用户_不存在】输入: userId=2L");
        System.out.println("【测试按ID查找用户_不存在】期望: 返回null");
        
        // 调用被测试方法
        User result = userService.getUserById(2L);
        
        System.out.println("【测试按ID查找用户_不存在】响应结果: " + result);
        assertNull(result, "查找不存在的用户时，结果应为null");
        System.out.println("【测试按ID查找用户_不存在】断言通过\n");
        
        // 验证selectById方法被调用
        Mockito.verify(userMapper).selectById(2L);
    }

    @Test
    void 测试按用户名查找用户_存在() {
        User user = new User();
        user.setUsername("test");
        user.setRole("STUDENT");
        
        // 设置selectOne返回值，使用双参数版本
        Mockito.when(userMapper.selectOne(any(), anyBoolean())).thenReturn(user);
        
        System.out.println("【测试按用户名查找用户_存在】输入: username=test");
        System.out.println("【测试按用户名查找用户_存在】期望: 返回User对象，用户名为test，角色为STUDENT");
        
        // 调用被测试方法
        User result = userService.getUserByUsername("test");
        
        System.out.println("【测试按用户名查找用户_存在】响应结果: " + result);
        assertNotNull(result, "查找存在用户名时，结果不应为null");
        assertEquals("test", result.getUsername(), "用户名应为test");
        assertEquals("STUDENT", result.getRole(), "角色应为STUDENT");
        System.out.println("【测试按用户名查找用户_存在】断言通过\n");
        
        // 验证查询参数
        Mockito.verify(userMapper).selectOne(any(), Mockito.eq(true));
    }

    @Test
    void 测试按用户名查找用户_不存在() {
        // 设置selectOne返回null，使用双参数版本
        Mockito.when(userMapper.selectOne(any(), anyBoolean())).thenReturn(null);
        
        System.out.println("【测试按用户名查找用户_不存在】输入: username=notfound");
        System.out.println("【测试按用户名查找用户_不存在】期望: 返回null");
        
        // 调用被测试方法
        User result = userService.getUserByUsername("notfound");
        
        System.out.println("【测试按用户名查找用户_不存在】响应结果: " + result);
        assertNull(result, "查找不存在用户名时，结果应为null");
        System.out.println("【测试按用户名查找用户_不存在】断言通过\n");
        
        // 验证查询参数
        Mockito.verify(userMapper).selectOne(any(), Mockito.eq(true));
    }
} 