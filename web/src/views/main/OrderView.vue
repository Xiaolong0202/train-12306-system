<template>
    <div style="float: left;">
        <span class="ticketInfo">{{ ticketInfo.dailyTrain.startDate }}</span>&nbsp;
        <span class="ticketInfo">{{ ticketInfo.dailyTrain.type }}{{ ticketInfo.dailyTrain.code }}</span>次&nbsp;
        <span class="ticketInfo">{{ ticketInfo.start }}</span>站&nbsp;
        <span class="ticketInfo">({{ ticketInfo.startTime }}开)</span>站&nbsp;
        ——
        <span class="ticketInfo">{{ ticketInfo.end }}</span>&nbsp;
        <span class="ticketInfo">({{ ticketInfo.startTime }}到)</span>&nbsp;
        <span style="font-weight: bold">(间隔：{{ ticketInfo.dailyTrain.intervalDay }}天)</span>
        <br><br>
        <div style="float: left">
            <template v-for="item in seatType" :key="item.code">
                <span v-if="ticketInfo[item.enumName]>=0">
                {{ item.description }}: <span class="ticketSell">{{
                    ticketInfo[item.enumName + 'Price']
                    }}￥  &nbsp;{{ ticketInfo[item.enumName] }}</span>&nbsp;张票
                </span>
            </template>
            <br><br><br>
        </div>
    </div>
    <br>
    <a-divider/>
    <a-checkbox-group style="float: left" v-model:value="chosePassengers" name="checkboxgroup"
                      :options="passengerOptions"/>
    <a-divider/>
    <div id="card-container">
        <a-row class="tickets-row" style=" background-color: dodgerblue;color: white;" v-show="tickets.length>0">
            <a-col :span="3">姓名</a-col>
            <a-col :span="9">身份证</a-col>
            <a-col :span="6">乘客类型</a-col>
            <a-col :span="6">座位类型</a-col>
        </a-row>
        <a-row class="tickets-row" v-for="item in tickets" :key="item.idCard">
            <a-col :span="3" style="text-align: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                {{ item.name }}
            </a-col>
            <a-col :span="9">{{ item.idCard }}</a-col>
            <a-col :span="6">
                <a-select v-model:value="item.passengerType">-->
                    <a-select-option v-for="p in passengerType" :value="p.code" :key="p.code">{{ p.description }}
                    </a-select-option>
                </a-select>
            </a-col>
            <a-col :span="6">
                <a-select v-model:value="item.seatType" placeholder="请选择席位类型">
                    <template v-for="s in seatType" :key="s.code">
                        <a-select-option v-if="ticketInfo[s.enumName]>=0" :value="s.code">{{ s.description }}
                        </a-select-option>
                    </template>
                </a-select>
            </a-col>
        </a-row>
        <br><br><br><br><br>
        <a-button style="float: left" v-if="tickets.length>0" @click="handleSubmit">提交订单</a-button>

        <a-modal
                width="500"
                title="请确认车票信息如下信息"
                v-model:open="chooseSeatVisible"
                :closable="false"
                :mask="true"
                :maskClosable="false"
                @ok="handleOpenCaptcha"
                @cancel="chooseSeatVisible=false">
            <a-row class="tickets-row" style=" background-color: dodgerblue;color: white;" v-show="tickets.length>0">
                <a-col :span="3">姓名</a-col>
                <a-col :span="9">身份证</a-col>
                <a-col :span="6">乘客类型</a-col>
                <a-col :span="6">座位类型</a-col>
            </a-row>
            <a-row class="tickets-row" v-for="item in tickets" :key="item.idCard">
                <a-col :span="3" style="text-align: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    {{ item.name }}
                </a-col>
                <a-col :span="9">{{ item.idCard }}</a-col>
                <a-col :span="6">
                    <template v-for="p in passengerType" :key="p.code">
                        <span v-if="p.code===item.passengerType">
                            {{ p.description }}
                        </span>
                    </template>
                </a-col>
                <a-col :span="6">
                    <template v-for="s in seatType" :key="s.code">
                        <span v-if="s.code===item.seatType">
                            {{ s.description }}
                        </span>
                    </template>
                </a-col>
            </a-row>
            <br> <br>
            <div v-if="chooseSeatType === 0" style="color: red">
                您购买的车票不支持选座<br>
                <div>只有当所有乘客选择的都是一等座或二等座才可以支持选座</div>
                <div>选中的座位类型余票数大于20张时才支持选座</div>
            </div>
            <div v-else id="chooseSeat" style="text-align: center">
                <p style="color: red">请选择您的座位</p>
                <a-switch v-for="item in SEAT_COL_ARR" :key="item.code"
                          v-model:checked="chooseSeatObj[item.description + 1].choose"
                          :checked-children="item.description + 1"
                          :un-checked-children="item.description + 1"/>
                <!--如果购票的人数大于1则渲染两排-->
                <template v-if="tickets.length>1">
                    <br>
                    <a-switch v-for="item in SEAT_COL_ARR" :key="item.code"
                              v-model:checked="chooseSeatObj[item.description + 2].choose"
                              :checked-children="item.description + 2"
                              :un-checked-children="item.description + 2"/>
                </template>
                <br>
                <span style="color: grey">提示：您可以选择{{ tickets.length }}个座位</span>
            </div>
        </a-modal>

        <a-modal
                title="输入验证码开始确认订单"
                v-model:open="captchaVisible"
                :closable="false"
                :mask="true"
                :maskClosable="false"
                @ok="verifyCodeAndOrder"
                @cancel="captchaVisible=false;Object.assign(captchaInfo,{captchaToken: null,captchaCodeImgURL: null,captchaCode: null})">
            <p>
                <a-input
                        v-model:value="captchaInfo.captchaCode"
                        size="large"
                        placeholder="请输入验证码">
                    <template #addonAfter v-if="!!captchaInfo.captchaCodeImgURL">
                        <a-image
                                :width="90"
                                @click="reloadCaptcha"
                                :src="captchaInfo.captchaCodeImgURL"
                        />
                    </template>
                </a-input>
            </p>
        </a-modal>

        <a-modal
                :footer="[]"
                title="输入验证码开始确认订单"
                v-model:open="queryOrderQueueModalVisible"
                :closable="false"
                :mask="true"
                :maskClosable="false">
            <p>
                <LoadingOutlined/>
                正在查询{{ confirmOrderId }}的订单情况
            </p>
            <p>
                你的前面大概还有{{ estimatedQueSize }}个人
            </p>
            <p>
                <a-button
                    @click="cancelOrderQue"
                    danger>
                    取消排队
                </a-button>
            </p>
        </a-modal>
    </div>
</template>

<script setup>
import {LoadingOutlined} from "@ant-design/icons-vue";
import {SESSION_ORDER} from "@/constant/SessionStorageKeys";
import {computed, onMounted, reactive, ref, watch} from "vue";
import axios from "axios";
import {generateUUID, info, isEmpty} from "@/util/info";

const ticketInfo = JSON.parse(sessionStorage.getItem(SESSION_ORDER) || '{}')

const passengers = ref([])
const passengerOptions = ref([])
const chosePassengers = ref([])
const tickets = ref([])

const seatType = ref([])
const passengerType = ref([])
const seatColType = ref([])

const confirmOrderId = ref(null)
const queryOrderQueueModalVisible = ref(false)
const estimatedQueSize = ref(99999)

const chooseSeatVisible = ref(false)

//0 不支持选座 1选 一等座 2 选二等座
const chooseSeatType = ref(0)

//计算选座类型
const SEAT_COL_ARR = computed(() => {
    return seatColType.value.filter(col => String(col.type) === String(chooseSeatType.value))
})

const captchaInfo = reactive({
    captchaToken: null,
    captchaCodeImgURL: null,
    captchaCode: null
})
const captchaVisible = ref(false)

const reloadCaptcha = () => {
    captchaInfo.captchaToken = generateUUID()
    captchaInfo.captchaCodeImgURL = process.env.VUE_APP_URL + '/business/captcha/' + captchaInfo.captchaToken
}
const handleOpenCaptcha = () => {
    reloadCaptcha()
    captchaVisible.value = true
}
/**选择的座位:
 *{
 *   A1  C1 D1 F1
 *   A2  C2 D2 D3
 *}
 * tickets结构：
 * 【{
 *   passengerId: item.id,
 *   name: item.name,
 *   idCard: item.idCard,
 *   passengerType: item.type,
 *   seatType: null,
 *   seat: null
 *}】
 *
 *
 */
const chooseSeatObj = ref({})

watch(() => SEAT_COL_ARR.value, () => {
    chooseSeatObj.value = {}
    for (let i = 1; i <= 2; i++) {
        SEAT_COL_ARR.value.forEach(item => {
            chooseSeatObj.value[item.description + i] = {}
            Object.assign(chooseSeatObj.value[item.description + i], item)
            chooseSeatObj.value[item.description + i].choose = false
            chooseSeatObj.value[item.description + i].code += String(i)
        })
    }
    console.log("初始化两排座位")
}, {immediate: true})


const getPassengers = () => {
    axios.get('/member/passenger/query-list')
        .then(res => {
            if (res) {
                if (res.data.success) {
                    passengerOptions.value = []
                    passengers.value = res.data.content.list
                    passengers.value.forEach(passenger => {
                        passengerOptions.value.push({
                            value: passenger,
                            label: passenger.name
                        })
                    })
                } else {
                    info('error', res.data.message)
                }
            }
        })
}

const getSeatType = () => {
    axios.get('/business/get-enum/business.enums.Seat')
        .then(res => {
            if (res) {
                seatType.value = res.data
                seatType.value.forEach(item => {
                    item.enumName = item.enumName.toLowerCase()
                })
            }
        })
}

const getSeatColType = () => {
    axios.get('/business/get-enum/business.enums.SeatCol')
        .then(res => {
            if (res) {
                seatColType.value = res.data
            }
        })
}

const getPassengerType = () => {
    axios.get('/member/get-enum/member.enums.Passenger')
        .then(res => {
            if (res) {
                passengerType.value = res.data
            }
        })
}

function periodicQueryOrderQue(orderId) {
    let timer
    timer = setInterval(()=>{
        axios.get('/business/confirmOrder/query-order-queue-status/' + orderId)
            .then(resp => {
                if (resp) {
                    if (resp.data.success) {
                        let res = resp.data.content
                        switch (res) {
                            case -1:
                                info('success','下单选座成功')
                                clearInterval(timer)
                                queryOrderQueueModalVisible.value = false
                                estimatedQueSize.value = 9999
                                confirmOrderId.value = null
                                break
                            case -2:
                                info('error','下单的时候出现了异常,选座失败')
                                clearInterval(timer)
                                queryOrderQueueModalVisible.value = false
                                estimatedQueSize.value = 9999
                                confirmOrderId.value = null
                                break
                            case -3:
                                info('error','已经没有空座了')
                                clearInterval(timer)
                                queryOrderQueueModalVisible.value = false
                                estimatedQueSize.value = 9999
                                confirmOrderId.value = null
                                break
                            case -4:
                                info('error','订单被取消')
                                clearInterval(timer)
                                queryOrderQueueModalVisible.value = false
                                estimatedQueSize.value = 9999
                                confirmOrderId.value = null
                                break
                            default:
                                estimatedQueSize.value = res
                        }
                    }
                }
            })
    },500)//每隔半秒查询一次订单状况
}

const cancelOrderQue = ()=>{
    axios.put('/business/confirmOrder/cancel/'+confirmOrderId.value)
        .then(resp=>{
            if (resp){
                if (resp.data.success){
                    if (resp.data.content>0){
                        info('success','排队取消成功')
                    }else {
                        info('error','排队取消失败,订单可能已经正在处理当中或者处理完成')
                    }
                }else {
                    info('error','排队取消失败,订单可能已经正在处理当中或者处理完成')
                }

            }
        })
}

const verifyCodeAndOrder = () => {
    if (isEmpty(captchaInfo.captchaCode)) {
        info('error', "验证码不能为空!")
        return
    }

    //设置每张票的座位
    //先清空Tickets中的 seat
    for (let i = 0; i < tickets.value.length; i++) {
        tickets.value[i].seat = null
    }
    let i = -1
    for (let obj in chooseSeatObj.value) {
        if (chooseSeatObj.value[obj].choose) {
            i++
            if (i > tickets.value.length - 1) {
                info('error', "所选座位数大于票数！")
                return;
            }
            tickets.value[i].seat = chooseSeatObj.value[obj].code
        }
    }
    if (i > -1 && i < tickets.value.length - 1) {
        info('error', "所选座位数小于票数！")
        return;
    }


    if (tickets.value.length > 5) {
        info('error', '最多只能购买五张票')
        return
    }

    //添加校验余票
    for (let i = 0; i < seatType.value.length; i++) {
        let seatTypeCount = 0;
        if (ticketInfo[seatType.value[i].enumName] >= 0) {
            tickets.value.forEach(ticket => {
                if (ticket.seatType === seatType.value[i].code) {
                    seatTypeCount++;
                }
            })
            if (seatTypeCount > ticketInfo[seatType.value[i].enumName]) {
                info('error', seatType.value[i].description + '已经没有票了')
                return;
            }
        }
    }

    axios.post('/business/confirmOrder/do', {
        date: ticketInfo.startDate,
        trainCode: ticketInfo.dailyTrain.code,
        start: ticketInfo.start,
        end: ticketInfo.end,
        dailyTrainTicketId: ticketInfo.id,
        tickets: tickets.value,
        captchaToken: captchaInfo.captchaToken,
        captchaCode: captchaInfo.captchaCode
    }).then(res => {
        if (res) {
            if (res.data.success) {
                info('success', res.data.message)
                confirmOrderId.value = res.data.content
                captchaVisible.value = false
                chooseSeatVisible.value = false
                queryOrderQueueModalVisible.value = true
                Object.assign(captchaInfo, {captchaToken: null, captchaCodeImgURL: null, captchaCode: null})
                periodicQueryOrderQue(confirmOrderId.value)
            } else {
                info('error', res.data.message)
            }
        }
    })
}

const handleSubmit = () => {

    for (let valueElement of tickets.value) {
        if (!valueElement.seatType) {
            info('error', '请给乘客选座席位类型')
            return;
        }
    }

    //判断是否支持选座，已经是选择一等座还是二等座
    let ticketsSeatTypeSet = new Set()
    tickets.value.forEach(item => {
        ticketsSeatTypeSet.add(String(item.seatType))
    })

    if (ticketsSeatTypeSet.size !== 1) {
        console.log('不支持选座')
        chooseSeatType.value = 0;//不支持选座
    } else if (ticketsSeatTypeSet.has('1')) {
        console.log('选 一等座')
        chooseSeatType.value = 1
    } else if (ticketsSeatTypeSet.has('2')) {
        console.log('选二等座')
        chooseSeatType.value = 2
    } else {
        console.log('不支持选座')
        chooseSeatType.value = 0
    }

    if (chooseSeatType.value !== 0) {
        for (let valueElement of seatType.value) {
            if (String(valueElement.code) === String(chooseSeatType.value)) {
                if (ticketInfo[valueElement.enumName] <= 20) {
                    console.log("不支持选座")
                    chooseSeatType.value = 0
                    break
                }
            }
        }
    }


    chooseSeatVisible.value = true
}

watch(() => chosePassengers.value, () => {
    tickets.value = []
    chosePassengers.value.forEach(item => {
        tickets.value.push({
            passengerId: item.id,
            name: item.name,
            idCard: item.idCard,
            passengerType: item.type,
            seatType: null,
            seat: null
        })
    })
})

onMounted(() => {
    getPassengers()
    getPassengerType()
    getSeatColType()
    getSeatType()
})

</script>

<style scoped>
.ticketInfo {
    font-size: 30px;
    font-weight: bold;
}

.ticketSell {
    font-size: 20px;
    font-weight: bolder;
    color: red;
}

.tickets-row {
    font-weight: bold;
    min-height: 4vh;
    text-align: center;
}

.tickets-row > * {
    line-height: 4vh;
}

#chooseSeat > * {
    text-align: center;
    margin: 10px;
}

</style>