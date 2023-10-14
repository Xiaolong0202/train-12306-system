<template>
    <div>
        <div>
            <a-button style="float: left" @click="queryTrainTokenList">refresh</a-button>
            <input style="float: left;margin-left: 15px" type="date" v-model="chooseDate">
        </div>
        <a-table :dataSource="dataSource" :columns="columns" :pagination="false" :loading="loading"
                 style="margin-top: 20px">
            <template #bodyCell="{column , record}">
                <template v-if="column.dataIndex === 'trainInfo'">
                    {{ record.type + '' + record.trainCode }}
                </template>
                <template v-if="column.dataIndex === 'action'">
                    <a-button @click="handleSetTokenCount(record)">设置令牌数</a-button>
                </template>
            </template>

        </a-table>
        <a-pagination v-model:current="pagination.current"
                      v-model:page-size="pagination.pageSize"
                      v-model:total="pagination.total"
                      :pageSizeOptions="['2','5','10','15']"
                      :showSizeChanger="true"
                      @change="queryTrainTokenList"
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
                    <a-input v-model:value="trainToken.tokenCount"/>
                </a-form-item>
                <a-form-item>
                    <a-popconfirm placement="topLeft" ok-text="Yes" cancel-text="No" @confirm="confirm">
                        <template #title>
                            are you sure to change?
                        </template>
                        <a-button>save</a-button>
                    </a-popconfirm>
                    <a-button @click="Object.assign(trainToken,{});open=false;" style="margin-left: 10px">cancel</a-button>
                </a-form-item>
            </a-form>
        </a-modal>
    </div>
</template>
<script setup>
import {onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {info} from "@/util/info";


const loading = ref(false);
const trainToken = reactive({
})
const chooseDate = ref(null)
const open = ref(false)


const dataSource = ref([])
const columns = [
    {
        title: '每日车次id',
        dataIndex: 'dailyTrainId',
        key: 'dailyTrainId',
    },
    {
        title: '火车号',
        dataIndex: 'trainInfo',
        key: 'trainInfo',
    },
    {
        title: '发车日期',
        dataIndex: 'startDate',
        key: 'startDate',
    },
    {
        title: '令牌余量',
        dataIndex: 'tokenCount',
        key: 'tokenCount',
    },
    {
        title: "action",
        dataIndex: 'action',
        key: 'action'
    }
]
const pagination = reactive({
    total: 0,
    current: 1,
    pageSize: 2,
})
const queryTrainTokenList = () => {
    loading.value = true
    axios.get("/business/trainToken/admin/query-list",
        {
            params: {
                currentPage: pagination.current,
                pageSize: pagination.pageSize,
                dailyTrainId: null,
                startDate: chooseDate.value
            }
        })
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

const confirm = () => {
    axios.put("/business/trainToken/admin/updateTokenCount", {
        id: trainToken.id,
        tokenCount: trainToken.tokenCount
    }).then(res => {
        if (res) {
            if (res.data.success) {
                info('success', res.data.message)
                open.value = false
                queryTrainTokenList()
            } else {
                info('error', res.data.message)
            }
        }
    })
}
const handleSetTokenCount = (record) => {
    open.value = true;
    Object.assign(trainToken,record)
}


onMounted(() => {
    queryTrainTokenList()
})


</script>
<style scoped>

</style>