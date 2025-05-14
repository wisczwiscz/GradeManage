package com.wtu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 学生实体类
 */
@Data
@TableName("student")
public class Student {
    
    /**
     * 学生ID，主键
     */
    @TableId(value = "student_id", type = IdType.AUTO)
    private Long studentId;
    
    /**
     * 学生姓名
     */
    private String name;
    
    /**
     * 班级名称
     */
    private String className;
} 