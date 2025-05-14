import { defineStore } from 'pinia'
import request from '../utils/request'

export const useScoreStore = defineStore('score', {
  state: () => ({
    scoreList: [],
    total: 0,
    currentPage: 1,
    pageSize: 10,
    loading: false,
    statistics: [],
    subjectStatistics: null
  }),
  
  actions: {
    async queryScores(params = {}) {
      this.loading = true
      try {
        const queryParams = {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          ...params
        }
        
        const response = await request.post('/api/scores/query', queryParams)
        
        if (response.code === 200) {
          const data = response.data
          this.scoreList = data.records
          this.total = data.total
          return { success: true }
        } else {
          return { success: false, message: response.message }
        }
      } catch (error) {
        console.error('查询错误:', error)
        return { success: false, message: error.response?.data?.message || '查询失败，请稍后再试' }
      } finally {
        this.loading = false
      }
    },
    
    async getScoreById(id) {
      try {
        const response = await request.get(`/api/scores/${id}`)
        if (response.code === 200) {
          return { success: true, data: response.data }
        } else {
          return { success: false, message: response.message }
        }
      } catch (error) {
        return { success: false, message: error.response?.data?.message || '获取成绩详情失败' }
      }
    },
    
    async addScore(scoreData) {
      try {
        const response = await request.post('/api/scores/add', scoreData)
        if (response.code === 200) {
          return { success: true, data: response.data }
        } else {
          return { success: false, message: response.message }
        }
      } catch (error) {
        return { success: false, message: error.response?.data?.message || '添加成绩失败' }
      }
    },
    
    async updateScore(scoreData) {
      try {
        const response = await request.put('/api/scores/update', scoreData)
        if (response.code === 200) {
          return { success: true }
        } else {
          return { success: false, message: response.message }
        }
      } catch (error) {
        return { success: false, message: error.response?.data?.message || '更新成绩失败' }
      }
    },
    
    async deleteScore(id) {
      try {
        const response = await request.delete(`/api/scores/${id}`)
        if (response.code === 200) {
          return { success: true }
        } else {
          return { success: false, message: response.message }
        }
      } catch (error) {
        return { success: false, message: error.response?.data?.message || '删除成绩失败' }
      }
    },
    
    async getBasicStatistics() {
      this.loading = true
      try {
        const response = await request.get('/api/statistics/basic')
        if (response.code === 200) {
          this.statistics = response.data
          return { success: true }
        } else {
          return { success: false, message: response.message }
        }
      } catch (error) {
        return { success: false, message: error.response?.data?.message || '获取统计数据失败' }
      } finally {
        this.loading = false
      }
    },
    
    async getSubjectStatistics(subject) {
      this.loading = true
      try {
        const response = await request.get(`/api/statistics/subject/${subject}`)
        if (response.code === 200) {
          this.subjectStatistics = response.data
          return { success: true }
        } else {
          return { success: false, message: response.message }
        }
      } catch (error) {
        return { success: false, message: error.response?.data?.message || '获取科目统计数据失败' }
      } finally {
        this.loading = false
      }
    },
    
    setPage(page) {
      this.currentPage = page
    }
  }
}) 