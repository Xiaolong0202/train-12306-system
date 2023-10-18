<template>
    <template v-if="!route.query.dailyTrainId">
        <h2 style="float: left">请去车票查询界面中选择你想要的车次,跳转至
            <router-link to="/main/ticket">车票查询界面</router-link>
        </h2>
    </template>
    <template v-else>
        <div>
            <h3 style="float: left">{{
                '日期：' + route.query.startDate +
                '  车次：' + route.query.dailyTrainType + route.query.dailyTrainCode +
                '  出发站：' + route.query.start +
                '  到达站：' + route.query.end
                }}</h3>
        </div>
        <a-divider :dashed="true"/>
        <table>
            <tr>
                <td style="width: 25px; background-color: #F27A59"></td>
                <td>已被购买</td>
                <td style="width: 25px; background-color: #717172"></td>
                <td>未被购买</td>
            </tr>
        </table>
        <a-divider :dashed="true"/>
        <template v-for="key in Object.keys(carriageAndSeats)" :key="key">
            <span  style="float: left;text-align: left;font-weight: bolder">{{key+' | '+carriageAndSeats[key][keyCarriageType]}}</span>
            <br>
            <table>
                <template v-for="seatSellArrKey in Object.keys(carriageAndSeats[key])" :key="seatSellArrKey">
                    <template v-if="carriageAndSeats[key][seatSellArrKey] instanceof Array">
                        <tr>
                            <td style="width: 25px">{{seatSellArrKey}}</td>
                            <template v-for="(seatSell,index) in carriageAndSeats[key][seatSellArrKey]" v-bind:key="index">
                                <template v-if="seatSell>0" >
                                    <!--大于0表示被出售-->
                                    <td style=" height:25px; width: 25px; background-color: #F27A59"/>
                                </template>
                                <template v-else>
                                    <td style="height:25px; width: 25px; background-color: #717172"/>
                                </template>
                            </template>
                        </tr>
                    </template>

                </template>
            </table>
            <a-divider :dashed="true"/>
        </template>




    </template>
<!--    <div>{{ route.query }}</div>-->
  <!--  <div>{{allSeats}}</div>-->
<!--    <div>{{ carriageAndSeats }}</div>-->
</template>

<script setup>
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {info} from '@/util/info'
import axios from "axios";

const keyCarriageType = 'keyCarriageType'
const route = useRoute()

const allSeats = ref([])
const carriageAndSeats = ref({})
const queryAllSeat = () => {
    axios.get('/business/leftSeat/' + route.query.dailyTrainId)
        .then(resp => {
            if (resp && resp.data.success) {
                allSeats.value = resp.data.content
                classifySeats()
            } else {
                info('error', resp.data.message)
            }
        })
}

const classifySeats = () => {
    for (let seat of allSeats.value) {
        const keyCarriage = '车厢：' + String(seat.carriageIndex)
        const keySeat = String(seat.seatCol.slice(-1))
        if (!carriageAndSeats.value[keyCarriage]) {
            carriageAndSeats.value[keyCarriage] = {}
        }
        if (!carriageAndSeats.value[keyCarriage][keySeat]) {
            carriageAndSeats.value[keyCarriage][keySeat] = []
        }
        if (!carriageAndSeats.value[keyCarriage][keyCarriageType]) {
            carriageAndSeats.value[keyCarriage][keyCarriageType] = '车厢类型：' + String(seat.seatCol.slice(0, -2))
        }
        let sellOfTicket = seat.seatSell.substring(route.query.startIndex, route.query.endIndex);
        carriageAndSeats.value[keyCarriage][keySeat].push(parseInt(sellOfTicket))
    }

    let message = Object.keys(carriageAndSeats.value);
    console.log(message);
    message.forEach(ket=>{
        console.log(carriageAndSeats.value[ket])
    })
}

onMounted(() => {
    if (route.query.dailyTrainId) {
        queryAllSeat()
    }
})
</script>
<style>
    td{
       border: white solid;
    }
</style>
