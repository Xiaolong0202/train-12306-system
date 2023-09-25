<template>
    <div>
        <div>
            <a-button type="primary" @click="showModal" style="float: left">add</a-button>
        </div>
        <a-table :dataSource="dataSource" :columns="columns" :pagination="false" :loading="loading" style="margin-top: 20px">
            <template #bodyCell="{column , record}">
                <template v-if="column.dataIndex === 'type'">
                    <template v-for="item in passenger_type" :key="item.code">
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
                      @change="queryPassengerList"
        />
        <a-modal v-model:open="open"
                 title="enter passenger info"
                 :closable="false"
                 :mask="true"
                 :maskClosable="false"
                 :footer="[]"
        >
            <a-form
                    :model="passenger"
                    name="passenger"
                    autocomplete="off"
            >
                <a-form-item
                        label="name"
                        name="name"
                >
                    <a-input v-model:value="passenger.name"/>
                </a-form-item>
                <a-form-item
                        label="idCard"
                        name="idCard"
                >
                    <a-input v-model:value="passenger.idCard"/>
                </a-form-item>
                <a-form-item
                        label="typeOfPassenger"
                        name="typeOfPassenger"
                >
                    <a-select v-model:value="passenger.type">
                        <a-select-option v-for="item in passenger_type" :value="item.code" :key="item.code">{{item.description}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item>
                    <a-popconfirm placement="topLeft" ok-text="Yes" cancel-text="No" @confirm="confirm">
                        <template #title>
                            are you sure to add this passenger?
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

const passenger = reactive({
    id: '',
    memberId: '',
    name: '',
    idCard: '',
    type: '1',
    createTime: '',
    updateTime: '',
})


const passenger_type = ref([])
const open = ref(false);
const loading = ref(false);
const showModal = () => {
    open.value = true
};
const confirm = async () => {
    let res = await doPost('/member/passenger/save', passenger);
    if (res) {
        resetPassenger()
        queryPassengerList()
        open.value = false;
    }
};
const cancel = () => {
    resetPassenger()
    open.value = false;
}

function resetPassenger() {
    passenger.name = ''
    passenger.idCard = ''
    passenger.type = '1'
}

const dataSource = ref([])
const columns = [
    {
        title: '姓名',
        dataIndex: 'name',
        key: 'name',
    },
    {
        title: '身份证',
        dataIndex: 'idCard',
        key: 'idCard',
    },
    {
        title: '乘客类型',
        dataIndex: 'type',
        key: 'type',
    },
    {
        title: '操作',
        dataIndex: 'action',
        key: 'action',
    }]
const pagination = reactive({
    total: 0,
    current: 1,
    pageSize: 2,
})
const queryPassengerList = () => {
    loading.value = true
    axios.get("/member/passenger/query-list?currentPage=" + pagination.current + "&pageSize=" + pagination.pageSize)
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

const queryPassengerType = ()=>{
    loading.value = true
    axios.get('/member/get-enum/member.enums.Passenger')
        .then(res =>{
            loading.value = false
            if (res) {
                passenger_type.value = res.data
            }
        })
}

const handleEdit = (record) => {
    Object.assign(passenger,record)//该方法相当于BeanUtil.copyProperties
    open.value = true
}

const handleDelete = (record)=>{
    axios.delete('/member/passenger/delete/'+record.id)
        .then(resp => {
            if (resp) {
                const data = resp.data
                if (data.success) {
                    info('success', data.message)
                    queryPassengerList()
                }
                if (data.success === false) {
                    info('error', data.message)
                }
            }
        })
}

onMounted(() => {
    queryPassengerList()
    queryPassengerType()
})


</script>
<style scoped>

</style>