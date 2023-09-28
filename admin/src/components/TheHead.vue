<template>
    <a-layout-header class="header">
        <div class="logo">
               控制台
        </div>
        <a-menu
                v-model:selectedKeys="selectedKeys"
                theme="dark"
                mode="horizontal"
                :style="{ lineHeight: '64px' }"
                :items="items"
                @click="toPage"
        >
        </a-menu>

    </a-layout-header>
</template>


<script setup>
import { h, reactive, ref} from "vue";
import {HomeOutlined, SmileOutlined, TeamOutlined, UserOutlined} from "@ant-design/icons-vue";
import router from "@/router";

//该方法只能用于setUp当中



function getItem(label, key, icon, children, type) {
    return {
        key,
        icon,
        children,
        label,
        type,
    };
}
const items = reactive([
    getItem('welcome', '/main/welcome', h(SmileOutlined),null, null),
    getItem('about', '/main/about', h(TeamOutlined),null, null),
    getItem('station', '/main/station', h(HomeOutlined),null, null),
    getItem('train', '/main/train', h(UserOutlined),null, null),
])
const toPage = ({key,keyPath}) => {
    let finalPath = '';
    sessionStorage.setItem('12306_selectedKey',JSON.stringify([key]))
    for (let i = 0; i < keyPath.length; i++) {
        finalPath += keyPath[i]
    }
    router.push(finalPath)
}
const selectedKeys = ref(['/main/welcome'])


</script>

<style scoped>
.logo {
    float: left;
    width: 150px;
    height: 31px;
    color: white;
    font-size: 20px;
    //background: rgba(255, 255, 255, 0.3);
}

</style>