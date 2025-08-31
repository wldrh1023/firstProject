import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import Home from "@/pages/Home.vue";
import Login from "@/pages/Login.vue";
import Signup from "@/pages/Signup.vue";
import Cart from "@/pages/Cart.vue";
import Order from "@/pages/Order.vue";
import Orders from "@/pages/Orders.vue";
import Withdraw from "@/pages/Withdraw.vue";

const routes = [
  {
    path: "/",
    component: Home,
    name: "Home",
  },
  {
    path: "/login",
    component: Login,
    name: "Login",
    meta: { requiresGuest: true }, // 로그인된 사용자는 접근 불가
  },
  {
    path: "/signup",
    component: Signup,
    name: "Signup",
    meta: { requiresGuest: true }, // 로그인된 사용자는 접근 불가
  },
  {
    path: "/cart",
    component: Cart,
    name: "Cart",
    meta: { requiresAuth: true }, // 인증 필요
  },
  {
    path: "/order",
    component: Order,
    name: "Order",
    meta: { requiresAuth: true }, // 인증 필요
  },
  {
    path: "/orders",
    component: Orders,
    name: "Orders",
    meta: { requiresAuth: true }, // 인증 필요
  },
  {
    path: "/withdraw",
    component: Withdraw,
    name: "Withdraw",
    meta: { requiresAuth: true }, // 인증 필요
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 라우트 가드 설정
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  // 인증이 필요한 페이지
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (!authStore.isAuthenticated) {
      // 로그인하지 않은 경우 로그인 페이지로 리디렉션
      next({
        name: "Login",
        query: { redirect: to.fullPath }, // 로그인 후 원래 페이지로 돌아가기 위해
      });
      return;
    }
  }

  // 게스트만 접근 가능한 페이지 (로그인 페이지 등)
  if (to.matched.some((record) => record.meta.requiresGuest)) {
    if (authStore.isAuthenticated) {
      // 이미 로그인한 경우 홈으로 리디렉션
      next({ name: "Home" });
      return;
    }
  }

  next();
});

export default router;
