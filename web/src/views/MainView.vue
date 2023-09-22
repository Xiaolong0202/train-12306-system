<template>
    <a-layout id="components-layout-demo-top-side-2" style="min-height: 100vh">
        <the-head/>
        <a-layout>
            <the-side/>
            <a-layout style="padding: 0 24px 24px">
                <a-breadcrumb style="margin: 16px 0">
                    <a-breadcrumb-item>Home</a-breadcrumb-item>
                    <a-breadcrumb-item>List</a-breadcrumb-item>
                    <a-breadcrumb-item>App</a-breadcrumb-item>
                </a-breadcrumb>
                <a-layout-content
                        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
                >
                    {{ memberNum }}
                </a-layout-content>
            </a-layout>
        </a-layout>
    </a-layout>
</template>
<script setup>
import TheHead from "@/components/TheHead.vue";
import TheSide from "@/components/TheSide.vue";
import {onMounted, ref} from "vue";
import axios from "axios";
    const memberNum = ref(0)
    onMounted(()=>{
        console.log(axios)
        axios.get('/member/count').then(resp=>{
            if (resp){
                memberNum.value = resp.data.content
            }
        })
    })

</script>
<style scoped>


</style>