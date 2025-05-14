package com.wtu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wtu.dto.ScoreDTO;
import com.wtu.dto.ScoreQueryDTO;
import com.wtu.entity.Score;

/**
 * 成绩服务接口
 */
public interface ScoreService {
    
    /**
     * 添加成绩
     *
     * @param score 成绩对象
     * @return 保存成功的成绩对象
     */
    Score addScore(Score score);
    
    /**
     * 查询成绩列表（带分页）
     *
     * @param queryDTO 查询条件
     * @return 成绩分页数据
     */
    Page<ScoreDTO> queryScores(ScoreQueryDTO queryDTO);
    
    /**
     * 获取成绩详情
     *
     * @param scoreId 成绩ID
     * @return 成绩DTO对象
     */
    ScoreDTO getScoreById(Long scoreId);
    
    /**
     * 更新成绩
     *
     * @param score 成绩对象
     * @return 是否更新成功
     */
    boolean updateScore(Score score);
    
    /**
     * 删除成绩
     *
     * @param scoreId 成绩ID
     * @return 是否删除成功
     */
    boolean deleteScore(Long scoreId);
} 