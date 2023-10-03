<template>
    <a-layout id="components-layout-demo-top-side-2" style="min-height: 100vh">
        <a-layout-header class="header">
            <div class="logo">
                {{menuInfo}}
            </div>
            <div style="float: right">
                <router-link to="/">返回主页</router-link>
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
import {h, onMounted, reactive, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {TeamOutlined} from "@ant-design/icons-vue";
import axios from "axios";
const route = useRoute();
const router = useRouter()
const selectedKeys = ref([sessionStorage.getItem('dailyTrainEditView')])
const routeParams = ref(route.params)
const dailyTrainDB = ref({})
const menuInfo = ref('')


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
    getItem('trainStation', '/dailyTrainEdit/'+routeParams.value.dailyTrainId+'/dailyTrainStation', h(TeamOutlined),null, null),
    getItem('trainCarriage', '/dailyTrainEdit/'+routeParams.value.dailyTrainId+'/dailyTrainCarriage', h(TeamOutlined),null, null),
    getItem('seat', '/dailyTrainEdit/'+routeParams.value.dailyTrainId+'/dailyTrainSeat', h(TeamOutlined),null, null),
])
const toPage = ({key,keyPath}) => {
    sessionStorage.setItem('TrainEditView',key)
    let finalPath = '';
    for (let i = 0; i < keyPath.length; i++) {
        finalPath += keyPath[i]
    }
    finalPath+='/'+routeParams.value.dailyTrainId
    router.push(finalPath)
}
const getTrainInfo = ()=>{
    axios.get('/business/dailyTrain/admin/query-one/'+routeParams.value.dailyTrainId)
        .then(res=>{
            if (res){
                const data = res.data
                if (data.success){
                    dailyTrainDB.value = data.content
                    menuInfo.value =  dailyTrainDB.value.type+dailyTrainDB.value.code+'  '+dailyTrainDB.value.start+'-'+dailyTrainDB.value.end+'\xa0\xa0\xa0\xa0\xa0\xa0发车日期 '+dailyTrainDB.value.startDate
                }
            }
        })
}


onMounted(()=>{
    getTrainInfo()
})

</script>
<style scoped>
.logo {
    float: left;
    width: 500px;
    height: 31px;
    color: white;
    font-size: 20px;
//background: rgba(255, 255, 255, 0.3);
}

</style>