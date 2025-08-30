import { defineStore } from "pinia";
import { ref } from "vue";

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
  };
});
