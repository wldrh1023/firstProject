import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

// 레이아웃 컴포넌트
const AuthLayout = () => import('@/views/layouts/AuthLayout.vue')
const MainLayout = () => import('@/views/layouts/MainLayout.vue')

// 페이지 컴포넌트
const Home = () => import('@/views/Home.vue')
const Login = () => import('@/views/auth/Login.vue')
const Register = () => import('@/views/auth/Register.vue')
const Profile = () => import('@/views/Profile.vue')
const Post = () => import('@/views/Post.vue')
const Search = () => import('@/views/Search.vue')
const Notifications = () => import('@/views/Notifications.vue')
const Settings = () => import('@/views/Settings.vue')

const routes = [
  {
    path: '/',
    component: MainLayout,
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'Home',
        component: Home,
        meta: { title: '홈' },
      },
      {
        path: '/profile/:username?',
        name: 'Profile',
        component: Profile,
        meta: { title: '프로필' },
      },
      {
        path: '/post/:id',
        name: 'Post',
        component: Post,
        meta: { title: '게시물' },
      },
      {
        path: '/search',
        name: 'Search',
        component: Search,
        meta: { title: '검색' },
      },
      {
        path: '/notifications',
        name: 'Notifications',
        component: Notifications,
        meta: { title: '알림' },
      },
      {
        path: '/settings',
        name: 'Settings',
        component: Settings,
        meta: { title: '설정' },
      },
    ],
  },
  {
    path: '/auth',
    component: AuthLayout,
    meta: { requiresGuest: true },
    children: [
      {
        path: 'login',
        name: 'Login',
        component: Login,
        meta: { title: '로그인' },
      },
      {
        path: 'register',
        name: 'Register',
        component: Register,
        meta: { title: '회원가입' },
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/',
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  },
})

// 네비게이션 가드
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()

  // 페이지 타이틀 설정
  document.title = to.meta.title ? `${to.meta.title} - ConnectHub` : 'ConnectHub'

  // 인증이 필요한 페이지
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/auth/login')
    return
  }

  // 게스트만 접근 가능한 페이지 (로그인/회원가입)
  if (to.meta.requiresGuest && authStore.isAuthenticated) {
    next('/')
    return
  }

  next()
})

export default router
