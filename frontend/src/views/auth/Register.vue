<template>
  <div>
    <h2 class="text-2xl font-bold text-center text-gray-900 mb-6">회원가입</h2>
    
    <!-- 에러 메시지 -->
    <div v-if="error" class="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded-lg">
      {{ error }}
    </div>
    
    <!-- 회원가입 폼 -->
    <form @submit.prevent="handleRegister" class="space-y-4">
      <div>
        <label for="username" class="block text-sm font-medium text-gray-700 mb-1">
          사용자명
        </label>
        <input
          id="username"
          v-model="form.username"
          type="text"
          required
          class="input-field"
          :class="{ 'border-red-500': usernameError, 'border-green-500': usernameValid }"
          placeholder="사용자명을 입력하세요 (3-50자)"
          :disabled="isLoading"
          @blur="checkUsername"
        />
        <p v-if="usernameError" class="mt-1 text-sm text-red-600">{{ usernameError }}</p>
        <p v-else-if="usernameValid" class="mt-1 text-sm text-green-600">사용 가능한 사용자명입니다.</p>
      </div>
      
      <div>
        <label for="email" class="block text-sm font-medium text-gray-700 mb-1">
          이메일
        </label>
        <input
          id="email"
          v-model="form.email"
          type="email"
          required
          class="input-field"
          :class="{ 'border-red-500': emailError, 'border-green-500': emailValid }"
          placeholder="이메일을 입력하세요"
          :disabled="isLoading"
          @blur="checkEmail"
        />
        <p v-if="emailError" class="mt-1 text-sm text-red-600">{{ emailError }}</p>
        <p v-else-if="emailValid" class="mt-1 text-sm text-green-600">사용 가능한 이메일입니다.</p>
      </div>
      
      <div>
        <label for="password" class="block text-sm font-medium text-gray-700 mb-1">
          비밀번호
        </label>
        <input
          id="password"
          v-model="form.password"
          type="password"
          required
          class="input-field"
          :class="{ 'border-red-500': passwordError }"
          placeholder="비밀번호를 입력하세요 (최소 8자)"
          :disabled="isLoading"
        />
        <p v-if="passwordError" class="mt-1 text-sm text-red-600">{{ passwordError }}</p>
      </div>
      
      <div>
        <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-1">
          비밀번호 확인
        </label>
        <input
          id="confirmPassword"
          v-model="confirmPassword"
          type="password"
          required
          class="input-field"
          :class="{ 'border-red-500': confirmPasswordError }"
          placeholder="비밀번호를 다시 입력하세요"
          :disabled="isLoading"
        />
        <p v-if="confirmPasswordError" class="mt-1 text-sm text-red-600">{{ confirmPasswordError }}</p>
      </div>
      
      <button
        type="submit"
        :disabled="isLoading || !isFormValid"
        class="w-full btn-primary disabled:opacity-50 disabled:cursor-not-allowed"
      >
        <span v-if="isLoading" class="flex items-center justify-center">
          <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          회원가입 중...
        </span>
        <span v-else>회원가입</span>
      </button>
    </form>
    
    <!-- 로그인 링크 -->
    <div class="mt-6 text-center">
      <p class="text-sm text-gray-600">
        이미 계정이 있으신가요?
        <RouterLink to="/auth/login" class="text-primary-600 hover:text-primary-500 font-medium">
          로그인
        </RouterLink>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

// State
const form = ref({
  username: '',
  email: '',
  password: ''
})

const confirmPassword = ref('')
const usernameValid = ref(false)
const emailValid = ref(false)
const usernameError = ref('')
const emailError = ref('')
const passwordError = ref('')
const confirmPasswordError = ref('')

// Computed
const isLoading = computed(() => authStore.isLoading)
const error = computed(() => authStore.error)

const isFormValid = computed(() => {
  return form.value.username.length >= 3 &&
         form.value.email &&
         form.value.password.length >= 8 &&
         form.value.password === confirmPassword.value &&
         usernameValid.value &&
         emailValid.value &&
         !passwordError.value &&
         !confirmPasswordError.value
})

// Watchers
watch(() => form.value.username, () => {
  usernameValid.value = false
  usernameError.value = ''
})

watch(() => form.value.email, () => {
  emailValid.value = false
  emailError.value = ''
})

watch(() => form.value.password, (newPassword) => {
  if (newPassword.length > 0 && newPassword.length < 8) {
    passwordError.value = '비밀번호는 최소 8자 이상이어야 합니다.'
  } else {
    passwordError.value = ''
  }
  
  
  // 비밀번호 확인 재검증
  if (confirmPassword.value) {
    validateConfirmPassword()
  }
})

watch(confirmPassword, validateConfirmPassword)

// Methods
const checkUsername = async () => {
  if (form.value.username.length < 3) {
    usernameError.value = '사용자명은 최소 3자 이상이어야 합니다.'
    return
  }
  
  if (form.value.username.length > 50) {
    usernameError.value = '사용자명은 50자를 초과할 수 없습니다.'
    return
  }
  
  try {
    const available = await authStore.checkUsername(form.value.username)
    if (available) {
      usernameValid.value = true
      usernameError.value = ''
    } else {
      usernameError.value = '이미 사용 중인 사용자명입니다.'
      usernameValid.value = false
    }
  } catch (error) {
    usernameError.value = '사용자명 확인 중 오류가 발생했습니다.'
    usernameValid.value = false
  }
}

const checkEmail = async () => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(form.value.email)) {
    emailError.value = '올바른 이메일 형식이 아닙니다.'
    return
  }
  
  try {
    const available = await authStore.checkEmail(form.value.email)
    if (available) {
      emailValid.value = true
      emailError.value = ''
    } else {
      emailError.value = '이미 사용 중인 이메일입니다.'
      emailValid.value = false
    }
  } catch (error) {
    emailError.value = '이메일 확인 중 오류가 발생했습니다.'
    emailValid.value = false
  }
}

const validateConfirmPassword = () => {
  if (confirmPassword.value && form.value.password !== confirmPassword.value) {
    confirmPasswordError.value = '비밀번호가 일치하지 않습니다.'
  } else {
    confirmPasswordError.value = ''
  }
}

const handleRegister = async () => {
  if (!isFormValid.value) return
  
  authStore.clearError()
  
  try {
    await authStore.signUp(form.value)
    // 회원가입 성공 시 로그인 페이지로 이동
    await router.push('/auth/login')
  } catch (error) {
    console.error('Registration failed:', error)
  }
}
</script>

