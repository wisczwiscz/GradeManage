package com.wtu.backend.blackbox.ManageScore;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@Rollback
public class ScoreDetailTest {

    @Autowired
    private MockMvc mockMvc;

    @Test @DisplayName("TC-D01: 查看有效成绩详情")
    public void testGetValidScoreDetail() throws Exception {
        System.out.println("========== TC-D01: 查看有效成绩详情 ==========");
        System.out.println("【测试输入】成绩ID=1");
        System.out.println("【预期结果】返回该成绩详细信息，code=200");
        System.out.println("【等价类覆盖】EC12");
        MvcResult result = mockMvc.perform(get("/api/scores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-D02: 查看不存在成绩详情")
    public void testGetNonExistentScoreDetail() throws Exception {
        System.out.println("========== TC-D02: 查看不存在成绩详情 ==========");
        System.out.println("【测试输入】成绩ID=999");
        System.out.println("【预期结果】返回\"成绩不存在\"，code=500");
        System.out.println("【等价类覆盖】EC13");
        MvcResult result = mockMvc.perform(get("/api/scores/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-D03: 查看非数字成绩ID")
    public void testGetScoreDetailWithNonNumericId() throws Exception {
        System.out.println("========== TC-D03: 查看非数字成绩ID ==========");
        System.out.println("【测试输入】成绩ID=\"abc\"");
        System.out.println("【预期结果】请求参数类型错误，HTTP 400");
        System.out.println("【等价类覆盖】EC14");
        mockMvc.perform(get("/api/scores/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test @DisplayName("TC-D04: 查看空成绩ID")
    public void testGetScoreDetailWithNullId() throws Exception {
        System.out.println("========== TC-D04: 查看空成绩ID ==========");
        System.out.println("【测试输入】成绩ID=null");
        System.out.println("【预期结果】请求参数类型错误，HTTP 400");
        System.out.println("【等价类覆盖】EC15");
        mockMvc.perform(get("/api/scores/null"))
                .andExpect(status().isBadRequest());
    }
} 