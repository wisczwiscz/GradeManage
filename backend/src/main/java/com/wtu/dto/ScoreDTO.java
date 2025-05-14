package com.wtu.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 成绩数据传输对象
 */
@Data
public class ScoreDTO {
    
    /**
     * 成绩ID
     */
    private Long scoreId;
    
    /**
     * 学生ID
     */
    private Long studentId;
    
    /**
     * 学生姓名
     */
    private String studentName;
    
    /**
     * 科目名称
     */
    private String subject;
    
    /**
     * 分数
     */
    private Integer score;
    
    /**
     * 考试日期
     */
    private LocalDate examDate;
    
    /**
     * 成绩状态：通过/不通过
     */
    private String status;
    
    /**
     * 成绩等级：A/B/C/D/F
     */
    private String grade;
} 