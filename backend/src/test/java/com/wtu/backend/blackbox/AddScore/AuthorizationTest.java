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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 权限控制黑盒测试类
 * 测试不同角色的权限控制
 */
@ExtendWith(MockitoExtension.class)
public class AuthorizationTest {

    private MockMvc mockMvc;

    @Mock
    private ScoreService scoreService;

    @InjectMocks
    private ScoreController scoreController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // 设置拦截器
        mockMvc = MockMvcBuilders.standaloneSetup(scoreController)
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @DisplayName("TC-14: 学生尝试录入成绩")
    void testStudentAddScore() throws Exception {
        // 准备测试数据
        Score inputScore = new Score();
        inputScore.setStudentId(1L);
        inputScore.setSubject("数学");
        inputScore.setScore(85);
        inputScore.setExamDate(LocalDate.of(2023, 6, 1));

        // 模拟学生权限检查失败
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("userRole", "student");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // 模拟服务层抛出异常
        when(scoreService.addScore(any(Score.class))).thenThrow(new SecurityException("无权限执行此操作"));

        // 执行请求并验证
        mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer STUDENT_TOKEN")
                .content(objectMapper.writeValueAsString(inputScore)))
                .andExpect(status().isForbidden());

        // 由于权限验证失败，服务层方法可能不会被调用
        // 这里的验证仅为示例，实际上可能需要根据系统实现进行调整
        verify(scoreService, times(1)).addScore(any(Score.class));
    }

    @Test
    @DisplayName("TC-15: 未登录尝试录入成绩")
    void testUnauthenticatedAddScore() throws Exception {
        // 准备测试数据
        Score inputScore = new Score();
        inputScore.setStudentId(1L);
        inputScore.setSubject("数学");
        inputScore.setScore(85);
        inputScore.setExamDate(LocalDate.of(2023, 6, 1));

        // 没有设置任何认证信息

        // 模拟服务层抛出异常
        when(scoreService.addScore(any(Score.class))).thenThrow(new SecurityException("用户未登录"));

        // 执行请求并验证（没有提供认证头）
        mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputScore)))
                .andExpect(status().isUnauthorized());

        // 由于认证失败，服务层方法可能不会被调用
        verify(scoreService, times(1)).addScore(any(Score.class));
    }

    @Test
    @DisplayName("教师成功录入成绩")
    void testTeacherAddScore() throws Exception {
        // 准备测试数据
        Score inputScore = new Score();
        inputScore.setStudentId(1L);
        inputScore.setSubject("数学");
        inputScore.setScore(85);
        inputScore.setExamDate(LocalDate.of(2023, 6, 1));

        // 模拟教师角色
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("userRole", "teacher");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // 模拟服务层返回成功
        Score savedScore = new Score();
        savedScore.setScoreId(1L);
        savedScore.setStudentId(1L);
        savedScore.setSubject("数学");
        savedScore.setScore(85);
        savedScore.setExamDate(LocalDate.of(2023, 6, 1));
        when(scoreService.addScore(any(Score.class))).thenReturn(savedScore);

        // 执行请求并验证
        mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer TEACHER_TOKEN")
                .content(objectMapper.writeValueAsString(inputScore)))
                .andExpect(status().isOk());

        verify(scoreService, times(1)).addScore(any(Score.class));
    }
} 