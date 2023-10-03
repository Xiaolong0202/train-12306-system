<template>
    <div>
        <div>
            <a-button style="float: left" @click="queryDailyTrainSeatList">refresh</a-button>
        </div>
        <a-table :dataSource="dataSource" :columns="columns" :pagination="false" :loading="loading" style="margin-top: 20px">
        </a-table>
        <a-pagination v-model:current="pagination.current"
                      v-model:page-size="pagination.pageSize"
                      v-model:total="pagination.total"
                      :pageSizeOptions="['2','5','10','15']"
                      :showSizeChanger="true"
                      @change="queryDailyTrainSeatList"
        />
    </div>
</template>
<script setup>
import {onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {useRoute} from "vue-router";


const route = useRoute()
const routeParams = ref(route.params)
const loading = ref(false);


const dataSource = ref([])
const columns = [
    {
        title: '每日车次ID',
        dataIndex: 'dailyTrainId',
        key: 'dailyTrainId',
    },
    {
        title: '出发站',
        dataIndex: 'start',
        key: 'start',
    },
    {
        title: '出发站拼音',
        dataIndex: 'startPinyin',
        key: 'startPinyin',
    },
    {
        title: '出发站序|本站是整个车次的第几站',
        dataIndex: 'startIndex',
        key: 'startIndex',
    },
    {
        title: '到达站',
        dataIndex: 'end',
        key: 'end',
    },
    {
        title: '到达站拼音',
        dataIndex: 'endPinyin',
        key: 'endPinyin',
    },
    {
        title: '到站站序|本站是整个车次的第几站',
        dataIndex: 'endIndex',
        key: 'endIndex',
    },
    {
        title: '一等座余票',
        dataIndex: 'ydz',
        key: 'ydz',
    },{
        title: '一等座票价',
        dataIndex: 'ydzPrice',
        key: 'ydzPrice',
    },{
        title: '二等座余票',
        dataIndex: 'edz',
        key: 'edz',
    },{
        title: '二等座票价',
        dataIndex: 'edzPrice',
        key: 'edzPrice',
    },{
        title: '软卧余票',
        dataIndex: 'rw',
        key: 'rw',
    },{
        title: '软卧票价',
        dataIndex: 'rwPrice',
        key: 'rwPrice',
    },{
        title: '硬卧余票',
        dataIndex: 'yw',
        key: 'yw',
    },{
        title: '硬卧票价',
        dataIndex: 'ywPrice',
        key: 'ywPrice',
    }
    ]
const pagination = reactive({
    total: 0,
    current: 1,
    pageSize: 2,
})
const queryDailyTrainSeatList = () => {
    loading.value = true
    axios.get("/business/dailyTrainTicket/admin/query-list?currentPage=" + pagination.current + "&pageSize=" + pagination.pageSize+"&dailyTrainId="+routeParams.value.dailyTrainId)
        .then(res => {
            loading.value = false
            if (res) {
                if (res.data.success) {
                    dataSource.value = res.data.content.list
                    pagination.total = res.data.content.total
                }
            }
        })
}



onMounted(() => {
    queryDailyTrainSeatList()
})


</script>
<style scoped>

</style>