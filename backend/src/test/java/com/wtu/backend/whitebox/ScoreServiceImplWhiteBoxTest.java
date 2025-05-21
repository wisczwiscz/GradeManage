package com.wtu.backend.whitebox;

import com.wtu.entity.Score;
import com.wtu.entity.Student;
import com.wtu.mapper.ScoreMapper;
import com.wtu.mapper.StudentMapper;
import com.wtu.service.impl.ScoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreServiceImplWhiteBoxTest {
    private ScoreServiceImpl scoreService;
    private StudentMapper studentMapper;
    private ScoreMapper scoreMapper;

    @BeforeEach
    void 初始化() throws Exception {
        studentMapper = Mockito.mock(StudentMapper.class);
        scoreMapper = Mockito.mock(ScoreMapper.class);
        scoreService = new ScoreServiceImpl(studentMapper);
        // 反射注入baseMapper，避免NPE
        java.lang.reflect.Field baseMapperField = com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.class.getDeclaredField("baseMapper");
        baseMapperField.setAccessible(true);
        baseMapperField.set(scoreService, scoreMapper);
    }

    @Test
    void 白盒_TC1_学生ID有效_分数正常_日期今天_应通过() {
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject("语文");
        score.setScore(50);
        score.setExamDate(LocalDate.now());
        Student student = new Student();
        student.setStudentId(1L);
        Mockito.when(studentMapper.selectById(1L)).thenReturn(student);
        assertDoesNotThrow(() -> scoreService.addScore(score));
    }

    @Test
    void 白盒_TC2_学生ID为空_应抛异常() {
        Score score = new Score();
        score.setStudentId(null);
        score.setSubject("数学");
        score.setScore(50);
        score.setExamDate(LocalDate.now());
        Exception ex = assertThrows(IllegalArgumentException.class, () -> scoreService.addScore(score));
        assertTrue(ex.getMessage().contains("学生ID不能为空"));
    }

    @Test
    void 白盒_TC3a_分数小于0_应抛异常() {
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject("英语");
        score.setScore(-5);
        score.setExamDate(LocalDate.now());
        Student student = new Student();
        student.setStudentId(1L);
        Mockito.when(studentMapper.selectById(1L)).thenReturn(student);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> scoreService.addScore(score));
        assertTrue(ex.getMessage().contains("分数必须在0-100之间"));
    }

    @Test
    void 白盒_TC3b_分数大于100_应抛异常() {
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject("物理");
        score.setScore(105);
        score.setExamDate(LocalDate.now());
        Student student = new Student();
        student.setStudentId(1L);
        Mockito.when(studentMapper.selectById(1L)).thenReturn(student);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> scoreService.addScore(score));
        assertTrue(ex.getMessage().contains("分数必须在0-100之间"));
    }

    @Test
    void 白盒_TC4_考试日期为未来_应抛异常() {
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject("化学");
        score.setScore(50);
        score.setExamDate(LocalDate.now().plusDays(1));
        Student student = new Student();
        student.setStudentId(1L);
        Mockito.when(studentMapper.selectById(1L)).thenReturn(student);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> scoreService.addScore(score));
        assertTrue(ex.getMessage().contains("考试日期不能是未来日期"));
    }

    @Test
    void 白盒_TC5_考试日期为空_应通过() {
        Score score = new Score();
        score.setStudentId(1L);
        score.setSubject("生物");
        score.setScore(50);
        score.setExamDate(null);
        Student student = new Student();
        student.setStudentId(1L);
        Mockito.when(studentMapper.selectById(1L)).thenReturn(student);
        assertDoesNotThrow(() -> scoreService.addScore(score));
    }
} 