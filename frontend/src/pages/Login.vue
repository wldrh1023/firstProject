<template>
  <div class="flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-4">
      <div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
          로그인
        </h2>
        <p class="mt-2 text-center text-sm text-gray-600">
          계정에 로그인하세요
        </p>
      </div>
      <div class="mt-8 space-y-6">
        <div class="space-y-4">
          <div>
            <label for="email" class="form-label">
              이메일 주소
            </label>
            <input id="email" name="email" type="email" required class="form-input" placeholder="example@email.com"
              v-model="state.form.email" @keyup.enter="submit()" />
          </div>
          <div>
            <label for="password" class="form-label">
              비밀번호
            </label>
            <input id="password" name="password" type="password" required class="form-input" placeholder="비밀번호를 입력하세요"
              v-model="state.form.password" @keyup.enter="submit()" />
          </div>
        </div>

        <div>
          <button @click="submit()" :disabled="loading"
            class="btn group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-md bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 disabled:opacity-50 disabled:cursor-not-allowed">
            <span v-if="loading" class="absolute left-0 inset-y-0 flex items-center pl-3">
              <div class="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
            </span>
            {{ loading ? '로그인 중...' : '로그인' }}
          </button>
        </div>
      </div>

      <div class="text-center">
        <p class="text-sm text-gray-500">
          &copy; 2025 Gallery Shop. All rights reserved.
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, ref } from "vue";
import { useAuthStore } from "@/stores/auth";
import { useNotificationStore } from "@/stores/notification";
import { useRoute, useRouter } from "vue-router";
import { authAPI } from "@/api";

export default {
  setup() {
    const authStore = useAuthStore();
    const notificationStore = useNotificationStore();
    const route = useRoute();
    const router = useRouter();

    const state = reactive({
      form: {
        email: "",
        password: ""
      }
    })

    const loading = ref(false)

    const submit = () => {
      // 유효성 검사
      if (state.form.email === "") {
        notificationStore.warning("이메일을 입력해주세요.");
        return;
      }
      if (state.form.password === "") {
        notificationStore.warning("비밀번호를 입력해주세요.");
        return;
      }

      loading.value = true;

      authAPI.login(state.form).then((res) => {
        // 백엔드 개선으로 인한 응답 구조 변경 대응
        const memberId = res.data.memberId || res.data;
        authStore.setAuth(memberId);

        // 원래 가려던 페이지로 리디렉션 (없으면 홈으로)
        const redirect = route.query.redirect || '/';
        router.push(redirect);

        notificationStore.success("로그인하였습니다.");
      }).catch((error) => {
        console.error("로그인 오류:", error);
        if (error.response && error.response.data) {
          notificationStore.error(error.response.data.message || "로그인에 실패했습니다.");
        } else {
          notificationStore.error("회원 정보가 존재하지 않습니다.");
        }
      }).finally(() => {
        loading.value = false;
      });
    }

    return { state, loading, submit }
  }
}
</script>

<style scoped>
.form-signin {
  max-width: 330px;
  padding: 15px;
}

.form-signin .form-floating:focus-within {
  z-index: 2
}

.form-signin input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}

.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

.btn {
  background-color: skyblue;
}
</style>