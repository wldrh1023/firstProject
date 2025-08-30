import { createApp } from "vue";
import { createPinia } from "pinia";
import router from "@/scripts/router";
import App from "./App.vue";
import "./styles/main.css"; // 상대 경로로 변경

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);
app.use(router);
app.mount("#app");
