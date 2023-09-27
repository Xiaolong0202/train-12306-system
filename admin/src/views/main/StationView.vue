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
                      @change="queryStationList"
        />
        <a-modal v-model:open="open"
                 title="enter station info"
                 :closable="false"
                 :mask="true"
                 :maskClosable="false"
                 :footer="[]"
        >
            <a-form
                    :model="station"
                    name="station"
                    autocomplete="off"
            >
                <a-form-item
                        label="车站"
                        name="name"
                >
                    <a-input v-model:value="station.name"/>
                </a-form-item>
                <a-form-item
                        label="车站拼音"
                        name="namePinyin"
                >
                    <a-input v-model:value="station.namePinyin" disabled/>
                </a-form-item>
                <a-form-item
                        label="车站拼音首字母缩写"
                        name="namePy"
                >
                    <a-input v-model:value="station.namePy" disabled/>
                </a-form-item>
                <a-form-item>
                    <a-popconfirm placement="topLeft" ok-text="Yes" cancel-text="No" @confirm="confirm">
                        <template #title>
                            are you sure to add this station?
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

const station = reactive({
    id: '',
    name: '',
    namePinyin: '',
    namePy: '',
    createTime: '',
    updateTime: '',
})


const open = ref(false);
const loading = ref(false);
const showModal = () => {
    open.value = true
};
const confirm = async () => {
    let res = await doPost('/business/station/admin/save', station);
    if (res) {
        resetStation()
        queryStationList()
        open.value = false;
    }
};
const cancel = () => {
    resetStation()
    open.value = false;
}

function resetStation() {
    Object.assign(station,{
        id: '',
        name: '',
        namePinyin: '',
        namePy: '',
        createTime: '',
        updateTime: '',
    })
}

const dataSource = ref([])
const columns = [
    {
        title: '车站名',
        dataIndex: 'name',
        key: 'name',
    },
    {
        title: '车站拼音',
        dataIndex: 'namePinyin',
        key: 'namePinyin',
    },
    {
        title: '车站拼音首字母',
        dataIndex: 'namePy',
        key: 'namePy',
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
const queryStationList = () => {
    loading.value = true
    axios.get("/business/station/admin/query-list?currentPage=" + pagination.current + "&pageSize=" + pagination.pageSize)
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
    Object.assign(station,record)//该方法相当于BeanUtil.copyProperties
    open.value = true
}

const handleDelete = (record)=>{
    axios.delete('/business/station/admin/delete/'+record.id)
        .then(resp => {
            if (resp) {
                const data = resp.data
                if (data.success) {
                    info('success', data.message)
                    queryStationList()
                }
                if (data.success === false) {
                    info('error', data.message)
                }
            }
        })
}

watch(()=>station.name,()=>{
    station.namePinyin = pinyin(station.name,{toneType: 'none'}).replace(" ",'')
    station.namePy = pinyin(station.name,{pattern:'first',toneType: 'none'}).replace(" ",'')
})

onMounted(() => {
    queryStationList()
})


</script>
<style scoped>

</style>