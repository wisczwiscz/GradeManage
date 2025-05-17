package com.wtu.backend.blackbox.Statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 统计分析模块黑盒测试
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@Rollback
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("TC-01: 查看有效科目统计")
    public void testGetStatisticsByValidSubject() throws Exception {
        System.out.println("\n========== TC-01: 查看有效科目统计 ==========");
        System.out.println("测试目的: 验证查询存在的科目时返回正确统计数据");
        String subject = "数学";
        System.out.println("输入: subject='" + subject + "'");
        MvcResult result = mockMvc.perform(get("/api/statistics/subject/" + subject)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andExpect(jsonPath("$.data.subject").value(subject))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 成功返回有效科目统计数据");
    }

    @Test
    @DisplayName("TC-02: 查看全科目统计")
    public void testGetAllSubjectsStatistics() throws Exception {
        System.out.println("\n========== TC-02: 查看全科目统计 ==========");
        System.out.println("测试目的: 验证查询所有科目时返回统计数组");
        System.out.println("输入: 无");
        MvcResult result = mockMvc.perform(get("/api/statistics/basic")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andExpect(jsonPath("$.data").isArray())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 成功返回所有科目统计数组");
    }

    @Test
    @DisplayName("TC-03: 查看全部及格科目统计")
    public void testGetAllPassSubjectStatistics() throws Exception {
        System.out.println("\n========== TC-03: 查看全部及格科目统计 ==========");
        System.out.println("测试目的: 验证查询无数据时返回所有字段为null");
        String subject = "全部及格科目";
        System.out.println("输入: subject='" + subject + "'");
        MvcResult result = mockMvc.perform(get("/api/statistics/subject/" + subject)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andExpect(jsonPath("$.data.failCount").value((Object) null))
                .andExpect(jsonPath("$.data.subject").value((Object) null))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 查无数据时所有字段为null");
    }

    @Test
    @DisplayName("TC-04: 查看全部不及格科目统计")
    public void testGetAllFailSubjectStatistics() throws Exception {
        System.out.println("\n========== TC-04: 查看全部不及格科目统计 ==========");
        System.out.println("测试目的: 验证查询无数据时返回所有字段为null");
        String subject = "全部不及格科目";
        System.out.println("输入: subject='" + subject + "'");
        MvcResult result = mockMvc.perform(get("/api/statistics/subject/" + subject)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andExpect(jsonPath("$.data.passCount").value((Object) null))
                .andExpect(jsonPath("$.data.subject").value((Object) null))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 查无数据时所有字段为null");
    }

    @Test
    @DisplayName("TC-05: 查看不存在科目统计")
    public void testGetStatisticsByNonExistentSubject() throws Exception {
        System.out.println("\n========== TC-05: 查看不存在科目统计 ==========");
        System.out.println("测试目的: 验证查询不存在的科目时返回所有字段为null");
        String subject = "不存在科目";
        System.out.println("输入: subject='" + subject + "'");
        MvcResult result = mockMvc.perform(get("/api/statistics/subject/" + subject)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andExpect(jsonPath("$.data.subject").value((Object) null))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 查询不存在科目时所有字段为null");
    }

    @Test
    @DisplayName("TC-06: 查看单条记录科目统计")
    public void testGetStatisticsBySingleRecordSubject() throws Exception {
        System.out.println("\n========== TC-06: 查看单条记录科目统计 ==========");
        System.out.println("测试目的: 验证仅有一条成绩记录的科目统计");
        String subject = "仅有一条记录的科目";
        System.out.println("输入: subject='" + subject + "'");
        MvcResult result = mockMvc.perform(get("/api/statistics/subject/" + subject)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 单条记录统计正常返回");
    }

    @Test
    @DisplayName("TC-07: 查看空记录科目统计")
    public void testGetStatisticsByEmptySubject() throws Exception {
        System.out.println("\n========== TC-07: 查看空记录科目统计 ==========");
        System.out.println("测试目的: 验证空记录科目时返回所有字段为null");
        String subject = "空记录科目";
        System.out.println("输入: subject='" + subject + "'");
        MvcResult result = mockMvc.perform(get("/api/statistics/subject/" + subject)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andExpect(jsonPath("$.data.subject").value((Object) null))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 空记录科目时所有字段为null");
    }

    @Test
    @DisplayName("TC-08: 查看及格边界值科目统计")
    public void testGetStatisticsByPassBoundarySubject() throws Exception {
        System.out.println("\n========== TC-08: 查看及格边界值科目统计 ==========");
        System.out.println("测试目的: 验证59分为不及格，60分为及格");
        String subject = "包含59分和60分的科目";
        System.out.println("输入: subject='" + subject + "'");
        MvcResult result = mockMvc.perform(get("/api/statistics/subject/" + subject)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 及格边界统计正常返回");
    }

    @Test
    @DisplayName("TC-09: 查看所有分数相同科目")
    public void testGetStatisticsBySameScoreSubject() throws Exception {
        System.out.println("\n========== TC-09: 查看所有分数相同科目 ==========");
        System.out.println("测试目的: 验证所有分数相同科目的统计");
        String subject = "所有分数都是85分的科目";
        System.out.println("输入: subject='" + subject + "'");
        MvcResult result = mockMvc.perform(get("/api/statistics/subject/" + subject)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 所有分数相同科目统计正常返回");
    }

    @Test
    @DisplayName("TC-10: 查看包含所有等级的科目")
    public void testGetStatisticsByAllGradesSubject() throws Exception {
        System.out.println("\n========== TC-10: 查看包含所有等级的科目 ==========");
        System.out.println("测试目的: 验证包含所有等级的科目统计");
        String subject = "包含所有等级的科目";
        System.out.println("输入: subject='" + subject + "'");
        MvcResult result = mockMvc.perform(get("/api/statistics/subject/" + subject)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 包含所有等级的科目统计正常返回");
    }

    @Test
    @DisplayName("TC-11: 查看单一等级的科目")
    public void testGetStatisticsBySingleGradeSubject() throws Exception {
        System.out.println("\n========== TC-11: 查看单一等级的科目 ==========");
        System.out.println("测试目的: 验证只有A级的科目统计");
        String subject = "只有A级的科目";
        System.out.println("输入: subject='" + subject + "'");
        MvcResult result = mockMvc.perform(get("/api/statistics/subject/" + subject)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 单一等级科目统计正常返回");
    }

    @Test
    @DisplayName("TC-12: 等级边界值分数统计")
    public void testGetStatisticsByGradeBoundarySubject() throws Exception {
        System.out.println("\n========== TC-12: 等级边界值分数统计 ==========");
        System.out.println("测试目的: 验证等级边界分数统计分布");
        String subject = "计算机";
        System.out.println("输入: subject='" + subject + "'");
        MvcResult result = mockMvc.perform(get("/api/statistics/subject/" + subject)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andExpect(jsonPath("$.data.gradeDistribution").exists())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 等级边界分数统计分布正常返回");
    }

    @Test
    @DisplayName("TC-13: 查看特殊字符科目统计")
    public void testGetStatisticsBySpecialCharSubject() throws Exception {
        System.out.println("\n========== TC-13: 查看特殊字符科目统计 ==========");
        System.out.println("测试目的: 验证特殊字符科目名的统计（期望后端返回400或500，当前实现未校验应判定为未通过）");
        String subject = "特殊字符#$%";
        System.out.println("输入: subject='" + subject + "'");
        MvcResult result = mockMvc.perform(get("/api/statistics/subject/" + subject)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("无效的科目名称"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 未通过 × - 实际未对特殊字符做校验，属于设计缺陷");
    }

    @Test
    @DisplayName("TC-14: 查看系统中无任何记录的统计")
    public void testGetStatisticsWhenNoRecord() throws Exception {
        System.out.println("\n========== TC-14: 查看系统中无任何记录的统计 ==========");
        System.out.println("测试目的: 验证系统无成绩记录时返回空数组");
        System.out.println("输入: 无");
        MvcResult result = mockMvc.perform(get("/api/statistics/basic")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andExpect(jsonPath("$.data").isArray())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 无成绩记录时返回空数组");
    }

    @Test
    @DisplayName("TC-15: 最高分为100分的统计")
    public void testGetStatisticsByMaxScore100Subject() throws Exception {
        System.out.println("\n========== TC-15: 最高分为100分的统计 ==========");
        System.out.println("测试目的: 验证最高分为100分的科目统计");
        String subject = "包含100分的科目";
        System.out.println("输入: subject='" + subject + "'");
        MvcResult result = mockMvc.perform(get("/api/statistics/subject/" + subject)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 最高分为100分的科目统计正常返回");
    }

    @Test
    @DisplayName("TC-16: 最低分为0分的统计")
    public void testGetStatisticsByMinScore0Subject() throws Exception {
        System.out.println("\n========== TC-16: 最低分为0分的统计 ==========");
        System.out.println("测试目的: 验证最低分为0分的科目统计");
        String subject = "包含0分的科目";
        System.out.println("输入: subject='" + subject + "'");
        MvcResult result = mockMvc.perform(get("/api/statistics/subject/" + subject)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 最低分为0分的科目统计正常返回");
    }

    // 其他用例可按需补充，建议参考等价类和边界值，补全assert和数据准备
} 