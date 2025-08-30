import apiClient from "./axios";

export const itemsAPI = {
  // 모든 아이템 조회
  getItems: () => {
    return apiClient.get("/api/items");
  },
};
