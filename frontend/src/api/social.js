import apiClient from './client'

export const socialAPI = {
  // 댓글 관련
  comments: {
    create: (postId, content) => {
      return apiClient.post(`/comments/post/${postId}`, { content })
    },

    getByPost: (postId) => {
      return apiClient.get(`/comments/post/${postId}`)
    },

    update: (commentId, content) => {
      return apiClient.put(`/comments/${commentId}`, { content })
    },

    delete: (commentId) => {
      return apiClient.delete(`/comments/${commentId}`)
    },
  },

  // 좋아요 관련
  likes: {
    toggle: (postId) => {
      return apiClient.post(`/likes/post/${postId}`)
    },

    getStatus: (postId) => {
      return apiClient.get(`/likes/post/${postId}`)
    },
  },

  // 팔로우 관련
  follows: {
    toggle: (userId) => {
      return apiClient.post(`/follows/user/${userId}`)
    },

    getStatus: (userId) => {
      return apiClient.get(`/follows/user/${userId}`)
    },
  },

  // 알림 관련
  notifications: {
    getAll: (page = 0, size = 20) => {
      return apiClient.get('/notifications', {
        params: { page, size },
      })
    },

    getUnread: () => {
      return apiClient.get('/notifications/unread')
    },

    getUnreadCount: () => {
      return apiClient.get('/notifications/unread/count')
    },

    markAsRead: (notificationId) => {
      return apiClient.put(`/notifications/${notificationId}/read`)
    },

    markAllAsRead: () => {
      return apiClient.put('/notifications/read-all')
    },

    delete: (notificationId) => {
      return apiClient.delete(`/notifications/${notificationId}`)
    },
  },
}
