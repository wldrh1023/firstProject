import apiClient from "./axios";

export const ordersAPI = {
  // 주문 목록 조회
  getOrders: () => {
    return apiClient.get("/api/orders");
  },

  // 주문 생성
  createOrder: (orderData) => {
    return apiClient.post("/api/orders", orderData);
  },

  // 주문 삭제
  deleteOrder: (orderId) => {
    return apiClient.delete(`/api/orders/${orderId}`);
  },
};
