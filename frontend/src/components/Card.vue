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
      <div class="d-flex justify-content-between align-items-center">
        <button v-if="isLoggedIn" class="btn btn-primary" @click="addToCart(item.id)">
          <i class="fa fa-shopping-cart" aria-hidden="true"></i>
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
import axios from "axios";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { computed } from "vue";

export default {
  name: "Card",
  props: {
    item: Object
  },
  setup() {
    const store = useStore();
    const router = useRouter();

    // 로그인 상태 확인
    const isLoggedIn = computed(() => {
      return store.state.account && store.state.account.id;
    });

    const addToCart = (itemId) => {
      axios.post(`/api/cart/items/${itemId}`, {}, {
        withCredentials: true
      }).then(() => {
        alert('장바구니에 추가되었습니다!');
        console.log('장바구니 추가 성공 - 상품 ID:', itemId);
      }).catch((error) => {
        if (error.response && error.response.status === 401) {
          alert('로그인이 필요합니다.');
          router.push('/login');
        } else {
          alert('장바구니 추가에 실패했습니다.');
          console.error('장바구니 추가 실패:', error);
        }
      });
    };

    const goToLogin = () => {
      alert('로그인이 필요합니다.');
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