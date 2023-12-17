<template>
    <div>
        <div id="top-bar">
            <a-button type="link" @click="queryDailyTrainTicketList">查找</a-button>
            <station-selector v-model:station-name="params.start"/>
            <station-selector v-model:station-name="params.end"/>
            <!--            <input type="date" v-model="params.startDate">-->
            <a-date-picker v-model:value="params.startDate" value-format="YYYY-MM-DD" :disabled-date="disableDate"/>
            <!--            <input type="date" v-model="">-->
        </div>
        <a-table :dataSource="dataSource" :columns="columns" :pagination="false" :loading="loading"
                 style="margin-top: 20px">
            <template #bodyCell="{column , record}">
                <template v-if="column.dataIndex === 'dailyTrain' && record.dailyTrain">
                    {{ record.dailyTrain.type }}{{ record.dailyTrain.code }}
                </template>
                <template v-if="column.dataIndex === 'time' ">
                    始：{{ record.startTime }}<br/>
                    终：{{ record.endTime }}
                </template>
                <template v-if="column.dataIndex === 'intervalTime' && record.dailyTrain ">
                    {{ computedTimeInterval(record, record.startTime, record.endTime, record.dailyTrain.intervalDay) }}
                </template>
                <template v-if="column.dataIndex === 'station' ">
                    始：{{ record.start }}<br/>
                    终：{{ record.end }}
                </template>
                <template v-if="column.dataIndex === 'ydzInfo' ">
                    余票：
                    <template v-if="record.ydz>=0">
                        {{ record.ydz }}
                        <br/>
                        {{ record.ydzPrice }}&nbsp;元
                    </template>
                    <template v-else>
                        --
                    </template>
                </template>
                <template v-if="column.dataIndex === 'edzInfo' ">
                    余票：
                    <template v-if="record.edz>=0">
                        {{ record.edz }}
                        <br/>
                        {{ record.edzPrice }}&nbsp;元
                    </template>
                    <template v-else>
                        --
                    </template>
                </template>
                <template v-if="column.dataIndex === 'rwInfo' ">
                    余票：
                    <template v-if="record.rw>=0">
                        {{ record.rw }}
                        <br/>
                        {{ record.rwPrice }}&nbsp;元
                    </template>
                    <template v-else>
                        --
                    </template>
                </template>
                <template v-if="column.dataIndex === 'ywInfo' ">
                    余票：
                    <template v-if="record.yw>=0">
                        {{ record.yw }}
                        <br/>
                        {{ record.ywPrice }}&nbsp;元
                    </template>
                    <template v-else>
                        --
                    </template>
                </template>
                <template v-if="column.dataIndex === 'action' ">
                    <a-button @click="toOrder(record)" :disabled="isExpire(record.startDate,record.startTime)">
                        {{ isExpire(record.startDate,record.startTime) ? '过期' : '订票'}}
                    </a-button>
                    <a-button style="margin-left: 10px" @click="queryStationInfo(record)">
                        车站信息
                    </a-button>
                    <a-button style="margin-left: 10px" @click="toTicketsLeft(record,record.dailyTrain)">
                        余票图
                    </a-button>
                </template>
            </template>
        </a-table>
<!--        <a-pagination v-model:current="pagination.current"-->
<!--                      v-model:page-size="pagination.pageSize"-->
<!--                      v-model:total="pagination.total"-->
<!--                      :pageSizeOptions="['2','5','10','15']"-->
<!--                      :showSizeChanger="true"-->
<!--                      @change="queryDailyTrainTicketList"-->
<!--        />-->
        <a-modal :footer="[]" @cancel="trainStationListVisible=false;trainStationList=[]"
                 :open="trainStationListVisible">
            <a-table :data-source="trainStationList" :pagination="false">
                <a-table-column key="trainIndex" title="站序" dataIndex="trainIndex"/>
                <a-table-column key="stationName" title="站名" dataIndex="stationName"/>
                <a-table-column key="inTime" title="入站时间" dataIndex="inTime">
                    <template #default="{record}">
                        {{ record.trainIndex === 0 ? '---' : record.inTime }}
                    </template>
                </a-table-column>
                <a-table-column key="outTime" title="出站时间" dataIndex="outTime">
                    <template #default="{record}">
                        {{ record.trainIndex === trainStationList.length - 1 ? '---' : record.outTime }}
                    </template>
                </a-table-column>
                <a-table-column key="stopTime" title="停留时间" dataIndex="stopTime">
                    <template #default="{record}">
                        {{
                        record.trainIndex === 0 || record.trainIndex === trainStationList.length - 1 ? '---' : record.stopTime
                        }}
                    </template>
                </a-table-column>

            </a-table>
            <!--            <a-list item-layout="horizontal" :data-source="trainStationList">-->
            <!--                <template #renderItem="{ item }">-->
            <!--                    <a-list-item>-->
            <!--                        <h3>{{ item.trainIndex }}、{{ item.stationName }}&nbsp;{{ item.namePinyin }}</h3>-->
            <!--                        &lt;!&ndash; 当该站为第一站的时候不显示停留时间与入站时间  当该站为最后一站的时候不显示停留时间与入站时间 &ndash;&gt;-->
            <!--                        入站时间:-->
            <!--                        <template v-if="item.trainIndex===0">-&#45;&#45;</template>-->
            <!--                        <template v-else>{{ item.inTime }}</template>-->
            <!--                        &nbsp;&nbsp;-->
            <!--                        出站时间:-->
            <!--                        <template v-if="item.trainIndex===trainStationList.length-1">-&#45;&#45;</template>-->
            <!--                        <template v-else>{{ item.outTime }}</template>-->
            <!--                        &nbsp;&nbsp;-->
            <!--                        停留时间:-->
            <!--                        <template v-if="item.trainIndex===trainStationList.length-1||item.trainIndex===0">-&#45;&#45;-->
            <!--                        </template>-->
            <!--                        <template v-else>{{ item.stopTime }}</template>-->
            <!--                    </a-list-item>-->
            <!--                </template>-->
            <!--            </a-list>-->
        </a-modal>
    </div>
</template>
<script setup>
import {onMounted, reactive, ref} from 'vue';
import axios from "axios";
import StationSelector from "@/components/StationSelector.vue";
import {info} from "@/util/info";
import dayjs from "dayjs";
import {SESSION_ORDER, SESSION_TICKET} from "@/constant/SessionStorageKeys";
import router from "@/router";

const loading = ref(false);

const params = reactive({
    start: null,
    end: null,
    startDate: null
})


const dataSource = ref([])
const columns = [
    {
        title: '车次',
        dataIndex: 'dailyTrain',
        key: 'dailyTrain'
    }, {
        title: '时间',
        dataIndex: 'time',
        key: 'time'
    }, {
        title: '历时',
        dataIndex: 'intervalTime',
        key: 'intervalTime'
    }, {
        title: '车站',
        dataIndex: 'station',
        key: 'station'
    }, {
        title: '一等座',
        dataIndex: 'ydzInfo',
        key: 'ydzInfo'
    }, {
        title: '二等座',
        dataIndex: 'edzInfo',
        key: 'edzInfo'
    }, {
        title: '软卧',
        dataIndex: 'rwInfo',
        key: 'rwInfo'
    }, {
        title: '硬卧',
        dataIndex: 'ywInfo',
        key: 'ywInfo'
    }, {
        title: '操作',
        dataIndex: 'action',
        key: 'action'
    }
]
// const pagination = reactive({
//     total: 0,
//     current: 1,
//     pageSize: 200,
// })

const trainStationList = ref([])
const trainStationListVisible = ref(false)
const queryDailyTrainTicketList = () => {

    if (!params.start) {
        info('error', "请输入出发站")
        return
    }
    if (!params.end) {
        info('error', "请输入终点站")
        return
    }
    if (!params.startDate) {
        info('error', "请输入出发日期")
        return
    }
    console.log(params.startDate)
    loading.value = true
    axios.get("/business/ticket/query-list",
        {
            params: {
                // currentPage: pagination.current,
                // pageSize: pagination.pageSize,
                startDate: params.startDate,
                start: params.start,
                end: params.end,
            }
        })
        .then(async res => {
            if (res) {
                if (res.data.success) {
                    for (let i = 0; i < res.data.content.list.length; i++) {
                        // res.data.content.list[i].train = await getDailyTrain(res.data.content.list[i].dailyTrainId)
                        sessionStorage.setItem(SESSION_TICKET, JSON.stringify(params))
                        loading.value = true
                    }

                    dataSource.value = res.data.content.list
                    // pagination.total = res.data.content.total

                }
            }
            loading.value = false
        })
}

// const getDailyTrain = async (id) => {
//     let train = {}
//     await axios.get('/business/dailyTrain/query-one/' + id)
//         .then(res => {
//             if (res) {
//                 if (res.data.success) {
//                     train = res.data.content
//                 }
//             }
//         })
//     loading.value = false
//     return train
// }

function toOrder(record) {
    sessionStorage.setItem(SESSION_ORDER, JSON.stringify(record))
    router.push('/main/order')
}


function computedTimeInterval(record, startTime, endTime, intervalDays) {
    // 将时间字符串解析为 day.js 对象
    const startDate = dayjs(startTime, 'HH:mm:ss');
    const endDate = dayjs(endTime, 'HH:mm:ss');

    // 计算时间差值
    let timeDifference = endDate.diff(startDate);


    // 加上间隔天数
    timeDifference += intervalDays * 24 * 60 * 60 * 1000;

    // 返回结果
    record.timeDifference = formatTimeDifference(timeDifference);
    return record.timeDifference // 返回hh:mm:ss格式的时间字符串
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

function queryStationInfo(record) {
    axios.get('/business/dailyTrainStation/' + record.dailyTrainId)
        .then(resp => {
            if (resp) {
                if (resp.data.success) {
                    trainStationList.value = resp.data.content
                    trainStationListVisible.value = true
                } else {
                    info('error', resp.data.message)
                }
            }
        })
}

function toTicketsLeft(record, tr) {
    router.push({
        path: '/main/leftTicket',
        query: {
            dailyTrainId: record.dailyTrainId,
            startDate: record.startDate,
            dailyTrainType: tr.type,
            dailyTrainCode: tr.code,
            startIndex: record.startIndex,
            endIndex: record.endIndex,
            start: record.start,
            end: record.end
        }
    })
}

function disableDate(chooseDate) {
    return chooseDate && (chooseDate <= dayjs().add(-1, 'day') || chooseDate > dayjs().add(14, 'day'))
}

function isExpire(startDate,startTime) {
    let now = new Date()
    let startDateTime = new Date(String(startDate).replace(/-/g, "/") + " " + startTime);
    return now.getTime() > startDateTime.getTime()
}


onMounted(() => {

    let parse = JSON.parse(sessionStorage.getItem(SESSION_TICKET) || '{}');
    if (parse.start && parse.end && parse.startDate) {
        Object.assign(params, parse)
        queryDailyTrainTicketList()
    }
})


</script>
<style scoped>

#top-bar > * {
    margin-right: 15px;
    float: left;
}
</style>