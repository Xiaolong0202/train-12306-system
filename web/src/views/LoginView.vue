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
                            :loading="codeButtonLoading"
                            v-model:value="formState.code"
                            placeholder="请输入验证码"
                            :enter-button="codeButtonVal"
                            size="large"
                            @search="onMessage"
                    />
                </a-form-item>

                <a-form-item>
                    <a-button type="primary" @click="login" size="large" :loading="loginButtonLoading">登录</a-button>
                </a-form-item>
            </a-form>
        </a-col>
    </a-row>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {info} from "@/util/info";
import {useRouter} from "vue-router";
import store from "@/store";

const codeButtonVal = ref("发送验证码")
const codeButtonLoading = ref(false)
const loginButtonLoading = ref(false)
const router = useRouter()

const formState = reactive({
    mobile: '',
    code: '',
});
const login = () => {
    loginButtonLoading.value = true
    axios.post("/member/login", formState).then(resp => {
        if (resp) {
            let type = 'success'
            if (!resp.data.success) {
                type = 'error'
            }
            info(type, resp.data.message)
            if (resp.data.success) {
                store.commit('setMember', resp.data.content)
                router.push("/main")
            }
            loginButtonLoading.value = false
        }
    })
}
const onMessage = () => {
    // codeButtonLoading.value = true
    // let startTime = new Date().getTime();
    // setTimeout(()=>{
    //     codeButtonLoading.value = false
    //     codeButtonVal.value = "发送验证码"
    // },60*1000+100)
    // let interval = setInterval(function(){
    //     codeButtonVal.value = '请等待'+Math.floor((60-(new Date().getTime() - startTime)/1000))+'秒之后再发送验证码'
    //     if(new Date().getTime() - startTime > 60000){
    //         clearInterval(interval);
    //     }
//do whatever here..
//     }, 1000);
    axios.post("/member/send-code", formState).then(resp => {
        if (resp) {
            let type = 'success'
            if (!resp.data.success) {
                type = 'error'
            }
            info(type, resp.data.message)
        }
    })
}

onMounted(() => {
    store.commit("setMember", {})
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
