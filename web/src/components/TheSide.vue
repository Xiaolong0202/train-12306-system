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
import { h, reactive, ref, watch} from "vue";
import {SmileOutlined, TeamOutlined} from "@ant-design/icons-vue";
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
    getItem('welcome', '/main/welcome', h(SmileOutlined),null, null),
    getItem('passenger', '/main/passenger', h(TeamOutlined),null, null),
    getItem('查找车次', '/main/ticket', h(TeamOutlined),null, null),
    getItem('myTickets', '/main/myTickets', h(TeamOutlined),null, null),
    getItem('车票余座图', '/main/leftTicket', h(TeamOutlined),null, null),
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
    //todo 改变一下该逻辑
    let item = sessionStorage.getItem('12306_selectedKey');
    if (item && typeof(item)!== 'undefined' && item!=='undefined'){
        selectedKeys.value =  JSON.parse(item)
    }
})


</script>
<style scoped>
.site-layout-background {
    background: #fff;
}
</style>