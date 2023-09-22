import axios from "axios";
import store from "@/store";
import {openNotificationWithIcon} from "@/util/info";
import router from "@/router";



function configAxios() {
    axios.defaults.baseURL = process.env.VUE_APP_URL
    axios.interceptors.request.use(config => {
        const token = store.state.member.token
        if (token) {
            config.headers.token = token;
        }
        console.log("请求参数: ", config);
        return config;
    }, err => {
        Promise.reject(err)
    })
    axios.interceptors.response.use(resp => {
        console.log("响应参数: ", resp);
        return resp;
    }, err => {
        if (err.response.status === 401) {
            console.log("未登录或登录超时，跳转到登录页面")
            store.commit("setMember", {})
            openNotificationWithIcon("error","未登录或登录超时，跳转到登录页面")
            router.push('/login')
        }else {
            Promise.reject(err)
        }
    })
}

export {
    configAxios
}