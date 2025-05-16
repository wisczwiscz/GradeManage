package com.wtu.backend.blackbox.ManageScore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtu.controller.ScoreController;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 成绩删除功能黑盒测试类
 * 基于等价类划分方法设计测试用例
 */
@ExtendWith(MockitoExtension.class)
public class ScoreDeleteTest {

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
        @DisplayName("TC-R01: 教师删除有效成绩")
        void testTeacherDeleteValidScore() throws Exception {
            // 准备测试数据
            Long scoreId = 1L;

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 模拟服务层返回成功
            when(scoreService.deleteScore(scoreId)).thenReturn(true);

            // 执行请求并验证
            mockMvc.perform(delete("/api/scores/{id}", scoreId)
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data", is(true)));

            verify(scoreService, times(1)).deleteScore(scoreId);
        }

        @Test
        @DisplayName("TC-R05: 学生尝试删除成绩")
        void testStudentDeleteScore() throws Exception {
            // 准备测试数据
            Long scoreId = 1L;

            // 模拟学生角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "student");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 模拟服务层抛出异常
            when(scoreService.deleteScore(scoreId)).thenThrow(new SecurityException("无权限执行此操作"));

            // 执行请求并验证
            mockMvc.perform(delete("/api/scores/{id}", scoreId)
                    .header("Authorization", "Bearer STUDENT_TOKEN"))
                    .andExpect(status().isForbidden());

            verify(scoreService, times(1)).deleteScore(scoreId);
        }
    }

    @Nested
    @DisplayName("中优先级测试用例")
    class MediumPriorityTests {

        @Test
        @DisplayName("TC-R02: 删除不存在成绩ID")
        void testDeleteNonExistentScore() throws Exception {
            // 准备测试数据
            Long scoreId = 999L;

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 模拟服务层返回失败
            when(scoreService.deleteScore(scoreId)).thenReturn(false);

            // 执行请求并验证
            mockMvc.perform(delete("/api/scores/{id}", scoreId)
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(400)))
                    .andExpect(jsonPath("$.message", is("删除失败")));

            verify(scoreService, times(1)).deleteScore(scoreId);
        }
    }

    @Nested
    @DisplayName("低优先级测试用例")
    class LowPriorityTests {

        @Test
        @DisplayName("TC-R03: 删除非数字成绩ID")
        void testDeleteNonNumericScoreId() throws Exception {
            // 准备测试数据：非数字ID
            String nonNumericId = "abc";

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 执行请求并验证
            mockMvc.perform(delete("/api/scores/{id}", nonNumericId)
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isBadRequest()); // 应该返回400错误

            // 因为参数类型不匹配，服务层方法不会被调用
            verify(scoreService, never()).deleteScore(anyLong());
        }

        @Test
        @DisplayName("TC-R04: 删除空成绩ID")
        void testDeleteNullScoreId() throws Exception {
            // 空ID情况实际上无法通过URL发送，但我们可以测试URL路径错误的情况
            // 例如：/api/scores/ 而不是 /api/scores/{id}
            
            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            mockMvc.perform(delete("/api/scores/")
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isNotFound()); // 应该返回404错误

            // 因为URL不匹配任何处理方法，所以服务层方法不会被调用
            verify(scoreService, never()).deleteScore(anyLong());
        }

        @Test
        @DisplayName("TC-R06: 未登录尝试删除成绩")
        void testUnauthenticatedDeleteScore() throws Exception {
            // 准备测试数据
            Long scoreId = 1L;

            // 未设置任何认证信息

            // 模拟服务层抛出异常
            when(scoreService.deleteScore(scoreId)).thenThrow(new SecurityException("用户未登录"));

            // 执行请求并验证（没有提供认证头）
            mockMvc.perform(delete("/api/scores/{id}", scoreId))
                    .andExpect(status().isUnauthorized());

            verify(scoreService, times(1)).deleteScore(scoreId);
        }
    }
} 