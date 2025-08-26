<template>
  <article class="post-card">
    <!-- 게시물 헤더 -->
    <div class="flex items-center justify-between mb-4">
      <div class="flex items-center space-x-3">
        <RouterLink :to="`/profile/${post.user.username}`">
          <img
            :src="post.user.profileImageUrl || 'https://ui-avatars.com/api/?name=' + (post.user.username || 'User') + '&background=3b82f6&color=fff'"
            :alt="post.user.username" class="avatar" />
        </RouterLink>

        <div>
          <RouterLink :to="`/profile/${post.user.username}`" class="font-semibold text-gray-900 hover:text-primary-600">
            {{ post.user.username }}
          </RouterLink>
          <p class="text-sm text-gray-500">
            {{ formatDate(post.createdAt) }}
          </p>
        </div>
      </div>

      <!-- 게시물 메뉴 (본인 게시물인 경우) -->
      <div v-if="isOwnPost" class="relative">
        <button @click="showMenu = !showMenu" class="p-1 rounded-full hover:bg-gray-100">
          <EllipsisHorizontalIcon class="h-5 w-5 text-gray-500" />
        </button>

        <div v-if="showMenu"
          class="absolute right-0 mt-1 w-32 bg-white rounded-lg shadow-lg border border-gray-200 py-1 z-10">
          <button @click="editPost" class="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
            수정
          </button>
          <button @click="deletePost" class="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-50">
            삭제
          </button>
        </div>
      </div>
    </div>

    <!-- 게시물 내용 -->
    <div class="mb-4">
      <p class="text-gray-900 whitespace-pre-wrap">{{ post.content }}</p>

      <!-- 이미지 -->
      <div v-if="post.imageUrl" class="mt-3">
        <img :src="post.imageUrl" :alt="'게시물 이미지'" class="w-full rounded-lg max-h-96 object-cover"
          @error="handleImageError" />
      </div>
    </div>

    <!-- 게시물 액션 -->
    <div class="flex items-center justify-between pt-3 border-t border-gray-200">
      <div class="flex items-center space-x-6">
        <!-- 좋아요 -->
        <button @click="$emit('like', post.id)"
          class="flex items-center space-x-2 text-gray-500 hover:text-red-500 transition-colors"
          :class="{ 'text-red-500': post.liked }">
          <HeartIcon class="h-5 w-5" :class="{ 'fill-current': post.liked }" />
          <span class="text-sm">{{ post.likesCount || 0 }}</span>
        </button>

        <!-- 댓글 -->
        <button @click="$emit('comment', post.id)"
          class="flex items-center space-x-2 text-gray-500 hover:text-primary-500 transition-colors">
          <ChatBubbleLeftIcon class="h-5 w-5" />
          <span class="text-sm">{{ post.commentsCount || 0 }}</span>
        </button>
      </div>

      <!-- 공유 버튼 (나중에 구현) -->
      <button class="text-gray-500 hover:text-primary-500 transition-colors">
        <ShareIcon class="h-5 w-5" />
      </button>
    </div>
  </article>
</template>

<script setup>
import { ref, computed } from 'vue'
import { RouterLink } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import {
  HeartIcon,
  ChatBubbleLeftIcon,
  ShareIcon,
  EllipsisHorizontalIcon
} from '@heroicons/vue/24/outline'

const props = defineProps({
  post: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['like', 'comment', 'edit', 'delete'])

const authStore = useAuthStore()

// State
const showMenu = ref(false)

// Computed
const isOwnPost = computed(() => {
  return authStore.currentUser?.id === props.post.user.id
})

// Methods
const formatDate = (dateString) => {
  const date = new Date(dateString)
  const now = new Date()
  const diffInMinutes = Math.floor((now - date) / (1000 * 60))

  if (diffInMinutes < 1) return '방금 전'
  if (diffInMinutes < 60) return `${diffInMinutes}분 전`
  if (diffInMinutes < 1440) return `${Math.floor(diffInMinutes / 60)}시간 전`
  if (diffInMinutes < 10080) return `${Math.floor(diffInMinutes / 1440)}일 전`

  return date.toLocaleDateString('ko-KR')
}

const handleImageError = (event) => {
  event.target.style.display = 'none'
}

const editPost = () => {
  emit('edit', props.post.id)
  showMenu.value = false
}

const deletePost = () => {
  if (confirm('정말로 이 게시물을 삭제하시겠습니까?')) {
    emit('delete', props.post.id)
  }
  showMenu.value = false
}
</script>
