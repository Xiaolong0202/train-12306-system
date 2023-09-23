<template>
    <div>
        <a-button type="primary" @click="showModal">add</a-button>
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
                        <a-select-option value="1">成人</a-select-option>
                        <a-select-option value="2">学生</a-select-option>
                        <a-select-option value="3">孩子</a-select-option>
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
import {reactive, ref} from 'vue';
import {doPost} from "@/util/axiosUtil";

const passenger = reactive({
    id: '',
    memberId: '',
    name: '',
    idCard: '',
    type: '1',
    createTime: '',
    updateTime: '',
})

const open = ref(false);
const showModal = () => {
    open.value = true
};
const confirm = async () => {
    let res = await doPost('/member/passenger/save', passenger);
    if (res) {
        resetPassenger()
        open.value = false;
    }
};
const cancel = ()=>{
    resetPassenger()
    open.value = false;
}
function resetPassenger(){
    passenger.name=''
    passenger.idCard=''
    passenger.type='1'
}
</script>
<style scoped>

</style>