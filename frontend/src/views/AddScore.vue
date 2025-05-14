<template>
  <div class="add-score-container">
    <el-card class="form-card">
      <template #header>
        <div>
          <span>录入成绩</span>
        </div>
      </template>
      
      <el-form
        :model="scoreForm"
        :rules="rules"
        ref="formRef"
        label-width="100px"
      >
        <el-form-item label="学生ID" prop="studentId">
          <el-input v-model.number="scoreForm.studentId" placeholder="请输入学生ID" />
        </el-form-item>
        
        <el-form-item label="科目" prop="subject">
          <el-input v-model="scoreForm.subject" placeholder="请输入科目名称" />
        </el-form-item>
        
        <el-form-item label="分数" prop="score">
          <el-input-number v-model="scoreForm.score" :min="0" :max="100" :precision="0" style="width: 200px" />
        </el-form-item>
        
        <el-form-item label="考试日期" prop="examDate">
          <el-date-picker
            v-model="scoreForm.examDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            style="width: 200px"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="submitLoading">提交</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useScoreStore } from '../stores/score'
import { ElMessage } from 'element-plus'

const router = useRouter()
const scoreStore = useScoreStore()
const formRef = ref(null)
const submitLoading = ref(false)

// 表单数据
const scoreForm = reactive({
  studentId: '',
  subject: '',
  score: 0,
  examDate: ''
})

// 表单验证规则
const rules = {
  studentId: [
    { required: true, message: '请输入学生ID', trigger: 'blur' },
    { type: 'number', message: '学生ID必须为数字', trigger: 'blur' }
  ],
  subject: [
    { required: true, message: '请输入科目', trigger: 'blur' }
  ],
  score: [
    { required: true, message: '请输入分数', trigger: 'blur' },
    { type: 'number', min: 0, max: 100, message: '分数必须在0-100之间', trigger: 'blur' }
  ],
  examDate: [
    { required: true, message: '请选择考试日期', trigger: 'blur' }
  ]
}

// 提交表单
const submitForm = async () => {
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const result = await scoreStore.addScore(scoreForm)
        if (result.success) {
          ElMessage.success('添加成功')
          router.push('/dashboard/scores')
        } else {
          ElMessage.error(result.message || '添加失败')
        }
      } catch (error) {
        console.error('添加错误', error)
        ElMessage.error('添加失败，请稍后再试')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  formRef.value.resetFields()
}
</script>

<style scoped>
.add-score-container {
  padding: 20px;
}

.form-card {
  max-width: 600px;
  margin: 0 auto;
}
</style> 