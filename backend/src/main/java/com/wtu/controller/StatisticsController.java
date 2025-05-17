package com.wtu.controller;

import com.wtu.dto.StatisticsDTO;
import com.wtu.result.Result;
import com.wtu.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 统计分析控制器
 */
@RestController
@RequestMapping("/api/statistics")
@Tag(name = "成绩统计分析接口", description = "提供成绩统计分析功能")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * 获取指定科目的统计数据
     *
     * @param subject 科目名称
     * @return 统计结果
     */
    @GetMapping(value = "/subject/{subject}", produces = "application/json;charset=UTF-8")
    @Operation(summary = "获取科目统计数据", description = "获取指定科目的成绩统计分析数据")
    public Result<StatisticsDTO> getStatisticsBySubject(@PathVariable String subject) {
        StatisticsDTO statistics = statisticsService.getStatisticsBySubject(subject);
        return Result.success(statistics);
    }

    /**
     * 获取所有科目的基础统计数据
     *
     * @return 所有科目的统计结果
     */
    @GetMapping(value = "/basic", produces = "application/json;charset=UTF-8")
    @Operation(summary = "获取基础统计数据", description = "获取所有科目的成绩统计分析数据")
    public Result<List<StatisticsDTO>> getAllSubjectsStatistics() {
        List<StatisticsDTO> statisticsList = statisticsService.getAllSubjectsStatistics();
        return Result.success(statisticsList);
    }
} 