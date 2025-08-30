import apiClient from "./axios";

export const cartAPI = {
  // 장바구니 아이템 조회
  getCartItems: () => {
    return apiClient.get("/api/cart/items");
  },

  // 장바구니에 아이템 추가
  addItem: (itemId) => {
    return apiClient.post(`/api/cart/items/${itemId}`);
  },

  // 장바구니에서 아이템 제거
  removeItem: (itemId) => {
    return apiClient.delete(`/api/cart/items/${itemId}`);
  },
};
