<template>
  <header class="bg-white shadow-lg">
    <!-- Mobile menu button -->
    <div class="lg:hidden">
      <div class="flex items-center justify-between p-4">
        <router-link to="/" class="flex items-center space-x-2 text-primary-600">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" stroke="currentColor"
            stroke-linecap="round" stroke-linejoin="round" stroke-width="2" viewBox="0 0 24 24">
            <path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path>
            <circle cx="12" cy="13" r="4"></circle>
          </svg>
          <span class="text-xl font-bold">Gallery Shop</span>
        </router-link>

        <div class="flex items-center space-x-4">
          <router-link to="/cart" v-if="authStore.isAuthenticated"
            class="p-2 text-gray-600 hover:text-primary-600 relative">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-1.1 5H17M9 19.5a1.5 1.5 0 103 0 1.5 1.5 0 00-3 0z">
              </path>
            </svg>
          </router-link>

          <button @click="toggleMobileMenu" class="p-2 text-gray-600 hover:text-gray-900 focus:outline-none">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>
            </svg>
          </button>
        </div>
      </div>

      <!-- Mobile menu -->
      <div v-if="showMobileMenu" class="border-t border-gray-200 bg-gray-50">
        <div class="px-4 py-6 space-y-4">
          <router-link to="/" class="block text-base font-medium text-gray-900 hover:text-primary-600"
            @click="closeMobileMenu">
            메인 화면
          </router-link>
          <router-link to="/orders" class="block text-base font-medium text-gray-900 hover:text-primary-600"
            @click="closeMobileMenu">
            주문 내역
          </router-link>
          <router-link to="/login" v-if="!authStore.isAuthenticated"
            class="block text-base font-medium text-gray-900 hover:text-primary-600" @click="closeMobileMenu">
            로그인
          </router-link>
          <button @click="logout; closeMobileMenu()" v-else
            class="block w-full text-left text-base font-medium text-gray-900 hover:text-primary-600">
            로그아웃
          </button>
        </div>
      </div>
    </div>

    <!-- Desktop menu -->
    <nav class="hidden lg:block">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center py-6">
          <!-- Logo -->
          <router-link to="/" class="flex items-center space-x-2 text-primary-600">
            <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" fill="none" stroke="currentColor"
              stroke-linecap="round" stroke-linejoin="round" stroke-width="2" viewBox="0 0 24 24">
              <path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path>
              <circle cx="12" cy="13" r="4"></circle>
            </svg>
            <span class="text-2xl font-bold">Gallery Shop</span>
          </router-link>

          <!-- Navigation Links -->
          <div class="flex items-center space-x-8">
            <router-link to="/" class="btn-secondary text-white px-4 py-2 rounded-md">
              메인
            </router-link>

            <router-link to="/orders" v-if="authStore.isAuthenticated"
              class="text-gray-700 hover:text-primary-600 px-3 py-2 text-base btn-primary font-medium transition-colors bg-blue-500 text-white rounded-md">
              주문 내역
            </router-link>

            <!-- Cart & Auth -->
            <div class="flex items-center space-x-4">
              <router-link to="/cart" v-if="authStore.isAuthenticated"
                class="p-2 text-gray-600 hover:text-primary-600 relative transition-colors">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-1.1 5H17M9 19.5a1.5 1.5 0 103 0 1.5 1.5 0 00-3 0z">
                  </path>
                </svg>
              </router-link>

              <router-link to="/login" v-if="!authStore.isAuthenticated"
                class="btn-primary text-white px-4 py-2 rounded-md">
                로그인
              </router-link>

              <button @click="logout" v-else class="btn-outline">
                로그아웃
              </button>
            </div>
          </div>
        </div>
      </div>
    </nav>
  </header>
</template>

<script>
import { ref } from "vue";
import router from "@/scripts/router";
import { useAuthStore } from "@/stores/auth";
import { useNotificationStore } from "@/stores/notification";
import { authAPI } from "@/api";

export default {
  name: "HeaderComponent",
  setup() {
    const authStore = useAuthStore();
    const notificationStore = useNotificationStore();
    const showMobileMenu = ref(false);

    const toggleMobileMenu = () => {
      showMobileMenu.value = !showMobileMenu.value;
    };

    const closeMobileMenu = () => {
      showMobileMenu.value = false;
    };

    const logout = async () => {
      try {
        await authAPI.logout();
        notificationStore.success("로그아웃되었습니다.");
      } catch (error) {
        console.error("로그아웃 중 오류:", error);
        // 에러는 이미 axios 인터셉터에서 처리됨
      } finally {
        authStore.logout();
        router.push({ path: "/" });
      }
    }

    return {
      authStore,
      showMobileMenu,
      toggleMobileMenu,
      closeMobileMenu,
      logout,
    }
  }
}
</script>

<style>
header .navbar .cart {
  margin-left: auto;
  color: #fff;
}
</style>