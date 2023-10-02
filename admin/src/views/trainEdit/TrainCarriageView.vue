<template>
    <div>
        <div>
            <a-button type="primary" @click="showModal" style="float: left">add</a-button>
        </div>
        <a-table :dataSource="dataSource" :columns="columns" :pagination="false" :loading="loading" style="margin-top: 20px">
            <template #bodyCell="{column , record}">
                <template v-if="column.dataIndex === 'seatType'">
                    <template v-for="item in trainCarriage_type" :key="item.code">
                        <span v-if="item.code === record.seatType">
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
                      @change="queryTrainCarriageList"
        />
        <a-modal v-model:open="open"
                 title="enter trainCarriage info"
                 :closable="false"
                 :mask="true"
                 :maskClosable="false"
                 :footer="[]"
        >
            <a-form
                    :model="trainCarriage"
                    name="trainCarriage"
                    autocomplete="off"
            >
                <a-form-item
                        label="车次ID"
                        name="trainId"
                >
                    <a-input v-model:value="trainCarriage.trainId" disabled/>
                </a-form-item>
                <a-form-item
                        label="火车箱号"
                        name="trainIndex"
                >
                    <a-input v-model:value="trainCarriage.trainIndex"/>
                </a-form-item>
                <a-form-item
                        label="座位类型"
                        name="seatType"
                >
                    <a-select v-model:value="trainCarriage.seatType">
                        <a-select-option v-for="item in trainCarriage_type" :value="item.code" :key="item.code">{{item.description}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item
                        label="排数"
                        name="rowCount"
                >
                    <a-input v-model:value="trainCarriage.rowCount"/>
                </a-form-item>
                <a-form-item>
                    <a-popconfirm placement="topLeft" ok-text="Yes" cancel-text="No" @confirm="confirm">
                        <template #title>
                            are you sure to add this trainCarriage?
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
import {useRoute} from "vue-router";


const route = useRoute()
const trainCarriage_type = ref([])
const open = ref(false);
const loading = ref(false);
const routeParams = ref(route.params)
const trainCarriage = reactive({
    trainId:  routeParams.value.trainId,
    id:  ``,
    trainIndex:  ``,
    seatType:  ``,
    seatCount:  ``,
    rowCount:  ``,
    columnCount:  ``,
    createTime:   ``,
    updateTime:  ``
})

const showModal = () => {
    open.value = true
};
const confirm = async () => {
    let res = await doPost('/business/trainCarriage/admin/save', trainCarriage);
    if (res) {
        resetTrainCarriage()
        queryTrainCarriageList()
        open.value = false;
    }
};
const cancel = () => {
    resetTrainCarriage()
    open.value = false;
}

function resetTrainCarriage() {
    Object.assign(trainCarriage,{
        trainId:  routeParams.value.trainId,
        id:  ``,
        trainIndex:  ``,
        seatType:  `1`,
        seatCount:  ``,
        rowCount:  ``,
        columnCount:  ``,
        createTime:   ``,
        updateTime:  ``
    })
}

const dataSource = ref([])
const columns = [
    {
        title: '编号',
        dataIndex: 'trainId',
        key: 'trainId',
    },
    {
        title: '火车箱号',
        dataIndex: 'trainIndex',
        key: 'trainIndex',
    },
    {
        title: '座位类型',
        dataIndex: 'seatType',
        key: 'seatType',
    },
    {
        title: '座位数',
        dataIndex: 'seatCount',
        key: 'seatCount',
    },
    {
        title: '排数',
        dataIndex: 'rowCount',
        key: 'rowCount',
    },
    {
        title: '列数',
        dataIndex: 'columnCount',
        key: 'columnCount',
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
const queryTrainCarriageList = () => {
    loading.value = true
    axios.get("/business/trainCarriage/admin/query-list?currentPage=" + pagination.current + "&pageSize=" + pagination.pageSize+"&trainId="+routeParams.value.trainId)
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

const queryTrainCarriageType = ()=>{
    loading.value = true
    axios.get('/business/get-enum/business.enums.Seat')
        .then(res =>{
            loading.value = false
            if (res) {
                trainCarriage_type.value = res.data
            }
        })
}

const handleEdit = (record) => {
    Object.assign(trainCarriage,record)//该方法相当于BeanUtil.copyProperties
    open.value = true
}

const handleDelete = (record)=>{
    axios.delete('/business/trainCarriage/admin/delete/'+record.id)
        .then(resp => {
            if (resp) {
                const data = resp.data
                if (data.success) {
                    info('success', data.message)
                    queryTrainCarriageList()
                }
                if (data.success === false) {
                    info('error', data.message)
                }
            }
        })
}

onMounted(() => {
    queryTrainCarriageList()
    queryTrainCarriageType()
})


</script>
<style scoped>

</style>