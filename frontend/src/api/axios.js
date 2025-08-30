import axios from "axios";
import { useAuthStore } from "@/stores/auth";
import { useNotificationStore } from "@/stores/notification";

// Axios 인스턴스 생성
const apiClient = axios.create({
  baseURL: "", // 백엔드 서버 주소 // 상대 경로 사용
  timeout: 10000,
  withCredentials: true, // 쿠키 포함
});

// 요청 인터셉터
apiClient.interceptors.request.use(
  (config) => {
    // JWT 토큰이 필요한 경우 Authorization 헤더에 추가
    // 현재는 쿠키 기반이므로 별도 처리 없음
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터
apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    const authStore = useAuthStore();
    const notificationStore = useNotificationStore();

    // 에러 메시지 추출
    let errorMessage = "요청 처리 중 오류가 발생했습니다.";

    if (error.response) {
      // 서버 응답이 있는 경우
      const { status, data } = error.response;

      switch (status) {
        case 401:
          errorMessage = "인증이 필요합니다. 다시 로그인해 주세요.";
          authStore.logout();

          // 로그인 페이지가 아닌 경우에만 리디렉션
          if (window.location.pathname !== "/login") {
            window.location.href = "/login";
          }
          break;

        case 403:
          errorMessage = "접근 권한이 없습니다.";
          break;

        case 404:
          errorMessage = "요청한 리소스를 찾을 수 없습니다.";
          break;

        case 409:
          errorMessage = "이미 존재하는 데이터입니다.";
          break;

        case 422:
          errorMessage = "입력 데이터를 확인해 주세요.";
          break;

        case 500:
          errorMessage =
            "서버에 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.";
          break;

        default:
          // 백엔드에서 보내는 에러 메시지가 있으면 사용
          if (data && data.message) {
            errorMessage = data.message;
          }
      }
    } else if (error.request) {
      // 네트워크 오류
      errorMessage = "네트워크 연결을 확인해 주세요.";
    }

    return Promise.reject(error);
  }
);

export default apiClient;
