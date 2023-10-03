<template>
    <div>
        <div id="top-bar">
            <a-button type="link"  @click="queryDailyTrainTicketList">查找</a-button>
            <station-selector v-model:station-name="start"/>
            <station-selector v-model:station-name="end"/>
            <input type="date" v-model="startDate">
        </div>
        <a-table :dataSource="dataSource" :columns="columns" :pagination="false" :loading="loading"
                 style="margin-top: 20px">
            <template #bodyCell="{column , record}">
                <template v-if="column.dataIndex === 'dailyTrain' ">
                   {{record.train.type}}{{record.train.code}}
                </template>
                <template v-if="column.dataIndex === 'time' ">
                    始：{{ record.startTime }}<br/>
                    终：{{record.endTime}}
                </template>
                <template v-if="column.dataIndex === 'intervalTime' ">
                    {{computedTimeInterval(record)}}
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
                      @change="queryDailyTrainTicketList"
        />
    </div>
</template>
<script setup>
import {onMounted, reactive, ref} from 'vue';
import axios from "axios";
import StationSelector from "@/components/StationSelector.vue";
import {info} from "@/util/info";
import dayjs from "dayjs";

const loading = ref(false);

const start = ref(null)
const end = ref(null)
const startDate = ref(null)

const dataSource = ref([])
const columns = [
    {
        title: '车次',
        dataIndex: 'dailyTrain',
        key: 'dailyTrain'
    },
    {
        title: '时间',
        dataIndex: 'time',
        key: 'time'
    },
    {
        title: '历时',
        dataIndex: 'intervalTime',
        key: 'intervalTime'
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
const queryDailyTrainTicketList = () => {

    if (!start.value){
        info('error',"请输入出发站")
        return
    }
    if (!end.value){
        info('error',"请输入终点站")
        return
    }
    if (!start.value){
        info('error',"请输入出发日期")
        return
    }

    loading.value = true
    axios.get("/business/ticket/query-list",
        {
            params: {
                currentPage: pagination.current,
                pageSize: pagination.pageSize,
                startDate: startDate.value,
                start: start.value,
                end: end.value,
            }
        })
        .then(async res => {
            if (res) {
                if (res.data.success) {
                    for (let i = 0; i < res.data.content.list.length; i++) {
                        res.data.content.list[i].train = await getDailyTrain(res.data.content.list[i].dailyTrainId)
                        loading.value = true
                    }

                    dataSource.value = res.data.content.list
                    pagination.total = res.data.content.total

                }
            }
            loading.value = false
        })
}

const getDailyTrain =  async (id) => {
    let train = {}
    await axios.get('/business/dailyTrain/query-one/' + id)
        .then(res => {
            if (res) {
                if (res.data.success) {
                    train = res.data.content
                }
            }
        })
    loading.value = false
    return train
}

const  computedTimeInterval = (record)=>{

}


onMounted(() => {

})


</script>
<style scoped>

#top-bar>*{
    margin-right: 15px;
    float: left;
}
</style>