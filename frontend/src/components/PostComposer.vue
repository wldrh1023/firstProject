<template>
  <div class="post-card">
    <div class="flex space-x-4">
      <img
        :src="currentUser?.profileImageUrl || 'https://ui-avatars.com/api/?name=' + (currentUser?.username || 'User') + '&background=3b82f6&color=fff'"
        :alt="currentUser?.username" class="avatar" />

      <div class="flex-1">
        <textarea v-model="content" placeholder="무슨 일이 일어나고 있나요?"
          class="w-full p-3 border border-gray-300 rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          rows="3" :disabled="isLoading"></textarea>

        <div class="flex justify-between items-center mt-4">
          <div class="flex items-center space-x-4">
            <input v-model="imageUrl" type="url" placeholder="이미지 URL (선택사항)"
              class="flex-1 px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
              :disabled="isLoading" />
          </div>

          <button @click="handlePost" :disabled="!canPost || isLoading"
            class="btn-primary disabled:opacity-50 disabled:cursor-not-allowed ml-4">
            <span v-if="isLoading">게시 중...</span>
            <span v-else>게시하기</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { usePostsStore } from '@/stores/posts'

const emit = defineEmits(['posted'])

const authStore = useAuthStore()
const postsStore = usePostsStore()

// State
const content = ref('')
const imageUrl = ref('')

// Computed
const currentUser = computed(() => authStore.currentUser)
const isLoading = computed(() => postsStore.isLoading)
const canPost = computed(() => content.value.trim().length > 0)

// Methods
const handlePost = async () => {
  if (!canPost.value) return

  try {
    const postData = {
      content: content.value.trim(),
      imageUrl: imageUrl.value.trim() || null
    }

    await postsStore.createPost(postData)

    // 폼 초기화
    content.value = ''
    imageUrl.value = ''

    emit('posted')
  } catch (error) {
    console.error('Failed to create post:', error)
  }
}
</script>
