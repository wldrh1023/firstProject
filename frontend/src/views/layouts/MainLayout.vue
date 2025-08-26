<template>
  <div class="min-h-screen bg-gray-50">
    <!-- 네비게이션 바 -->
    <nav class="bg-white shadow-sm border-b border-gray-200 fixed w-full top-0 z-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <!-- 로고 및 메인 네비게이션 -->
          <div class="flex items-center">
            <RouterLink to="/" class="flex items-center">
              <h1 class="text-2xl font-bold text-primary-600">ConnectHub</h1>
            </RouterLink>

            <!-- 검색바 -->
            <div class="hidden md:block ml-10">
              <div class="relative">
                <input v-model="searchQuery" @keyup.enter="handleSearch" type="text" placeholder="사용자나 게시물 검색..."
                  class="w-80 pl-10 pr-4 py-2 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent" />
                <MagnifyingGlassIcon class="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
              </div>
            </div>
          </div>

          <!-- 우측 메뉴 -->
          <div class="flex items-center space-x-4">
            <!-- 홈 -->
            <RouterLink to="/"
              class="p-2 text-gray-600 hover:text-primary-600 rounded-lg hover:bg-gray-100 transition-colors"
              :class="{ 'text-primary-600 bg-primary-50': $route.name === 'Home' }">
              <HomeIcon class="h-6 w-6" />
            </RouterLink>

            <!-- 알림 -->
            <button @click="$router.push('/notifications')"
              class="relative p-2 text-gray-600 hover:text-primary-600 rounded-lg hover:bg-gray-100 transition-colors"
              :class="{ 'text-primary-600 bg-primary-50': $route.name === 'Notifications' }">
              <BellIcon class="h-6 w-6" />
              <span v-if="unreadCount > 0"
                class="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                {{ unreadCount > 99 ? '99+' : unreadCount }}
              </span>
            </button>

            <!-- 프로필 드롭다운 -->
            <div class="relative" ref="profileDropdown">
              <button @click="showProfileMenu = !showProfileMenu"
                class="flex items-center space-x-2 p-2 rounded-lg hover:bg-gray-100 transition-colors">
                <img
                  :src="currentUser?.profileImageUrl || 'https://ui-avatars.com/api/?name=' + (currentUser?.username || 'User') + '&background=3b82f6&color=fff'"
                  :alt="currentUser?.username" class="avatar-sm" />
                <ChevronDownIcon class="h-4 w-4 text-gray-600" />
              </button>

              <!-- 드롭다운 메뉴 -->
              <Transition name="fade">
                <div v-if="showProfileMenu"
                  class="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-gray-200 py-1 z-50">
                  <RouterLink :to="`/profile/${currentUser?.username}`" @click="showProfileMenu = false"
                    class="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                    <UserIcon class="h-4 w-4 mr-3" />
                    내 프로필
                  </RouterLink>
                  <RouterLink to="/settings" @click="showProfileMenu = false"
                    class="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                    <CogIcon class="h-4 w-4 mr-3" />
                    설정
                  </RouterLink>
                  <hr class="my-1" />
                  <button @click="handleLogout"
                    class="flex items-center w-full px-4 py-2 text-sm text-red-600 hover:bg-red-50">
                    <ArrowRightOnRectangleIcon class="h-4 w-4 mr-3" />
                    로그아웃
                  </button>
                </div>
              </Transition>
            </div>
          </div>
        </div>
      </div>
    </nav>

    <!-- 메인 콘텐츠 -->
    <main class="pt-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <RouterView />
      </div>
    </main>

    <!-- 모바일 검색 -->
    <div v-if="showMobileSearch" class="md:hidden fixed inset-0 bg-white z-50">
      <div class="p-4">
        <div class="flex items-center space-x-4">
          <button @click="showMobileSearch = false">
            <XMarkIcon class="h-6 w-6" />
          </button>
          <input v-model="searchQuery" @keyup.enter="handleSearch" type="text" placeholder="검색..."
            class="flex-1 p-2 border border-gray-300 rounded-lg" autofocus />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { RouterView, RouterLink, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { socialAPI } from '@/api'
import {
  HomeIcon,
  BellIcon,
  UserIcon,
  CogIcon,
  MagnifyingGlassIcon,
  ChevronDownIcon,
  ArrowRightOnRectangleIcon,
  XMarkIcon
} from '@heroicons/vue/24/outline'

const router = useRouter()
const authStore = useAuthStore()

// State
const searchQuery = ref('')
const showProfileMenu = ref(false)
const showMobileSearch = ref(false)
const unreadCount = ref(0)
const profileDropdown = ref(null)

// Computed
const currentUser = computed(() => authStore.currentUser)

// Methods
const handleSearch = () => {
  if (searchQuery.value.trim()) {
    router.push(`/search?q=${encodeURIComponent(searchQuery.value.trim())}`)
    showMobileSearch.value = false
  }
}

const handleLogout = async () => {
  try {
    authStore.signOut()
    await router.push('/auth/login')
  } catch (error) {
    console.error('Logout error:', error)
  }
  showProfileMenu.value = false
}

const fetchUnreadCount = async () => {
  try {
    const response = await socialAPI.notifications.getUnreadCount()
    if (response.success && response.data) {
      unreadCount.value = response.data.unreadCount
    }
  } catch (error) {
    console.error('Failed to fetch unread count:', error)
  }
}

const handleClickOutside = (event) => {
  if (profileDropdown.value && !profileDropdown.value.contains(event.target)) {
    showProfileMenu.value = false
  }
}

// Lifecycle
onMounted(() => {
  fetchUnreadCount()
  // 5분마다 알림 개수 업데이트
  const interval = setInterval(fetchUnreadCount, 300000)

  // 외부 클릭 감지
  document.addEventListener('click', handleClickOutside)

  onUnmounted(() => {
    clearInterval(interval)
    document.removeEventListener('click', handleClickOutside)
  })
})
</script>
