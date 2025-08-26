import apiClient from './client'

export const authAPI = {
  // 회원가입
  signUp: (userData) => {
    return apiClient.post('/auth/signup', userData)
  },

  // 로그인
  signIn: (credentials) => {
    return apiClient.post('/auth/signin', credentials)
  },

  // 사용자명 중복 확인
  checkUsername: (username) => {
    return apiClient.get('/auth/check-username', {
      params: { username },
    })
  },

  // 이메일 중복 확인
  checkEmail: (email) => {
    return apiClient.get('/auth/check-email', {
      params: { email },
    })
  },
}
