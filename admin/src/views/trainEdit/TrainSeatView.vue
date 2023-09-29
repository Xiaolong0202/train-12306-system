<template>
    <div>
        <div>
            <a-button type="primary" @click="showModal" style="float: left">add</a-button>
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
                      @change="queryTrainSeatList"
        />
        <a-modal v-model:open="open"
                 title="enter trainSeat info"
                 :closable="false"
                 :mask="true"
                 :maskClosable="false"
                 :footer="[]"
        >
            <a-form
                    :model="trainSeat"
                    name="trainSeat"
                    autocomplete="off"
            >
                <a-form-item
                        label="车次编号"
                        name="trainId"
                >
                    <a-input v-model:value="trainSeat.trainId" disabled/>
                </a-form-item>
                <a-form-item
                        label="火车箱号"
                        name="carriageId"
                >
                    <a-input v-model:value="trainSeat.carriageId"/>
                </a-form-item>
                <a-form-item
                        label="座位类型"
                        name="seatType"
                >
                    <a-select v-model:value="trainSeat.seatType">
                        <a-select-option v-for="item in trainSeat_type" :value="item.code" :key="item.code">{{item.description}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item
                        label="排"
                        name="seatRow"
                >
                    <a-input v-model:value="trainSeat.seatRow"/>
                </a-form-item>
                <a-form-item
                        label="列"
                        name="seatCol"
                >
                    <a-select v-model:value="trainSeat.seatCol">
                        <a-select-option v-for="item in trainSeatCol_type" :value="item.code" :key="item.code">{{item.description}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item
                        label="同车厢座序"
                        name="columnCount"
                >
                    <a-input v-model:value="trainSeat.carriageSeatIndex"/>
                </a-form-item>
                <a-form-item>
                    <a-popconfirm placement="topLeft" ok-text="Yes" cancel-text="No" @confirm="confirm">
                        <template #title>
                            are you sure to add this trainSeat?
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
const routeParams = ref(route.params)
const trainSeat_type = ref([])
const trainSeatCol_type = ref([])
const open = ref(false);
const loading = ref(false);
const trainSeat = reactive({
    id:  ``,
    trainId:  routeParams.value.trainId,
    carriageId:  ``,
    seatType:  ``,
    seatRow:  ``,
    seatCol:  `C`,
    carriageSeatIndex:  ``,
    createTime:  ``,
    updateTime:  ``
})
const showModal = () => {
    open.value = true
};
const confirm = async () => {
    let res = await doPost('/business/trainSeat/admin/save', trainSeat);
    if (res) {
        resetTrainSeat()
        queryTrainSeatList()
        open.value = false;
    }
};
const cancel = () => {
    resetTrainSeat()
    open.value = false;
}

function resetTrainSeat() {
    Object.assign(trainSeat,{
        id:  ``,
        trainId:  routeParams.value.trainId,
        carriageId:  ``,
        seatType:  ``,
        seatRow:  ``,
        seatCol:  `C`,
        carriageSeatIndex:  ``,
        createTime:  ``,
        updateTime:  ``
    })
}

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

const handleEdit = (record) => {
    Object.assign(trainSeat,record)//该方法相当于BeanUtil.copyProperties
    open.value = true
}

const handleDelete = (record)=>{
    axios.delete('/business/trainSeat/admin/delete/'+record.id)
        .then(resp => {
            if (resp) {
                const data = resp.data
                if (data.success) {
                    info('success', data.message)
                    queryTrainSeatList()
                }
                if (data.success === false) {
                    info('error', data.message)
                }
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