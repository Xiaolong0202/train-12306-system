import axios from "axios";
import {info} from "@/util/info";

const doPost = async (url, data) => {
    let res = false;
    await axios.post(url, data) //使用await关键字将请求变成同步步
        .then(resp => {
            if (resp) {
                const data = resp.data
                if (data.success) {
                    info('success', data.message)
                    res = true
                }
                if (data.success === false) {
                    info('error', data.message)
                }
            }
        })
    return res
}
export {
    doPost
}