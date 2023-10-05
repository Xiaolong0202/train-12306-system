<template>
    <div>
        <div>
            <a-button style="float: left" @click="queryConfirmOrderList">refresh</a-button>
        </div>
        <a-table :dataSource="dataSource" :columns="columns" :pagination="false" :loading="loading" style="margin-top: 20px">
            <template #bodyCell="{column , record}">
                <template v-if="column.dataIndex === 'status'">
                    <template v-for="item in confirmOrder_type" :key="item.code">
                        <span v-if="item.code === record.seatType">
                            {{item.description}}
                        </span>
                    </template>
                </template>
            </template>

        </a-table>
        <a-pagination v-model:current="pagination.current"
                      v-model:page-size="pagination.pageSize"
                      v-model:total="pagination.total"
                      :pageSizeOptions="['2','5','10','15']"
                      :showSizeChanger="true"
                      @change="queryConfirmOrderList"
        />
    </div>
</template>
<script setup>
import {onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {useRoute} from "vue-router";


const route = useRoute()
const routeParams = ref(route.params)
const confirmOrder_type = ref([])
const loading = ref(false);


const dataSource = ref([])
const columns = [
    {
        title: '会员id',
        dataIndex: 'memberId',
        key: 'memberId',
    },{
        title: '日期',
        dataIndex: 'date',
        key: 'date',
    },{
        title: '车次编号',
        dataIndex: 'trainCode',
        key: 'trainCode',
    },{
        title: '出发站',
        dataIndex: 'start',
        key: 'start',
    },{
        title: '到达站',
        dataIndex: 'end',
        key: 'end',
    },{
        title: '余票ID',
        dataIndex: 'dailyTrainTicketId',
        key: 'dailyTrainTicketId',
    },{
        title: '会员id',
        dataIndex: 'memberId',
        key: 'memberId',
    },{
        title: '会员id',
        dataIndex: 'memberId',
        key: 'memberId',
    },{
        title: '车票',
        dataIndex: 'tickets',
        key: 'tickets',
    },{
        title: '订单状态',
        dataIndex: 'status',
        key: 'status',
    },{
        title: '新增时间',
        dataIndex: 'createTime',
        key: 'createTime',
    }, {
        title: '修改时间',
        dataIndex: 'createTime',
        key: 'createTime',
    },
]
const pagination = reactive({
    total: 0,
    current: 1,
    pageSize: 2,
})
const queryConfirmOrderList = () => {
    loading.value = true
    axios.get("/business/confirmOrder/admin/query-list?currentPage=" + pagination.current + "&pageSize=" + pagination.pageSize+"&trainId="+routeParams.value.trainId)
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

const queryConfirmOrderType = ()=>{
    loading.value = true
    axios.get('/business/get-enum/business.enums.ConfirmOrderStatus')
        .then(res =>{
            loading.value = false
            if (res) {
                confirmOrder_type.value = res.data
            }
        })
    loading.value = true
}

onMounted(() => {
    queryConfirmOrderList()
    queryConfirmOrderType()
})


</script>
<style scoped>

</style>