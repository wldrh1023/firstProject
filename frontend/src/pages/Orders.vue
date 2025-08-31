<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 헤더 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">주문 내역</h1>
        <p class="mt-2 text-gray-600">지금까지 주문하신 상품들의 내역입니다.</p>
      </div>

      <!-- 로딩 상태 -->
      <div v-if="state.loading" class="flex justify-center items-center py-12">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>

      <!-- 빈 상태 -->
      <div v-else-if="state.orders.length === 0" class="text-center py-12">
        <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z">
          </path>
        </svg>
        <h3 class="mt-2 text-sm font-medium text-gray-900">주문 내역이 없습니다</h3>
        <p class="mt-1 text-sm text-gray-500">첫 번째 주문을 해보세요.</p>
        <div class="mt-6">
          <router-link to="/"
            class="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700">
            쇼핑하러 가기
          </router-link>
        </div>
      </div>

      <!-- 주문 목록 -->
      <div v-else class="space-y-6">
        <div v-for="(order, idx) in state.orders" :key="order.id || idx"
          class="bg-white shadow-lg rounded-lg overflow-hidden">
          <!-- 주문 헤더 -->
          <div class="px-6 py-4 border-b border-gray-200 bg-gray-50">
            <div class="flex items-center justify-between">
              <div class="flex items-center space-x-4">
                <div class="flex-shrink-0">
                  <div class="w-8 h-8 bg-blue-600 rounded-full flex items-center justify-center">
                    <span class="text-white text-sm font-medium">{{ state.orders.length - idx }}</span>
                  </div>
                </div>
                <div>
                  <h3 class="text-lg font-semibold text-gray-900">주문 #{{ order.id || (state.orders.length - idx) }}</h3>
                  <p class="text-xs text-gray-400">ID: {{ order.id }}</p>
                </div>
              </div>
              <div class="flex items-center space-x-2">
                <span
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                  완료
                </span>
                <button @click="deleteOrder(order.id)" class="text-red-600 hover:text-red-800 
                transition-colors p-1" :disabled="deletingOrderId === order.id" :title="`주문 삭제 (ID: ${order.id})`">
                  <svg v-if="deletingOrderId !== order.id" class="w-5 h-5" fill="none" stroke="currentColor"
                    viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16">
                    </path>
                  </svg>
                  <svg v-else class="w-5 h-5 animate-spin" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor"
                      d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                    </path>
                  </svg>
                </button>
              </div>
            </div>
          </div>

          <!-- 주문 정보 -->
          <div class="p-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <!-- 주문자 정보 -->
              <div>
                <h4 class="text-sm font-medium text-gray-900 mb-3">주문자 정보</h4>
                <div class="space-y-2">
                  <div class="flex items-center">
                    <svg class="w-4 h-4 text-gray-400 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                    </svg>
                    <span class="text-sm text-gray-600">{{ order.name }}</span>
                  </div>
                  <div class="flex items-start">
                    <svg class="w-4 h-4 text-gray-400 mr-2 mt-0.5" fill="none" stroke="currentColor"
                      viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path>
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path>
                    </svg>
                    <span class="text-sm text-gray-600">{{ order.address }}</span>
                  </div>
                  <div class="flex items-center">
                    <svg class="w-4 h-4 text-gray-400 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z">
                      </path>
                    </svg>
                    <span class="text-sm text-gray-600">{{ getPaymentMethodText(order.payment) }}</span>
                  </div>
                </div>
              </div>

              <!-- 주문 상품 -->
              <div>
                <h4 class="text-sm font-medium text-gray-900 mb-3">주문 상품</h4>
                <div class="space-y-3">
                  <div v-for="(item, itemIdx) in order.items" :key="itemIdx" class="flex items-center space-x-3">
                    <img :src="item.imgPath" :alt="item.name" class="w-10 h-10 object-cover rounded-md" />
                    <div class="flex-1 min-w-0">
                      <p class="text-sm font-medium text-gray-900 truncate">{{ item.name }}</p>
                      <p class="text-sm text-gray-500">
                        {{ lib.getNumberFormatted(item.price - item.price * item.discountPer / 100) }}원
                        <span v-if="item.discountPer > 0" class="text-red-600">({{ item.discountPer }}% 할인)</span>
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 총 금액 -->
            <div class="mt-6 pt-4 border-t border-gray-200">
              <div class="flex justify-between items-center">
                <span class="text-lg font-medium text-gray-900">총 결제 금액</span>
                <span class="text-xl font-bold text-blue-600">
                  {{ lib.getNumberFormatted(calculateTotal(order.items)) }}원
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, onMounted, ref } from "vue";
import { ordersAPI } from "@/api";
import { useNotificationStore } from "@/stores/notification";
import lib from "@/scripts/lib";

export default {
  setup() {
    const notificationStore = useNotificationStore();
    const deletingOrderId = ref(null);
    const state = reactive({
      orders: [],
      loading: true,

    })

    const formatDate = (dateString) => {
      if (!dateString || dateString === '날짜 정보 없음') return '날짜 정보 없음';

      try {
        // String을 Date 객체로 변환 (yyyy-MM-dd HH:mm:ss 형식)
        const date = new Date(dateString.replace(' ', 'T'));
        const now = new Date();
        const diffInHours = Math.floor((now - date) / (1000 * 60 * 60));
        const diffInDays = Math.floor((now - date) / (1000 * 60 * 60 * 24));

        // 상대적 시간 표시
        if (diffInHours < 1) {
          const diffInMinutes = Math.floor((now - date) / (1000 * 60));
          return `${diffInMinutes}분 전`;
        } else if (diffInHours < 24) {
          return `${diffInHours}시간 전`;
        } else if (diffInDays < 7) {
          return `${diffInDays}일 전`;
        } else {
          // 7일 이상 지난 경우 절대 시간 표시
          return date.toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
          });
        }
      } catch (error) {
        console.error('날짜 파싱 오류:', error);
        return '날짜 정보 없음';
      }
    }

    const formatFullDate = (dateString) => {
      if (!dateString || dateString === '날짜 정보 없음') return '날짜 정보 없음';

      try {
        // String을 Date 객체로 변환
        const date = new Date(dateString.replace(' ', 'T'));
        return date.toLocaleDateString('ko-KR', {
          year: 'numeric',
          month: 'long',
          day: 'numeric',
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit'
        });
      } catch (error) {
        console.error('날짜 파싱 오류:', error);
        return '날짜 정보 없음';
      }
    }

    const getPaymentMethodText = (payment) => {
      switch (payment) {
        case 'card':
          return '신용카드';
        case 'bank':
          return '무통장입금';
        default:
          return payment || '결제 수단 정보 없음';
      }
    }

    const calculateTotal = (items) => {
      if (!items || !Array.isArray(items)) return 0;

      return items.reduce((total, item) => {
        const price = item.price || 0;
        const discountPer = item.discountPer || 0;
        return total + (price - price * discountPer / 100);
      }, 0);
    }
    const loadOrders = async () => {
      try {
        state.loading = true;
        state.orders = []; // 배열 초기화
        const { data } = await ordersAPI.getOrders();

        for (let d of data) {
          if (d.items) {
            try {
              d.items = JSON.parse(d.items);
            } catch (e) {
              console.error('JSON 파싱 오류:', e);
              d.items = [];
            }
          }
          state.orders.push(d);
        }
      } catch (error) {
        console.error('주문 내역 로딩 실패:', error);
        notificationStore.error('주문 내역을 불러오는데 실패했습니다.');
      } finally {
        state.loading = false;
      }
    }

    const deleteOrder = async (orderId) => {
      if (!confirm('정말로 이 주문을 삭제하시겠습니까?\n\n삭제된 주문은 복구할 수 없습니다.')) {

        return;
      }

      try {
        deletingOrderId.value = orderId;
        await ordersAPI.deleteOrder(orderId);
        notificationStore.success('주문이 삭제되었습니다.');
        // 주문 목록 다시 로드
        await loadOrders();
      } catch (error) {
        console.error('주문 삭제 실패:', error);
        const message = error.response?.data?.message || '주문 삭제에 실패했습니다.';
        notificationStore.error(message);
      } finally {
        deletingOrderId.value = null;
      }
    }

    onMounted(() => {
      loadOrders();
    });

    return {
      state,
      lib,
      formatDate,
      formatFullDate,
      getPaymentMethodText,
      calculateTotal,
      deleteOrder,
      deletingOrderId
    }
  }
}
</script>

<style scoped></style>