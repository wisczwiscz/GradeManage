<template>
  <div class="statistics-container">
    <el-card class="filter-card">
      <el-form inline>
        <el-form-item label="选择科目">
          <el-select v-model="selectedSubject" placeholder="请选择科目" clearable @change="handleSubjectChange">
            <el-option
              v-for="stat in scoreStore.statistics"
              :key="stat.subject"
              :label="stat.subject"
              :value="stat.subject"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="loadStatistics" :loading="scoreStore.loading">刷新</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 科目整体概览 -->
    <el-row :gutter="20" class="statistics-cards" v-if="!selectedSubject">
      <el-col :span="24">
        <el-card class="stat-card">
          <template #header>
            <div class="card-header">
              <span>所有科目成绩统计</span>
            </div>
          </template>
          
          <el-table
            :data="scoreStore.statistics"
            border
            style="width: 100%"
            v-loading="scoreStore.loading"
          >
            <el-table-column prop="subject" label="科目" />
            <el-table-column prop="avgScore" label="平均分" />
            <el-table-column prop="maxScore" label="最高分" />
            <el-table-column prop="minScore" label="最低分" />
            <el-table-column prop="passCount" label="及格人数" />
            <el-table-column prop="failCount" label="不及格人数" />
            <el-table-column label="操作">
              <template #default="scope">
                <el-button 
                  type="primary" 
                  size="small" 
                  @click="handleViewDetail(scope.row.subject)"
                >
                  详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 单个科目详情 -->
    <template v-else>
      <el-row :gutter="20" class="statistics-cards">
        <el-col :xs="24" :sm="12" :md="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-title">平均分</div>
              <div class="stat-value">{{ subjectStat.avgScore }}</div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-title">最高分</div>
              <div class="stat-value">{{ subjectStat.maxScore }}</div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-title">最低分</div>
              <div class="stat-value">{{ subjectStat.minScore }}</div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-title">总人数</div>
              <div class="stat-value">{{ subjectStat.passCount + subjectStat.failCount }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" class="statistics-cards">
        <!-- 及格率数据 -->
        <el-col :span="12">
          <el-card class="stat-card">
            <template #header>
              <div class="card-header">
                <span>及格情况</span>
              </div>
            </template>
            
            <div class="chart-container">
              <div class="pass-stats">
                <div class="pass-item">
                  <div class="pass-label">及格</div>
                  <div class="pass-value">{{ subjectStat.passCount }}</div>
                  <div class="pass-rate">{{ calculatePassRate() }}%</div>
                </div>
                <div class="pass-item">
                  <div class="pass-label fail">不及格</div>
                  <div class="pass-value">{{ subjectStat.failCount }}</div>
                  <div class="pass-rate">{{ calculateFailRate() }}%</div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <!-- 等级分布 -->
        <el-col :span="12">
          <el-card class="stat-card">
            <template #header>
              <div class="card-header">
                <span>等级分布</span>
              </div>
            </template>
            
            <div class="grade-distribution">
              <div 
                v-for="(count, grade) in subjectStat.gradeDistribution" 
                :key="grade"
                class="grade-item"
              >
                <div class="grade-label" :class="'grade-' + grade.toLowerCase()">{{ grade }}</div>
                <div class="grade-count">{{ count }}人</div>
                <div class="grade-rate">{{ calculateGradeRate(count) }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useScoreStore } from '../stores/score'

const scoreStore = useScoreStore()
const selectedSubject = ref('')

// 获取当前选中科目的统计数据
const subjectStat = computed(() => {
  if (!selectedSubject.value) return {}
  
  return scoreStore.statistics.find(stat => stat.subject === selectedSubject.value) || 
         scoreStore.subjectStatistics || {}
})

// 加载统计数据
const loadStatistics = async () => {
  await scoreStore.getBasicStatistics()
}

// 处理科目选择变化
const handleSubjectChange = async (subject) => {
  if (subject) {
    await scoreStore.getSubjectStatistics(subject)
  }
}

// 查看科目详情
const handleViewDetail = (subject) => {
  selectedSubject.value = subject
  scoreStore.getSubjectStatistics(subject)
}

// 计算及格率
const calculatePassRate = () => {
  const total = subjectStat.value.passCount + subjectStat.value.failCount
  if (total === 0) return 0
  return Math.round((subjectStat.value.passCount / total) * 100)
}

// 计算不及格率
const calculateFailRate = () => {
  const total = subjectStat.value.passCount + subjectStat.value.failCount
  if (total === 0) return 0
  return Math.round((subjectStat.value.failCount / total) * 100)
}

// 计算等级比例
const calculateGradeRate = (count) => {
  const total = subjectStat.value.passCount + subjectStat.value.failCount
  if (total === 0) return 0
  return Math.round((count / total) * 100)
}

// 初始加载
onMounted(async () => {
  await loadStatistics()
})
</script>

<style scoped>
.statistics-container {
  padding: 10px;
}

.filter-card {
  margin-bottom: 20px;
}

.statistics-cards {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-card {
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
  padding: 20px 0;
}

.stat-title {
  font-size: 16px;
  color: #909399;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.chart-container {
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.pass-stats {
  display: flex;
  justify-content: space-around;
  width: 100%;
}

.pass-item {
  text-align: center;
}

.pass-label {
  font-size: 16px;
  color: #67C23A;
  margin-bottom: 10px;
}

.pass-label.fail {
  color: #F56C6C;
}

.pass-value {
  font-size: 24px;
  font-weight: bold;
}

.pass-rate {
  font-size: 18px;
  color: #909399;
  margin-top: 5px;
}

.grade-distribution {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
}

.grade-item {
  text-align: center;
  padding: 10px;
  width: 20%;
}

.grade-label {
  font-size: 18px;
  font-weight: bold;
  width: 30px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  border-radius: 50%;
  margin: 0 auto 5px;
}

.grade-a {
  background-color: #67C23A;
  color: white;
}

.grade-b {
  background-color: #67C23A;
  color: white;
}

.grade-c {
  background-color: #E6A23C;
  color: white;
}

.grade-d {
  background-color: #E6A23C;
  color: white;
}

.grade-f {
  background-color: #F56C6C;
  color: white;
}

.grade-count {
  font-size: 16px;
  font-weight: bold;
}

.grade-rate {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}
</style> 