package com.wtu.dto;

import lombok.Data;

import java.util.Map;

/**
 * 统计数据传输对象
 */
@Data
public class StatisticsDTO {
    
    /**
     * 科目名称
     */
    private String subject;
    
    /**
     * 平均分
     */
    private Double avgScore;
    
    /**
     * 最高分
     */
    private Integer maxScore;
    
    /**
     * 最低分
     */
    private Integer minScore;
    
    /**
     * 及格人数
     */
    private Integer passCount;
    
    /**
     * 不及格人数
     */
    private Integer failCount;
    
    /**
     * 各等级人数统计
     * key: 等级 (A, B, C, D, F)
     * value: 人数
     */
    private Map<String, Integer> gradeDistribution;
} 