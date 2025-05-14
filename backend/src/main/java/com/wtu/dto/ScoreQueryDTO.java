package com.wtu.dto;

import lombok.Data;

/**
 * 成绩查询数据传输对象
 */
@Data
public class ScoreQueryDTO {
    
    /**
     * 学生ID
     */
    private Long studentId;
    
    /**
     * 学生姓名
     */
    private String studentName;
    
    /**
     * 科目
     */
    private String subject;
    
    /**
     * 当前页码，默认第1页
     */
    private Integer pageNum = 1;
    
    /**
     * 每页记录数，默认10条
     */
    private Integer pageSize = 10;
} 