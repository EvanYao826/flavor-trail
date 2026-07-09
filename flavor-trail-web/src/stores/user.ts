import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { UserInfo, LoginRequest, RegisterRequest } from '../api'
import { authApi, userApi } from '../api'

export const useUserStore = defineStore('user', () => {
  const user = ref<UserInfo | null>(null)
  const token = ref('')

  const init = () => {
    const savedToken = localStorage.getItem('token')
    const savedUser = localStorage.getItem('user')
    if (savedToken) {
      token.value = savedToken
    }
    if (savedUser) {
      try {
        user.value = JSON.parse(savedUser)
      } catch {
        localStorage.removeItem('user')
      }
    }
  }

  const login = async (data: LoginRequest) => {
    const response = await authApi.login(data)
    const { token: newToken, userInfo } = response.data.data
    token.value = newToken
    user.value = userInfo
    localStorage.setItem('token', newToken)
    localStorage.setItem('user', JSON.stringify(userInfo))
  }

  const register = async (data: RegisterRequest) => {
    const response = await authApi.register(data)
    const { token: newToken, userInfo } = response.data.data
    token.value = newToken
    user.value = userInfo
    localStorage.setItem('token', newToken)
    localStorage.setItem('user', JSON.stringify(userInfo))
  }

  const logout = () => {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  const updateProfile = async (data: { nickname?: string; avatarUrl?: string; gender?: number }) => {
    await userApi.updateProfile(data)
    if (user.value) {
      if (data.nickname !== undefined) user.value.nickname = data.nickname
      if (data.avatarUrl !== undefined) user.value.avatarUrl = data.avatarUrl
      if (data.gender !== undefined) user.value.gender = data.gender
      localStorage.setItem('user', JSON.stringify(user.value))
    }
  }

  const getProfile = async () => {
    const response = await userApi.getProfile()
    user.value = response.data.data
    localStorage.setItem('user', JSON.stringify(user.value))
  }

  return {
    user,
    token,
    init,
    login,
    register,
    logout,
    updateProfile,
    getProfile
  }
})