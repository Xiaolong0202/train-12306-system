<template>
    <div style="float: left;">
        <span class="ticketInfo">{{ ticketInfo.train.startDate }}</span>&nbsp;
        <span class="ticketInfo">{{ticketInfo.train.type}}{{ticketInfo.train.code}}</span>次&nbsp;
        <span class="ticketInfo">{{ticketInfo.start}}</span>站&nbsp;
        <span class="ticketInfo">({{ticketInfo.startTime}}开)</span>站&nbsp;
        ——
        <span class="ticketInfo">{{ticketInfo.end}}</span>&nbsp;
        <span class="ticketInfo">({{ticketInfo.startTime}}到)</span>&nbsp;
        <hr>
    </div>




</template>

<script setup>
import {SESSION_ORDER} from "@/constant/SessionStorageKeys";

const ticketInfo = JSON.parse(sessionStorage.getItem(SESSION_ORDER) || '{}')
</script>

<style scoped>
    .ticketInfo{
        font-size: 30px;
        font-weight: bold;
    }
</style>