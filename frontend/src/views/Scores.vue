<template>
  <div class="scores-container">
    <el-card class="filter-card">
      <el-form :model="queryForm" inline>
        <el-form-item label="学生ID">
          <el-input v-model="queryForm.studentId" placeholder="请输入学生ID" clearable />
        </el-form-item>
        
        <el-form-item label="学生姓名">
          <el-input v-model="queryForm.studentName" placeholder="请输入学生姓名" clearable />
        </el-form-item>
        
        <el-form-item label="科目">
          <el-input v-model="queryForm.subject" placeholder="请输入科目名称" clearable />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>成绩列表</span>
          <el-button 
            v-if="userStore.isTeacher" 
            type="primary" 
            size="small" 
            @click="$router.push('/dashboard/add-score')"
          >
            新增成绩
          </el-button>
        </div>
      </template>
      
      <el-table
        :data="scoreStore.scoreList"
        border
        style="width: 100%"
        v-loading="scoreStore.loading"
      >
        <el-table-column prop="scoreId" label="成绩ID" width="80" />
        <el-table-column prop="studentId" label="学生ID" width="80" />
        <el-table-column prop="studentName" label="学生姓名" width="100" />
        <el-table-column prop="subject" label="科目" width="100" />
        <el-table-column prop="score" label="分数" width="80" />
        <el-table-column prop="examDate" label="考试日期" width="120" />
        <el-table-column prop="status" label="通过状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === '通过' ? 'success' : 'danger'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="grade" label="等级" width="80">
          <template #default="scope">
            <el-tag :type="getTagType(scope.row.grade)">
              {{ scope.row.grade }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="180" v-if="userStore.isTeacher">
          <template #default="scope">
            <el-button 
              size="small" 
              type="primary" 
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              size="small" 
              type="danger" 
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          :page-sizes="[10, 20, 50]"
          :page-size="scoreStore.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="scoreStore.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 编辑成绩对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      title="编辑成绩" 
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form 
        :model="editForm" 
        :rules="rules" 
        ref="editFormRef" 
        label-width="80px"
      >
        <el-form-item label="学生ID" prop="studentId">
          <el-input v-model="editForm.studentId" disabled />
        </el-form-item>
        
        <el-form-item label="学生姓名">
          <el-input v-model="editForm.studentName" disabled />
        </el-form-item>
        
        <el-form-item label="科目" prop="subject">
          <el-input v-model="editForm.subject" />
        </el-form-item>
        
        <el-form-item label="分数" prop="score">
          <el-input-number v-model="editForm.score" :min="0" :max="100" />
        </el-form-item>
        
        <el-form-item label="考试日期" prop="examDate">
          <el-date-picker
            v-model="editForm.examDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEdit" :loading="submitLoading">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useScoreStore } from '../stores/score'
import { useUserStore } from '../stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const scoreStore = useScoreStore()
const userStore = useUserStore()

// 表单数据
const queryForm = reactive({
  studentId: '',
  studentName: '',
  subject: ''
})

// 分页数据
const currentPage = ref(1)

// 编辑相关
const dialogVisible = ref(false)
const editFormRef = ref(null)
const editForm = reactive({
  scoreId: '',
  studentId: '',
  studentName: '',
  subject: '',
  score: 0,
  examDate: ''
})
const submitLoading = ref(false)

// 表单验证规则
const rules = {
  subject: [
    { required: true, message: '请输入科目', trigger: 'blur' }
  ],
  score: [
    { required: true, message: '请输入分数', trigger: 'blur' }
  ],
  examDate: [
    { required: true, message: '请选择考试日期', trigger: 'blur' }
  ]
}

// 标签颜色
const getTagType = (grade) => {
  switch (grade) {
    case 'A': return 'success'
    case 'B': return 'success'
    case 'C': return 'warning'
    case 'D': return 'warning'
    case 'F': return 'danger'
    default: return ''
  }
}

// 初始加载数据
onMounted(async () => {
  await handleQuery()
})

// 查询
const handleQuery = async () => {
  // 处理studentId
  if (queryForm.studentId && !isNaN(queryForm.studentId)) {
    queryForm.studentId = parseInt(queryForm.studentId)
  } else {
    queryForm.studentId = null
  }
  
  // 查询成绩
  await scoreStore.queryScores(queryForm)
}

// 重置查询
const resetQuery = () => {
  Object.keys(queryForm).forEach(key => {
    queryForm[key] = ''
  })
  handleQuery()
}

// 分页大小变化
const handleSizeChange = (val) => {
  scoreStore.pageSize = val
  handleQuery()
}

// 页码变化
const handleCurrentChange = (val) => {
  currentPage.value = val
  scoreStore.setPage(val)
  handleQuery()
}

// 编辑成绩
const handleEdit = (row) => {
  Object.keys(editForm).forEach(key => {
    editForm[key] = row[key]
  })
  dialogVisible.value = true
}

// 提交编辑
const submitEdit = async () => {
  await editFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const result = await scoreStore.updateScore(editForm)
        if (result.success) {
          ElMessage.success('更新成功')
          dialogVisible.value = false
          handleQuery()
        } else {
          ElMessage.error(result.message || '更新失败')
        }
      } catch (error) {
        console.error('更新错误', error)
        ElMessage.error('更新失败，请稍后再试')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 删除成绩
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除 ${row.studentName} 的 ${row.subject} 成绩吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const result = await scoreStore.deleteScore(row.scoreId)
    if (result.success) {
      ElMessage.success('删除成功')
      handleQuery()
    } else {
      ElMessage.error(result.message || '删除失败')
    }
  }).catch(() => {})
}
</script>

<style scoped>
.scores-container {
  padding: 10px;
}

.filter-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style> 