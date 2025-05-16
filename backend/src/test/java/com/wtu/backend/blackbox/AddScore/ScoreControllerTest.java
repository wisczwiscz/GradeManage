package com.wtu.backend.black.AddScoreMoudle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtu.controller.ScoreController;
import com.wtu.entity.Score;
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

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 成绩录入控制器黑盒测试类
 * 基于等价类划分方法设计测试用例
 */
@ExtendWith(MockitoExtension.class)
public class ScoreControllerTest {

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
        // 配置ObjectMapper以处理Java 8日期类型
        objectMapper.findAndRegisterModules();
    }

    @Nested
    @DisplayName("高优先级测试用例")
    class HighPriorityTests {

        @Test
        @DisplayName("TC-01: 教师录入有效成绩")
        void testAddValidScore() throws Exception {
            // 准备测试数据和模拟响应
            Score inputScore = new Score();
            inputScore.setStudentId(1L);
            inputScore.setSubject("数学");
            inputScore.setScore(85);
            inputScore.setExamDate(LocalDate.of(2023, 6, 1));

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
                    .content(objectMapper.writeValueAsString(inputScore)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.studentId", is(1)))
                    .andExpect(jsonPath("$.data.subject", is("数学")))
                    .andExpect(jsonPath("$.data.score", is(85)));

            verify(scoreService, times(1)).addScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-02: 录入不存在学生ID")
        void testAddScoreWithNonExistentStudentId() throws Exception {
            // 准备测试数据
            Score inputScore = new Score();
            inputScore.setStudentId(999L);
            inputScore.setSubject("数学");
            inputScore.setScore(85);
            inputScore.setExamDate(LocalDate.of(2023, 6, 1));

            // 模拟服务层抛出异常
            when(scoreService.addScore(any(Score.class))).thenThrow(new RuntimeException("学生不存在"));

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputScore)))
                    .andExpect(status().isInternalServerError());

            verify(scoreService, times(1)).addScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-07: 录入负数分数")
        void testAddNegativeScore() throws Exception {
            // 准备测试数据
            Score inputScore = new Score();
            inputScore.setStudentId(1L);
            inputScore.setSubject("数学");
            inputScore.setScore(-5);
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
        @DisplayName("TC-08: 录入超过100的分数")
        void testAddScoreOver100() throws Exception {
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
        @DisplayName("TC-16: 录入分数下边界值0")
        void testAddScoreLowerBoundary() throws Exception {
            // 准备测试数据和模拟响应
            Score inputScore = new Score();
            inputScore.setStudentId(1L);
            inputScore.setSubject("数学");
            inputScore.setScore(0);
            inputScore.setExamDate(LocalDate.of(2023, 6, 1));

            Score savedScore = new Score();
            savedScore.setScoreId(1L);
            savedScore.setStudentId(1L);
            savedScore.setSubject("数学");
            savedScore.setScore(0);
            savedScore.setExamDate(LocalDate.of(2023, 6, 1));

            when(scoreService.addScore(any(Score.class))).thenReturn(savedScore);

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputScore)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.score", is(0)));

            verify(scoreService, times(1)).addScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-17: 录入分数上边界值100")
        void testAddScoreUpperBoundary() throws Exception {
            // 准备测试数据和模拟响应
            Score inputScore = new Score();
            inputScore.setStudentId(1L);
            inputScore.setSubject("数学");
            inputScore.setScore(100);
            inputScore.setExamDate(LocalDate.of(2023, 6, 1));

            Score savedScore = new Score();
            savedScore.setScoreId(1L);
            savedScore.setStudentId(1L);
            savedScore.setSubject("数学");
            savedScore.setScore(100);
            savedScore.setExamDate(LocalDate.of(2023, 6, 1));

            when(scoreService.addScore(any(Score.class))).thenReturn(savedScore);

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputScore)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.score", is(100)));

            verify(scoreService, times(1)).addScore(any(Score.class));
        }
    }

    @Nested
    @DisplayName("中优先级测试用例")
    class MediumPriorityTests {

        @Test
        @DisplayName("TC-04: 录入空学生ID")
        void testAddScoreWithNullStudentId() throws Exception {
            // 准备测试数据
            Score inputScore = new Score();
            // 不设置学生ID，保持为null
            inputScore.setSubject("数学");
            inputScore.setScore(85);
            inputScore.setExamDate(LocalDate.of(2023, 6, 1));

            // 模拟服务层抛出异常
            when(scoreService.addScore(any(Score.class))).thenThrow(new IllegalArgumentException("请输入学生ID"));

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputScore)))
                    .andExpect(status().isBadRequest());

            verify(scoreService, times(1)).addScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-05: 录入空科目")
        void testAddScoreWithEmptySubject() throws Exception {
            // 准备测试数据
            Score inputScore = new Score();
            inputScore.setStudentId(1L);
            inputScore.setSubject(""); // 空科目
            inputScore.setScore(85);
            inputScore.setExamDate(LocalDate.of(2023, 6, 1));

            // 模拟服务层抛出异常
            when(scoreService.addScore(any(Score.class))).thenThrow(new IllegalArgumentException("请输入科目"));

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputScore)))
                    .andExpect(status().isBadRequest());

            verify(scoreService, times(1)).addScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-10: 录入空分数")
        void testAddScoreWithNullScore() throws Exception {
            // 准备测试数据
            Score inputScore = new Score();
            inputScore.setStudentId(1L);
            inputScore.setSubject("数学");
            // 不设置分数，保持为null
            inputScore.setExamDate(LocalDate.of(2023, 6, 1));

            // 模拟服务层抛出异常
            when(scoreService.addScore(any(Score.class))).thenThrow(new IllegalArgumentException("请输入分数"));

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputScore)))
                    .andExpect(status().isBadRequest());

            verify(scoreService, times(1)).addScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-13: 录入空日期")
        void testAddScoreWithNullDate() throws Exception {
            // 准备测试数据
            Score inputScore = new Score();
            inputScore.setStudentId(1L);
            inputScore.setSubject("数学");
            inputScore.setScore(85);
            // 不设置日期，保持为null

            // 模拟服务层抛出异常
            when(scoreService.addScore(any(Score.class))).thenThrow(new IllegalArgumentException("请选择考试日期"));

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputScore)))
                    .andExpect(status().isBadRequest());

            verify(scoreService, times(1)).addScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-20: 录入及格边界值59")
        void testAddScoreBoundary59() throws Exception {
            // 准备测试数据和模拟响应
            Score inputScore = new Score();
            inputScore.setStudentId(1L);
            inputScore.setSubject("数学");
            inputScore.setScore(59);
            inputScore.setExamDate(LocalDate.of(2023, 6, 1));

            Score savedScore = new Score();
            savedScore.setScoreId(1L);
            savedScore.setStudentId(1L);
            savedScore.setSubject("数学");
            savedScore.setScore(59);
            savedScore.setExamDate(LocalDate.of(2023, 6, 1));

            when(scoreService.addScore(any(Score.class))).thenReturn(savedScore);

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputScore)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.score", is(59)));

            verify(scoreService, times(1)).addScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-21: 录入及格边界值60")
        void testAddScoreBoundary60() throws Exception {
            // 准备测试数据和模拟响应
            Score inputScore = new Score();
            inputScore.setStudentId(1L);
            inputScore.setSubject("数学");
            inputScore.setScore(60);
            inputScore.setExamDate(LocalDate.of(2023, 6, 1));

            Score savedScore = new Score();
            savedScore.setScoreId(1L);
            savedScore.setStudentId(1L);
            savedScore.setSubject("数学");
            savedScore.setScore(60);
            savedScore.setExamDate(LocalDate.of(2023, 6, 1));

            when(scoreService.addScore(any(Score.class))).thenReturn(savedScore);

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputScore)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(200)))
                    .andExpect(jsonPath("$.data.score", is(60)));

            verify(scoreService, times(1)).addScore(any(Score.class));
        }
    }

    @Nested
    @DisplayName("低优先级测试用例")
    class LowPriorityTests {

        @Test
        @DisplayName("TC-03: 录入非数字学生ID")
        void testAddScoreWithNonNumericStudentId() throws Exception {
            // 注意：这个测试在实际场景中可能不适用，因为Java类型系统会阻止将非数字设置为Long类型
            // 但作为黑盒测试，我们假设前端可能传入非数字的studentId

            // 准备含有非法学生ID的JSON
            String invalidJson = "{\"studentId\":\"abc\",\"subject\":\"数学\",\"score\":85,\"examDate\":\"2023-06-01\"}";

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson))
                    .andExpect(status().isBadRequest());

            // 由于JSON解析错误，服务层方法不会被调用
            verify(scoreService, never()).addScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-06: 录入超长科目名称")
        void testAddScoreWithLongSubject() throws Exception {
            // 准备测试数据
            Score inputScore = new Score();
            inputScore.setStudentId(1L);
            // 创建一个长度为100的科目名称
            StringBuilder longSubject = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                longSubject.append("长");
            }
            inputScore.setSubject(longSubject.toString());
            inputScore.setScore(85);
            inputScore.setExamDate(LocalDate.of(2023, 6, 1));

            // 模拟服务层抛出异常
            when(scoreService.addScore(any(Score.class))).thenThrow(new IllegalArgumentException("科目名称过长"));

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputScore)))
                    .andExpect(status().isBadRequest());

            verify(scoreService, times(1)).addScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-09: 录入非数字分数")
        void testAddNonNumericScore() throws Exception {
            // 注意：这个测试在实际场景中可能不适用，因为Java类型系统会阻止将非数字设置为Integer类型
            // 但作为黑盒测试，我们假设前端可能传入非数字的score

            // 准备含有非法分数的JSON
            String invalidJson = "{\"studentId\":1,\"subject\":\"数学\",\"score\":\"优秀\",\"examDate\":\"2023-06-01\"}";

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson))
                    .andExpect(status().isBadRequest());

            // 由于JSON解析错误，服务层方法不会被调用
            verify(scoreService, never()).addScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-11: 录入无效日期格式")
        void testAddScoreWithInvalidDateFormat() throws Exception {
            // 准备含有非法日期格式的JSON
            String invalidJson = "{\"studentId\":1,\"subject\":\"数学\",\"score\":85,\"examDate\":\"06/01/2023\"}";

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson))
                    .andExpect(status().isBadRequest());

            // 由于JSON解析错误，服务层方法不会被调用
            verify(scoreService, never()).addScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-12: 录入未来日期")
        void testAddScoreWithFutureDate() throws Exception {
            // 准备测试数据
            Score inputScore = new Score();
            inputScore.setStudentId(1L);
            inputScore.setSubject("数学");
            inputScore.setScore(85);
            // 设置未来日期
            inputScore.setExamDate(LocalDate.now().plusYears(1));

            // 模拟服务层抛出异常
            when(scoreService.addScore(any(Score.class))).thenThrow(new IllegalArgumentException("不能选择未来日期"));

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputScore)))
                    .andExpect(status().isBadRequest());

            verify(scoreService, times(1)).addScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-25: 多字段无效-学生ID和分数")
        void testAddScoreWithMultipleInvalidFields() throws Exception {
            // 准备测试数据
            Score inputScore = new Score();
            inputScore.setStudentId(999L); // 不存在的ID
            inputScore.setSubject("数学");
            inputScore.setScore(-5); // 无效分数
            inputScore.setExamDate(LocalDate.of(2023, 6, 1));

            // 模拟服务层抛出异常
            when(scoreService.addScore(any(Score.class))).thenThrow(new IllegalArgumentException("多个字段错误"));

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputScore)))
                    .andExpect(status().isBadRequest());

            verify(scoreService, times(1)).addScore(any(Score.class));
        }

        @Test
        @DisplayName("TC-26: 多字段无效-科目和日期")
        void testAddScoreWithMultipleInvalidFields2() throws Exception {
            // 准备测试数据
            Score inputScore = new Score();
            inputScore.setStudentId(1L);
            inputScore.setSubject(""); // 空科目
            inputScore.setScore(85);
            // 不设置日期，保持为null

            // 模拟服务层抛出异常
            when(scoreService.addScore(any(Score.class))).thenThrow(new IllegalArgumentException("多个字段错误"));

            // 执行请求并验证
            mockMvc.perform(post("/api/scores/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputScore)))
                    .andExpect(status().isBadRequest());

            verify(scoreService, times(1)).addScore(any(Score.class));
        }
    }
} 