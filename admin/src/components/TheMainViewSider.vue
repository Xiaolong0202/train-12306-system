<template>
  <a-layout-sider  width="200" style="background: #fff">
      <a-menu
              v-model:openKeys="openKeys"
              v-model:selectedKeys="selectedKeys"
              theme="light"
              mode="inline"
              :items="items"
              @click="toPage"
      />

  </a-layout-sider>
</template>

<script setup>
import {h, reactive, ref} from "vue";
import router from "@/router";
import {FormOutlined, HomeOutlined, UserOutlined} from "@ant-design/icons-vue";

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
    getItem('base', '/main/base', h(HomeOutlined),[
        getItem('station', '/station', h(HomeOutlined),null, null),
        getItem('车次', '/train', h(UserOutlined),null, null),
    ], null),
    getItem('跑批', '/main/batch', h(FormOutlined),[
        getItem('JOB', '/job', h(UserOutlined),null, null),
    ], null),
])
const toPage = ({key,keyPath}) => {
    let finalPath = '';
    sessionStorage.setItem('adminMainKey',key)
    for (let i = 0; i < keyPath.length; i++) {
        finalPath += keyPath[i]
    }
    router.push(finalPath)
}
const selectedKeys = ref([sessionStorage.getItem('adminMainKey')])
const openKeys = ref(['/main/base','/main/batch'])
</script>

<style scoped>

</style>