<template>
    <a-layout-header class="header">
        <div class="logo"/>
        <div style="float: right; color: white;font-size: large">
               您好: {{member.mobile}} &nbsp;&nbsp;
            <router-link to="/login" style="text-decoration-color: dodgerblue;font-size: small" >
                退出登录
            </router-link>
        </div>
        <a-menu
                v-model:selectedKeys="selectedKeys1"
                theme="dark"
                mode="horizontal"
                :style="{ lineHeight: '64px' }"
        >
            <a-menu-item key="1">nav 1</a-menu-item>
            <a-menu-item key="2">{{ member.mobile }}</a-menu-item>
        </a-menu>

    </a-layout-header>
</template>


<script setup>
import {ref} from "vue";
import store from "@/store";

const member = store.state.member
const selectedKeys1 = ref(['2'])
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