<template>
    <div>
        <div>
            <a-button style="float: left" @click="queryTicketList">刷新</a-button>
        </div>
        <a-table :dataSource="dataSource" :columns="columns" :pagination="false" :loading="loading" style="margin-top: 20px">
            <template #bodyCell="{column , record}">
                <template v-if="column.dataIndex === 'seatType'">
                    <template v-for="item in trainSeat_type" :key="item.code">
                        <span v-if="item.code === record.seatType">
                            {{item.description}}
                        </span>
                    </template>
                </template>
                <template v-if="column.dataIndex === 'seatCol'">
                    <template v-for="item in trainSeatCol_type" :key="item.code">
                        <span v-if="item.code === record.col">
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
                      @change="queryTicketList"
        />
    </div>
</template>
<script setup>
import {onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {useRoute} from "vue-router";


const route = useRoute()
const routeParams = ref(route.params)
const trainSeat_type = ref([])
const trainSeatCol_type = ref([])
const loading = ref(false);


const dataSource = ref([])
const columns = [
    {
        title: '乘客姓名',
        dataIndex: 'passengerName',
        key: 'passengerName',
    },
    {
        title: '发车日期',
        dataIndex: 'date',
        key: 'date',
    },
    {
        title: '座位类型',
        dataIndex: 'seatType',
        key: 'seatType',
    },
    {
        title: '车次编号',
        dataIndex: 'trainCode',
        key: 'trainCode',
    },
    {
        title: '箱序',
        dataIndex: 'carriageIndex',
        key: 'carriageIndex',
    },
    {
        title: '排号',
        dataIndex: 'row',
        key: 'row',
    },
    {
        title: '列号',
        dataIndex: 'col',
        key: 'col',
    },
    {
        title: '出发站',
        dataIndex: 'start',
        key: 'start',
    },
    {
        title: '出发时间',
        dataIndex: 'startTime',
        key: 'startTime',
    },
    {
        title: '到达站',
        dataIndex: 'end',
        key: 'end',
    },
    {
        title: '到达时间',
        dataIndex: 'endTime',
        key: 'endTime',
    },
    {
        title: '座位类型',
        dataIndex: 'seatType',
        key: 'seatType',
    }
]
const pagination = reactive({
    total: 0,
    current: 1,
    pageSize: 2,
})
const queryTicketList = () => {
    loading.value = true
    axios.get("/member/ticket/query-list?currentPage=" + pagination.current + "&pageSize=" + pagination.pageSize+"&dailyTrainId="+routeParams.value.dailyTrainId)
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

const queryTicketAndColType = ()=>{
    loading.value = true
    axios.get('/business/get-enum/business.enums.Seat')
        .then(res =>{
            loading.value = false
            if (res) {
                trainSeat_type.value = res.data
            }
        })
    loading.value = true
    axios.get('/business/get-enum/business.enums.SeatCol')
        .then(res =>{
            loading.value = false
            if (res) {
                trainSeatCol_type.value = res.data
            }
        })
}

onMounted(() => {
    queryTicketList()
    queryTicketAndColType()
})


</script>
<style scoped>

</style>