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
                    {{computedTimeInterval(record.startTime,record.endTime,record.train.intervalDay)}}
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

<<<<<<< HEAD
const  computedTimeInterval = (record)=>{

}

=======

function computedTimeInterval(startTime, endTime, intervalDays) {
    // 将时间字符串解析为 day.js 对象
    const startDate = dayjs(startTime,'HH:mm:ss');
    const endDate = dayjs(endTime,'HH:mm:ss');

    // 计算时间差值
    let timeDifference = endDate.diff(startDate);


    // 加上间隔天数
    timeDifference += intervalDays * 24 * 60 * 60 * 1000;


    // 返回结果
    return formatTimeDifference(timeDifference) // 返回hh:mm:ss格式的时间字符串
}

function formatTimeDifference(timeDifference) {
    // 将毫秒数转换为秒
    const totalSeconds = Math.floor(timeDifference / 1000);

    // 计算小时、分钟和剩余秒数
    const hours = Math.floor(totalSeconds / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const seconds = totalSeconds % 60;

    // 格式化为 HH:mm:ss
    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
}



>>>>>>> 66229f0 (实现了会员界面的查找余票信息的界面,并且在车票表当中添加了startDate字段)

onMounted(() => {

})


</script>
<style scoped>

#top-bar>*{
    margin-right: 15px;
    float: left;
}
</style>