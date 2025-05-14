package com.wtu.service;

import com.wtu.dto.StatisticsDTO;

import java.util.List;

/**
 * 统计分析服务接口
 */
public interface StatisticsService {
    
    /**
     * 获取指定科目的统计数据
     *
     * @param subject 科目名称
     * @return 统计结果
     */
    StatisticsDTO getStatisticsBySubject(String subject);
    
    /**
     * 获取所有科目的统计数据
     *
     * @return 统计结果列表
     */
    List<StatisticsDTO> getAllSubjectsStatistics();
} 