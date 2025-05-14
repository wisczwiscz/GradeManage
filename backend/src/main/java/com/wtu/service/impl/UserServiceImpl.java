package com.wtu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wtu.entity.User;
import com.wtu.mapper.UserMapper;
import com.wtu.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(String username, String password) {
        // 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        queryWrapper.eq(User::getPassword, password);
        
        return getOne(queryWrapper);
    }

    @Override
    public User addUser(User user) {
        // 保存用户
        save(user);
        return user;
    }

    @Override
    public User getUserById(Long userId) {
        return getById(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        // 根据用户名查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        
        return getOne(queryWrapper);
    }
} 