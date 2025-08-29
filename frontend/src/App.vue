<template>
  <div id="app">
    <HeaderComponent />
    <RouterView />
    <FooterComponent />
  </div>
</template>

<script>
import HeaderComponent from './components/Header.vue';
import FooterComponent from './components/Footer.vue';
import axios from 'axios';
import { watch } from 'vue';
import { useRoute } from 'vue-router';

export default {
  name: "App",
  components: {
    HeaderComponent,
    FooterComponent,
  },
  setup() {
    const check = () => {
      axios.get("/api/account/check")
        .then(({ data }) => {
          console.log(data);

          store.commit("setAccount", data || 0);
        })
    }

    const route = useRoute();

    watch(route, () => {
      check();
    })
  }
}
</script>

<style></style>