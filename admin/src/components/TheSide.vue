<template>
    <a-layout-sider width="200" style="background: #fff">
        <a-menu
                v-model:selectedKeys="selectedKeys"
                v-model:openKeys="openKeys"
                mode="inline"
                :style="{ height: '100%', borderRight: 0 }"
                :items="items"
                @click="toPage"
        >
        </a-menu>
    </a-layout-sider>
</template>
<script setup>
import { h, reactive, ref} from "vue";
import {TeamOutlined} from "@ant-design/icons-vue";
import router from "@/router";
const openKeys = ref(['sub1']);


/**
 * 快速构建一个Item对象,在该项目当中我将key设置为路径
 */
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
    getItem('trainStation', '/main/trainStation', h(TeamOutlined),null, null),
    getItem('trainCarriage', '/main/trainCarriage', h(TeamOutlined),null, null),
    getItem('seat', '/main/trainSeat', h(TeamOutlined),null, null),
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
.site-layout-background {
    background: #fff;
}
</style>