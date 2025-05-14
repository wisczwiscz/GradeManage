package com.wtu.service;

import com.wtu.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户信息，登录失败返回null
     */
    User login(String username, String password);
    
    /**
     * 添加用户
     *
     * @param user 用户信息
     * @return 添加成功的用户信息
     */
    User addUser(User user);
    
    /**
     * 根据ID查询用户
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Long userId);
    
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);
} 