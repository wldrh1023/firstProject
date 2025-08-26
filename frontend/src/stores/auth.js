import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authAPI, usersAPI } from '@/api'

export const useAuthStore = defineStore('auth', () => {
  // State
  const user = ref(null)
  const token = ref(localStorage.getItem('accessToken'))
  const isLoading = ref(false)
  const error = ref(null)

  // Getters
  const isAuthenticated = computed(() => !!token.value && !!user.value)
  const currentUser = computed(() => user.value)

  // Actions
  const signUp = async (userData) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await authAPI.signUp(userData)
      return response
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const signIn = async (credentials) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await authAPI.signIn(credentials)

      if (response.success && response.data) {
        token.value = response.data.accessToken
        localStorage.setItem('accessToken', response.data.accessToken)

        // 사용자 정보 가져오기
        await fetchCurrentUser()

        return response
      }
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const signOut = () => {
    user.value = null
    token.value = null
    localStorage.removeItem('accessToken')
    localStorage.removeItem('user')
  }

  const fetchCurrentUser = async () => {
    if (!token.value) return

    try {
      const response = await usersAPI.getCurrentUser()
      if (response.success && response.data) {
        user.value = response.data
        localStorage.setItem('user', JSON.stringify(response.data))
      }
    } catch (err) {
      console.error('Failed to fetch current user:', err)
      signOut()
    }
  }

  const updateProfile = async (profileData) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await usersAPI.updateProfile(profileData)
      if (response.success && response.data) {
        user.value = { ...user.value, ...response.data }
        localStorage.setItem('user', JSON.stringify(user.value))
      }
      return response
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const checkUsername = async (username) => {
    try {
      const response = await authAPI.checkUsername(username)
      return response.data?.available || false
    } catch (err) {
      console.error('Username check failed:', err)
      return false
    }
  }

  const checkEmail = async (email) => {
    try {
      const response = await authAPI.checkEmail(email)
      return response.data?.available || false
    } catch (err) {
      console.error('Email check failed:', err)
      return false
    }
  }

  // 초기화 - 로컬 스토리지에서 사용자 정보 복원
  const initializeAuth = async () => {
    const savedUser = localStorage.getItem('user')
    if (savedUser && token.value) {
      try {
        user.value = JSON.parse(savedUser)
        // 토큰이 유효한지 확인
        await fetchCurrentUser()
      } catch (err) {
        console.error('Failed to initialize auth:', err)
        signOut()
      }
    }
  }

  const clearError = () => {
    error.value = null
  }

  return {
    // State
    user,
    token,
    isLoading,
    error,

    // Getters
    isAuthenticated,
    currentUser,

    // Actions
    signUp,
    signIn,
    signOut,
    fetchCurrentUser,
    updateProfile,
    checkUsername,
    checkEmail,
    initializeAuth,
    clearError,
  }
})
