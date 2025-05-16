package com.wtu.backend.blackbox.ManageScore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtu.controller.ScoreController;
import com.wtu.dto.ScoreDTO;
import com.wtu.result.Result;
import com.wtu.service.ScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 成绩详情查看功能黑盒测试类
 * 基于等价类划分方法设计测试用例
 */
@ExtendWith(MockitoExtension.class)
public class ScoreDetailTest {

    private MockMvc mockMvc;

    @Mock
    private ScoreService scoreService;

    @InjectMocks
    private ScoreController scoreController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(scoreController)
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @DisplayName("TC-D01: 查看有效成绩详情")
    void testGetValidScoreDetail() throws Exception {
        // 准备测试数据
        Long scoreId = 1L;

        // 准备模拟响应
        ScoreDTO scoreDTO = new ScoreDTO();
        scoreDTO.setScoreId(1L);
        scoreDTO.setStudentId(1L);
        scoreDTO.setStudentName("张三");
        scoreDTO.setSubject("数学");
        scoreDTO.setScore(85);

        when(scoreService.getScoreById(scoreId)).thenReturn(scoreDTO);

        // 执行请求并验证
        mockMvc.perform(get("/api/scores/{id}", scoreId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data.scoreId", is(1)))
                .andExpect(jsonPath("$.data.studentId", is(1)))
                .andExpect(jsonPath("$.data.studentName", is("张三")))
                .andExpect(jsonPath("$.data.subject", is("数学")))
                .andExpect(jsonPath("$.data.score", is(85)));

        verify(scoreService, times(1)).getScoreById(scoreId);
    }

    @Test
    @DisplayName("TC-D02: 查看不存在成绩详情")
    void testGetNonExistentScoreDetail() throws Exception {
        // 准备测试数据
        Long scoreId = 999L;

        // 模拟服务层返回空结果
        when(scoreService.getScoreById(scoreId)).thenReturn(null);

        // 执行请求并验证
        mockMvc.perform(get("/api/scores/{id}", scoreId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("成绩不存在")));

        verify(scoreService, times(1)).getScoreById(scoreId);
    }

    @Test
    @DisplayName("TC-D03: 查看非数字成绩ID")
    void testGetScoreWithNonNumericId() throws Exception {
        // 准备测试数据：非数字ID
        String nonNumericId = "abc";

        // 执行请求并验证
        mockMvc.perform(get("/api/scores/{id}", nonNumericId))
                .andExpect(status().isBadRequest()); // 应该返回400错误

        // 因为参数类型不匹配，服务层方法不会被调用
        verify(scoreService, never()).getScoreById(anyLong());
    }

    @Test
    @DisplayName("TC-D04: 查看空成绩ID")
    void testGetScoreWithNullId() throws Exception {
        // 空ID情况实际上无法通过URL发送，但我们可以测试URL路径错误的情况
        // 例如：/api/scores/ 而不是 /api/scores/{id}
        mockMvc.perform(get("/api/scores/"))
                .andExpect(status().isNotFound()); // 应该返回404错误

        // 因为URL不匹配任何处理方法，所以服务层方法不会被调用
        verify(scoreService, never()).getScoreById(anyLong());
    }
} 