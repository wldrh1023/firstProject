import './style.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { useAuthStore } from './stores/auth'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

// 앱 초기화 시 인증 상태 복원
const authStore = useAuthStore()
authStore.initializeAuth()

app.mount('#app')
