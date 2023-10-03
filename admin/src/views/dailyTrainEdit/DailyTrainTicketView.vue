<template>
    <div>
        <div>
            <a-button type="link" style="float: left" @click="queryDailyTrainSeatList">refresh</a-button>
            <a-select
                    style="float: left;margin-left: 10px"
                    v-model:value="start"
                    placeholder="Select a Start Staiion"
                    search
                    :options="options"
            />
            <a-select
                    style="float: left;margin-left: 10px"
                    v-model:value="end"
                    placeholder="Select a End Staiion"
                    search
                    :options="options"
            />
        </div>
        <a-table :dataSource="dataSource" :columns="columns" :pagination="false" :loading="loading"
                 style="margin-top: 20px">
            <template #bodyCell="{column , record}">
                <template v-if="column.dataIndex === 'time' ">
                    始：{{ record.startTime }}<br/>
                    终：{{record.endTime}}
                </template>
                <template v-if="column.dataIndex === 'station' ">
                    始：{{record.start}}<br/>
                    终：{{record.end}}
                </template>
                <template v-if="column.dataIndex === 'ydzInfo' ">
                    余票：
                    <template v-if="record.ydz>=0">
                        {{record.ydz}}
                        <br/>
                        {{record.ydzPrice}}&nbsp;元
                    </template>
                    <template v-else>
                        --
                    </template>
                </template>
                <template v-if="column.dataIndex === 'edzInfo' ">
                    余票：
                    <template v-if="record.edz>=0">
                        {{record.edz}}
                        <br/>
                        {{record.edzPrice}}&nbsp;元
                    </template>
                    <template v-else>
                        --
                    </template>
                </template>
                <template v-if="column.dataIndex === 'rwInfo' ">
                    余票：
                    <template v-if="record.rw>=0">
                        {{record.rw}}
                        <br/>
                        {{record.rwPrice}}&nbsp;元
                    </template>
                    <template v-else>
                        --
                    </template>
                </template>
                <template v-if="column.dataIndex === 'ywInfo' ">
                    余票：
                    <template v-if="record.yw>=0">
                        {{record.yw}}
                        <br/>
                        {{record.ywPrice}}&nbsp;元
                    </template>
                    <template v-else>
                        --
                    </template>
                </template>
            </template>
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
        title: '时间',
        dataIndex: 'time',
        key: 'time'
    },
    {
        title: '车站',
        dataIndex: 'station',
        key: 'station'
    },
    {
        title: '一等座',
        dataIndex: 'ydzInfo',
        key: 'ydzInfo'
    },
    {
        title: '二等座',
        dataIndex: 'edzInfo',
        key: 'edzInfo'
    },
    {
        title: '软卧',
        dataIndex: 'rwInfo',
        key: 'rwInfo'
    },
    {
        title: '硬卧',
        dataIndex: 'ywInfo',
        key: 'ywInfo'
    },
]
const pagination = reactive({
    total: 0,
    current: 1,
    pageSize: 2,
})
const queryDailyTrainSeatList = () => {
    loading.value = true
    axios.get("/business/dailyTrainTicket/admin/query-list",
        {
            params: {
                currentPage: pagination.current,
                pageSize: pagination.pageSize,
                dailyTrainId: routeParams.value.dailyTrainId,
                start: start.value,
                end: end.value,
            }
        })
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

const options = ref([])
const start = ref(null)
const end = ref(null)

const searchTrainStations = () => {
    axios.get('/business/dailyTrainStation/admin/query-list?dailyTrainId=' + routeParams.value.dailyTrainId)
        .then(res => {
            if (res) {
                if (res.data.success) {
                    console.log("res.data.success -->" + res)
                    console.log(res)
                    options.value = []
                    options.value.push({
                        value: null,
                        label: '不选择车站'
                    })
                    for (let i = 0; i < res.data.content.list.length; i++) {
                        options.value.push({
                            value: res.data.content.list[i].stationName,
                            label: res.data.content.list[i].stationName
                        })
                    }
                }
            }
        })
}


onMounted(() => {
    queryDailyTrainSeatList()
    searchTrainStations()
})


</script>
<style scoped>

</style>