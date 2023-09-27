<template>
    <a-select
            v-model:value="stationName"
            show-search
            placeholder="Select a person"
            style="width: 200px"
            :options="options"
            :filter-option="filterOption"
            @change="handleChange"
    ></a-select>
</template>



<script setup>
import {onMounted, ref, watch} from 'vue';
import axios from "axios";

const props = defineProps({
    stationName:{
        type: String,
        default:null
    }
})
const emit = defineEmits(['update:stationName'])


const options = ref([]);
const handleChange = value => {
    emit('update:stationName',value)
};

const filterOption = (input, option) => {
    return option.value.toLowerCase().indexOf(input.toLowerCase()) >= 0;
};
const stationName = ref(undefined);

const getStations = ()=>{
    axios.get('/business/station/admin/query-list')
        .then(res=>{
            if (res){
                if (res.data.success){
                    options.value = []
                    for (let i = 0; i < res.data.content.list.length; i++) {
                        let stat = res.data.content.list[i];
                        options.value.push({
                            value: stat.name,
                            label: stat.name
                        })
                    }
                }
            }
        })
}
watch(()=>props.stationName,()=>{
    stationName.value = props.stationName
})
onMounted(()=>{
    getStations()
})
</script>

<style scoped>

</style>