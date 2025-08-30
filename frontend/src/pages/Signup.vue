<template>
  <div class="bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
    <div class="sm:mx-auto sm:w-full sm:max-w-md">
      <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
        회원가입
      </h2>
    </div>

    <div class="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
      <div class="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
        <form class="space-y-6" @submit.prevent="handleSignup">
          <div>
            <label for="email" class="block text-sm font-medium text-gray-700">
              이메일
            </label>
            <div class="mt-1">
              <input id="email" name="email" type="email" required v-model="form.email"
                class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                placeholder="이메일을 입력하세요" />
            </div>
          </div>

          <div>
            <label for="password" class="block text-sm font-medium text-gray-700">
              비밀번호
            </label>
            <div class="mt-1">
              <input id="password" name="password" type="password" required v-model="form.password"
                class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                placeholder="비밀번호를 입력하세요" />
            </div>
          </div>
          <div>
            <button type="submit" :disabled="loading"
              class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50">
              {{ loading ? '회원가입 중...' : '회원가입' }}
            </button>
          </div>
        </form>

        <div class="mt-6">
          <div class="relative">
            <div class="absolute inset-0 flex items-center">
              <div class="w-full border-t border-gray-300" />
            </div>
            <div class="relative flex justify-center text-sm">
              <span class="px-2 bg-white text-gray-500">이미 계정이 있으신가요?</span>
            </div>
          </div>

          <div class="mt-6">
            <router-link to="/login"
              class="w-full flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
              로그인하기
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { authAPI } from "@/api";
import { useNotificationStore } from "@/stores/notification";

export default {
  setup() {
    const router = useRouter();
    const notificationStore = useNotificationStore();
    const loading = ref(false);

    const form = reactive({
      email: "",
      password: "",
    });

    const handleSignup = async () => {
      if (loading.value) return;

      try {
        loading.value = true;
        console.log("=== 회원가입 요청 시작 ===");
        console.log("요청 데이터:", form);
        console.log("요청 URL:", "/api/account/signup");

        const response = await authAPI.signup(form);
        console.log("회원가입 응답:", response);

        if (response.data.success) {
          notificationStore.success(response.data.message);
          router.push("/login");
        } else {
          notificationStore.error(response.data.message);
        }
      } catch (error) {
        console.error("회원가입 실패:", error);
        console.error("에러 상세:", {
          message: error.message,
          status: error.response?.status,
          data: error.response?.data,
          config: error.config
        });
        notificationStore.error("회원가입 중 오류가 발생했습니다.");
      } finally {
        loading.value = false;
      }
    };

    return {
      form,
      loading,
      handleSignup,
    };
  },
};
</script>
