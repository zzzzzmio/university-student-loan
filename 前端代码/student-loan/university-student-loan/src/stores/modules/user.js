import { defineStore } from 'pinia'
import { ref } from 'vue'
import { userGetInfo } from '@/api/user'

// 用户模块
export const useUserStore = defineStore(
  'big-user',
  () => {
    const token = ref('') // 定义 token
    const user = ref({})
    const setToken = (t) => (token.value = t) // 设置 token
    const getUser = async () => {
      const res = await userGetInfo()
      user.value = res.data.data
    }
    const setUserInfo = (object) => {
      user.value = object
    }
    const removeToken = () => (token.value = '')
    const removeUserInfo = () => (user.value = {})
    return {
      token,
      setToken,
      setUserInfo,
      removeToken,
      removeUserInfo,
      getUser,
      user
    }
  },
  {
    persist: true // 持久化
  }
)
