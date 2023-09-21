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
import {reactive} from 'vue';
import axios from "axios";
import {openNotificationWithIcon} from "@/util/info";
import {useRouter} from "vue-router";


const router = useRouter()

const formState = reactive({
    mobile: '',
    code: '',
});
const login = () => {
    axios.post("/member/login", formState).then(resp => {
        let type = 'success'
        if (!resp.data.success) {
            type = 'error'
        }
        openNotificationWithIcon(type, resp.data.message)
        if (resp.data.success) {
            router.push("/")
        }
    })
}
const onMessage = () => {
    axios.post("/member/send-code", formState).then(resp => {
        let type = 'success'
        if (!resp.data.success) {
            type = 'error'
        }
        openNotificationWithIcon(type, resp.data.message)
    })

}
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
