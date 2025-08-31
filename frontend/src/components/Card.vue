<template>
  <div class="card shadow-sm">
    <span class="img" :style="{ backgroundImage: `url(${item.imgPath})` }" />
    <div class="card-body">
      <p class="card-text">
        <span>{{ item.name }} &nbsp;</span>
        <span class="discount badge bg-danger">
          {{ item.discountPer }}%
        </span>
      </p>
      <div class="mt-3 d-flex justify-content-between align-items-center ">
        <button v-if="isLoggedIn" class="btn btn-primary bg-blue-500" @click="addToCart(item.id)">
          <!-- 장바구니 아이콘 -->
          <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
            <path
              d="M3 1a1 1 0 000 2h1.22l.305 1.222a.997.997 0 00.01.042l1.358 5.43-.893.892C3.74 11.846 4.632 14 6.414 14H15a1 1 0 000-2H6.414l1-1H14a1 1 0 00.894-.553l3-6A1 1 0 0017 3H6.28l-.31-1.243A1 1 0 005 1H3zM16 16.5a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0zM6.5 18a1.5 1.5 0 100-3 1.5 1.5 0 000 3z" />
          </svg>
        </button>
        <button v-else class="btn btn-secondary" @click="goToLogin">
          로그인 필요
        </button>
        <small class="price text-muted">
          {{ lib.getNumberFormatted(item.price) }}원
        </small>
        <small class="real text-danger">
          {{ lib.getNumberFormatted(item.price - (item.price * item.discountPer / 100)) }}원
        </small>
      </div>
    </div>
  </div>
</template>

<script>
import lib from "@/scripts/lib";
import { useAuthStore } from "@/stores/auth";
import { useNotificationStore } from "@/stores/notification";
import { useRouter } from "vue-router";
import { computed } from "vue";
import { cartAPI } from "@/api";

export default {
  name: "Card",
  props: {
    item: Object
  },
  setup() {
    const authStore = useAuthStore();
    const notificationStore = useNotificationStore();
    const router = useRouter();

    // 로그인 상태 확인
    const isLoggedIn = computed(() => {
      return authStore.isAuthenticated;
    });

    const addToCart = async (itemId) => {
      try {
        await cartAPI.addItem(itemId);
        notificationStore.success('장바구니에 추가되었습니다!');
        console.log('장바구니 추가 성공 - 상품 ID:', itemId);

        // Header의 장바구니 개수 업데이트를 위한 이벤트 발생
        window.dispatchEvent(new CustomEvent('cart-updated'));
      } catch (error) {
        console.error('장바구니 추가 실패:', error);
        // 에러는 이미 axios 인터셉터에서 처리됨
      }
    };

    const goToLogin = () => {
      notificationStore.info('로그인이 필요합니다.');
      router.push('/login');
    };

    return {
      lib,
      addToCart,
      goToLogin,
      isLoggedIn
    }
  }
}
</script>

<style scoped>
.card .img {
  display: inline-block;
  width: 100%;
  height: 250px;
  background-size: cover;
  background-position: center;
}

.card .card-body .price {
  text-decoration: line-through;
}
</style>