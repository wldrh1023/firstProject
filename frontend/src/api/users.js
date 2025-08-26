import apiClient from './client'

export const usersAPI = {
  // 현재 사용자 정보
  getCurrentUser: () => {
    return apiClient.get('/users/me')
  },

  // 사용자 프로필 조회
  getUserProfile: (userId) => {
    return apiClient.get(`/users/${userId}`)
  },

  // 사용자명으로 조회
  getUserByUsername: (username) => {
    return apiClient.get(`/users/username/${username}`)
  },

  // 프로필 수정
  updateProfile: (profileData) => {
    return apiClient.put('/users/me', profileData)
  },

  // 사용자 검색
  searchUsers: (keyword) => {
    return apiClient.get('/users/search', {
      params: { keyword },
    })
  },

  // 팔로워 목록
  getFollowers: (userId) => {
    return apiClient.get(`/users/${userId}/followers`)
  },

  // 팔로잉 목록
  getFollowing: (userId) => {
    return apiClient.get(`/users/${userId}/following`)
  },
}
