<template>
  <div>{{route.query}}</div>
</template>

<script setup>
  import {onMounted} from "vue";
  import {useRoute} from "vue-router";

  const route = useRoute()

  onMounted(()=>{

  })
</script>
