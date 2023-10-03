<template>
    <a-layout-header class="header">
        <div class="logo"/>
        <div style="float: right; color: white;font-size: large">
            您好: {{ member.mobile }} &nbsp;&nbsp;
            <router-link to="/login" style="text-decoration-color: dodgerblue;font-size: small">
                退出登录
            </router-link>
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
import store from "@/store";
import { h, reactive, ref, watch} from "vue";
import {SmileOutlined, TeamOutlined} from "@ant-design/icons-vue";
import router from "@/router";

//该方法只能用于setUp当中

const member = store.state.member


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
    getItem('passenger', '/main/passenger', h(TeamOutlined),null, null),
    getItem('ticket', '/main/ticket', h(TeamOutlined),null, null),
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
watch(()=>router.currentRoute.value.path,( ) => {
    let item = sessionStorage.getItem('12306_selectedKey');
    if (item && typeof(item)!== 'undefined' && item!=='undefined'){
        selectedKeys.value =  JSON.parse(item)
    }
})


</script>

<style scoped>
#components-layout-demo-top-side-2 .logo {
    float: left;
    width: 120px;
    height: 31px;
    margin: 16px 24px 16px 0;
    background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
    float: right;
    margin: 16px 0 16px 24px;
}
</style>