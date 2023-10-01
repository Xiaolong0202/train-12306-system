

<template>
    <a-select
            v-model:value="code"
            show-search
            placeholder="Select a train"
            style="width: 200px"
            :options="options"
            :filter-option="filterOption"
            @change="handleChange"
    ></a-select>
</template>

<script setup>
import axios from "axios";
import {onMounted, ref} from "vue";

const props = defineProps({
    trainCode:{
        type: String,
        default:null
    }
})

const emit = defineEmits(['update:trainCode','onChange'])

const code = ref(props.trainCode)
const options= ref([])
const filterOption = (input, option) => {
    return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
};


const handleChange = ()=>{
    emit('update:trainCode',code.value)
    emit('onChange',trainList.value.filter(tra=>tra.code===code.value)[0])
}

const trainList = ref([])

const getTrains = ()=>{
    axios.get('/business/train/admin/query-list')
        .then(res=>{
            if (res){
                if (res.data.success){
                    let list = res.data.content.list;
                    options.value = []
                    trainList.value = []
                    for (let i = 0; i < list.length; i++) {
                        const currentTrain = list[i]
                        trainList.value.push(currentTrain)
                        options.value.push({
                            value: currentTrain.code,
                            label: currentTrain.type+currentTrain.code+' '+currentTrain.start+'-'+currentTrain.end
                        })
                    }

                }
            }
        })
}

onMounted(()=>{
    getTrains()
})
</script>
<style scoped>

</style>