<template>
  <div v-if="user" class="max-w-4xl mx-auto">
    <!-- 프로필 헤더 -->
    <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
      <div class="flex items-start justify-between">
        <div class="flex items-center space-x-6">
          <img
            :src="user.profileImageUrl || 'https://ui-avatars.com/api/?name=' + (user.username || 'User') + '&background=3b82f6&color=fff'"
            :alt="user.username" class="avatar-lg" />

          <div>
            <h1 class="text-2xl font-bold text-gray-900">{{ user.username }}</h1>
            <p v-if="user.bio" class="text-gray-600 mt-2">{{ user.bio }}</p>

            <div class="flex items-center space-x-6 mt-4 text-sm text-gray-600">
              <span><strong class="text-gray-900">{{ user.postsCount }}</strong> 게시물</span>
              <span><strong class="text-gray-900">{{ user.followersCount }}</strong> 팔로워</span>
              <span><strong class="text-gray-900">{{ user.followingCount }}</strong> 팔로잉</span>
            </div>
          </div>
        </div>

        <!-- 액션 버튼 -->
        <div v-if="!isOwnProfile">
          <button @click="toggleFollow" :disabled="isFollowLoading" class="btn-primary"
            :class="user.isFollowing ? 'bg-gray-600 hover:bg-gray-700' : ''">
            <span v-if="isFollowLoading">처리 중...</span>
            <span v-else>{{ user.isFollowing ? '언팔로우' : '팔로우' }}</span>
          </button>
        </div>

        <div v-else>
          <RouterLink to="/settings" class="btn-outline">
            프로필 편집
          </RouterLink>
        </div>
      </div>
    </div>

    <!-- 게시물 목록 -->
    <div class="space-y-6">
      <div v-if="isLoading && posts.length === 0" class="text-center py-8">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600 mx-auto"></div>
        <p class="mt-2 text-gray-600">게시물을 불러오는 중...</p>
      </div>

      <div v-else-if="posts.length === 0" class="text-center py-12">
        <p class="text-gray-600 text-lg">아직 게시물이 없습니다.</p>
      </div>

      <PostCard v-for="post in posts" :key="post.id" :post="post" @like="handleLike" @comment="handleComment"
        @edit="handleEdit" @delete="handleDelete" />

      <!-- 더 보기 버튼 -->
      <div v-if="hasMore" class="text-center py-4">
        <button @click="loadMore" :disabled="isLoading" class="btn-outline disabled:opacity-50">
          <span v-if="isLoading">로딩 중...</span>
          <span v-else>더 보기</span>
        </button>
      </div>
    </div>
  </div>

  <div v-else-if="error" class="text-center py-12">
    <p class="text-red-600 text-lg">{{ error }}</p>
  </div>

  <div v-else class="text-center py-8">
    <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600 mx-auto"></div>
    <p class="mt-2 text-gray-600">프로필을 불러오는 중...</p>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { usePostsStore } from '@/stores/posts'
import { usersAPI, socialAPI } from '@/api'
import PostCard from '@/components/PostCard.vue'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const postsStore = usePostsStore()

// State
const user = ref(null)
const error = ref(null)
const isFollowLoading = ref(false)

// Computed
const currentUser = computed(() => authStore.currentUser)
const posts = computed(() => postsStore.feedPosts)
const isLoading = computed(() => postsStore.isLoading)
const hasMore = computed(() => postsStore.hasMore)

const isOwnProfile = computed(() => {
  return !route.params.username ||
    route.params.username === currentUser.value?.username
})

const targetUsername = computed(() => {
  return route.params.username || currentUser.value?.username
})

// Methods
const fetchUserProfile = async () => {
  try {
    error.value = null

    if (isOwnProfile.value) {
      user.value = currentUser.value
    } else {
      const response = await usersAPI.getUserByUsername(targetUsername.value)
      if (response.success) {
        user.value = response.data
      }
    }
  } catch (err) {
    error.value = '사용자를 찾을 수 없습니다.'
    console.error('Failed to fetch user profile:', err)
  }
}

const fetchUserPosts = async () => {
  if (user.value) {
    await postsStore.fetchUserPosts(user.value.id, 0, true)
  }
}

const toggleFollow = async () => {
  if (!user.value || isFollowLoading.value) return

  isFollowLoading.value = true

  try {
    const response = await socialAPI.follows.toggle(user.value.id)
    if (response.success && response.data) {
      user.value.isFollowing = response.data.isFollowing
      user.value.followersCount = response.data.followerCount
    }
  } catch (error) {
    console.error('Failed to toggle follow:', error)
  } finally {
    isFollowLoading.value = false
  }
}

const handleLike = async (postId) => {
  await postsStore.toggleLike(postId)
}

const handleComment = (postId) => {
  router.push(`/post/${postId}`)
}

const handleEdit = (postId) => {
  // 게시물 편집 모달 또는 페이지로 이동
  console.log('Edit post:', postId)
}

const handleDelete = async (postId) => {
  try {
    await postsStore.deletePost(postId)
    if (user.value) {
      user.value.postsCount -= 1
    }
  } catch (error) {
    console.error('Failed to delete post:', error)
  }
}

const loadMore = () => {
  if (user.value) {
    postsStore.fetchUserPosts(user.value.id, postsStore.currentPage + 1)
  }
}

// Watchers
watch(() => route.params.username, () => {
  fetchUserProfile().then(() => {
    fetchUserPosts()
  })
})

// Lifecycle
onMounted(() => {
  fetchUserProfile().then(() => {
    fetchUserPosts()
  })
})
</script>
