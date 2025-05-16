package com.wtu.backend.blackbox.Statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtu.controller.StatisticsController;
import com.wtu.dto.StatisticsDTO;
import com.wtu.result.Result;
import com.wtu.service.StatisticsService;
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

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 单科目统计功能黑盒测试类
 * 基于等价类划分方法设计测试用例
 */
@ExtendWith(MockitoExtension.class)
public class SubjectStatisticsTest {

    private MockMvc mockMvc;

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private StatisticsController statisticsController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(statisticsController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Nested
    @DisplayName("高优先级测试用例")
    class HighPriorityTests {

        @Test
        @DisplayName("TC-S01: 教师查看有效科目统计")
        void testTeacherViewValidSubjectStatistics() throws Exception {
            // 准备测试数据
            String subject = "数学";

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 准备模拟响应
            StatisticsDTO statisticsDTO = new StatisticsDTO();
            statisticsDTO.setSubject(subject);
            statisticsDTO.setAvgScore(75.5);
            statisticsDTO.setMaxScore(100);
            statisticsDTO.setMinScore(40);
            statisticsDTO.setPassCount(18);
            statisticsDTO.setFailCount(7);

            Map<String, Integer> gradeDistribution = new HashMap<>();
            gradeDistribution.put("A", 5);
            gradeDistribution.put("B", 7);
            gradeDistribution.put("C", 4);
            gradeDistribution.put("D", 2);
            gradeDistribution.put("F", 7);
            statisticsDTO.setGradeDistribution(gradeDistribution);

            when(statisticsService.getStatisticsBySubject(subject)).thenReturn(statisticsDTO);

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/subject/{subject}", subject)
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.subject", is("数学")))
                    .andExpect(jsonPath("$.data.avgScore", is(75.5)))
                    .andExpect(jsonPath("$.data.maxScore", is(100)))
                    .andExpect(jsonPath("$.data.minScore", is(40)))
                    .andExpect(jsonPath("$.data.passCount", is(18)))
                    .andExpect(jsonPath("$.data.failCount", is(7)))
                    .andExpect(jsonPath("$.data.gradeDistribution.A", is(5)))
                    .andExpect(jsonPath("$.data.gradeDistribution.B", is(7)))
                    .andExpect(jsonPath("$.data.gradeDistribution.C", is(4)))
                    .andExpect(jsonPath("$.data.gradeDistribution.D", is(2)))
                    .andExpect(jsonPath("$.data.gradeDistribution.F", is(7)));

            verify(statisticsService, times(1)).getStatisticsBySubject(subject);
        }

        @Test
        @DisplayName("TC-S02: 学生查看有效科目统计")
        void testStudentViewValidSubjectStatistics() throws Exception {
            // 准备测试数据
            String subject = "数学";

            // 模拟学生角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "student");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 准备模拟响应
            StatisticsDTO statisticsDTO = new StatisticsDTO();
            statisticsDTO.setSubject(subject);
            statisticsDTO.setAvgScore(75.5);
            statisticsDTO.setMaxScore(100);
            statisticsDTO.setMinScore(40);
            statisticsDTO.setPassCount(18);
            statisticsDTO.setFailCount(7);

            Map<String, Integer> gradeDistribution = new HashMap<>();
            gradeDistribution.put("A", 5);
            gradeDistribution.put("B", 7);
            gradeDistribution.put("C", 4);
            gradeDistribution.put("D", 2);
            gradeDistribution.put("F", 7);
            statisticsDTO.setGradeDistribution(gradeDistribution);

            when(statisticsService.getStatisticsBySubject(subject)).thenReturn(statisticsDTO);

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/subject/{subject}", subject)
                    .header("Authorization", "Bearer STUDENT_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.subject", is("数学")));

            verify(statisticsService, times(1)).getStatisticsBySubject(subject);
        }

        @Test
        @DisplayName("TC-S07: 查看全部及格科目统计")
        void testViewAllPassSubjectStatistics() throws Exception {
            // 准备测试数据
            String subject = "全部及格科目";

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 准备模拟响应
            StatisticsDTO statisticsDTO = new StatisticsDTO();
            statisticsDTO.setSubject(subject);
            statisticsDTO.setAvgScore(85.5);
            statisticsDTO.setMaxScore(100);
            statisticsDTO.setMinScore(60);
            statisticsDTO.setPassCount(25);
            statisticsDTO.setFailCount(0);

            Map<String, Integer> gradeDistribution = new HashMap<>();
            gradeDistribution.put("A", 8);
            gradeDistribution.put("B", 10);
            gradeDistribution.put("C", 5);
            gradeDistribution.put("D", 2);
            gradeDistribution.put("F", 0);
            statisticsDTO.setGradeDistribution(gradeDistribution);

            when(statisticsService.getStatisticsBySubject(subject)).thenReturn(statisticsDTO);

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/subject/{subject}", subject)
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.failCount", is(0)))
                    .andExpect(jsonPath("$.data.gradeDistribution.F", is(0)));

            verify(statisticsService, times(1)).getStatisticsBySubject(subject);
        }

        @Test
        @DisplayName("TC-S08: 查看全部不及格科目统计")
        void testViewAllFailSubjectStatistics() throws Exception {
            // 准备测试数据
            String subject = "全部不及格科目";

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 准备模拟响应
            StatisticsDTO statisticsDTO = new StatisticsDTO();
            statisticsDTO.setSubject(subject);
            statisticsDTO.setAvgScore(45.5);
            statisticsDTO.setMaxScore(59);
            statisticsDTO.setMinScore(20);
            statisticsDTO.setPassCount(0);
            statisticsDTO.setFailCount(15);

            Map<String, Integer> gradeDistribution = new HashMap<>();
            gradeDistribution.put("A", 0);
            gradeDistribution.put("B", 0);
            gradeDistribution.put("C", 0);
            gradeDistribution.put("D", 0);
            gradeDistribution.put("F", 15);
            statisticsDTO.setGradeDistribution(gradeDistribution);

            when(statisticsService.getStatisticsBySubject(subject)).thenReturn(statisticsDTO);

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/subject/{subject}", subject)
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.passCount", is(0)))
                    .andExpect(jsonPath("$.data.gradeDistribution.A", is(0)))
                    .andExpect(jsonPath("$.data.gradeDistribution.B", is(0)))
                    .andExpect(jsonPath("$.data.gradeDistribution.C", is(0)))
                    .andExpect(jsonPath("$.data.gradeDistribution.D", is(0)))
                    .andExpect(jsonPath("$.data.gradeDistribution.F", is(15)));

            verify(statisticsService, times(1)).getStatisticsBySubject(subject);
        }
    }

    @Nested
    @DisplayName("中优先级测试用例")
    class MediumPriorityTests {

        @Test
        @DisplayName("TC-S03: 查看不存在科目统计")
        void testViewNonExistentSubjectStatistics() throws Exception {
            // 准备测试数据
            String subject = "不存在科目";

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 模拟服务层返回空结果
            when(statisticsService.getStatisticsBySubject(subject)).thenReturn(null);

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/subject/{subject}", subject)
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(400)))
                    .andExpect(jsonPath("$.message", containsString("没有成绩记录")));

            verify(statisticsService, times(1)).getStatisticsBySubject(subject);
        }

        @Test
        @DisplayName("TC-S04: 查看单条记录科目统计")
        void testViewSingleRecordSubjectStatistics() throws Exception {
            // 准备测试数据
            String subject = "仅有一条记录的科目";

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 准备模拟响应
            StatisticsDTO statisticsDTO = new StatisticsDTO();
            statisticsDTO.setSubject(subject);
            statisticsDTO.setAvgScore(85.0);
            statisticsDTO.setMaxScore(85);
            statisticsDTO.setMinScore(85);
            statisticsDTO.setPassCount(1);
            statisticsDTO.setFailCount(0);

            Map<String, Integer> gradeDistribution = new HashMap<>();
            gradeDistribution.put("A", 0);
            gradeDistribution.put("B", 1);
            gradeDistribution.put("C", 0);
            gradeDistribution.put("D", 0);
            gradeDistribution.put("F", 0);
            statisticsDTO.setGradeDistribution(gradeDistribution);

            when(statisticsService.getStatisticsBySubject(subject)).thenReturn(statisticsDTO);

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/subject/{subject}", subject)
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.avgScore", is(85.0)))
                    .andExpect(jsonPath("$.data.maxScore", is(85)))
                    .andExpect(jsonPath("$.data.minScore", is(85)));

            verify(statisticsService, times(1)).getStatisticsBySubject(subject);
        }

        @Test
        @DisplayName("TC-S05: 查看空记录科目统计")
        void testViewEmptyRecordSubjectStatistics() throws Exception {
            // 准备测试数据
            String subject = "空记录科目";

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 模拟服务层返回空结果
            when(statisticsService.getStatisticsBySubject(subject)).thenReturn(null);

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/subject/{subject}", subject)
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(400)))
                    .andExpect(jsonPath("$.message", containsString("没有成绩记录")));

            verify(statisticsService, times(1)).getStatisticsBySubject(subject);
        }

        @Test
        @DisplayName("TC-S10: 查看及格边界值科目统计")
        void testViewBoundaryValueSubjectStatistics() throws Exception {
            // 准备测试数据
            String subject = "包含59分和60分的科目";

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 准备模拟响应
            StatisticsDTO statisticsDTO = new StatisticsDTO();
            statisticsDTO.setSubject(subject);
            statisticsDTO.setAvgScore(59.5);
            statisticsDTO.setMaxScore(60);
            statisticsDTO.setMinScore(59);
            statisticsDTO.setPassCount(1);
            statisticsDTO.setFailCount(1);

            Map<String, Integer> gradeDistribution = new HashMap<>();
            gradeDistribution.put("A", 0);
            gradeDistribution.put("B", 0);
            gradeDistribution.put("C", 0);
            gradeDistribution.put("D", 1);
            gradeDistribution.put("F", 1);
            statisticsDTO.setGradeDistribution(gradeDistribution);

            when(statisticsService.getStatisticsBySubject(subject)).thenReturn(statisticsDTO);

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/subject/{subject}", subject)
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.passCount", is(1)))
                    .andExpect(jsonPath("$.data.failCount", is(1)))
                    .andExpect(jsonPath("$.data.gradeDistribution.D", is(1)))
                    .andExpect(jsonPath("$.data.gradeDistribution.F", is(1)));

            verify(statisticsService, times(1)).getStatisticsBySubject(subject);
        }
    }

    @Nested
    @DisplayName("低优先级测试用例")
    class LowPriorityTests {

        @Test
        @DisplayName("TC-S06: 查看所有分数相同科目")
        void testViewAllSameScoreSubjectStatistics() throws Exception {
            // 准备测试数据
            String subject = "所有分数都是85分的科目";

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 准备模拟响应
            StatisticsDTO statisticsDTO = new StatisticsDTO();
            statisticsDTO.setSubject(subject);
            statisticsDTO.setAvgScore(85.0);
            statisticsDTO.setMaxScore(85);
            statisticsDTO.setMinScore(85);
            statisticsDTO.setPassCount(20);
            statisticsDTO.setFailCount(0);

            Map<String, Integer> gradeDistribution = new HashMap<>();
            gradeDistribution.put("A", 0);
            gradeDistribution.put("B", 20);
            gradeDistribution.put("C", 0);
            gradeDistribution.put("D", 0);
            gradeDistribution.put("F", 0);
            statisticsDTO.setGradeDistribution(gradeDistribution);

            when(statisticsService.getStatisticsBySubject(subject)).thenReturn(statisticsDTO);

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/subject/{subject}", subject)
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.avgScore", is(85.0)))
                    .andExpect(jsonPath("$.data.maxScore", is(85)))
                    .andExpect(jsonPath("$.data.minScore", is(85)));

            verify(statisticsService, times(1)).getStatisticsBySubject(subject);
        }

        @Test
        @DisplayName("TC-S09: 查看特殊字符科目统计")
        void testViewSpecialCharacterSubjectStatistics() throws Exception {
            // 准备测试数据
            String subject = "特殊字符#$%";

            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 准备模拟响应
            StatisticsDTO statisticsDTO = new StatisticsDTO();
            statisticsDTO.setSubject(subject);
            statisticsDTO.setAvgScore(75.0);
            statisticsDTO.setMaxScore(95);
            statisticsDTO.setMinScore(45);
            statisticsDTO.setPassCount(8);
            statisticsDTO.setFailCount(2);

            when(statisticsService.getStatisticsBySubject(subject)).thenReturn(statisticsDTO);

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/subject/{subject}", subject)
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.subject", is("特殊字符#$%")));

            verify(statisticsService, times(1)).getStatisticsBySubject(subject);
        }

        @Test
        @DisplayName("TC-S11: 未登录查看科目统计")
        void testUnauthenticatedViewSubjectStatistics() throws Exception {
            // 准备测试数据
            String subject = "数学";

            // 未设置任何认证信息

            // 模拟服务层抛出异常
            when(statisticsService.getStatisticsBySubject(anyString())).thenThrow(new SecurityException("用户未登录"));

            // 执行请求并验证（没有提供认证头）
            mockMvc.perform(get("/api/statistics/subject/{subject}", subject))
                    .andExpect(status().isUnauthorized());

            // 服务层方法可能会被调用，但会抛出异常
            verify(statisticsService, times(1)).getStatisticsBySubject(subject);
        }
    }
} 