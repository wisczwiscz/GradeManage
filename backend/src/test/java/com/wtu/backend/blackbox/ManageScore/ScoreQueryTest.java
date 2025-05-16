package com.wtu.backend.blackbox.ManageScore;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtu.controller.ScoreController;
import com.wtu.dto.ScoreDTO;
import com.wtu.dto.ScoreQueryDTO;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 成绩查询功能黑盒测试类
 * 基于等价类划分方法设计测试用例
 */
@ExtendWith(MockitoExtension.class)
public class ScoreQueryTest {

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
        @DisplayName("TC-Q01: 按有效学生ID查询")
        void testQueryByValidStudentId() throws Exception {
            // 准备查询条件
            ScoreQueryDTO queryDTO = new ScoreQueryDTO();
            queryDTO.setStudentId(1L);
            queryDTO.setPageNum(1);
            queryDTO.setPageSize(10);

            // 准备模拟响应
            Page<ScoreDTO> page = new Page<>(1, 10);
            List<ScoreDTO> records = new ArrayList<>();
            ScoreDTO scoreDTO = new ScoreDTO();
            scoreDTO.setScoreId(1L);
            scoreDTO.setStudentId(1L);
            scoreDTO.setStudentName("张三");
            scoreDTO.setSubject("数学");
            scoreDTO.setScore(85);
            records.add(scoreDTO);
            page.setRecords(records);
            page.setTotal(1);

            when(scoreService.queryScores(any(ScoreQueryDTO.class))).thenReturn(page);

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/query")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(queryDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.records", hasSize(1)))
                    .andExpect(jsonPath("$.data.records[0].studentId", is(1)))
                    .andExpect(jsonPath("$.data.records[0].studentName", is("张三")))
                    .andExpect(jsonPath("$.data.records[0].subject", is("数学")))
                    .andExpect(jsonPath("$.data.records[0].score", is(85)));

            verify(scoreService, times(1)).queryScores(any(ScoreQueryDTO.class));
        }

        @Test
        @DisplayName("TC-Q03: 按有效学生姓名查询")
        void testQueryByValidStudentName() throws Exception {
            // 准备查询条件
            ScoreQueryDTO queryDTO = new ScoreQueryDTO();
            queryDTO.setStudentName("张三");
            queryDTO.setPageNum(1);
            queryDTO.setPageSize(10);

            // 准备模拟响应
            Page<ScoreDTO> page = new Page<>(1, 10);
            List<ScoreDTO> records = new ArrayList<>();
            ScoreDTO scoreDTO = new ScoreDTO();
            scoreDTO.setScoreId(1L);
            scoreDTO.setStudentId(1L);
            scoreDTO.setStudentName("张三");
            scoreDTO.setSubject("数学");
            scoreDTO.setScore(85);
            records.add(scoreDTO);
            page.setRecords(records);
            page.setTotal(1);

            when(scoreService.queryScores(any(ScoreQueryDTO.class))).thenReturn(page);

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/query")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(queryDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.records", hasSize(1)))
                    .andExpect(jsonPath("$.data.records[0].studentName", is("张三")));

            verify(scoreService, times(1)).queryScores(any(ScoreQueryDTO.class));
        }

        @Test
        @DisplayName("TC-Q05: 按有效科目查询")
        void testQueryByValidSubject() throws Exception {
            // 准备查询条件
            ScoreQueryDTO queryDTO = new ScoreQueryDTO();
            queryDTO.setSubject("数学");
            queryDTO.setPageNum(1);
            queryDTO.setPageSize(10);

            // 准备模拟响应
            Page<ScoreDTO> page = new Page<>(1, 10);
            List<ScoreDTO> records = new ArrayList<>();
            ScoreDTO scoreDTO1 = new ScoreDTO();
            scoreDTO1.setScoreId(1L);
            scoreDTO1.setStudentId(1L);
            scoreDTO1.setStudentName("张三");
            scoreDTO1.setSubject("数学");
            scoreDTO1.setScore(85);
            records.add(scoreDTO1);
            
            ScoreDTO scoreDTO2 = new ScoreDTO();
            scoreDTO2.setScoreId(2L);
            scoreDTO2.setStudentId(2L);
            scoreDTO2.setStudentName("李四");
            scoreDTO2.setSubject("数学");
            scoreDTO2.setScore(90);
            records.add(scoreDTO2);
            
            page.setRecords(records);
            page.setTotal(2);

            when(scoreService.queryScores(any(ScoreQueryDTO.class))).thenReturn(page);

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/query")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(queryDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.records", hasSize(2)))
                    .andExpect(jsonPath("$.data.records[0].subject", is("数学")))
                    .andExpect(jsonPath("$.data.records[1].subject", is("数学")));

            verify(scoreService, times(1)).queryScores(any(ScoreQueryDTO.class));
        }

        @Test
        @DisplayName("TC-Q07: 多条件组合查询")
        void testQueryByMultipleConditions() throws Exception {
            // 准备查询条件
            ScoreQueryDTO queryDTO = new ScoreQueryDTO();
            queryDTO.setStudentId(1L);
            queryDTO.setSubject("数学");
            queryDTO.setPageNum(1);
            queryDTO.setPageSize(10);

            // 准备模拟响应
            Page<ScoreDTO> page = new Page<>(1, 10);
            List<ScoreDTO> records = new ArrayList<>();
            ScoreDTO scoreDTO = new ScoreDTO();
            scoreDTO.setScoreId(1L);
            scoreDTO.setStudentId(1L);
            scoreDTO.setStudentName("张三");
            scoreDTO.setSubject("数学");
            scoreDTO.setScore(85);
            records.add(scoreDTO);
            page.setRecords(records);
            page.setTotal(1);

            when(scoreService.queryScores(any(ScoreQueryDTO.class))).thenReturn(page);

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/query")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(queryDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.records", hasSize(1)))
                    .andExpect(jsonPath("$.data.records[0].studentId", is(1)))
                    .andExpect(jsonPath("$.data.records[0].subject", is("数学")));

            verify(scoreService, times(1)).queryScores(any(ScoreQueryDTO.class));
        }
    }

    @Nested
    @DisplayName("中优先级测试用例")
    class MediumPriorityTests {

        @Test
        @DisplayName("TC-Q02: 按不存在学生ID查询")
        void testQueryByNonExistentStudentId() throws Exception {
            // 准备查询条件
            ScoreQueryDTO queryDTO = new ScoreQueryDTO();
            queryDTO.setStudentId(999L);
            queryDTO.setPageNum(1);
            queryDTO.setPageSize(10);

            // 准备模拟响应（空结果）
            Page<ScoreDTO> page = new Page<>(1, 10);
            page.setRecords(Collections.emptyList());
            page.setTotal(0);

            when(scoreService.queryScores(any(ScoreQueryDTO.class))).thenReturn(page);

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/query")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(queryDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.records", hasSize(0)))
                    .andExpect(jsonPath("$.data.total", is(0)));

            verify(scoreService, times(1)).queryScores(any(ScoreQueryDTO.class));
        }

        @Test
        @DisplayName("TC-Q04: 按不存在学生姓名查询")
        void testQueryByNonExistentStudentName() throws Exception {
            // 准备查询条件
            ScoreQueryDTO queryDTO = new ScoreQueryDTO();
            queryDTO.setStudentName("不存在");
            queryDTO.setPageNum(1);
            queryDTO.setPageSize(10);

            // 准备模拟响应（空结果）
            Page<ScoreDTO> page = new Page<>(1, 10);
            page.setRecords(Collections.emptyList());
            page.setTotal(0);

            when(scoreService.queryScores(any(ScoreQueryDTO.class))).thenReturn(page);

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/query")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(queryDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.records", hasSize(0)))
                    .andExpect(jsonPath("$.data.total", is(0)));

            verify(scoreService, times(1)).queryScores(any(ScoreQueryDTO.class));
        }

        @Test
        @DisplayName("TC-Q06: 按不存在科目查询")
        void testQueryByNonExistentSubject() throws Exception {
            // 准备查询条件
            ScoreQueryDTO queryDTO = new ScoreQueryDTO();
            queryDTO.setSubject("不存在");
            queryDTO.setPageNum(1);
            queryDTO.setPageSize(10);

            // 准备模拟响应（空结果）
            Page<ScoreDTO> page = new Page<>(1, 10);
            page.setRecords(Collections.emptyList());
            page.setTotal(0);

            when(scoreService.queryScores(any(ScoreQueryDTO.class))).thenReturn(page);

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/query")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(queryDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.records", hasSize(0)))
                    .andExpect(jsonPath("$.data.total", is(0)));

            verify(scoreService, times(1)).queryScores(any(ScoreQueryDTO.class));
        }

        @Test
        @DisplayName("TC-Q08: 无条件查询")
        void testQueryWithoutConditions() throws Exception {
            // 准备查询条件（无条件，仅分页参数）
            ScoreQueryDTO queryDTO = new ScoreQueryDTO();
            queryDTO.setPageNum(1);
            queryDTO.setPageSize(10);

            // 准备模拟响应
            Page<ScoreDTO> page = new Page<>(1, 10);
            List<ScoreDTO> records = new ArrayList<>();
            ScoreDTO scoreDTO1 = new ScoreDTO();
            scoreDTO1.setScoreId(1L);
            scoreDTO1.setStudentId(1L);
            scoreDTO1.setStudentName("张三");
            scoreDTO1.setSubject("数学");
            scoreDTO1.setScore(85);
            records.add(scoreDTO1);
            
            ScoreDTO scoreDTO2 = new ScoreDTO();
            scoreDTO2.setScoreId(2L);
            scoreDTO2.setStudentId(2L);
            scoreDTO2.setStudentName("李四");
            scoreDTO2.setSubject("语文");
            scoreDTO2.setScore(90);
            records.add(scoreDTO2);
            
            page.setRecords(records);
            page.setTotal(20); // 假设总共有20条记录

            when(scoreService.queryScores(any(ScoreQueryDTO.class))).thenReturn(page);

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/query")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(queryDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.records", hasSize(2)))
                    .andExpect(jsonPath("$.data.total", is(20)))
                    .andExpect(jsonPath("$.data.current", is(1)))
                    .andExpect(jsonPath("$.data.size", is(10)));

            verify(scoreService, times(1)).queryScores(any(ScoreQueryDTO.class));
        }

        @Test
        @DisplayName("TC-Q11: 分页查询第二页")
        void testQuerySecondPage() throws Exception {
            // 准备查询条件
            ScoreQueryDTO queryDTO = new ScoreQueryDTO();
            queryDTO.setPageNum(2);
            queryDTO.setPageSize(10);

            // 准备模拟响应（第二页数据）
            Page<ScoreDTO> page = new Page<>(2, 10);
            List<ScoreDTO> records = new ArrayList<>();
            ScoreDTO scoreDTO1 = new ScoreDTO();
            scoreDTO1.setScoreId(11L);
            scoreDTO1.setStudentId(5L);
            scoreDTO1.setStudentName("王五");
            scoreDTO1.setSubject("英语");
            scoreDTO1.setScore(75);
            records.add(scoreDTO1);
            
            ScoreDTO scoreDTO2 = new ScoreDTO();
            scoreDTO2.setScoreId(12L);
            scoreDTO2.setStudentId(6L);
            scoreDTO2.setStudentName("赵六");
            scoreDTO2.setSubject("物理");
            scoreDTO2.setScore(80);
            records.add(scoreDTO2);
            
            page.setRecords(records);
            page.setTotal(20); // 假设总共有20条记录

            when(scoreService.queryScores(any(ScoreQueryDTO.class))).thenReturn(page);

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/query")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(queryDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.records", hasSize(2)))
                    .andExpect(jsonPath("$.data.total", is(20)))
                    .andExpect(jsonPath("$.data.current", is(2)))
                    .andExpect(jsonPath("$.data.size", is(10)));

            verify(scoreService, times(1)).queryScores(any(ScoreQueryDTO.class));
        }
    }

    @Nested
    @DisplayName("低优先级测试用例")
    class LowPriorityTests {

        @Test
        @DisplayName("TC-Q09: 无效页码查询")
        void testQueryWithInvalidPageNumber() throws Exception {
            // 准备查询条件（无效页码）
            ScoreQueryDTO queryDTO = new ScoreQueryDTO();
            queryDTO.setPageNum(0); // 无效页码
            queryDTO.setPageSize(10);

            // 准备模拟响应（系统应该会自动处理并返回第一页）
            Page<ScoreDTO> page = new Page<>(1, 10); // 假设系统自动转为第一页
            List<ScoreDTO> records = new ArrayList<>();
            ScoreDTO scoreDTO = new ScoreDTO();
            scoreDTO.setScoreId(1L);
            scoreDTO.setStudentId(1L);
            scoreDTO.setStudentName("张三");
            scoreDTO.setSubject("数学");
            scoreDTO.setScore(85);
            records.add(scoreDTO);
            page.setRecords(records);
            page.setTotal(1);

            when(scoreService.queryScores(any(ScoreQueryDTO.class))).thenReturn(page);

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/query")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(queryDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.current", is(1))); // 应该自动调整为第1页

            verify(scoreService, times(1)).queryScores(any(ScoreQueryDTO.class));
        }

        @Test
        @DisplayName("TC-Q10: 无效每页记录数查询")
        void testQueryWithInvalidPageSize() throws Exception {
            // 准备查询条件（无效每页记录数）
            ScoreQueryDTO queryDTO = new ScoreQueryDTO();
            queryDTO.setPageNum(1);
            queryDTO.setPageSize(-1); // 无效每页记录数

            // 准备模拟响应（系统应该会自动处理并使用默认每页记录数）
            Page<ScoreDTO> page = new Page<>(1, 10); // 假设系统使用默认值10
            List<ScoreDTO> records = new ArrayList<>();
            ScoreDTO scoreDTO = new ScoreDTO();
            scoreDTO.setScoreId(1L);
            scoreDTO.setStudentId(1L);
            scoreDTO.setStudentName("张三");
            scoreDTO.setSubject("数学");
            scoreDTO.setScore(85);
            records.add(scoreDTO);
            page.setRecords(records);
            page.setTotal(1);

            when(scoreService.queryScores(any(ScoreQueryDTO.class))).thenReturn(page);

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/query")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(queryDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.size", is(10))); // 应该使用默认的每页记录数

            verify(scoreService, times(1)).queryScores(any(ScoreQueryDTO.class));
        }
    }
} 