package com.wtu.backend.blackbox.ManageScore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtu.controller.ScoreController;
import com.wtu.entity.Score;
import com.wtu.result.Result;
import com.wtu.service.ScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 成绩编辑功能黑盒测试类
 * 基于等价类划分方法设计测试用例
 */
@ExtendWith(MockitoExtension.class)
public class ScoreUpdateTest {

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

    @Nested
    @DisplayName("高优先级测试用例")
    class HighPriorityTests {

        @Test
        @DisplayName("TC-E01: 教师修改有效成绩")
        void testTeacherUpdateValidScore() throws Exception {
            // 准备测试数据
            Score score = new Score();
            score.setScoreId(1L);
            score.setStudentId(1L);
            score.setSubject("数学");
            score.setScore(85);
            score.setExamDate(LocalDate.of(2023, 6, 1));

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 模拟服务层返回成功
            when(scoreService.updateScore(any(Score.class))).thenReturn(true);

            // 执行请求并验证
            mockMvc.perform(put("/api/scores/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer TEACHER_TOKEN")
                    .content(objectMapper.writeValueAsString(score)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data", is(true)));

            verify(scoreService, times(1)).updateScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-E05: 学生尝试修改成绩")
        void testStudentUpdateScore() throws Exception {
            // 准备测试数据
            Score score = new Score();
            score.setScoreId(1L);
            score.setStudentId(1L);
            score.setSubject("数学");
            score.setScore(85);
            score.setExamDate(LocalDate.of(2023, 6, 1));

            // 模拟学生角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "student");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 模拟服务层抛出异常
            when(scoreService.updateScore(any(Score.class))).thenThrow(new SecurityException("无权限执行此操作"));

            // 执行请求并验证
            mockMvc.perform(put("/api/scores/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer STUDENT_TOKEN")
                    .content(objectMapper.writeValueAsString(score)))
                    .andExpect(status().isForbidden());

            verify(scoreService, times(1)).updateScore(any(Score.class));
        }
    }

    @Nested
    @DisplayName("中优先级测试用例")
    class MediumPriorityTests {

        @Test
        @DisplayName("TC-E03: 修改成绩为负数")
        void testUpdateScoreWithNegativeValue() throws Exception {
            // 准备测试数据
            Score score = new Score();
            score.setScoreId(1L);
            score.setStudentId(1L);
            score.setSubject("数学");
            score.setScore(-5); // 负数分数
            score.setExamDate(LocalDate.of(2023, 6, 1));

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 模拟服务层抛出异常
            when(scoreService.updateScore(any(Score.class))).thenThrow(new IllegalArgumentException("分数必须在0-100之间"));

            // 执行请求并验证
            mockMvc.perform(put("/api/scores/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer TEACHER_TOKEN")
                    .content(objectMapper.writeValueAsString(score)))
                    .andExpect(status().isBadRequest());

            verify(scoreService, times(1)).updateScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-E04: 修改成绩超过100")
        void testUpdateScoreExceeding100() throws Exception {
            // 准备测试数据
            Score score = new Score();
            score.setScoreId(1L);
            score.setStudentId(1L);
            score.setSubject("数学");
            score.setScore(101); // 超过100的分数
            score.setExamDate(LocalDate.of(2023, 6, 1));

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 模拟服务层抛出异常
            when(scoreService.updateScore(any(Score.class))).thenThrow(new IllegalArgumentException("分数必须在0-100之间"));

            // 执行请求并验证
            mockMvc.perform(put("/api/scores/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer TEACHER_TOKEN")
                    .content(objectMapper.writeValueAsString(score)))
                    .andExpect(status().isBadRequest());

            verify(scoreService, times(1)).updateScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-E07: 修改成绩为及格边界值60")
        void testUpdateScoreWithBoundary60() throws Exception {
            // 准备测试数据
            Score score = new Score();
            score.setScoreId(1L);
            score.setStudentId(1L);
            score.setSubject("数学");
            score.setScore(60); // 及格边界值
            score.setExamDate(LocalDate.of(2023, 6, 1));

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 模拟服务层返回成功
            when(scoreService.updateScore(any(Score.class))).thenReturn(true);

            // 执行请求并验证
            mockMvc.perform(put("/api/scores/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer TEACHER_TOKEN")
                    .content(objectMapper.writeValueAsString(score)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data", is(true)));

            verify(scoreService, times(1)).updateScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-E08: 修改成绩为及格边界值59")
        void testUpdateScoreWithBoundary59() throws Exception {
            // 准备测试数据
            Score score = new Score();
            score.setScoreId(1L);
            score.setStudentId(1L);
            score.setSubject("数学");
            score.setScore(59); // 不及格边界值
            score.setExamDate(LocalDate.of(2023, 6, 1));

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 模拟服务层返回成功
            when(scoreService.updateScore(any(Score.class))).thenReturn(true);

            // 执行请求并验证
            mockMvc.perform(put("/api/scores/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer TEACHER_TOKEN")
                    .content(objectMapper.writeValueAsString(score)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data", is(true)));

            verify(scoreService, times(1)).updateScore(any(Score.class));
        }
    }

    @Nested
    @DisplayName("低优先级测试用例")
    class LowPriorityTests {

        @Test
        @DisplayName("TC-E02: 修改不存在成绩ID")
        void testUpdateNonExistentScore() throws Exception {
            // 准备测试数据
            Score score = new Score();
            score.setScoreId(999L); // 不存在的ID
            score.setStudentId(1L);
            score.setSubject("数学");
            score.setScore(85);
            score.setExamDate(LocalDate.of(2023, 6, 1));

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 模拟服务层返回失败
            when(scoreService.updateScore(any(Score.class))).thenReturn(false);

            // 执行请求并验证
            mockMvc.perform(put("/api/scores/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer TEACHER_TOKEN")
                    .content(objectMapper.writeValueAsString(score)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(400)))
                    .andExpect(jsonPath("$.message", is("更新失败")));

            verify(scoreService, times(1)).updateScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-E06: 未登录尝试修改成绩")
        void testUnauthenticatedUpdateScore() throws Exception {
            // 准备测试数据
            Score score = new Score();
            score.setScoreId(1L);
            score.setStudentId(1L);
            score.setSubject("数学");
            score.setScore(85);
            score.setExamDate(LocalDate.of(2023, 6, 1));

            // 未设置任何认证信息

            // 模拟服务层抛出异常
            when(scoreService.updateScore(any(Score.class))).thenThrow(new SecurityException("用户未登录"));

            // 执行请求并验证（没有提供认证头）
            mockMvc.perform(put("/api/scores/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(score)))
                    .andExpect(status().isUnauthorized());

            verify(scoreService, times(1)).updateScore(any(Score.class));
        }
    }
} 