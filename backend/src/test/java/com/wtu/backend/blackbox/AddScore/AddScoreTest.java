package com.wtu.backend.blackbox.AddScore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wtu.entity.Score;
import com.wtu.result.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 成绩录入模块黑盒测试
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // 禁用Spring Security过滤器
@Transactional  // 添加事务支持
@Rollback       // 测试完成后回滚事务
public class AddScoreTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    // 基本功能测试（优先级：高）

    @Test
    @DisplayName("TC-01: 教师录入有效成绩")
    public void testAddValidScore() throws Exception {
        System.out.println("\n========== TC-01: 教师录入有效成绩 ==========");
        System.out.println("测试目的: 验证有效输入能成功录入成绩");
        System.out.println("输入: 学生ID=1, 科目='数学', 分数=85, 日期='2023-06-01'");
        
        // 准备测试数据
        Score score = new Score();
        score.setStudentId(1L); // 假设数据库中存在ID为1的学生
        score.setSubject("数学");
        score.setScore(85);
        score.setExamDate(LocalDate.of(2023, 6, 1));

        // 执行测试
        MvcResult result = mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.studentId").value(1))
                .andExpect(jsonPath("$.data.subject").value("数学"))
                .andExpect(jsonPath("$.data.score").value(85))
                .andReturn();

        // 解析响应结果
        String content = result.getResponse().getContentAsString();
        assertNotNull(content);
        
        // 验证返回的是Result对象
        Result response = objectMapper.readValue(content, Result.class);
        assertEquals(200, response.getCode());
        assertNotNull(response.getData());
        
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 系统成功接受有效输入并录入成绩");
    }

    @Test
    @DisplayName("TC-02: 录入不存在学生ID")
    public void testAddNonExistentStudentId() throws Exception {
        System.out.println("\n========== TC-02: 录入不存在学生ID ==========");
        System.out.println("测试目的: 验证系统正确拒绝不存在的学生ID");
        System.out.println("输入: 学生ID=999 (不存在), 科目='数学', 分数=85, 日期='2023-06-01'");
        
        // 准备测试数据
        Score score = new Score();
        score.setStudentId(999L); // 假设数据库中不存在ID为999的学生
        score.setSubject("数学");
        score.setScore(85);
        score.setExamDate(LocalDate.of(2023, 6, 1));

        // 执行测试 - 现在预期返回错误状态
        MvcResult result = mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())  // HTTP状态仍为200
                .andExpect(jsonPath("$.code").value(500))  // 业务状态码为500
                .andExpect(jsonPath("$.message").value("学生ID不存在"))
                .andReturn();
                
        // 解析响应结果
        String content = result.getResponse().getContentAsString();
        Result response = objectMapper.readValue(content, Result.class);
        assertEquals(500, response.getCode());
        assertNull(response.getData());
        
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 系统正确拒绝了不存在的学生ID并返回错误信息");
    }

    @Test
    @DisplayName("TC-03: 录入分数下边界值0")
    public void testAddScoreLowerBoundary() throws Exception {
        System.out.println("\n========== TC-03: 录入分数下边界值0 ==========");
        System.out.println("测试目的: 验证系统接受下边界值0");
        System.out.println("输入: 学生ID=1, 科目='数学', 分数=0, 日期='2023-06-01'");
        
        // 准备测试数据
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject("数学");
        score.setScore(0); // 下边界值
        score.setExamDate(LocalDate.of(2023, 6, 1));

        // 执行测试
        MvcResult result = mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.score").value(0))
                .andReturn();
                
        // 解析响应结果
        String content = result.getResponse().getContentAsString();
        Result response = objectMapper.readValue(content, Result.class);
        assertEquals(200, response.getCode());
        
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 系统成功接受分数下边界值0并录入成绩");
    }

    @Test
    @DisplayName("TC-04: 录入分数上边界值100")
    public void testAddScoreUpperBoundary() throws Exception {
        System.out.println("\n========== TC-04: 录入分数上边界值100 ==========");
        System.out.println("测试目的: 验证系统接受上边界值100");
        System.out.println("输入: 学生ID=1, 科目='数学', 分数=100, 日期='2023-06-01'");
        
        // 准备测试数据
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject("数学");
        score.setScore(100); // 上边界值
        score.setExamDate(LocalDate.of(2023, 6, 1));

        // 执行测试
        MvcResult result = mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.score").value(100))
                .andReturn();
                
        // 解析响应结果
        String content = result.getResponse().getContentAsString();
        Result response = objectMapper.readValue(content, Result.class);
        assertEquals(200, response.getCode());
        
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 系统成功接受分数上边界值100并录入成绩");
    }

    @Test
    @DisplayName("TC-05: 录入及格边界值60")
    public void testAddScorePassingGrade() throws Exception {
        System.out.println("\n========== TC-05: 录入及格边界值60 ==========");
        System.out.println("测试目的: 验证系统接受及格边界值60");
        System.out.println("输入: 学生ID=1, 科目='数学', 分数=60, 日期='2023-06-01'");
        
        // 准备测试数据
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject("数学");
        score.setScore(60); // 及格边界值
        score.setExamDate(LocalDate.of(2023, 6, 1));

        // 执行测试
        MvcResult result = mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.score").value(60))
                .andReturn();
                
        // 解析响应结果
        String content = result.getResponse().getContentAsString();
        Result response = objectMapper.readValue(content, Result.class);
        assertEquals(200, response.getCode());
        
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 系统成功接受及格边界值60并录入成绩");
    }

    // 无效输入测试（优先级：中）

    @Test
    @DisplayName("TC-06: 录入空学生ID")
    public void testAddNullStudentId() throws Exception {
        System.out.println("\n========== TC-06: 录入空学生ID ==========");
        System.out.println("测试目的: 验证系统正确拒绝空学生ID");
        System.out.println("输入: 学生ID=null, 科目='数学', 分数=85, 日期='2023-06-01'");
        
        // 准备测试数据
        Score score = new Score();
        // studentId未设置，保持为null
        score.setSubject("数学");
        score.setScore(85);
        score.setExamDate(LocalDate.of(2023, 6, 1));

        // 执行测试 - 现在预期返回错误状态
        MvcResult result = mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())  // HTTP状态仍为200
                .andExpect(jsonPath("$.code").value(500))  // 业务状态码为500
                .andExpect(jsonPath("$.message").value("学生ID不能为空"))
                .andReturn();
                
        // 解析响应结果
        String content = result.getResponse().getContentAsString();
        Result response = objectMapper.readValue(content, Result.class);
        assertEquals(500, response.getCode());
        assertNull(response.getData());
        
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 系统正确拒绝了空学生ID并返回错误信息");
    }

    @Test
    @DisplayName("TC-07: 录入空科目")
    public void testAddEmptySubject() throws Exception {
        System.out.println("\n========== TC-07: 录入空科目 ==========");
        System.out.println("测试目的: 验证系统正确拒绝空科目");
        System.out.println("输入: 学生ID=1, 科目='', 分数=85, 日期='2023-06-01'");
        
        // 准备测试数据
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject(""); // 空科目
        score.setScore(85);
        score.setExamDate(LocalDate.of(2023, 6, 1));

        // 执行测试 - 现在预期返回错误状态
        MvcResult result = mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())  // HTTP状态仍为200
                .andExpect(jsonPath("$.code").value(500))  // 业务状态码为500
                .andExpect(jsonPath("$.message").value("科目不能为空"))
                .andReturn();
                
        // 解析响应结果
        String content = result.getResponse().getContentAsString();
        Result response = objectMapper.readValue(content, Result.class);
        assertEquals(500, response.getCode());
        assertNull(response.getData());
        
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 系统正确拒绝了空科目并返回错误信息");
    }

    @Test
    @DisplayName("TC-08: 录入空分数")
    public void testAddNullScore() throws Exception {
        System.out.println("\n========== TC-08: 录入空分数 ==========");
        System.out.println("测试目的: 验证系统正确拒绝空分数");
        System.out.println("输入: 学生ID=1, 科目='数学', 分数=null, 日期='2023-06-01'");
        
        // 准备测试数据
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject("数学");
        // score未设置，保持为null
        score.setExamDate(LocalDate.of(2023, 6, 1));

        // 执行测试 - 现在预期返回错误状态
        MvcResult result = mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())  // HTTP状态仍为200
                .andExpect(jsonPath("$.code").value(500))  // 业务状态码为500
                .andExpect(jsonPath("$.message").value("分数不能为空"))
                .andReturn();
                
        // 解析响应结果
        String content = result.getResponse().getContentAsString();
        Result response = objectMapper.readValue(content, Result.class);
        assertEquals(500, response.getCode());
        assertNull(response.getData());
        
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 系统正确拒绝了空分数并返回错误信息");
    }

    @Test
    @DisplayName("TC-09: 录入空日期")
    public void testAddNullExamDate() throws Exception {
        System.out.println("\n========== TC-09: 录入空日期 ==========");
        System.out.println("测试目的: 验证系统接受空日期");
        System.out.println("输入: 学生ID=1, 科目='数学', 分数=85, 日期=null");
        
        // 准备测试数据
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject("数学");
        score.setScore(85);
        // examDate未设置，保持为null

        // 执行测试 - 日期可以为空，预期成功
        MvcResult result = mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
                
        // 解析响应结果
        String content = result.getResponse().getContentAsString();
        Result response = objectMapper.readValue(content, Result.class);
        assertEquals(200, response.getCode());
        assertNotNull(response.getData());
        // 验证返回的examDate确实是null
        assertTrue(content.contains("\"examDate\":null"));
        
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 系统成功接受空日期并录入成绩");
    }
    
    @Test
    @DisplayName("TC-10: 录入非数字学生ID")
    public void testAddNonNumericStudentId() throws Exception {
        System.out.println("\n========== TC-10: 录入非数字学生ID ==========");
        System.out.println("测试目的: 验证系统正确拒绝非数字学生ID");
        System.out.println("输入: 学生ID='abc', 科目='数学', 分数=85, 日期='2023-06-01'");
        
        // 准备测试数据 - 注意：需要直接构造JSON字符串，因为Java不允许将字符串赋值给Long类型
        String requestJson = "{\"studentId\":\"abc\",\"subject\":\"数学\",\"score\":85,\"examDate\":[2023,6,1]}";

        // 执行测试 - 预期应该返回400错误，因为studentId应该是数字
        MvcResult result = mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest()) // 期望返回400错误
                .andReturn();
                
        // 获取响应内容
        String content = result.getResponse().getContentAsString();
        
        System.out.println("系统响应: HTTP 400 Bad Request - " + content);
        System.out.println("测试结果: 通过 ✓ - 系统正确拒绝了非数字学生ID（格式错误）");
    }

    @Test
    @DisplayName("TC-11: 录入负数分数")
    public void testAddNegativeScore() throws Exception {
        System.out.println("\n========== TC-11: 录入负数分数 ==========");
        System.out.println("测试目的: 验证系统正确拒绝负数分数");
        System.out.println("输入: 学生ID=1, 科目='数学', 分数=-1, 日期='2023-06-01'");
        
        // 准备测试数据
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject("数学");
        score.setScore(-1); // 负数分数
        score.setExamDate(LocalDate.of(2023, 6, 1));

        // 执行测试 - 现在预期返回错误状态
        MvcResult result = mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())  // HTTP状态仍为200
                .andExpect(jsonPath("$.code").value(500))  // 业务状态码为500
                .andExpect(jsonPath("$.message").value("分数必须在0-100之间"))
                .andReturn();
                
        // 解析响应结果
        String content = result.getResponse().getContentAsString();
        Result response = objectMapper.readValue(content, Result.class);
        assertEquals(500, response.getCode());
        assertNull(response.getData());
        
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 系统正确拒绝了负数分数并返回错误信息");
    }

    @Test
    @DisplayName("TC-12: 录入超过100的分数")
    public void testAddScoreGreaterThan100() throws Exception {
        System.out.println("\n========== TC-12: 录入超过100的分数 ==========");
        System.out.println("测试目的: 验证系统正确拒绝超过100的分数");
        System.out.println("输入: 学生ID=1, 科目='数学', 分数=101, 日期='2023-06-01'");
        
        // 准备测试数据
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject("数学");
        score.setScore(101); // 超过100的分数
        score.setExamDate(LocalDate.of(2023, 6, 1));

        // 执行测试 - 现在预期返回错误状态
        MvcResult result = mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())  // HTTP状态仍为200
                .andExpect(jsonPath("$.code").value(500))  // 业务状态码为500
                .andExpect(jsonPath("$.message").value("分数必须在0-100之间"))
                .andReturn();
                
        // 解析响应结果
        String content = result.getResponse().getContentAsString();
        Result response = objectMapper.readValue(content, Result.class);
        assertEquals(500, response.getCode());
        assertNull(response.getData());
        
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 系统正确拒绝了超过100的分数并返回错误信息");
    }
    
    @Test
    @DisplayName("TC-13: 录入非数字分数")
    public void testAddNonNumericScore() throws Exception {
        System.out.println("\n========== TC-13: 录入非数字分数 ==========");
        System.out.println("测试目的: 验证系统正确拒绝非数字分数");
        System.out.println("输入: 学生ID=1, 科目='数学', 分数='优秀', 日期='2023-06-01'");
        
        // 准备测试数据 - 注意：需要直接构造JSON字符串，因为Java不允许将字符串赋值给Integer类型
        String requestJson = "{\"studentId\":1,\"subject\":\"数学\",\"score\":\"优秀\",\"examDate\":[2023,6,1]}";

        // 执行测试 - 预期应该返回400错误，因为score应该是数字
        MvcResult result = mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest()) // 期望返回400错误
                .andReturn();
                
        // 获取响应内容
        String content = result.getResponse().getContentAsString();
        
        System.out.println("系统响应: HTTP 400 Bad Request - " + content);
        System.out.println("测试结果: 通过 ✓ - 系统正确拒绝了非数字分数（格式错误）");
    }
    
    @Test
    @DisplayName("TC-14: 录入无效日期格式")
    public void testAddInvalidDateFormat() throws Exception {
        System.out.println("\n========== TC-14: 录入无效日期格式 ==========");
        System.out.println("测试目的: 验证系统正确拒绝无效日期格式");
        System.out.println("输入: 学生ID=1, 科目='数学', 分数=85, 日期='06/01/2023'(错误格式)");
        
        // 准备测试数据 - 注意：需要直接构造JSON字符串，因为LocalDate不接受格式错误的日期
        String requestJson = "{\"studentId\":1,\"subject\":\"数学\",\"score\":85,\"examDate\":\"06/01/2023\"}";

        // 执行测试 - 预期应该返回400错误，因为日期格式不正确
        MvcResult result = mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest()) // 期望返回400错误
                .andReturn();
                
        // 获取响应内容
        String content = result.getResponse().getContentAsString();
        
        System.out.println("系统响应: HTTP 400 Bad Request - " + content);
        System.out.println("测试结果: 通过 ✓ - 系统正确拒绝了无效日期格式（格式错误）");
    }

    @Test
    @DisplayName("TC-15: 录入未来日期")
    public void testAddFutureDate() throws Exception {
        System.out.println("\n========== TC-15: 录入未来日期 ==========");
        System.out.println("测试目的: 验证系统正确拒绝未来日期");
        System.out.println("输入: 学生ID=1, 科目='数学', 分数=85, 日期='2099-12-31'");
        
        // 准备测试数据
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject("数学");
        score.setScore(85);
        score.setExamDate(LocalDate.of(2099, 12, 31)); // 未来日期

        // 执行测试 - 现在预期返回错误状态
        MvcResult result = mockMvc.perform(post("/api/scores/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(score)))
                .andExpect(status().isOk())  // HTTP状态仍为200
                .andExpect(jsonPath("$.code").value(500))  // 业务状态码为500
                .andExpect(jsonPath("$.message").value("考试日期不能是未来日期"))
                .andReturn();
                
        // 解析响应结果
        String content = result.getResponse().getContentAsString();
        Result response = objectMapper.readValue(content, Result.class);
        assertEquals(500, response.getCode());
        assertNull(response.getData());
        
        System.out.println("系统响应: " + content);
        System.out.println("测试结果: 通过 ✓ - 系统正确拒绝了未来日期并返回错误信息");
    }

    // 注意：由于禁用了Spring Security过滤器，以下权限测试将无法正常工作
    // 如果需要测试权限，请移除@AutoConfigureMockMvc(addFilters = false)并启动后端服务
} 