package com.wtu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wtu.dto.ScoreDTO;
import com.wtu.dto.ScoreQueryDTO;
import com.wtu.entity.Score;
import com.wtu.result.Result;
import com.wtu.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * 成绩控制器
 */
@RestController
@RequestMapping("/api/scores")
@Tag(name = "成绩管理接口", description = "提供成绩的增删改查功能")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    /**
     * 添加成绩
     * 
     * @param score 成绩对象
     * @return 添加结果
     */
    @PostMapping("/add")
    @Operation(summary = "添加成绩", description = "教师添加学生的课程成绩")
    public Result<Score> addScore(@RequestBody Score score) {
        try {
            Score savedScore = scoreService.addScore(score);
            return Result.success(savedScore);
        } catch (IllegalArgumentException e) {
            // 捕获验证异常，返回失败结果
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常
            return Result.fail("添加成绩失败：" + e.getMessage());
        }
    }

    /**
     * 查询成绩列表
     * 
     * @param queryDTO 查询条件
     * @return 成绩列表
     */
    @PostMapping("/query")
    @Operation(summary = "查询成绩", description = "根据条件查询成绩并支持分页")
    public Result<Page<ScoreDTO>> queryScores(@RequestBody ScoreQueryDTO queryDTO) {
        Page<ScoreDTO> page = scoreService.queryScores(queryDTO);
        return Result.success(page);
    }

    /**
     * 获取成绩详情
     * 
     * @param id 成绩ID
     * @return 成绩详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取成绩详情", description = "根据ID获取成绩详细信息")
    public Result<ScoreDTO> getScoreById(@PathVariable("id") Long id) {
        ScoreDTO scoreDTO = scoreService.getScoreById(id);
        if (scoreDTO == null) {
            return Result.fail("成绩不存在");
        }
        return Result.success(scoreDTO);
    }

    /**
     * 更新成绩
     * 
     * @param score 成绩对象
     * @return 更新结果
     */
    @PutMapping("/update")
    @Operation(summary = "更新成绩", description = "教师更新学生成绩信息")
    public Result<Boolean> updateScore(@RequestBody Score score) {
        try {
            boolean success = scoreService.updateScore(score);
            return success ? Result.success(true) : Result.fail("更新失败");
        } catch (IllegalArgumentException e) {
            // 捕获验证异常，返回失败结果
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常
            return Result.fail("更新成绩失败：" + e.getMessage());
        }
    }

    /**
     * 删除成绩
     * 
     * @param id 成绩ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除成绩", description = "教师删除学生成绩")
    public Result<Boolean> deleteScore(@PathVariable("id") Long id) {
        boolean success = scoreService.deleteScore(id);
        return success ? Result.success(true) : Result.fail("删除失败");
    }
} 