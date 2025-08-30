<template>
  <div id="app">
    <HeaderComponent />
    <RouterView />
    <FooterComponent />
    <Notification />
  </div>
</template>

<script>
import HeaderComponent from './components/Header.vue';
import FooterComponent from './components/Footer.vue';
import Notification from './components/Notification.vue';
import { watch, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { authAPI } from '@/api';

export default {
  name: "App",
  components: {
    HeaderComponent,
    FooterComponent,
    Notification,
  },
  setup() {
    const authStore = useAuthStore();
    const route = useRoute();

    const check = () => {
      authAPI.checkAuth()
        .then(({ data }) => {
          console.log(data);
          authStore.setAuth(data);
        })
        .catch(() => {
          authStore.setAuth(0);
        });
    }

    // 초기 인증 체크
    onMounted(() => {
      authStore.initAuth();
      check();
    });

    // 라우트 변경시마다 인증 체크
    watch(route, () => {
      check();
    });
  }
}
</script>

<style></style>