<template>
    <div>
        <div>
            <a-button type="primary" @click="showModal" style="float: left">add</a-button>
        </div>
        <a-table :dataSource="dataSource" :columns="columns" :pagination="false" :loading="loading" style="margin-top: 20px">
            <template #bodyCell="{column , record}">
                <template v-if="column.dataIndex === 'action'">
                    <a-popconfirm
                            v-if="dataSource.length"
                            title="Sure to edit?"
                            @confirm="handleEdit(record)"
                    >
                        <a>Edit</a>
                    </a-popconfirm>
                    <a-popconfirm
                            v-if="dataSource.length"
                            title="Sure to delete?"
                            @confirm="handleDelete(record)"
                    >
                        <a style="margin-left:  10px">delete</a>
                    </a-popconfirm>
                </template>
            </template>

        </a-table>
        <a-pagination v-model:current="pagination.current"
                      v-model:page-size="pagination.pageSize"
                      v-model:total="pagination.total"
                      :pageSizeOptions="['2','5','10','15']"
                      :showSizeChanger="true"
                      @change="queryTrainStationList"
        />
        <a-modal v-model:open="open"
                 title="enter trainStation info"
                 :closable="false"
                 :mask="true"
                 :maskClosable="false"
                 :footer="[]"
        >
            <a-form
                    :model="trainStation"
                    name="trainStation"
                    autocomplete="off"
            >
                <a-form-item
                        label="车次编号"
                        name="trainId"
                >
                    <a-input v-model:value="trainStation.trainId" disabled/>
                </a-form-item>
                <a-form-item
                        label="车站序号"
                        name="trainIndex"
                >
                    <a-input v-model:value="trainStation.trainIndex"/>
                </a-form-item>
                <a-form-item
                        label="站名"
                        name="stationName"
                >
                    <station-selector v-model:station-name="trainStation.stationName"/>
                </a-form-item>
                <a-form-item
                        label="站名拼音"
                        name="namePinyin"
                >
                    <a-input v-model:value="trainStation.namePinyin" disabled/>
                </a-form-item>
                <a-form-item
                        label="进站时间"
                        name="inTime"
                >
                    <a-time-picker v-model:value="trainStation.inTime" value-format="HH:mm:ss" format="HH:mm:ss" />
                </a-form-item>
                <a-form-item
                        label="出站时间"
                        name="outTime"
                >
                    <a-time-picker v-model:value="trainStation.outTime" value-format="HH:mm:ss" format="HH:mm:ss" />
                </a-form-item>
                <a-form-item
                        label="里程（公里）| 从上一站到本站的距离"
                        name="code"
                >
                    <a-input v-model:value="trainStation.km"/>
                </a-form-item>
                <a-form-item>
                    <a-popconfirm placement="topLeft" ok-text="Yes" cancel-text="No" @confirm="confirm">
                        <template #title>
                            are you sure to add this trainStation?
                        </template>
                        <a-button>save</a-button>
                    </a-popconfirm>
                    <a-button @click="cancel" style="margin-left: 10px">cancel</a-button>
                </a-form-item>
            </a-form>
        </a-modal>
    </div>
</template>
<script setup>
import {onMounted, reactive, ref, watch} from 'vue';
import {doPost} from "@/util/axiosUtil";
import axios from "axios";
import {info} from "@/util/info";
import {pinyin} from "pinyin-pro";
import {useRoute} from "vue-router";
import StationSelector from "@/components/StationSelector.vue";



const route = useRoute()
const routeParams = ref(route.params)
const open = ref(false);
const loading = ref(false);
const trainStation = reactive({
    id: '',
    trainId:  routeParams.value.trainId,
    trainIndex: '',
    stationName: '',
    namePinyin: '',
    inTime: '',
    outTime: '',
    stopTime: '',
    km: '',
    createTime: '',
    updateTime:''
})
const showModal = () => {
    open.value = true
};
const confirm = async () => {
    let res = await doPost('/business/trainStation/admin/save', trainStation);
    if (res) {
        resetTrainStation()
        queryTrainStationList()
        open.value = false;
    }
};
const cancel = () => {
    resetTrainStation()
    open.value = false;
}

function resetTrainStation() {
    Object.assign(trainStation,{
        id: '',
        trainId:  routeParams.value.trainId,
        trainIndex: '',
        stationName: '',
        namePinyin: '',
        inTime: '',
        outTime: '',
        stopTime: '',
        km: '',
        createTime: '',
        updateTime:''
    })
}

const dataSource = ref([])
const columns = [
    {
        title: '车次编号',
        dataIndex: 'trainId',
        key: 'trainId',
    },
    {
        title: '车站序号',
        dataIndex: 'trainIndex',
        key: 'trainIndex',
    },
    {
        title: '站名',
        dataIndex: 'stationName',
        key: 'stationName',
    },
    {
        title: '站名拼音',
        dataIndex: 'namePinyin',
        key: 'namePinyin',
    },
    {
        title: '进站时间',
        dataIndex: 'inTime',
        key: 'inTime',
    },
    {
        title: '出站时间',
        dataIndex: 'outTime',
        key: 'outTime',
    },
    {
        title: '停站时间',
        dataIndex: 'stopTime',
        key: 'stopTime',
    },
    {
        title: '里程（公里）| 从上一站到本站的距离',
        dataIndex: 'km',
        key: 'km',
    },
    {
        title: 'action',
        dataIndex: 'action',
        key: 'action'
    }]
const pagination = reactive({
    total: 0,
    current: 1,
    pageSize: 2,
})
const queryTrainStationList = () => {
    loading.value = true
    axios.get("/business/trainStation/admin/query-list?currentPage=" + pagination.current + "&pageSize=" + pagination.pageSize+"&trainId="+routeParams.value.trainId)
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


const handleEdit = (record) => {
    Object.assign(trainStation,record)//该方法相当于BeanUtil.copyProperties
    open.value = true
}

const handleDelete = (record)=>{
    axios.delete('/business/trainStation/admin/delete/'+record.id)
        .then(resp => {
            if (resp) {
                const data = resp.data
                if (data.success) {
                    info('success', data.message)
                    queryTrainStationList()
                }
                if (data.success === false) {
                    info('error', data.message)
                }
            }
        })
}

onMounted(() => {
    queryTrainStationList()
})
watch(()=>trainStation.stationName,()=>{
    trainStation.namePinyin = pinyin(trainStation.stationName,{toneType: 'none'}).replace(' ','')
})

</script>
<style scoped>

</style>