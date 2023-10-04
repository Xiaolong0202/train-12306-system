<template>
    <div style="float: left;">
        <span class="ticketInfo">{{ ticketInfo.train.startDate }}</span>&nbsp;
        <span class="ticketInfo">{{ ticketInfo.train.type }}{{ ticketInfo.train.code }}</span>次&nbsp;
        <span class="ticketInfo">{{ ticketInfo.start }}</span>站&nbsp;
        <span class="ticketInfo">({{ ticketInfo.startTime }}开)</span>站&nbsp;
        ——
        <span class="ticketInfo">{{ ticketInfo.end }}</span>&nbsp;
        <span class="ticketInfo">({{ ticketInfo.startTime }}到)</span>&nbsp;
        <br><br>
        <div style="float: left">
            <span v-if="ticketInfo.ydz>=0">
                一等座: <span class="ticketSell">{{ ticketInfo.ydzPrice }}￥  &nbsp;{{ ticketInfo.ydz }}</span>&nbsp;张票
            </span>
            <span v-if="ticketInfo.edz>=0">
                二等座: <span class="ticketSell">{{ ticketInfo.edzPrice }}￥  &nbsp;{{ ticketInfo.edz }}</span>&nbsp;张票
            </span>
            <span v-if="ticketInfo.yw>=0">
                硬卧: <span class="ticketSell">{{ ticketInfo.ywPrice }}￥  &nbsp;{{ ticketInfo.yw }}</span>&nbsp;张票
            </span>
            <span v-if="ticketInfo.rw>=0">
                软卧: <span class="ticketSell">{{ ticketInfo.rwPrice }}￥ &nbsp;{{ ticketInfo.rw }}</span>&nbsp;张票
        </span>
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
                <a-select v-model:value="item.type">-->
                    <a-select-option v-for="p in passengerType" :value="p.code" :key="p.code">{{p.description}}</a-select-option>
                </a-select>
            </a-col>
            <a-col :span="6">
                <a-select v-model:value="item.seatType">
                    <a-select-option v-for="s in seatType" :value="s.code" :key="s.code">{{s.description}}</a-select-option>
                </a-select>
            </a-col>
        </a-row>
        <br><br><br><br><br>
        <a-button style="float: left" v-if="tickets.length>0" @click="visible=true">提交订单</a-button>

        <a-modal
            width="500"
            title="请确认车票信息如下信息"
            v-model:open="visible"
            :closable="false"
            :mask="true"
            :maskClosable="false"
            @ok="handleOk"
            @cancel="visible=false">
            <a-row class="tickets-row" style=" background-color: dodgerblue;color: white;" v-show="tickets.length>0">
                <a-col :span="3" >姓名</a-col>
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
                    <template v-for="p in passengerType"  :key="p.code">
                        <span v-if="p.code===item.type">
                            {{p.description}}
                        </span>
                    </template>
                </a-col>
                <a-col :span="6">
                    <template v-for="s in seatType"  :key="s.code">
                        <span v-if="s.code===item.seatType">
                            {{s.description}}
                        </span>
                    </template>
                </a-col>
            </a-row>
        </a-modal>

    </div>
  <!--    {{passengers}}-->
<!--      {{ticketInfo}}-->
  <!--    {{chosePassengers}}-->
<!--      {{tickets}}-->
<!--  {{ seatType }}-->
<!--  {{ passengerType }}-->
</template>

<script setup>
import {SESSION_ORDER} from "@/constant/SessionStorageKeys";
import {onMounted, ref, watch} from "vue";
import axios from "axios";
import {info} from "@/util/info";

const ticketInfo = JSON.parse(sessionStorage.getItem(SESSION_ORDER) || '{}')

const passengers = ref([])
const passengerOptions = ref([])
const chosePassengers = ref([])
const tickets = ref([])

const seatType = ref([])
const passengerType = ref([])

const visible = ref(false)

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

const handleOk = ()=>{
        if (tickets.value.length>5){
            info('error','最多只能购买五张票')
            return
        }
        visible.value = false
}

watch(() => chosePassengers.value, () => {
    tickets.value = []
    chosePassengers.value.forEach(item => {
        tickets.value.push({
            passengerId: item.id,
            name: item.name,
            idCard: item.idCard,
            type: item.type,
            seatType: '1'
        })
    })
})

onMounted(() => {
    getPassengers()
    getPassengerType()
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

.tickets-row > *{
    line-height: 4vh;
}

</style>