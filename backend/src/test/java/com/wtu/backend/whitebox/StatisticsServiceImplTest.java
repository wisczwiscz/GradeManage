package com.wtu.backend.whitebox;

import com.wtu.dto.StatisticsDTO;
import com.wtu.entity.Score;
import com.wtu.mapper.ScoreMapper;
import com.wtu.service.impl.StatisticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StatisticsServiceImplTest {
    private StatisticsServiceImpl statisticsService;
    private ScoreMapper scoreMapper;

    @BeforeEach
    void 初始化() {
        scoreMapper = Mockito.mock(ScoreMapper.class);
        statisticsService = new StatisticsServiceImpl(scoreMapper);
    }

    @Test
    void 测试空数据() {
        Mockito.when(scoreMapper.selectList(Mockito.any())).thenReturn(Collections.emptyList());
        StatisticsDTO dto = statisticsService.getStatisticsBySubject("物理");
        assertNull(dto.getSubject(), "空数据时，科目应为null");
        assertNull(dto.getAvgScore(), "空数据时，平均分应为null");
        assertNull(dto.getMaxScore(), "空数据时，最高分应为null");
        assertNull(dto.getMinScore(), "空数据时，最低分应为null");
        assertNull(dto.getPassCount(), "空数据时，及格人数应为null");
        assertNull(dto.getFailCount(), "空数据时，不及格人数应为null");
        assertNull(dto.getGradeDistribution(), "空数据时，等级分布应为null");
    }

    @Test
    void 测试正常分数分布() {
        List<Score> scores = Arrays.asList(
                createScore(1L, "数学", 85),
                createScore(2L, "数学", 90),
                createScore(3L, "数学", 60)
        );
        Mockito.when(scoreMapper.selectList(Mockito.any())).thenReturn(scores);
        StatisticsDTO dto = statisticsService.getStatisticsBySubject("数学");
        assertEquals("数学", dto.getSubject(), "科目应为数学");
        assertEquals(78.3, dto.getAvgScore(), 0.1, "平均分应为78.3");
        assertEquals(90, dto.getMaxScore(), "最高分应为90");
        assertEquals(60, dto.getMinScore(), "最低分应为60");
        assertEquals(2, dto.getPassCount(), "及格人数应为2");
        assertEquals(1, dto.getFailCount(), "不及格人数应为1");
        assertEquals(1, dto.getGradeDistribution().get("A"), "A等级人数应为1");
        assertEquals(1, dto.getGradeDistribution().get("B"), "B等级人数应为1");
        assertEquals(0, dto.getGradeDistribution().get("C"), "C等级人数应为0");
        assertEquals(1, dto.getGradeDistribution().get("D"), "D等级人数应为1");
        assertEquals(0, dto.getGradeDistribution().get("F"), "F等级人数应为0");
    }

    @Test
    void 测试全部及格() {
        List<Score> scores = Arrays.asList(
                createScore(1L, "英语", 60),
                createScore(2L, "英语", 100)
        );
        Mockito.when(scoreMapper.selectList(Mockito.any())).thenReturn(scores);
        StatisticsDTO dto = statisticsService.getStatisticsBySubject("英语");
        assertEquals(2, dto.getPassCount(), "全部及格时及格人数应为2");
        assertEquals(0, dto.getFailCount(), "全部及格时不及格人数应为0");
        assertEquals(1, dto.getGradeDistribution().get("D"), "D等级人数应为1");
        assertEquals(1, dto.getGradeDistribution().get("A"), "A等级人数应为1");
    }

    @Test
    void 测试全部不及格() {
        List<Score> scores = Arrays.asList(
                createScore(1L, "英语", 50),
                createScore(2L, "英语", 59)
        );
        Mockito.when(scoreMapper.selectList(Mockito.any())).thenReturn(scores);
        StatisticsDTO dto = statisticsService.getStatisticsBySubject("英语");
        assertEquals(0, dto.getPassCount(), "全部不及格时及格人数应为0");
        assertEquals(2, dto.getFailCount(), "全部不及格时不及格人数应为2");
        assertEquals(2, dto.getGradeDistribution().get("F"), "F等级人数应为2");
    }

    @Test
    void 测试单条分数() {
        List<Score> scores = Collections.singletonList(
                createScore(1L, "生物", 77)
        );
        Mockito.when(scoreMapper.selectList(Mockito.any())).thenReturn(scores);
        StatisticsDTO dto = statisticsService.getStatisticsBySubject("生物");
        assertEquals(77.0, dto.getAvgScore(), "单条分数时平均分应为77.0");
        assertEquals(77, dto.getMaxScore(), "单条分数时最高分应为77");
        assertEquals(77, dto.getMinScore(), "单条分数时最低分应为77");
        assertEquals(1, dto.getGradeDistribution().values().stream().mapToInt(Integer::intValue).sum(), "等级分布总人数应为1");
    }

    @Test
    void 测试等级边界() {
        List<Score> scores = Arrays.asList(
                createScore(1L, "等级边界", 90), // A
                createScore(2L, "等级边界", 80), // B
                createScore(3L, "等级边界", 70), // C
                createScore(4L, "等级边界", 60), // D
                createScore(5L, "等级边界", 59)  // F
        );
        Mockito.when(scoreMapper.selectList(Mockito.any())).thenReturn(scores);
        StatisticsDTO dto = statisticsService.getStatisticsBySubject("等级边界");
        assertEquals(1, dto.getGradeDistribution().get("A"), "A等级人数应为1");
        assertEquals(1, dto.getGradeDistribution().get("B"), "B等级人数应为1");
        assertEquals(1, dto.getGradeDistribution().get("C"), "C等级人数应为1");
        assertEquals(1, dto.getGradeDistribution().get("D"), "D等级人数应为1");
        assertEquals(1, dto.getGradeDistribution().get("F"), "F等级人数应为1");
    }

    @Test
    void 测试所有分数相同() {
        List<Score> scores = Arrays.asList(
                createScore(1L, "全相同", 85),
                createScore(2L, "全相同", 85),
                createScore(3L, "全相同", 85)
        );
        Mockito.when(scoreMapper.selectList(Mockito.any())).thenReturn(scores);
        StatisticsDTO dto = statisticsService.getStatisticsBySubject("全相同");
        assertEquals(85.0, dto.getAvgScore(), "所有分数相同时平均分应为85.0");
        assertEquals(85, dto.getMaxScore(), "所有分数相同时最高分应为85");
        assertEquals(85, dto.getMinScore(), "所有分数相同时最低分应为85");
        assertEquals(3, dto.getGradeDistribution().get("B"), "B等级人数应为3");
    }

    @Test
    void 测试异常分数() {
        List<Score> scores = Arrays.asList(
                createScore(1L, "异常", -5),
                createScore(2L, "异常", 105)
        );
        Mockito.when(scoreMapper.selectList(Mockito.any())).thenReturn(scores);
        StatisticsDTO dto = statisticsService.getStatisticsBySubject("异常");
        assertEquals(-5, dto.getMinScore(), "异常分数时最低分应为-5");
        assertEquals(105, dto.getMaxScore(), "异常分数时最高分应为105");
    }

    @Test
    void 测试空科目() {
        Mockito.when(scoreMapper.selectList(Mockito.any())).thenReturn(Collections.emptyList());
        StatisticsDTO dto = statisticsService.getStatisticsBySubject(null);
        assertNull(dto.getSubject(), "空科目时，科目应为null");
    }

    // 辅助方法，便于构造Score对象
    private Score createScore(Long id, String subject, Integer scoreValue) {
        Score score = new Score();
        score.setScoreId(id);
        score.setStudentId(1L);
        score.setSubject(subject);
        score.setScore(scoreValue);
        score.setExamDate(null);
        return score;
    }
} 