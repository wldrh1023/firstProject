import { defineStore } from "pinia";
import { ref } from "vue";
import { authAPI } from "../api/auth";
import { useNotificationStore } from "./notification";

export const useAuthStore = defineStore("auth", () => {
  // State
  const userId = ref(0);
  const isAuthenticated = ref(false);

  // Getters
  const getUserId = () => userId.value;
  const getIsAuthenticated = () => isAuthenticated.value;

  // Actions
  function setAuth(id) {
    userId.value = id || 0;
    isAuthenticated.value = !!(id && id !== 0);

    if (id && id !== 0) {
      sessionStorage.setItem("id", id);
    } else {
      sessionStorage.removeItem("id");
    }
  }

  function logout() {
    userId.value = 0;
    isAuthenticated.value = false;
    sessionStorage.removeItem("id");
  }

  function initAuth() {
    const storedId = sessionStorage.getItem("id");
    if (storedId && storedId !== "0") {
      setAuth(parseInt(storedId));
    }
  }

  async function withdraw() {
    const notificationStore = useNotificationStore();

    try {
      console.log("auth store withdraw 시작");
      await authAPI.withdraw();
      console.log("API 호출 성공");
      logout(); // 로그아웃 처리
      console.log("로그아웃 처리 완료");
      notificationStore.success("회원탈퇴가 완료되었습니다.");
      console.log("알림 표시 완료");
      return true;
    } catch (error) {
      console.error("회원탈퇴 실패:", error);
      const errorMessage =
        error.response?.data || "회원탈퇴 중 오류가 발생했습니다.";
      notificationStore.error(errorMessage);
      return false;
    }
  }

  return {
    // State
    userId,
    isAuthenticated,

    // Getters
    getUserId,
    getIsAuthenticated,

    // Actions
    setAuth,
    logout,
    initAuth,
    withdraw,
  };
});
