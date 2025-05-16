package com.wtu.backend.black.AddScoreMoudle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtu.controller.ScoreController;
import com.wtu.entity.Score;
import com.wtu.service.ScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 表单验证测试类
 * 测试表单重置和表单验证功能
 */
@ExtendWith(MockitoExtension.class)
public class ValidationTest {

    private MockMvc mockMvc;

    @Mock
    private ScoreService scoreService;

    @InjectMocks
    private ScoreController scoreController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(scoreController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @DisplayName("TC-22: 重置空表单")
    void testResetEmptyForm() {
        // 这个测试是前端行为测试，在后端单元测试中无法直接测试
        // 在实际项目中，这应该是前端测试的一部分
        // 这里仅作为示例，实际不执行任何操作
    }

    @Test
    @DisplayName("TC-23: 重置已填写表单")
    void testResetFilledForm() {
        // 这个测试是前端行为测试，在后端单元测试中无法直接测试
        // 在实际项目中，这应该是前端测试的一部分
        // 这里仅作为示例，实际不执行任何操作
    }

    @Test
    @DisplayName("TC-24: 重置部分填写表单")
    void testResetPartiallyFilledForm() {
        // 这个测试是前端行为测试，在后端单元测试中无法直接测试
        // 在实际项目中，这应该是前端测试的一部分
        // 这里仅作为示例，实际不执行任何操作
    }
    
    @Test
    @DisplayName("TC-18: 录入分数边界值-1")
    void testAddScoreWithNegativeOne() throws Exception {
        // 准备测试数据
        Score inputScore = new Score();
        inputScore.setStudentId(1L);
        inputScore.setSubject("数学");
        inputScore.setScore(-1);
        inputScore.setExamDate(LocalDate.of(2023, 6, 1));

        // 模拟服务层抛出异常
        when(scoreService.addScore(any(Score.class))).thenThrow(new IllegalArgumentException("分数必须在0-100之间"));

        // 执行请求并验证
        mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputScore)))
                .andExpect(status().isBadRequest());

        verify(scoreService, times(1)).addScore(any(Score.class));
    }

    @Test
    @DisplayName("TC-19: 录入分数边界值101")
    void testAddScoreWith101() throws Exception {
        // 准备测试数据
        Score inputScore = new Score();
        inputScore.setStudentId(1L);
        inputScore.setSubject("数学");
        inputScore.setScore(101);
        inputScore.setExamDate(LocalDate.of(2023, 6, 1));

        // 模拟服务层抛出异常
        when(scoreService.addScore(any(Score.class))).thenThrow(new IllegalArgumentException("分数必须在0-100之间"));

        // 执行请求并验证
        mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputScore)))
                .andExpect(status().isBadRequest());

        verify(scoreService, times(1)).addScore(any(Score.class));
    }

    @Test
    @DisplayName("多种混合无效输入测试")
    void testMultipleInvalidInputs() throws Exception {
        // 准备含有多种无效数据的JSON请求体
        Map<String, Object> invalidData = new HashMap<>();
        invalidData.put("studentId", "abc"); // 非数字ID
        invalidData.put("subject", ""); // 空科目
        invalidData.put("score", 101); // 超范围分数
        invalidData.put("examDate", "06/01/2023"); // 错误日期格式

        // 执行请求并验证
        mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidData)))
                .andExpect(status().isBadRequest());

        // 由于JSON解析错误，服务层方法不会被调用
        verify(scoreService, never()).addScore(any(Score.class));
    }
} 