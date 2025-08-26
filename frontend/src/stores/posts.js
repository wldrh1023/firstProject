import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { postsAPI, socialAPI } from '@/api'

export const usePostsStore = defineStore('posts', () => {
  // State
  const posts = ref([])
  const currentPost = ref(null)
  const isLoading = ref(false)
  const error = ref(null)
  const hasMore = ref(true)
  const currentPage = ref(0)

  // Getters
  const feedPosts = computed(() => posts.value)
  const postCount = computed(() => posts.value.length)

  // Actions
  const createPost = async (postData) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await postsAPI.createPost(postData)
      if (response.success && response.data) {
        // 새 게시물을 목록 맨 앞에 추가
        posts.value.unshift(response.data)
      }
      return response
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const fetchFeed = async (page = 0, refresh = false) => {
    if (isLoading.value && !refresh) return

    isLoading.value = true
    error.value = null

    try {
      const response = await postsAPI.getFeed(page, 20)

      if (response.success && response.data) {
        const newPosts = response.data.content || []

        if (refresh || page === 0) {
          posts.value = newPosts
          currentPage.value = 0
        } else {
          posts.value.push(...newPosts)
        }

        hasMore.value = !response.data.last
        currentPage.value = page
      }

      return response
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const fetchUserPosts = async (userId, page = 0, refresh = false) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await postsAPI.getUserPosts(userId, page, 20)

      if (response.success && response.data) {
        const newPosts = response.data.content || []

        if (refresh || page === 0) {
          posts.value = newPosts
          currentPage.value = 0
        } else {
          posts.value.push(...newPosts)
        }

        hasMore.value = !response.data.last
        currentPage.value = page
      }

      return response
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const fetchPost = async (postId) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await postsAPI.getPost(postId)
      if (response.success && response.data) {
        currentPost.value = response.data
      }
      return response
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const updatePost = async (postId, postData) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await postsAPI.updatePost(postId, postData)
      if (response.success && response.data) {
        // 목록에서 해당 게시물 업데이트
        const index = posts.value.findIndex((post) => post.id === postId)
        if (index !== -1) {
          posts.value[index] = response.data
        }

        // 현재 게시물도 업데이트
        if (currentPost.value?.id === postId) {
          currentPost.value = response.data
        }
      }
      return response
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const deletePost = async (postId) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await postsAPI.deletePost(postId)
      if (response.success) {
        // 목록에서 해당 게시물 제거
        posts.value = posts.value.filter((post) => post.id !== postId)

        // 현재 게시물이 삭제된 게시물이면 null로 설정
        if (currentPost.value?.id === postId) {
          currentPost.value = null
        }
      }
      return response
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const toggleLike = async (postId) => {
    try {
      const response = await socialAPI.likes.toggle(postId)
      if (response.success && response.data) {
        // 목록에서 해당 게시물의 좋아요 상태 업데이트
        const post = posts.value.find((p) => p.id === postId)
        if (post) {
          post.liked = response.data.isLiked
          post.likesCount = response.data.likeCount
        }

        // 현재 게시물도 업데이트
        if (currentPost.value?.id === postId) {
          currentPost.value.liked = response.data.isLiked
          currentPost.value.likesCount = response.data.likeCount
        }
      }
      return response
    } catch (err) {
      error.value = err.message
      throw err
    }
  }

  const addComment = async (postId, content) => {
    try {
      const response = await socialAPI.comments.create(postId, content)
      if (response.success && response.data) {
        // 게시물의 댓글 수 증가
        const post = posts.value.find((p) => p.id === postId)
        if (post) {
          post.commentsCount = (post.commentsCount || 0) + 1
        }

        if (currentPost.value?.id === postId) {
          currentPost.value.commentsCount = (currentPost.value.commentsCount || 0) + 1
          if (currentPost.value.comments) {
            currentPost.value.comments.push(response.data)
          }
        }
      }
      return response
    } catch (err) {
      error.value = err.message
      throw err
    }
  }

  const searchPosts = async (keyword, page = 0, refresh = false) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await postsAPI.searchPosts(keyword, page, 20)

      if (response.success && response.data) {
        const newPosts = response.data.content || []

        if (refresh || page === 0) {
          posts.value = newPosts
          currentPage.value = 0
        } else {
          posts.value.push(...newPosts)
        }

        hasMore.value = !response.data.last
        currentPage.value = page
      }

      return response
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const loadMorePosts = async () => {
    if (hasMore.value && !isLoading.value) {
      await fetchFeed(currentPage.value + 1)
    }
  }

  const refreshFeed = async () => {
    await fetchFeed(0, true)
  }

  const clearPosts = () => {
    posts.value = []
    currentPost.value = null
    currentPage.value = 0
    hasMore.value = true
  }

  const clearError = () => {
    error.value = null
  }

  return {
    // State
    posts,
    currentPost,
    isLoading,
    error,
    hasMore,
    currentPage,

    // Getters
    feedPosts,
    postCount,

    // Actions
    createPost,
    fetchFeed,
    fetchUserPosts,
    fetchPost,
    updatePost,
    deletePost,
    toggleLike,
    addComment,
    searchPosts,
    loadMorePosts,
    refreshFeed,
    clearPosts,
    clearError,
  }
})
