import { defineStore } from "pinia";
import { ref } from "vue";

export const useNotificationStore = defineStore("notification", () => {
  // State
  const notifications = ref([]);

  // Actions
  function addNotification(notification) {
    const id = Date.now() + Math.random();
    const newNotification = {
      id,
      type: notification.type || "info", // success, error, warning, info
      title: notification.title || "",
      message: notification.message || "",
      duration: notification.duration || 5000,
    };

    notifications.value.push(newNotification);

    // 자동으로 알림 제거
    if (newNotification.duration > 0) {
      setTimeout(() => {
        removeNotification(id);
      }, newNotification.duration);
    }

    return id;
  }

  function removeNotification(id) {
    const index = notifications.value.findIndex((n) => n.id === id);
    if (index > -1) {
      notifications.value.splice(index, 1);
    }
  }

  function clearAll() {
    notifications.value = [];
  }

  // 편의 메서드들
  function success(message, title = "성공") {
    return addNotification({ type: "success", title, message });
  }

  function error(message, title = "오류") {
    return addNotification({ type: "error", title, message });
  }

  function warning(message, title = "경고") {
    return addNotification({ type: "warning", title, message });
  }

  function info(message, title = "알림") {
    return addNotification({ type: "info", title, message });
  }

  return {
    // State
    notifications,

    // Actions
    addNotification,
    removeNotification,
    clearAll,
    success,
    error,
    warning,
    info,
  };
});
