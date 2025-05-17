package com.wtu.backend.blackbox.ManageScore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wtu.entity.Score;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@Rollback
public class ScoreUpdateTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test @DisplayName("TC-E01: 修改有效成绩")
    public void testUpdateValidScore() throws Exception {
        System.out.println("========== TC-E01: 修改有效成绩 ==========");
        System.out.println("【测试输入】成绩ID=1, 科目=\"数学\", 分数=85");
        System.out.println("【预期结果】成功更新成绩，code=200");
        System.out.println("【等价类覆盖】EC12, EC16");
        Score score = new Score();
        score.setScoreId(1L);
        score.setStudentId(1L);
        score.setSubject("数学");
        score.setScore(85);
        score.setExamDate(LocalDate.of(2023, 6, 1));
        MvcResult result = mockMvc.perform(put("/api/scores/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-E02: 修改不存在成绩ID")
    public void testUpdateNonExistentScoreId() throws Exception {
        System.out.println("========== TC-E02: 修改不存在成绩ID ==========");
        System.out.println("【测试输入】成绩ID=999, 科目=\"数学\", 分数=85");
        System.out.println("【预期结果】更新失败，提示\"成绩不存在\"，code=500");
        System.out.println("【等价类覆盖】EC13, EC16");
        Score score = new Score();
        score.setScoreId(999L);
        score.setSubject("数学");
        score.setScore(85);
        score.setExamDate(LocalDate.of(2023, 6, 1));
        MvcResult result = mockMvc.perform(put("/api/scores/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-E03: 修改成绩为负数")
    public void testUpdateScoreWithNegativeValue() throws Exception {
        System.out.println("========== TC-E03: 修改成绩为负数 ==========");
        System.out.println("【测试输入】成绩ID=1, 科目=\"数学\", 分数=-5");
        System.out.println("【预期结果】提示\"分数必须在0-100之间\"，code=500");
        System.out.println("【等价类覆盖】EC12, EC17");
        Score score = new Score();
        score.setScoreId(1L);
        score.setSubject("数学");
        score.setScore(-5);
        score.setExamDate(LocalDate.of(2023, 6, 1));
        MvcResult result = mockMvc.perform(put("/api/scores/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-E04: 修改成绩超过100")
    public void testUpdateScoreWithValueOver100() throws Exception {
        System.out.println("========== TC-E04: 修改成绩超过100 ==========");
        System.out.println("【测试输入】成绩ID=1, 科目=\"数学\", 分数=101");
        System.out.println("【预期结果】提示\"分数必须在0-100之间\"，code=500");
        System.out.println("【等价类覆盖】EC12, EC18");
        Score score = new Score();
        score.setScoreId(1L);
        score.setSubject("数学");
        score.setScore(101);
        score.setExamDate(LocalDate.of(2023, 6, 1));
        MvcResult result = mockMvc.perform(put("/api/scores/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-E05: 修改成绩为非数字")
    public void testUpdateScoreWithNonNumericValue() throws Exception {
        System.out.println("========== TC-E05: 修改成绩为非数字 ==========");
        System.out.println("【测试输入】成绩ID=1, 科目=\"数学\", 分数=\"abc\"");
        System.out.println("【预期结果】请求参数类型错误，HTTP 400");
        System.out.println("【等价类覆盖】EC12, EC19");
        String requestJson = "{\"scoreId\":1,\"studentId\":1,\"subject\":\"数学\",\"score\":\"abc\",\"examDate\":[2023,6,1]}";
        mockMvc.perform(put("/api/scores/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test @DisplayName("TC-E06: 修改成绩为空")
    public void testUpdateScoreWithNullValue() throws Exception {
        System.out.println("========== TC-E06: 修改成绩为空 ==========");
        System.out.println("【测试输入】成绩ID=1, 科目=\"数学\", 分数=null");
        System.out.println("【预期结果】返回参数错误提示，code=500");
        System.out.println("【等价类覆盖】EC12, EC20");
        String requestJson = "{\"scoreId\":1,\"subject\":\"数学\",\"examDate\":[2023,6,1]}";
        MvcResult result = mockMvc.perform(put("/api/scores/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-E07: 修改成绩为及格边界60")
    public void testUpdateScoreWithBoundary60() throws Exception {
        System.out.println("========== TC-E07: 修改成绩为及格边界60 ==========");
        System.out.println("【测试输入】成绩ID=1, 科目=\"数学\", 分数=60");
        System.out.println("【预期结果】成功，状态为\"通过\"，code=200");
        System.out.println("【等价类覆盖】EC12, EC16");
        Score score = new Score();
        score.setScoreId(1L);
        score.setStudentId(1L);
        score.setSubject("数学");
        score.setScore(60);
        score.setExamDate(LocalDate.of(2023, 6, 1));
        MvcResult result = mockMvc.perform(put("/api/scores/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-E08: 修改成绩为及格边界59")
    public void testUpdateScoreWithBoundary59() throws Exception {
        System.out.println("========== TC-E08: 修改成绩为及格边界59 ==========");
        System.out.println("【测试输入】成绩ID=1, 科目=\"数学\", 分数=59");
        System.out.println("【预期结果】成功，状态为\"不通过\"，code=200");
        System.out.println("【等价类覆盖】EC12, EC16");
        Score score = new Score();
        score.setScoreId(1L);
        score.setStudentId(1L);
        score.setSubject("数学");
        score.setScore(59);
        score.setExamDate(LocalDate.of(2023, 6, 1));
        MvcResult result = mockMvc.perform(put("/api/scores/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }
} 