<template>
  <div class="min-h-screen bg-gray-50 py-12">
    <div class="max-w-md mx-auto bg-white rounded-lg shadow-md p-8">
      <div class="text-center mb-8">
        <h1 class="text-2xl font-bold text-gray-900 mb-2">회원탈퇴</h1>
        <p class="text-gray-600">정말로 탈퇴하시겠습니까?</p>
      </div>

      <div class="bg-red-50 border border-red-200 rounded-lg p-4 mb-6">
        <div class="flex">
          <div class="flex-shrink-0">
            <svg class="h-5 w-5 text-red-400" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd"
                d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z"
                clip-rule="evenodd" />
            </svg>
          </div>
          <div class="ml-3">
            <h3 class="text-sm font-medium text-red-800">주의사항</h3>
            <div class="mt-2 text-sm text-red-700">
              <ul class="list-disc list-inside space-y-1">
                <li>탈퇴 시 모든 회원 정보가 영구적으로 삭제됩니다.</li>
                <li>장바구니, 주문 내역 등 모든 데이터가 삭제됩니다.</li>
                <li>탈퇴 후에는 복구가 불가능합니다.</li>
              </ul>
            </div>
          </div>
        </div>
      </div>

      <div class="space-y-4">
        <button @click="handleWithdraw" :disabled="isLoading"
          class="w-full bg-red-600 text-white py-3 px-4 rounded-lg font-medium hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed transition-colors">
          <span v-if="isLoading" class="flex items-center justify-center">
            <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none"
              viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor"
                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
              </path>
            </svg>
            처리 중...
          </span>
          <span v-else>회원탈퇴</span>
        </button>

        <button @click="goBack"
          class="w-full bg-gray-300 text-gray-700 py-3 px-4 rounded-lg font-medium hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2 transition-colors">
          취소
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';
import { useNotificationStore } from '../stores/notification';

const router = useRouter();
const authStore = useAuthStore();
const notificationStore = useNotificationStore();

const isLoading = ref(false);

const handleWithdraw = async () => {
  if (!authStore.getIsAuthenticated()) {
    notificationStore.error('로그인이 필요합니다.');
    router.push('/login');
    return;
  }

  isLoading.value = true;

  try {
    console.log('회원탈퇴 시작...');
    const success = await authStore.withdraw();
    console.log('회원탈퇴 결과:', success);

    if (success) {
      console.log('메인페이지로 이동 시도...');
      // 상태 변경이 완료되도록 약간의 지연
      setTimeout(async () => {
        try {
          await router.push({ name: 'Home' });
          console.log('라우터 이름으로 이동 완료');
        } catch (routerError) {
          console.log('라우터 이름 이동 실패, 경로로 시도:', routerError);
          try {
            await router.push('/');
            console.log('라우터 경로로 이동 완료');
          } catch (pathError) {
            console.log('라우터 경로 이동 실패, window.location 사용:', pathError);
            window.location.href = '/';
          }
        }
      }, 100);
    } else {
      console.log('회원탈퇴 실패');
    }
  } catch (error) {
    console.error('회원탈퇴 중 오류:', error);
  } finally {
    isLoading.value = false;
  }
};

const goBack = () => {
  router.back();
};
</script>
