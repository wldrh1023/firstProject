import apiClient from "./axios";
import axios from "axios";

// 주문 삭제용 별도 클라이언트 (백엔드로 직접 요청)
const deleteApiClient = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 10000,
  withCredentials: true,
});

export const ordersAPI = {
  // 주문 목록 조회
  getOrders: () => {
    return apiClient.get("/api/orders");
  },

  // 주문 생성
  createOrder: (orderData) => {
    return apiClient.post("/api/orders", orderData);
  },

  // 주문 삭제 - 백엔드로 직접 요청
  deleteOrder: (orderId) => {
    return deleteApiClient.delete(`/api/orders/${orderId}`);
  },
};
