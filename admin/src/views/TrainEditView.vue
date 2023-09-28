<template>
    <a-layout id="components-layout-demo-top-side-2" style="min-height: 100vh">
        <a-layout-header class="header">
            <div class="logo">
              火车ID {{routeParams.trainId}}
            </div>
        </a-layout-header>
        <a-layout>
            <a-layout-sider width="200" style="background: #fff">
                <a-menu
                    v-model:selectedKeys="selectedKeys"
                    mode="inline"
                    :style="{ height: '100%', borderRight: 0 }"
                    :items="items"
                    @click="toPage"
                >
                </a-menu>
            </a-layout-sider>
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
import {h, reactive, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {TeamOutlined} from "@ant-design/icons-vue";
const route = useRoute();
const router = useRouter()

const routeParams = ref(route.params)



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
    getItem('trainStation', '/trainEdit/'+routeParams.value.trainId+'/trainStation', h(TeamOutlined),null, null),
    getItem('trainCarriage', '/trainEdit/'+routeParams.value.trainId+'/trainCarriage', h(TeamOutlined),null, null),
    getItem('seat', '/trainEdit/'+routeParams.value.trainId+'/trainSeat', h(TeamOutlined),null, null),
])
const toPage = ({keyPath}) => {
    let finalPath = '';
    for (let i = 0; i < keyPath.length; i++) {
        finalPath += keyPath[i]
    }
    finalPath+='/'+routeParams.value.trainId
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