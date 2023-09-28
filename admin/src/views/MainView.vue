<template>
    <a-layout id="components-layout-demo-top-side-2" style="min-height: 100vh">
        <the-head/>
        <a-layout>
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


</script>
<style scoped>


</style>