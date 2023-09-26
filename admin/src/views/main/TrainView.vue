<template>
    <div>
        <div>
            <a-button type="primary" @click="showModal" style="float: left">add</a-button>
        </div>
        <a-table :dataSource="dataSource" :columns="columns" :pagination="false" :loading="loading" style="margin-top: 20px">
            <template #bodyCell="{column , record}">
                <template v-if="column.dataIndex === 'type'">
                    <template v-for="item in train_type" :key="item.code">
                        <span v-if="item.code === record.type">
                            {{item.description}}
                        </span>
                    </template>
                </template>
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
                      @change="queryTrainList"
        />
        <a-modal v-model:open="open"
                 title="enter train info"
                 :closable="false"
                 :mask="true"
                 :maskClosable="false"
                 :footer="[]"
        >
            <a-form
                    :model="train"
                    name="train"
                    autocomplete="off"
            >
                <a-form-item
                        label="车次编号"
                        name="code"
                >
                    <a-input v-model:value="train.code"/>
                </a-form-item>
                <a-form-item
                        label="typeOfTrain"
                        name="typeOfTrain"
                >
                    <a-select v-model:value="train.type">
                        <a-select-option v-for="item in train_type" :value="item.code" :key="item.code">{{item.description}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item
                    label="始发站"
                    name="start"
                >
                    <a-input v-model:value="train.start"/>
                </a-form-item>
                <a-form-item
                    label="始发站拼音"
                    name="startPinyin"
                >
                    <a-input v-model:value="train.startPinyin"/>
                </a-form-item>
                <a-form-item
                    label="出发时间"
                    name="startTime"
                >
                    <a-input v-model:value="train.startTime"/>
                </a-form-item>
                <a-form-item
                    label="终点站"
                    name="end"
                >
                    <a-input v-model:value="train.end"/>
                </a-form-item>
                <a-form-item
                    label="终点站拼音"
                    name="code"
                >
                    <a-input v-model:value="train.endPinyin"/>
                </a-form-item>
                <a-form-item
                    label="到终点站时间"
                    name="endTime"
                >
                    <a-input v-model:value="train.endTime"/>
                </a-form-item>
                <a-form-item
                    label="出发和到站之间间隔的天数"
                    name="intervalDay"
                >
                    <a-input v-model:value="train.intervalDay"/>
                </a-form-item>
                <a-form-item>
                    <a-popconfirm placement="topLeft" ok-text="Yes" cancel-text="No" @confirm="confirm">
                        <template #title>
                            are you sure to add this train?
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
import {onMounted, reactive, ref} from 'vue';
import {doPost} from "@/util/axiosUtil";
import axios from "axios";
import {info} from "@/util/info";

const train = reactive({
    id:'',
    code:'',
    type:'K',
    start:'',
    startPinyin:'',
    startTime:'',
    end:'',
    endPinyin:'',
    endTime:'',
    intervalDay:'',
    createTime:'',
    updateTime:'',
})


const train_type = ref([])
const open = ref(false);
const loading = ref(false);
const showModal = () => {
    open.value = true
};
const confirm = async () => {
    let res = await doPost('/business/train/admin/save', train);
    if (res) {
        resetTrain()
        queryTrainList()
        open.value = false;
    }
};
const cancel = () => {
    resetTrain()
    open.value = false;
}

function resetTrain() {
    Object.assign(train,{
        id:'',
        code:'',
        type:'K',
        start:'',
        startPinyin:'',
        startTime:'',
        end:'',
        endPinyin:'',
        endTime:'',
        intervalDay:'',
        createTime:'',
        updateTime:'',
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
const queryTrainList = () => {
    loading.value = true
    axios.get("/business/train/admin/query-list?currentPage=" + pagination.current + "&pageSize=" + pagination.pageSize)
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

const queryTrainType = ()=>{
    loading.value = true
    axios.get('/business/get-enum/business.enums.Train')
        .then(res =>{
            loading.value = false
            if (res) {
                train_type.value = res.data
            }
        })
}

const handleEdit = (record) => {
    Object.assign(train,record)//该方法相当于BeanUtil.copyProperties
    open.value = true
}

const handleDelete = (record)=>{
    axios.delete('/business/train/admin/delete/'+record.id)
        .then(resp => {
            if (resp) {
                const data = resp.data
                if (data.success) {
                    info('success', data.message)
                    queryTrainList()
                }
                if (data.success === false) {
                    info('error', data.message)
                }
            }
        })
}

onMounted(() => {
    queryTrainList()
    queryTrainType()
})


</script>
<style scoped>

</style>