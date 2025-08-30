import apiClient from "./axios";

export const authAPI = {
  // 로그인
  login: (credentials) => {
    return apiClient.post("/api/account/login", credentials);
  },

  // 회원가입
  signup: (userData) => {
    console.log("authAPI.signup 호출됨");
    console.log("전송할 데이터:", userData);
    console.log("요청 URL:", "/api/account/signup");
    return apiClient.post("/api/account/signup", userData);
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
