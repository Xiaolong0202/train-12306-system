import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css';
import * as Icons from '@ant-design/icons-vue';
import axios from "axios";

let app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');
//引入图标
for (let iconsKey in Icons) {
    app.component(iconsKey,Icons[iconsKey])
}
axios.defaults.baseURL = 'http://localhost'

