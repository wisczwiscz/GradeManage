package com.wtu.backend.blackbox.Statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtu.controller.StatisticsController;
import com.wtu.dto.StatisticsDTO;
import com.wtu.service.StatisticsService;
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

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 全科目统计功能黑盒测试类
 * 基于等价类划分方法设计测试用例
 */
@ExtendWith(MockitoExtension.class)
public class AllSubjectsStatisticsTest {

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
        @DisplayName("TC-A01: 教师查看全科目统计")
        void testTeacherViewAllSubjectsStatistics() throws Exception {
            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 准备模拟响应
            List<StatisticsDTO> statisticsList = new ArrayList<>();
            
            // 数学科目统计
            StatisticsDTO mathStatistics = new StatisticsDTO();
            mathStatistics.setSubject("数学");
            mathStatistics.setAvgScore(75.5);
            mathStatistics.setMaxScore(100);
            mathStatistics.setMinScore(40);
            mathStatistics.setPassCount(18);
            mathStatistics.setFailCount(7);

            Map<String, Integer> mathGradeDistribution = new HashMap<>();
            mathGradeDistribution.put("A", 5);
            mathGradeDistribution.put("B", 7);
            mathGradeDistribution.put("C", 4);
            mathGradeDistribution.put("D", 2);
            mathGradeDistribution.put("F", 7);
            mathStatistics.setGradeDistribution(mathGradeDistribution);
            
            // 语文科目统计
            StatisticsDTO chineseStatistics = new StatisticsDTO();
            chineseStatistics.setSubject("语文");
            chineseStatistics.setAvgScore(80.2);
            chineseStatistics.setMaxScore(98);
            chineseStatistics.setMinScore(55);
            chineseStatistics.setPassCount(22);
            chineseStatistics.setFailCount(3);

            Map<String, Integer> chineseGradeDistribution = new HashMap<>();
            chineseGradeDistribution.put("A", 8);
            chineseGradeDistribution.put("B", 9);
            chineseGradeDistribution.put("C", 3);
            chineseGradeDistribution.put("D", 2);
            chineseGradeDistribution.put("F", 3);
            chineseStatistics.setGradeDistribution(chineseGradeDistribution);
            
            statisticsList.add(mathStatistics);
            statisticsList.add(chineseStatistics);

            when(statisticsService.getAllSubjectsStatistics()).thenReturn(statisticsList);

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/basic")
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data", hasSize(2)))
                    .andExpect(jsonPath("$.data[0].subject", is("数学")))
                    .andExpect(jsonPath("$.data[1].subject", is("语文")))
                    .andExpect(jsonPath("$.data[0].avgScore", is(75.5)))
                    .andExpect(jsonPath("$.data[1].avgScore", is(80.2)));

            verify(statisticsService, times(1)).getAllSubjectsStatistics();
        }

        @Test
        @DisplayName("TC-A02: 学生查看全科目统计")
        void testStudentViewAllSubjectsStatistics() throws Exception {
            // 模拟学生角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "student");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 准备模拟响应
            List<StatisticsDTO> statisticsList = new ArrayList<>();
            
            // 数学科目统计
            StatisticsDTO mathStatistics = new StatisticsDTO();
            mathStatistics.setSubject("数学");
            mathStatistics.setAvgScore(75.5);
            mathStatistics.setMaxScore(100);
            mathStatistics.setMinScore(40);
            mathStatistics.setPassCount(18);
            mathStatistics.setFailCount(7);
            
            // 语文科目统计
            StatisticsDTO chineseStatistics = new StatisticsDTO();
            chineseStatistics.setSubject("语文");
            chineseStatistics.setAvgScore(80.2);
            chineseStatistics.setMaxScore(98);
            chineseStatistics.setMinScore(55);
            chineseStatistics.setPassCount(22);
            chineseStatistics.setFailCount(3);
            
            statisticsList.add(mathStatistics);
            statisticsList.add(chineseStatistics);

            when(statisticsService.getAllSubjectsStatistics()).thenReturn(statisticsList);

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/basic")
                    .header("Authorization", "Bearer STUDENT_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data", hasSize(2)));

            verify(statisticsService, times(1)).getAllSubjectsStatistics();
        }
    }

    @Nested
    @DisplayName("中优先级测试用例")
    class MediumPriorityTests {

        @Test
        @DisplayName("TC-A03: 查看包含无记录科目的统计")
        void testViewStatisticsWithEmptySubjects() throws Exception {
            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 准备模拟响应（只返回有记录的科目）
            List<StatisticsDTO> statisticsList = new ArrayList<>();
            
            // 数学科目统计
            StatisticsDTO mathStatistics = new StatisticsDTO();
            mathStatistics.setSubject("数学");
            mathStatistics.setAvgScore(75.5);
            mathStatistics.setMaxScore(100);
            mathStatistics.setMinScore(40);
            statisticsList.add(mathStatistics);
            
            // 系统中虽然有语文科目，但没有成绩记录，所以不会返回该科目统计

            when(statisticsService.getAllSubjectsStatistics()).thenReturn(statisticsList);

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/basic")
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andExpect(jsonPath("$.data[0].subject", is("数学")));

            verify(statisticsService, times(1)).getAllSubjectsStatistics();
        }

        @Test
        @DisplayName("TC-A06: 查看包含所有等级的科目统计")
        void testViewStatisticsWithAllGrades() throws Exception {
            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 准备模拟响应
            List<StatisticsDTO> statisticsList = new ArrayList<>();
            
            // 数学科目统计（包含所有等级）
            StatisticsDTO mathStatistics = new StatisticsDTO();
            mathStatistics.setSubject("数学");
            mathStatistics.setAvgScore(75.5);
            mathStatistics.setMaxScore(100);
            mathStatistics.setMinScore(40);
            mathStatistics.setPassCount(18);
            mathStatistics.setFailCount(7);

            Map<String, Integer> mathGradeDistribution = new HashMap<>();
            mathGradeDistribution.put("A", 5);
            mathGradeDistribution.put("B", 7);
            mathGradeDistribution.put("C", 4);
            mathGradeDistribution.put("D", 2);
            mathGradeDistribution.put("F", 7);
            mathStatistics.setGradeDistribution(mathGradeDistribution);
            
            statisticsList.add(mathStatistics);

            when(statisticsService.getAllSubjectsStatistics()).thenReturn(statisticsList);

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/basic")
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data[0].gradeDistribution.A", is(5)))
                    .andExpect(jsonPath("$.data[0].gradeDistribution.B", is(7)))
                    .andExpect(jsonPath("$.data[0].gradeDistribution.C", is(4)))
                    .andExpect(jsonPath("$.data[0].gradeDistribution.D", is(2)))
                    .andExpect(jsonPath("$.data[0].gradeDistribution.F", is(7)));

            verify(statisticsService, times(1)).getAllSubjectsStatistics();
        }
    }

    @Nested
    @DisplayName("低优先级测试用例")
    class LowPriorityTests {

        @Test
        @DisplayName("TC-A04: 查看系统中无任何记录的统计")
        void testViewStatisticsWithNoRecords() throws Exception {
            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 模拟服务层返回空列表（无任何记录）
            when(statisticsService.getAllSubjectsStatistics()).thenReturn(Collections.emptyList());

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/basic")
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data", hasSize(0)));

            verify(statisticsService, times(1)).getAllSubjectsStatistics();
        }

        @Test
        @DisplayName("TC-A05: 未登录查看全科目统计")
        void testUnauthenticatedViewAllSubjectsStatistics() throws Exception {
            // 未设置任何认证信息

            // 模拟服务层抛出异常
            when(statisticsService.getAllSubjectsStatistics()).thenThrow(new SecurityException("用户未登录"));

            // 执行请求并验证（没有提供认证头）
            mockMvc.perform(get("/api/statistics/basic"))
                    .andExpect(status().isUnauthorized());

            // 服务层方法可能会被调用，但会抛出异常
            verify(statisticsService, times(1)).getAllSubjectsStatistics();
        }

        @Test
        @DisplayName("TC-A07: 查看单一等级的科目统计")
        void testViewStatisticsWithSingleGrade() throws Exception {
            // 模拟教师角色
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("userRole", "teacher");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            // 准备模拟响应
            List<StatisticsDTO> statisticsList = new ArrayList<>();
            
            // 科目统计（只有A级）
            StatisticsDTO statistics = new StatisticsDTO();
            statistics.setSubject("只有A级的科目");
            statistics.setAvgScore(95.5);
            statistics.setMaxScore(100);
            statistics.setMinScore(90);
            statistics.setPassCount(10);
            statistics.setFailCount(0);

            Map<String, Integer> gradeDistribution = new HashMap<>();
            gradeDistribution.put("A", 10);
            gradeDistribution.put("B", 0);
            gradeDistribution.put("C", 0);
            gradeDistribution.put("D", 0);
            gradeDistribution.put("F", 0);
            statistics.setGradeDistribution(gradeDistribution);
            
            statisticsList.add(statistics);

            when(statisticsService.getAllSubjectsStatistics()).thenReturn(statisticsList);

            // 执行请求并验证
            mockMvc.perform(get("/api/statistics/basic")
                    .header("Authorization", "Bearer TEACHER_TOKEN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data[0].gradeDistribution.A", is(10)))
                    .andExpect(jsonPath("$.data[0].gradeDistribution.B", is(0)))
                    .andExpect(jsonPath("$.data[0].gradeDistribution.C", is(0)))
                    .andExpect(jsonPath("$.data[0].gradeDistribution.D", is(0)))
                    .andExpect(jsonPath("$.data[0].gradeDistribution.F", is(0)));

            verify(statisticsService, times(1)).getAllSubjectsStatistics();
        }
    }
} 