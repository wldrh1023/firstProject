<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="bg-white shadow-lg rounded-lg overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h1 class="text-2xl font-bold text-gray-900">장바구니</h1>
        </div>

        <div v-if="state.loading" class="flex justify-center items-center py-12">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>

        <div v-else-if="state.items.length === 0" class="text-center py-12">
          <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-1.1 5H17M9 19.5a1.5 1.5 0 103 0 1.5 1.5 0 00-3 0z"></path>
          </svg>
          <h3 class="mt-2 text-sm font-medium text-gray-900">장바구니가 비어있습니다</h3>
          <p class="mt-1 text-sm text-gray-500">상품을 추가해보세요.</p>
          <div class="mt-6">
            <router-link to="/"
              class="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700">
              쇼핑 계속하기
            </router-link>
          </div>
        </div>

        <div v-else>
          <ul class="divide-y divide-gray-200">
            <li v-for="(i, idx) in state.items" :key="idx" class="flex items-center p-6 hover:bg-gray-50">
              <div class="flex-shrink-0">
                <img :src="i.imgPath" :alt="i.name" class="w-20 h-20 object-cover rounded-lg" />
              </div>
              <div class="ml-6 flex-1">
                <div class="flex items-center justify-between">
                  <div>
                    <h3 class="text-lg font-medium text-gray-900">{{ i.name }}</h3>
                    <p class="text-sm text-gray-500">상품 ID: {{ i.id }}</p>
                  </div>
                  <div class="flex items-center space-x-4">
                    <div class="text-right">
                      <p class="text-lg font-semibold text-gray-900">
                        {{ lib.getNumberFormatted(i.price - i.price * i.discountPer / 100) }}원
                      </p>
                      <p v-if="i.discountPer > 0" class="text-sm text-red-600">
                        {{ i.discountPer }}% 할인
                      </p>
                    </div>
                    <button @click="remove(i.id)" class="text-red-600 hover:text-red-800 transition-colors">
                      <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16">
                        </path>
                      </svg>
                    </button>
                  </div>
                </div>
              </div>
            </li>
          </ul>

          <div class="px-6 py-4 border-t border-gray-200 bg-gray-50">
            <div class="flex justify-between items-center">
              <div class="text-lg font-medium text-gray-900">
                총 {{ state.items.length }}개 상품
              </div>
              <router-link to="/order"
                class="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                구매하기
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import lib from '@/scripts/lib';
import { reactive, onMounted } from 'vue';
import { cartAPI } from '@/api';
import { useNotificationStore } from '@/stores/notification';

export default {
  setup() {
    const notificationStore = useNotificationStore();
    const state = reactive({
      items: [],
      loading: true
    })

    const load = async () => {
      try {
        state.loading = true;
        const { data } = await cartAPI.getCartItems();
        console.log(data);
        state.items = data;
      } catch (error) {
        console.error('장바구니 로딩 실패:', error);
      } finally {
        state.loading = false;
      }
    }

    const remove = async (itemId) => {
      try {
        await cartAPI.removeItem(itemId);
        notificationStore.success('장바구니에서 상품이 제거되었습니다.');
        load();
      } catch (error) {
        console.error('상품 제거 실패:', error);
      }
    }

    onMounted(() => {
      load();
    });

    return {
      state, lib, remove
    }
  }
}
</script>

<style scoped>
.cart ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.cart ul li {
  border: 1px solid #eee;
  margin: 25px;
  margin-bottom: 25px;
}

.cart ul li img {
  width: 100px;
  height: 100px
}

.cart ul li svg {
  float: right;
  margin-top: 65px;
  margin-right: 10px;
}

.cart .btn {
  width: 300px;
  display: block;
  font-size: 20px;
  margin: auto;
}
</style>