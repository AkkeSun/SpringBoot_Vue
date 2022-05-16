import { createApp } from 'vue'
import App from './App.vue'
import router from './router/router.js';
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import index from "./store/index"; // store load
const app = createApp(App);

app.use(index).use(router).mount('#app');
