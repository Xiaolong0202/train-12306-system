<template>
    <a-row>
        <a-col :span="8" :offset="8" class="login-main">
            <a-form
                    :model="formState"
                    autocomplete="off"
                    @finish="onFinish"
                    @finishFailed="onFinishFailed"
            >

                <a-form-item>
                    <h1 style="text-align: center">
                        <ThunderboltTwoTone/>&nbsp;售票系统
                    </h1>
                </a-form-item>
                <a-form-item
                        :rules="[{ required: true, message: '请输入手机号!' }]"
                >
                    <a-input v-model:value="formState.mobile" placeholder="请输入手机号" size="large"/>
                </a-form-item>

                <a-form-item
                        :rules="[{ required: true, message: '请输入验证码!' }]"
                >
                    <a-input-search
                            v-model:value="formState.code"
                            placeholder="请输入验证码"
                            enter-button="发送验证码"
                            size="large"
                            @search="onMessage"
                    />
                </a-form-item>

                <a-form-item>
                    <a-button type="primary" @click="login" size="large">登录</a-button>
                </a-form-item>
            </a-form>
        </a-col>
    </a-row>
</template>

<script setup>
import {onMounted, reactive} from 'vue';
import axios from "axios";
import {info} from "@/util/info";
import {useRouter} from "vue-router";
import store from "@/store";


const router = useRouter()

const formState = reactive({
    mobile: '',
    code: '',
});
const login = () => {
    axios.post("/member/login", formState).then(resp => {
        if (resp){
            let type = 'success'
            if (!resp.data.success) {
                type = 'error'
            }
            info(type, resp.data.message)
            if (resp.data.success) {
                store.commit('setMember',resp.data.content)
                router.push("/main")
            }
        }
    })
}
const onMessage = () => {
    axios.post("/member/send-code", formState).then(resp => {
        if (resp){
            let type = 'success'
            if (!resp.data.success) {
                type = 'error'
            }
            info(type, resp.data.message)
        }
    })
}

onMounted(()=>{
    store.commit("setMember",{})
})
</script>

<style scoped>
.login-main {
    margin-top: 15%;
    padding: 30px;
    border: 2px grey solid;
    border-radius: 10px;
    background-color: #f5fff5;
    width: 80%;
}
</style>
