import axios from "axios";




function configAxios() {
    axios.defaults.baseURL = process.env.VUE_APP_URL
    axios.interceptors.request.use(config => {
        console.log("请求参数: ", config);
        return config;
    }, err => {
        Promise.reject(err)
    })
    axios.interceptors.response.use(resp => {
        console.log("响应参数: ", resp);
        return resp;
    }, err => {
            Promise.reject(err)
    })
}

export {
    configAxios
}