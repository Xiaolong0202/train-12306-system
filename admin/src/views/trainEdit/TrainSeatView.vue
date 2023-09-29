<template>
    <div>
        <div>
            <a-button style="float: left" @click="queryTrainSeatList">refresh</a-button>
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
                        <span v-if="item.code === record.seatCol">
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
                      @change="queryTrainSeatList"
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
        title: '火车id',
        dataIndex: 'trainId',
        key: 'trainId',
    },
    {
        title: '火车箱号',
        dataIndex: 'carriageId',
        key: 'carriageId',
    },
    {
        title: '座位类型',
        dataIndex: 'seatType',
        key: 'seatType',
    },
    {
        title: '排',
        dataIndex: 'seatRow',
        key: 'seatRow',
    },
    {
        title: '列',
        dataIndex: 'seatCol',
        key: 'seatCol',
    },
    {
        title: '同车厢座序',
        dataIndex: 'carriageSeatIndex',
        key: 'carriageSeatIndex',
    }
    ]
const pagination = reactive({
    total: 0,
    current: 1,
    pageSize: 2,
})
const queryTrainSeatList = () => {
    loading.value = true
    axios.get("/business/trainSeat/admin/query-list?currentPage=" + pagination.current + "&pageSize=" + pagination.pageSize+"&trainId="+routeParams.value.trainId)
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

const queryTrainSeatAndColType = ()=>{
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
    queryTrainSeatList()
    queryTrainSeatAndColType()
})


</script>
<style scoped>

</style>