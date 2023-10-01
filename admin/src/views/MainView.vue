<template>
    <a-layout id="components-layout-demo-top-side-2" style="min-height: 100vh">
        <a-layout>
            <the-main-view-head/>
            <a-layout>
                <the-main-view-sider/>
                <a-layout-content
                        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
                >
                    <router-view></router-view>
                </a-layout-content>
            </a-layout>
        </a-layout>
    </a-layout>
</template>
<script setup>

import TheMainViewSider from "@/components/TheMainViewSider.vue";
import TheMainViewHead from "@/components/TheMainViewHead.vue";


</script>
<style scoped>


</style>