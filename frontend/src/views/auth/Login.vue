<template>
  <div>
    <h2 class="text-2xl font-bold text-center text-gray-900 mb-6">로그인</h2>

    <!-- 에러 메시지 -->
    <div v-if="error" class="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded-lg">
      {{ error }}
    </div>

    <!-- 로그인 폼 -->
    <form @submit.prevent="handleLogin" class="space-y-4">
      <div>
        <label for="usernameOrEmail" class="block text-sm font-medium text-gray-700 mb-1">
          이메일 또는 사용자명
        </label>
        <input id="usernameOrEmail" v-model="form.usernameOrEmail" type="text" required class="input-field"
          placeholder="이메일 또는 사용자명을 입력하세요" :disabled="isLoading" />
      </div>

      <div>
        <label for="password" class="block text-sm font-medium text-gray-700 mb-1">
          비밀번호
        </label>
        <input id="password" v-model="form.password" type="password" required class="input-field"
          placeholder="비밀번호를 입력하세요" :disabled="isLoading" />
      </div>

      <button type="submit" :disabled="isLoading"
        class="w-full btn-primary disabled:opacity-50 disabled:cursor-not-allowed">
        <span v-if="isLoading" class="flex items-center justify-center">
          <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none"
            viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor"
              d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
            </path>
          </svg>
          로그인 중...
        </span>
        <span v-else>로그인</span>
      </button>
    </form>

    <!-- 회원가입 링크 -->
    <div class="mt-6 text-center">
      <p class="text-sm text-gray-600">
        계정이 없으신가요?
        <RouterLink to="/auth/register" class="text-primary-600 hover:text-primary-500 font-medium">
          회원가입
        </RouterLink>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

// State
const form = ref({
  usernameOrEmail: '',
  password: ''
})

// Computed
const isLoading = computed(() => authStore.isLoading)
const error = computed(() => authStore.error)

// Methods
const handleLogin = async () => {
  authStore.clearError()

  try {
    await authStore.signIn(form.value)
    await router.push('/')
  } catch (error) {
    console.error('Login failed:', error)
  }
}
</script>
