import apiClient from './client'

export const postsAPI = {
  // 게시물 생성
  createPost: (postData) => {
    return apiClient.post('/posts', postData)
  },

  // 게시물 조회
  getPost: (postId) => {
    return apiClient.get(`/posts/${postId}`)
  },

  // 피드 조회
  getFeed: (page = 0, size = 20) => {
    return apiClient.get('/posts/feed', {
      params: { page, size },
    })
  },

  // 사용자 게시물 목록
  getUserPosts: (userId, page = 0, size = 20) => {
    return apiClient.get(`/posts/user/${userId}`, {
      params: { page, size },
    })
  },

  // 게시물 검색
  searchPosts: (keyword, page = 0, size = 20) => {
    return apiClient.get('/posts/search', {
      params: { keyword, page, size },
    })
  },

  // 게시물 수정
  updatePost: (postId, postData) => {
    return apiClient.put(`/posts/${postId}`, postData)
  },

  // 게시물 삭제
  deletePost: (postId) => {
    return apiClient.delete(`/posts/${postId}`)
  },
}
