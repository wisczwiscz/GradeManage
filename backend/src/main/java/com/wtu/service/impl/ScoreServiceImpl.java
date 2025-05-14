package com.wtu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wtu.dto.ScoreDTO;
import com.wtu.dto.ScoreQueryDTO;
import com.wtu.entity.Score;
import com.wtu.entity.Student;
import com.wtu.mapper.ScoreMapper;
import com.wtu.mapper.StudentMapper;
import com.wtu.service.ScoreService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 成绩服务实现类
 */
@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {

    private final StudentMapper studentMapper;

    public ScoreServiceImpl(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Override
    public Score addScore(Score score) {
        // 保存成绩记录
        save(score);
        return score;
    }

    @Override
    public Page<ScoreDTO> queryScores(ScoreQueryDTO queryDTO) {
        // 构建查询条件
        LambdaQueryWrapper<Score> queryWrapper = new LambdaQueryWrapper<>();
        
        // 根据学生ID查询
        if (queryDTO.getStudentId() != null) {
            queryWrapper.eq(Score::getStudentId, queryDTO.getStudentId());
        }
        
        // 根据科目查询
        if (StringUtils.hasText(queryDTO.getSubject())) {
            queryWrapper.eq(Score::getSubject, queryDTO.getSubject());
        }
        
        // 分页查询
        Page<Score> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        Page<Score> scorePage = page(page, queryWrapper);
        
        // 转换为DTO对象
        Page<ScoreDTO> dtoPage = new Page<>();
        BeanUtils.copyProperties(scorePage, dtoPage, "records");
        
        List<ScoreDTO> scoreDTOList = new ArrayList<>();
        for (Score score : scorePage.getRecords()) {
            ScoreDTO dto = convertToDTO(score);
            
            // 如果有学生姓名过滤条件，跳过不匹配的记录
            if (StringUtils.hasText(queryDTO.getStudentName()) && 
                    !dto.getStudentName().contains(queryDTO.getStudentName())) {
                continue;
            }
            
            scoreDTOList.add(dto);
        }
        
        dtoPage.setRecords(scoreDTOList);
        return dtoPage;
    }

    @Override
    public ScoreDTO getScoreById(Long scoreId) {
        Score score = getById(scoreId);
        if (score == null) {
            return null;
        }
        return convertToDTO(score);
    }

    @Override
    public boolean updateScore(Score score) {
        return updateById(score);
    }

    @Override
    public boolean deleteScore(Long scoreId) {
        return removeById(scoreId);
    }
    
    /**
     * 将Score实体转换为ScoreDTO
     *
     * @param score 成绩实体
     * @return 成绩DTO
     */
    private ScoreDTO convertToDTO(Score score) {
        ScoreDTO dto = new ScoreDTO();
        BeanUtils.copyProperties(score, dto);
        
        // 查询学生信息
        Student student = studentMapper.selectById(score.getStudentId());
        if (student != null) {
            dto.setStudentName(student.getName());
        }
        
        // 设置成绩状态（通过/不通过）
        dto.setStatus(score.getScore() >= 60 ? "通过" : "不通过");
        
        // 设置成绩等级
        int scoreValue = score.getScore();
        if (scoreValue >= 90) {
            dto.setGrade("A");
        } else if (scoreValue >= 80) {
            dto.setGrade("B");
        } else if (scoreValue >= 70) {
            dto.setGrade("C");
        } else if (scoreValue >= 60) {
            dto.setGrade("D");
        } else {
            dto.setGrade("F");
        }
        
        return dto;
    }
} 