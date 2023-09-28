<template>
    <a-layout id="components-layout-demo-top-side-2" style="min-height: 100vh">
        <the-head/>
        <a-layout>
            <the-side v-if="router.currentRoute.value.path.startsWith('/main/train')"/>
            <a-layout style="padding: 0 24px 24px">
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
import TheHead from "@/components/TheHead.vue";
import TheSide from "@/components/TheSide.vue";
import router from "@/router"

</script>
<style scoped>


</style>