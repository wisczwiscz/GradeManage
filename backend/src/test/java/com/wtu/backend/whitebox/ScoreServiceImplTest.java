package com.wtu.backend.whitebox;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wtu.dto.ScoreDTO;
import com.wtu.dto.ScoreQueryDTO;
import com.wtu.entity.Score;
import com.wtu.entity.Student;
import com.wtu.mapper.ScoreMapper;
import com.wtu.mapper.StudentMapper;
import com.wtu.service.impl.ScoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreServiceImplTest {
    private ScoreServiceImpl scoreService;
    private StudentMapper studentMapper;
    private ScoreMapper scoreMapper;

    @BeforeEach
    void 初始化() {
        studentMapper = Mockito.mock(StudentMapper.class);
        scoreMapper = Mockito.mock(ScoreMapper.class);
        scoreService = new ScoreServiceImpl(studentMapper);
    }

    @Test
    void 测试添加成绩_学生ID有效() {
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject("数学");
        score.setScore(90);
        score.setExamDate(LocalDate.now());
        Student student = new Student();
        student.setStudentId(1L);
        Mockito.when(studentMapper.selectById(1L)).thenReturn(student);
        Score result = scoreService.addScore(score);
        assertEquals(score, result, "添加成绩时，返回的成绩对象应与输入一致");
    }

    @Test
    void 测试添加成绩_学生ID无效() {
        Score score = new Score();
        score.setStudentId(999L);
        score.setSubject("数学");
        score.setScore(90);
        Mockito.when(studentMapper.selectById(999L)).thenReturn(null);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> scoreService.addScore(score));
        assertTrue(ex.getMessage().contains("学生ID不存在"), "学生ID无效时应抛出异常");
    }

    @Test
    void 测试添加成绩_分数无效() {
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject("数学");
        score.setScore(120);
        Student student = new Student();
        student.setStudentId(1L);
        Mockito.when(studentMapper.selectById(1L)).thenReturn(student);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> scoreService.addScore(score));
        assertTrue(ex.getMessage().contains("分数必须在0-100之间"), "分数越界时应抛出异常");
    }

    @Test
    void 测试按ID查找成绩_存在() {
        Score score = new Score();
        score.setScoreId(1L);
        score.setStudentId(1L);
        score.setSubject("语文");
        score.setScore(80);
        Mockito.when(scoreMapper.selectById(1L)).thenReturn(score);
        Student student = new Student();
        student.setStudentId(1L);
        student.setName("张三");
        Mockito.when(studentMapper.selectById(1L)).thenReturn(student);
        ScoreDTO dto = scoreService.getScoreById(1L);
        assertNotNull(dto, "查找存在成绩时，结果不应为null");
        assertEquals("张三", dto.getStudentName(), "学生姓名应为张三");
        assertEquals("语文", dto.getSubject(), "科目应为语文");
        assertEquals(80, dto.getScore(), "分数应为80");
    }

    @Test
    void 测试按ID查找成绩_不存在() {
        Mockito.when(scoreMapper.selectById(2L)).thenReturn(null);
        ScoreDTO dto = scoreService.getScoreById(2L);
        assertNull(dto, "查找不存在成绩时，结果应为null");
    }

    // 你可以继续补充queryScores、updateScore、deleteScore等方法的边界和异常分支
} 