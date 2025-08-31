<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 로딩 상태 -->
      <div v-if="state.loading" class="flex justify-center items-center py-12">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>

      <!-- 빈 장바구니 상태 -->
      <div v-else-if="state.items.length === 0" class="text-center py-12">
        <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-1.1 5H17M9 19.5a1.5 1.5 0 103 0 1.5 1.5 0 00-3 0z"></path>
        </svg>
        <h3 class="mt-2 text-sm font-medium text-gray-900">장바구니가 비어있습니다</h3>
        <p class="mt-1 text-sm text-gray-500">상품을 추가한 후 주문해주세요.</p>
        <div class="mt-6">
          <router-link to="/"
            class="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700">
            쇼핑하러 가기
          </router-link>
        </div>
      </div>

      <!-- 주문 폼 -->
      <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- 주문자 정보 및 결제 폼 -->
        <div class="lg:col-span-2">
          <div class="bg-white shadow-lg rounded-lg overflow-hidden">
            <div class="px-6 py-4 border-b border-gray-200">
              <h1 class="text-2xl font-bold text-gray-900">주문 정보</h1>
            </div>

            <div class="p-6 space-y-8">
              <!-- 주문자 정보 -->
              <div>
                <h2 class="text-lg font-semibold text-gray-900 mb-4">주문자 정보</h2>
                <div class="grid grid-cols-1 gap-4">
                  <div>
                    <label for="name" class="block text-sm font-medium text-gray-700 mb-2">
                      이름 <span class="text-red-500">*</span>
                    </label>
                    <input type="text" id="name" v-model="state.form.name" required
                      class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      placeholder="주문자 이름을 입력하세요" />
                  </div>
                  <div>
                    <label for="address" class="block text-sm font-medium text-gray-700 mb-2">
                      배송 주소 <span class="text-red-500">*</span>
                    </label>
                    <input type="text" id="address" v-model="state.form.address" required
                      class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      placeholder="배송받을 주소를 입력하세요" />
                  </div>
                </div>
              </div>

              <!-- 결제 수단 -->
              <div>
                <h2 class="text-lg font-semibold text-gray-900 mb-4">결제 수단</h2>
                <div class="space-y-3">
                  <div class="flex items-center">
                    <input id="card" name="paymentMethod" type="radio" value="card" v-model="state.form.payment"
                      class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300" />
                    <label for="card" class="ml-3 block text-sm font-medium text-gray-700">
                      신용카드
                    </label>
                  </div>
                  <div class="flex items-center">
                    <input id="bank" name="paymentMethod" type="radio" value="bank" v-model="state.form.payment"
                      class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300" />
                    <label for="bank" class="ml-3 block text-sm font-medium text-gray-700">
                      무통장입금
                    </label>
                  </div>
                </div>

                <!-- 카드 번호 입력 -->
                <div v-if="state.form.payment === 'card'" class="mt-4">
                  <label for="cardNumber" class="block text-sm font-medium text-gray-700 mb-2">
                    카드 번호
                  </label>
                  <input type="text" id="cardNumber" v-model="state.form.cardNumber"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    placeholder="1234-5678-9012-3456" />
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 주문 요약 -->
        <div class="lg:col-span-1">
          <div class="bg-white shadow-lg rounded-lg overflow-hidden sticky top-8">
            <div class="px-6 py-4 border-b border-gray-200">
              <h2 class="text-lg font-semibold text-gray-900">주문 요약</h2>
              <div class="flex items-center justify-between mt-2">
                <span class="text-sm text-gray-600">총 {{ state.items.length }}개 상품</span>
                <span
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                  {{ state.items.length }}
                </span>
              </div>
            </div>

            <div class="p-6">
              <!-- 상품 목록 -->
              <div class="space-y-4 mb-6">
                <div v-for="(item, idx) in state.items" :key="idx" class="flex items-center space-x-3">
                  <img :src="item.imgPath" :alt="item.name" class="w-12 h-12 object-cover rounded-md" />
                  <div class="flex-1 min-w-0">
                    <p class="text-sm font-medium text-gray-900 truncate">{{ item.name }}</p>
                    <p class="text-sm text-gray-500">상품 ID: {{ item.id }}</p>
                  </div>
                  <div class="text-right">
                    <p class="text-sm font-semibold text-gray-900">
                      {{ lib.getNumberFormatted(item.price - item.price * item.discountPer / 100) }}원
                    </p>
                    <p v-if="item.discountPer > 0" class="text-xs text-red-600">
                      {{ item.discountPer }}% 할인
                    </p>
                  </div>
                </div>
              </div>

              <!-- 총 금액 -->
              <div class="border-t border-gray-200 pt-4">
                <div class="flex justify-between items-center">
                  <span class="text-lg font-medium text-gray-900">총 결제 금액</span>
                  <span class="text-2xl font-bold text-blue-600">
                    {{ lib.getNumberFormatted(computedPrice) }}원
                  </span>
                </div>
              </div>

              <!-- 결제 버튼 -->
              <button @click="submit()" :disabled="!isFormValid || submitting"
                class="w-full mt-6 bg-blue-600 text-white py-3 px-4 rounded-md font-medium hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors">
                <span v-if="submitting" class="flex items-center justify-center">
                  <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none"
                    viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor"
                      d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                    </path>
                  </svg>
                  결제 처리중...
                </span>
                <span v-else>결제하기</span>
              </button>

              <!-- 안내 메시지 -->
              <p class="text-xs text-gray-500 mt-3 text-center">
                결제 완료 후 주문 내역에서 확인할 수 있습니다.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed, reactive, onMounted, ref } from "vue";
import { cartAPI, ordersAPI } from "@/api";
import { useNotificationStore } from "@/stores/notification";
import lib from "@/scripts/lib";
import { useRouter } from "vue-router";

export default {
  setup() {
    const notificationStore = useNotificationStore();
    const router = useRouter();
    const submitting = ref(false);

    const state = reactive({
      items: [],
      loading: true,
      form: {
        name: "",
        address: "",
        payment: "",
        cardNumber: "",
        items: "",
      }
    })

    const load = async () => {
      try {
        state.loading = true;
        const { data } = await cartAPI.getCartItems();
        console.log(data);
        state.items = data;

        // 장바구니가 비어있으면 홈으로 리디렉션
        if (data.length === 0) {
          notificationStore.warning('장바구니가 비어있습니다.');
          router.push('/');
        }
      } catch (error) {
        console.error('장바구니 로딩 실패:', error);
        notificationStore.error('장바구니 정보를 불러오는데 실패했습니다.');
      } finally {
        state.loading = false;
      }
    };

    const submit = async () => {
      // 폼 검증
      if (!state.form.name.trim()) {
        notificationStore.warning('주문자 이름을 입력해주세요.');
        return;
      }

      if (!state.form.address.trim()) {
        notificationStore.warning('배송 주소를 입력해주세요.');
        return;
      }

      if (!state.form.payment) {
        notificationStore.warning('결제 수단을 선택해주세요.');
        return;
      }

      if (state.form.payment === 'card' && !state.form.cardNumber.trim()) {
        notificationStore.warning('카드 번호를 입력해주세요.');
        return;
      }

      try {
        submitting.value = true;
        const args = JSON.parse(JSON.stringify(state.form));
        args.items = JSON.stringify(state.items);

        await ordersAPI.createOrder(args);
        notificationStore.success('주문이 완료되었습니다!');

        // Header의 장바구니 개수 업데이트 (결제 후 장바구니가 비워지므로 0으로 설정)
        window.dispatchEvent(new CustomEvent('cart-updated'));

        router.push({ path: "/orders" });
      } catch (error) {
        console.error('주문 처리 실패:', error);
        notificationStore.error('주문 처리 중 오류가 발생했습니다.');
      } finally {
        submitting.value = false;
      }
    }

    const computedPrice = computed(() => {
      let result = 0;

      for (let i of state.items) {
        result += i.price - i.price * i.discountPer / 100;
      }

      return result;
    })

    const isFormValid = computed(() => {
      return state.form.name.trim() &&
        state.form.address.trim() &&
        state.form.payment &&
        (state.form.payment !== 'card' || state.form.cardNumber.trim());
    })

    onMounted(() => {
      load();
    });

    return { state, lib, computedPrice, submit, submitting, isFormValid }
  }
}
</script>

<style scoped></style>