package com.wtu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wtu.dto.StatisticsDTO;
import com.wtu.entity.Score;
import com.wtu.mapper.ScoreMapper;
import com.wtu.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计分析服务实现类
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final ScoreMapper scoreMapper;

    public StatisticsServiceImpl(ScoreMapper scoreMapper) {
        this.scoreMapper = scoreMapper;
    }

    @Override
    public StatisticsDTO getStatisticsBySubject(String subject) {
        // 查询指定科目的所有成绩
        LambdaQueryWrapper<Score> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Score::getSubject, subject);
        List<Score> scoreList = scoreMapper.selectList(queryWrapper);
        
        if (scoreList.isEmpty()) {
            return new StatisticsDTO();
        }
        
        return calculateStatistics(subject, scoreList);
    }

    @Override
    public List<StatisticsDTO> getAllSubjectsStatistics() {
        // 查询所有成绩
        List<Score> allScores = scoreMapper.selectList(null);
        
        // 按科目分组
        Map<String, List<Score>> scoresBySubject = allScores.stream()
                .collect(Collectors.groupingBy(Score::getSubject));
        
        // 计算每个科目的统计数据
        List<StatisticsDTO> statisticsList = new ArrayList<>();
        for (Map.Entry<String, List<Score>> entry : scoresBySubject.entrySet()) {
            StatisticsDTO statistics = calculateStatistics(entry.getKey(), entry.getValue());
            statisticsList.add(statistics);
        }
        
        return statisticsList;
    }
    
    /**
     * 根据成绩列表计算统计数据
     *
     * @param subject   科目名称
     * @param scoreList 成绩列表
     * @return 统计结果
     */
    private StatisticsDTO calculateStatistics(String subject, List<Score> scoreList) {
        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setSubject(subject);
        
        // 提取分数列表
        List<Integer> scores = scoreList.stream()
                .map(Score::getScore)
                .collect(Collectors.toList());
        
        // 计算平均分
        double avgScore = scores.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
        statisticsDTO.setAvgScore(Math.round(avgScore * 10.0) / 10.0); // 保留一位小数
        
        // 计算最高分和最低分
        statisticsDTO.setMaxScore(scores.stream().mapToInt(Integer::intValue).max().orElse(0));
        statisticsDTO.setMinScore(scores.stream().mapToInt(Integer::intValue).min().orElse(0));
        
        // 计算及格和不及格人数
        int passCount = (int) scores.stream().filter(score -> score >= 60).count();
        int failCount = scores.size() - passCount;
        statisticsDTO.setPassCount(passCount);
        statisticsDTO.setFailCount(failCount);
        
        // 计算各等级人数分布
        Map<String, Integer> gradeDistribution = new HashMap<>();
        gradeDistribution.put("A", 0);
        gradeDistribution.put("B", 0);
        gradeDistribution.put("C", 0);
        gradeDistribution.put("D", 0);
        gradeDistribution.put("F", 0);
        
        for (Integer score : scores) {
            if (score >= 90) {
                gradeDistribution.put("A", gradeDistribution.get("A") + 1);
            } else if (score >= 80) {
                gradeDistribution.put("B", gradeDistribution.get("B") + 1);
            } else if (score >= 70) {
                gradeDistribution.put("C", gradeDistribution.get("C") + 1);
            } else if (score >= 60) {
                gradeDistribution.put("D", gradeDistribution.get("D") + 1);
            } else {
                gradeDistribution.put("F", gradeDistribution.get("F") + 1);
            }
        }
        
        statisticsDTO.setGradeDistribution(gradeDistribution);
        
        return statisticsDTO;
    }
} 