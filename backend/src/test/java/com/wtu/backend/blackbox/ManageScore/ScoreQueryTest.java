package com.wtu.backend.blackbox.ManageScore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtu.dto.ScoreQueryDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@Rollback
public class ScoreQueryTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test @DisplayName("TC-Q01: 按有效学生ID查询")
    public void testQueryByValidStudentId() throws Exception {
        System.out.println("========== TC-Q01: 按有效学生ID查询 ==========");
        System.out.println("【测试输入】学生ID=1, 姓名=\"\", 科目=\"\", 页码=1, 每页=10");
        System.out.println("【预期结果】返回该学生所有成绩，code=200");
        System.out.println("【等价类覆盖】EC1, EC9");
        ScoreQueryDTO query = new ScoreQueryDTO();
        query.setStudentId(1L);
        query.setPageNum(1);
        query.setPageSize(10);
        MvcResult result = mockMvc.perform(post("/api/scores/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-Q02: 按不存在学生ID查询")
    public void testQueryByNonExistentStudentId() throws Exception {
        System.out.println("========== TC-Q02: 按不存在学生ID查询 ==========");
        System.out.println("【测试输入】学生ID=999, 姓名=\"\", 科目=\"\", 页码=1, 每页=10");
        System.out.println("【预期结果】返回空记录列表，code=200");
        System.out.println("【等价类覆盖】EC2, EC9");
        ScoreQueryDTO query = new ScoreQueryDTO();
        query.setStudentId(999L);
        query.setPageNum(1);
        query.setPageSize(10);
        MvcResult result = mockMvc.perform(post("/api/scores/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-Q03: 按有效学生姓名查询")
    public void testQueryByValidStudentName() throws Exception {
        System.out.println("========== TC-Q03: 按有效学生姓名查询 ==========");
        System.out.println("【测试输入】学生ID=null, 姓名=\"张三\", 科目=\"\", 页码=1, 每页=10");
        System.out.println("【预期结果】返回该学生所有成绩，code=200");
        System.out.println("【等价类覆盖】EC4, EC9");
        ScoreQueryDTO query = new ScoreQueryDTO();
        query.setStudentName("张三");
        query.setPageNum(1);
        query.setPageSize(10);
        MvcResult result = mockMvc.perform(post("/api/scores/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-Q04: 按不存在学生姓名查询")
    public void testQueryByNonExistentStudentName() throws Exception {
        System.out.println("========== TC-Q04: 按不存在学生姓名查询 ==========");
        System.out.println("【测试输入】学生ID=null, 姓名=\"不存在\", 科目=\"\", 页码=1, 每页=10");
        System.out.println("【预期结果】返回空记录列表，code=200");
        System.out.println("【等价类覆盖】EC5, EC9");
        ScoreQueryDTO query = new ScoreQueryDTO();
        query.setStudentName("不存在");
        query.setPageNum(1);
        query.setPageSize(10);
        MvcResult result = mockMvc.perform(post("/api/scores/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-Q05: 按有效科目查询")
    public void testQueryByValidSubject() throws Exception {
        System.out.println("========== TC-Q05: 按有效科目查询 ==========");
        System.out.println("【测试输入】学生ID=null, 姓名=\"\", 科目=\"数学\", 页码=1, 每页=10");
        System.out.println("【预期结果】返回该科目所有成绩，code=200");
        System.out.println("【等价类覆盖】EC7, EC9");
        ScoreQueryDTO query = new ScoreQueryDTO();
        query.setSubject("数学");
        query.setPageNum(1);
        query.setPageSize(10);
        MvcResult result = mockMvc.perform(post("/api/scores/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-Q06: 按不存在科目查询")
    public void testQueryByNonExistentSubject() throws Exception {
        System.out.println("========== TC-Q06: 按不存在科目查询 ==========");
        System.out.println("【测试输入】学生ID=null, 姓名=\"\", 科目=\"不存在\", 页码=1, 每页=10");
        System.out.println("【预期结果】返回空记录列表，code=200");
        System.out.println("【等价类覆盖】EC8, EC9");
        ScoreQueryDTO query = new ScoreQueryDTO();
        query.setSubject("不存在");
        query.setPageNum(1);
        query.setPageSize(10);
        MvcResult result = mockMvc.perform(post("/api/scores/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-Q07: 多条件组合查询")
    public void testQueryByMultiCondition() throws Exception {
        System.out.println("========== TC-Q07: 多条件组合查询 ==========");
        System.out.println("【测试输入】学生ID=1, 姓名=\"\", 科目=\"数学\", 页码=1, 每页=10");
        System.out.println("【预期结果】返回符合条件的成绩，code=200");
        System.out.println("【等价类覆盖】EC1, EC7, EC9");
        ScoreQueryDTO query = new ScoreQueryDTO();
        query.setStudentId(1L);
        query.setSubject("数学");
        query.setPageNum(1);
        query.setPageSize(10);
        MvcResult result = mockMvc.perform(post("/api/scores/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-Q08: 无条件查询")
    public void testQueryWithoutCondition() throws Exception {
        System.out.println("========== TC-Q08: 无条件查询 ==========");
        System.out.println("【测试输入】学生ID=null, 姓名=\"\", 科目=\"\", 页码=1, 每页=10");
        System.out.println("【预期结果】返回所有成绩（第一页），code=200");
        System.out.println("【等价类覆盖】EC9");
        ScoreQueryDTO query = new ScoreQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);
        MvcResult result = mockMvc.perform(post("/api/scores/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-Q09: 无效页码查询")
    public void testQueryWithInvalidPageNum() throws Exception {
        System.out.println("========== TC-Q09: 无效页码查询 ==========");
        System.out.println("【测试输入】学生ID=null, 姓名=\"\", 科目=\"\", 页码=0, 每页=10");
        System.out.println("【预期结果】提示错误或自动调整，code=500");
        System.out.println("【等价类覆盖】EC10");
        ScoreQueryDTO query = new ScoreQueryDTO();
        query.setPageNum(0);
        query.setPageSize(10);
        MvcResult result = mockMvc.perform(post("/api/scores/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-Q10: 无效每页记录数查询")
    public void testQueryWithInvalidPageSize() throws Exception {
        System.out.println("========== TC-Q10: 无效每页记录数查询 ==========");
        System.out.println("【测试输入】学生ID=null, 姓名=\"\", 科目=\"\", 页码=1, 每页=-1");
        System.out.println("【预期结果】提示错误或自动调整，code=500");
        System.out.println("【等价类覆盖】EC11");
        ScoreQueryDTO query = new ScoreQueryDTO();
        query.setPageNum(1);
        query.setPageSize(-1);
        MvcResult result = mockMvc.perform(post("/api/scores/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-Q11: 分页查询第二页")
    public void testQuerySecondPage() throws Exception {
        System.out.println("========== TC-Q11: 分页查询第二页 ==========");
        System.out.println("【测试输入】学生ID=null, 姓名=\"\", 科目=\"\", 页码=2, 每页=10");
        System.out.println("【预期结果】返回第二页的记录，code=200");
        System.out.println("【等价类覆盖】EC9");
        ScoreQueryDTO query = new ScoreQueryDTO();
        query.setPageNum(2);
        query.setPageSize(10);
        MvcResult result = mockMvc.perform(post("/api/scores/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }

    @Test @DisplayName("TC-Q12: 学生ID为非数字")
    public void testQueryWithNonNumericStudentId() throws Exception {
        System.out.println("========== TC-Q12: 学生ID为非数字 ==========");
        System.out.println("【测试输入】学生ID=\"abc\", 姓名=\"\", 科目=\"\", 页码=1, 每页=10");
        System.out.println("【预期结果】请求参数类型错误，HTTP 400");
        System.out.println("【等价类覆盖】EC3");
        String requestJson = "{\"studentId\":\"abc\",\"pageNum\":1,\"pageSize\":10}";
        mockMvc.perform(post("/api/scores/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test @DisplayName("TC-Q13: 姓名为特殊字符")
    public void testQueryWithSpecialCharName() throws Exception {
        System.out.println("========== TC-Q13: 姓名为特殊字符 ==========");
        System.out.println("【测试输入】学生ID=null, 姓名=\"@#￥%\", 科目=\"\", 页码=1, 每页=10");
        System.out.println("【预期结果】返回空记录或参数错误，code=200/500");
        System.out.println("【等价类覆盖】EC6");
        ScoreQueryDTO query = new ScoreQueryDTO();
        query.setStudentName("@#￥%");
        query.setPageNum(1);
        query.setPageSize(10);
        MvcResult result = mockMvc.perform(post("/api/scores/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("系统响应: " + result.getResponse().getContentAsString());
    }
} 