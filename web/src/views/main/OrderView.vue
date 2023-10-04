<template>
    <div style="float: left;">
        <span class="ticketInfo">{{ ticketInfo.train.startDate }}</span>&nbsp;
        <span class="ticketInfo">{{ ticketInfo.train.type }}{{ ticketInfo.train.code }}</span>次&nbsp;
        <span class="ticketInfo">{{ ticketInfo.start }}</span>站&nbsp;
        <span class="ticketInfo">({{ ticketInfo.startTime }}开)</span>站&nbsp;
        ——
        <span class="ticketInfo">{{ ticketInfo.end }}</span>&nbsp;
        <span class="ticketInfo">({{ ticketInfo.startTime }}到)</span>&nbsp;
        <hr>
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
        </div>
    </div>
    <br>

    <a-divider/>
    <a-checkbox-group v-model:value="chosePassengers" name="checkboxgroup" :options="passengerOptions" />
<!--    {{passengers}}-->
  <!--    {{ticketInfo}}-->
    {{chosePassengers}}
    {{tickets}}


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

const getPassengers = ()=>{
    axios.get('/member/passenger/query-list')
        .then(res=>{
            if (res){
                if (res.data.success){
                    passengerOptions.value = []
                    passengers.value = res.data.content.list
                    passengers.value.forEach(passenger=>{
                        passengerOptions.value.push({
                            value: passenger,
                            label: passenger.name
                        })
                    })
                }else {
                    info('error',res.data.message)
                }
            }
        })
}

watch(()=>chosePassengers.value,()=>{
    tickets.value = []
    chosePassengers.value.forEach(item =>{
        tickets.value.push({
            name: item.name,
            idCard: item.idCard,
            type: item.type,
            seatType: '1'
        })
    })
})

onMounted(()=>{
    getPassengers()
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
</style>