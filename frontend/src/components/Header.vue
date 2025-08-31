<template>
  <header class="bg-white border-b border-gray-100 sticky top-0 z-50 backdrop-blur-sm bg-white/95">
    <nav class="max-w-7xl mx-auto px-6">
      <div class="flex items-center justify-between h-16">
        <!-- Logo -->
        <router-link to="/" class="flex items-center space-x-3 group">
          <div
            class="w-10 h-10 bg-gradient-to-br from-blue-500 to-purple-600 rounded-xl flex items-center justify-center shadow-lg group-hover:shadow-xl transition-all duration-300">
            <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path>
            </svg>
          </div>
          <div>
            <h1 class="text-xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">
              Gallery Shop
            </h1>
            <p class="text-xs text-gray-500 -mt-1">Premium Shopping</p>
          </div>
        </router-link>

        <!-- Navigation Links -->
        <div class="flex items-center space-x-1">
          <router-link to="/orders" v-if="authStore.isAuthenticated"
            class="px-4 py-2 rounded-lg text-gray-700 hover:text-blue-600 hover:bg-blue-50 transition-all duration-200 font-medium">
            주문 내역
          </router-link>
        </div>

        <!-- Right Side Actions -->
        <div class="flex items-center space-x-3">
          <!-- Cart -->
          <router-link to="/cart" v-if="authStore.isAuthenticated"
            class="relative p-2 text-gray-600 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-all duration-200 group">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-1.1 5H17M9 19.5a1.5 1.5 0 103 0 1.5 1.5 0 00-3 0z">
              </path>
            </svg>
            <div v-if="cartCount > 0"
              class="absolute -top-1 -right-1 w-5 h-5 bg-red-500 text-white text-xs rounded-full flex items-center justify-center">
              {{ cartCount }}
            </div>
          </router-link>

          <!-- Auth Buttons -->
          <div v-if="!authStore.isAuthenticated" class="flex items-center space-x-2">
            <router-link to="/signup"
              class="px-4 py-2 text-gray-700 hover:text-blue-600 font-medium transition-colors duration-200">
              회원가입
            </router-link>
            <router-link to="/login"
              class="px-4 py-2 bg-gradient-to-r from-blue-500 to-purple-600 text-white rounded-lg hover:from-blue-600 hover:to-purple-700 transition-all duration-200 font-medium shadow-lg hover:shadow-xl">
              로그인
            </router-link>
          </div>

          <!-- User Menu -->
          <div v-else class="relative user-menu">
            <button @click="toggleUserMenu"
              class="flex items-center space-x-2 p-2 rounded-lg hover:bg-gray-50 transition-all duration-200">
              <div
                class="w-8 h-8 bg-gradient-to-br from-blue-500 to-purple-600 rounded-full flex items-center justify-center">
                <span class="text-white text-sm font-medium">{{ getUserInitials() }}</span>
              </div>
              <svg class="w-4 h-4 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
              </svg>
            </button>

            <!-- Dropdown Menu -->
            <div v-if="showUserMenu"
              class="absolute right-0 mt-2 w-48 bg-white rounded-xl shadow-lg border border-gray-100 py-2 z-50">
              <div class="px-4 py-2 border-b border-gray-100">
                <p class="text-sm font-medium text-gray-900">사용자님</p>
                <p class="text-xs text-gray-500">환영합니다!</p>
              </div>
              <button @click="logout"
                class="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-red-50 hover:text-red-600 transition-colors duration-200">
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
import { ref, onMounted } from "vue";
import router from "@/scripts/router";
import { useAuthStore } from "@/stores/auth";
import { useNotificationStore } from "@/stores/notification";
import { authAPI, cartAPI } from "@/api";

export default {
  name: "HeaderComponent",
  setup() {
    const authStore = useAuthStore();
    const notificationStore = useNotificationStore();
    const showUserMenu = ref(false);
    const cartCount = ref(0);

    const logout = async () => {
      try {
        await authAPI.logout();
        notificationStore.success("로그아웃되었습니다.");
      } catch (error) {
        console.error("로그아웃 중 오류:", error);
        // 에러는 이미 axios 인터셉터에서 처리됨
      } finally {
        authStore.logout();
        router.push({ path: "/login" });
      }
    }

    const toggleUserMenu = () => {
      showUserMenu.value = !showUserMenu.value;
    }

    const getUserInitials = () => {
      // 실제 사용자 정보가 있다면 사용자 이름의 이니셜을 반환
      // 현재는 기본값으로 "U" 반환
      return "U";
    }

    const loadCartCount = async () => {
      if (authStore.isAuthenticated) {
        try {
          const { data } = await cartAPI.getCartItems();
          cartCount.value = data.length;
        } catch (error) {
          console.error('장바구니 개수 로딩 실패:', error);
          cartCount.value = 0;
        }
      } else {
        cartCount.value = 0;
      }
    }

    // 사용자 메뉴 외부 클릭시 닫기
    onMounted(() => {
      // 장바구니 개수 로드
      loadCartCount();

      // 장바구니 업데이트 이벤트 리스너
      window.addEventListener('cart-updated', loadCartCount);

      document.addEventListener('click', (e) => {
        const userMenu = document.querySelector('.user-menu');
        if (userMenu && !userMenu.contains(e.target)) {
          showUserMenu.value = false;
        }
      });
    });

    return {
      authStore,
      logout,
      showUserMenu,
      toggleUserMenu,
      getUserInitials,
      cartCount,
    }
  }
}
</script>

<style scoped>
header .navbar .cart {
  margin-left: auto;
  color: #fff;
}
</style>