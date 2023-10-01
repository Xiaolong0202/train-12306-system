<template>
    <div>
        <div>
            <a-button type="primary" @click="showModal" style="float: left">add</a-button>
        </div>
        <a-table :dataSource="dataSource" :columns="columns" :pagination="false" :loading="loading"
                 style="margin-top: 20px">
            <template #bodyCell="{column , record}">
                <template v-if="column.dataIndex === 'type'">
                    <template v-for="item in dailyTrain_type" :key="item.code">
                        <span v-if="item.code === record.type">
                            {{ item.description }}
                        </span>
                    </template>
                </template>
                <template v-if="column.dataIndex === 'action'">
                    <a-popconfirm
                            v-if="dataSource.length"
                            title="Sure to edit?"
                            @confirm="handleEdit(record)"
                    >
                        <a style="margin-left:  10px">Edit</a>
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
                      @change="queryDailyTrainList"
        />
        <a-modal v-model:open="open"
                 title="enter dailyTrain info"
                 :closable="false"
                 :mask="true"
                 :maskClosable="false"
                 :footer="[]"
        >
            <a-form
                    :model="dailyTrain"
                    name="dailyTrain"
                    autocomplete="off"
            >
                <a-form-item
                        label="车次编号"
                        name="code"
                >
                    <a-input v-model:value="dailyTrain.code"/>
                </a-form-item>
                <a-form-item
                        label="typeOfDailyTrain"
                        name="typeOfDailyTrain"
                >
                    <a-select v-model:value="dailyTrain.type">
                        <a-select-option v-for="item in dailyTrain_type" :value="item.code" :key="item.code">
                            {{ item.description }}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item
                    label="出发日期"
                    name="startDate"
                >
                    <input v-model ="dailyTrain.startDate" type="date">
<!--                    {{dailyTrain.startDate}}-->
                </a-form-item>
                <a-form-item
                        label="始发站"
                        name="start"
                >
                    <station-selector v-model:station-name="dailyTrain.start"/>
                </a-form-item>
                <a-form-item
                        label="始发站拼音"
                        name="startPinyin"
                >
                    <a-input v-model:value="dailyTrain.startPinyin" disabled/>
                </a-form-item>
                <a-form-item
                        label="出发时间"
                        name="startTime"
                >
                    <a-time-picker v-model:value="dailyTrain.startTime" value-format="HH:mm:ss" format="HH:mm:ss"/>
                </a-form-item>
                <a-form-item
                        label="终点站"
                        name="end"
                >
                    <station-selector v-model:station-name="dailyTrain.end"/>
                    <!--                    <a-input v-model:value="dailyTrain.end"/>-->
                </a-form-item>
                <a-form-item
                        label="终点站拼音"
                        name="code"
                >
                    <a-input v-model:value="dailyTrain.endPinyin" disabled/>
                </a-form-item>
                <a-form-item
                        label="到终点站时间"
                        name="endTime"
                >
                    <a-time-picker v-model:value="dailyTrain.endTime" value-format="HH:mm:ss" format="HH:mm:ss"/>
                </a-form-item>
                <a-form-item
                        label="出发和到站之间间隔的天数"
                        name="intervalDay"
                >
                    <a-input v-model:value="dailyTrain.intervalDay"/>
                </a-form-item>
                <a-form-item>
                    <a-popconfirm placement="topLeft" ok-text="Yes" cancel-text="No" @confirm="confirm">
                        <template #title>
                            are you sure to add this dailyTrain?
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
import StationSelector from "@/components/StationSelector.vue";


const dailyTrain = reactive({
    id: '',
    startDate:'',
    code: '',
    type: 'K',
    start: '',
    startPinyin: '',
    startTime: '',
    end: '',
    endPinyin: '',
    endTime: '',
    intervalDay: '',
    createTime: '',
    updateTime: '',
})


const dailyTrain_type = ref([])
const open = ref(false);
const loading = ref(false);
const showModal = () => {
    open.value = true
};
const confirm = async () => {
    let res = await doPost('/business/dailyTrain/admin/save', dailyTrain);
    if (res) {
        resetDailyTrain()
        queryDailyTrainList()
        open.value = false;
    }
};
const cancel = () => {
    resetDailyTrain()
    open.value = false;
}

function resetDailyTrain() {
    Object.assign(dailyTrain, {
        id: '',
        startDate:'',
        code: '',
        type: 'K',
        start: '',
        startPinyin: '',
        startTime: '',
        end: '',
        endPinyin: '',
        endTime: '',
        intervalDay: '',
        createTime: '',
        updateTime: '',
    })
}

const dataSource = ref([])
const columns = [
    {
        title: '编号',
        dataIndex: 'code',
        key: 'code',
    },
    {
        title: '车次类型',
        dataIndex: 'type',
        key: 'type',
    },
    {
        title: '出发日期',
        dataIndex: 'startDate',
        key: 'startDate'
    },
    {
        title: '始发站',
        dataIndex: 'start',
        key: 'start',
    },
    {
        title: '始发站拼音',
        dataIndex: 'startPinyin',
        key: 'startPinyin',
    },
    {
        title: '出发时间',
        dataIndex: 'startTime',
        key: 'startTime',
    },
    {
        title: '终点站',
        dataIndex: 'end',
        key: 'end',
    },
    {
        title: '终点站拼音',
        dataIndex: 'endPinyin',
        key: 'endPinyin',
    },
    {
        title: '出发和到站之间间隔的天数',
        dataIndex: 'intervalDay',
        key: 'intervalDay',
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
const queryDailyTrainList = () => {
    loading.value = true
    axios.get("/business/dailyTrain/admin/query-list?currentPage=" + pagination.current + "&pageSize=" + pagination.pageSize)
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

const queryDailyTrainType = () => {
    loading.value = true
    axios.get('/business/get-enum/business.enums.Train')
        .then(res => {
            loading.value = false
            if (res) {
                dailyTrain_type.value = res.data
            }
        })
}

const handleEdit = (record) => {
    Object.assign(dailyTrain, record)//该方法相当于BeanUtil.copyProperties
    open.value = true
}

const handleDelete = (record) => {
    axios.delete('/business/dailyTrain/admin/delete/' + record.id)
        .then(resp => {
            if (resp) {
                const data = resp.data
                if (data.success) {
                    info('success', data.message)
                    queryDailyTrainList()
                }
                if (data.success === false) {
                    info('error', data.message)
                }
            }
        })
}


onMounted(() => {
    queryDailyTrainList()
    queryDailyTrainType()
})

watch(() => dailyTrain.start, () => {
    dailyTrain.startPinyin = pinyin(dailyTrain.start, {toneType: 'none'}).replace(" ", '')
})

watch(() => dailyTrain.end, () => {
    dailyTrain.endPinyin = pinyin(dailyTrain.end, {toneType: 'none'}).replace(" ", '')
})


</script>
<style scoped>

</style>