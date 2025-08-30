import apiClient from "./axios";

export const authAPI = {
  // 로그인
  login: (credentials) => {
    return apiClient.post("/api/account/login", credentials);
  },

  // 로그아웃
  logout: () => {
    return apiClient.post("/api/account/logout");
  },

  // 인증 확인
  checkAuth: () => {
    return apiClient.get("/api/account/check");
  },
};
