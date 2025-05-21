package com.wtu.backend.whitebox;

import com.wtu.entity.Score;
import com.wtu.mapper.ScoreMapper;
import com.wtu.service.impl.StatisticsServiceImpl;
import com.wtu.dto.StatisticsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StatisticsServiceImplWhiteBoxTest {
    private StatisticsServiceImpl statisticsService;
    private ScoreMapper scoreMapper;

    @BeforeEach
    void setUp() {
        scoreMapper = Mockito.mock(ScoreMapper.class);
        statisticsService = new StatisticsServiceImpl(scoreMapper);
    }

    // 反射调用private方法
    private StatisticsDTO callCalculateStatistics(String subject, List<Score> scores) throws Exception {
        Method method = StatisticsServiceImpl.class.getDeclaredMethod("calculateStatistics", String.class, List.class);
        method.setAccessible(true);
        return (StatisticsDTO) method.invoke(statisticsService, subject, scores);
    }

    private Score makeScore(int score) {
        Score s = new Score();
        s.setScore(score);
        return s;
    }

    @Test
    void TC01_95分_A类别加1() throws Exception {
        List<Score> scores = Collections.singletonList(makeScore(95));
        StatisticsDTO dto = callCalculateStatistics("test", scores);
        assertEquals(1, dto.getGradeDistribution().get("A"));
        assertEquals(0, dto.getGradeDistribution().get("B"));
        assertEquals(0, dto.getGradeDistribution().get("C"));
        assertEquals(0, dto.getGradeDistribution().get("D"));
        assertEquals(0, dto.getGradeDistribution().get("F"));
    }

    @Test
    void TC02_85分_B类别加1() throws Exception {
        List<Score> scores = Collections.singletonList(makeScore(85));
        StatisticsDTO dto = callCalculateStatistics("test", scores);
        assertEquals(0, dto.getGradeDistribution().get("A"));
        assertEquals(1, dto.getGradeDistribution().get("B"));
        assertEquals(0, dto.getGradeDistribution().get("C"));
        assertEquals(0, dto.getGradeDistribution().get("D"));
        assertEquals(0, dto.getGradeDistribution().get("F"));
    }

    @Test
    void TC03_75分_C类别加1() throws Exception {
        List<Score> scores = Collections.singletonList(makeScore(75));
        StatisticsDTO dto = callCalculateStatistics("test", scores);
        assertEquals(0, dto.getGradeDistribution().get("A"));
        assertEquals(0, dto.getGradeDistribution().get("B"));
        assertEquals(1, dto.getGradeDistribution().get("C"));
        assertEquals(0, dto.getGradeDistribution().get("D"));
        assertEquals(0, dto.getGradeDistribution().get("F"));
    }

    @Test
    void TC04_65分_D类别加1() throws Exception {
        List<Score> scores = Collections.singletonList(makeScore(65));
        StatisticsDTO dto = callCalculateStatistics("test", scores);
        assertEquals(0, dto.getGradeDistribution().get("A"));
        assertEquals(0, dto.getGradeDistribution().get("B"));
        assertEquals(0, dto.getGradeDistribution().get("C"));
        assertEquals(1, dto.getGradeDistribution().get("D"));
        assertEquals(0, dto.getGradeDistribution().get("F"));
    }

    @Test
    void TC05_55分_F类别加1() throws Exception {
        List<Score> scores = Collections.singletonList(makeScore(55));
        StatisticsDTO dto = callCalculateStatistics("test", scores);
        assertEquals(0, dto.getGradeDistribution().get("A"));
        assertEquals(0, dto.getGradeDistribution().get("B"));
        assertEquals(0, dto.getGradeDistribution().get("C"));
        assertEquals(0, dto.getGradeDistribution().get("D"));
        assertEquals(1, dto.getGradeDistribution().get("F"));
    }

    @Test
    void TC06_五个分数_各类别各加1() throws Exception {
        List<Score> scores = Arrays.asList(makeScore(95), makeScore(85), makeScore(75), makeScore(65), makeScore(55));
        StatisticsDTO dto = callCalculateStatistics("test", scores);
        assertEquals(1, dto.getGradeDistribution().get("A"));
        assertEquals(1, dto.getGradeDistribution().get("B"));
        assertEquals(1, dto.getGradeDistribution().get("C"));
        assertEquals(1, dto.getGradeDistribution().get("D"));
        assertEquals(1, dto.getGradeDistribution().get("F"));
    }

    @Test
    void TC07_空分数列表_不执行循环() throws Exception {
        List<Score> scores = new ArrayList<>();
        StatisticsDTO dto = callCalculateStatistics("test", scores);
        assertEquals(0, dto.getGradeDistribution().get("A"));
        assertEquals(0, dto.getGradeDistribution().get("B"));
        assertEquals(0, dto.getGradeDistribution().get("C"));
        assertEquals(0, dto.getGradeDistribution().get("D"));
        assertEquals(0, dto.getGradeDistribution().get("F"));
    }

    @Test
    void TC08_85和75_B加1_C加1() throws Exception {
        List<Score> scores = Arrays.asList(makeScore(85), makeScore(75));
        StatisticsDTO dto = callCalculateStatistics("test", scores);
        assertEquals(0, dto.getGradeDistribution().get("A"));
        assertEquals(1, dto.getGradeDistribution().get("B"));
        assertEquals(1, dto.getGradeDistribution().get("C"));
        assertEquals(0, dto.getGradeDistribution().get("D"));
        assertEquals(0, dto.getGradeDistribution().get("F"));
    }

    @Test
    void TC09_85_75_65_55_BCDE各加1() throws Exception {
        List<Score> scores = Arrays.asList(makeScore(85), makeScore(75), makeScore(65), makeScore(55));
        StatisticsDTO dto = callCalculateStatistics("test", scores);
        assertEquals(0, dto.getGradeDistribution().get("A"));
        assertEquals(1, dto.getGradeDistribution().get("B"));
        assertEquals(1, dto.getGradeDistribution().get("C"));
        assertEquals(1, dto.getGradeDistribution().get("D"));
        assertEquals(1, dto.getGradeDistribution().get("F"));
    }

    @Test
    void TC10_六个分数_多F类别() throws Exception {
        List<Score> scores = Arrays.asList(makeScore(95), makeScore(85), makeScore(75), makeScore(65), makeScore(55), makeScore(50));
        StatisticsDTO dto = callCalculateStatistics("test", scores);
        assertEquals(1, dto.getGradeDistribution().get("A"));
        assertEquals(1, dto.getGradeDistribution().get("B"));
        assertEquals(1, dto.getGradeDistribution().get("C"));
        assertEquals(1, dto.getGradeDistribution().get("D"));
        assertEquals(2, dto.getGradeDistribution().get("F"));
    }
} 