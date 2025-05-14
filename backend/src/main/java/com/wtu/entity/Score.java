package com.wtu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

/**
 * 成绩实体类
 */
@Data
@TableName("score")
public class Score {
    
    /**
     * 成绩ID，主键
     */
    @TableId(value = "score_id", type = IdType.AUTO)
    private Long scoreId;
    
    /**
     * 学生ID，外键
     */
    private Long studentId;
    
    /**
     * 科目名称
     */
    private String subject;
    
    /**
     * 分数（0-100）
     */
    private Integer score;
    
    /**
     * 考试日期
     */
    private LocalDate examDate;
} 