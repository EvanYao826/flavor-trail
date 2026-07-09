<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()
const foodId = route.params.id
</script>

<template>
  <div>
    <h1>美食详情 - {{ foodId }}</h1>
  </div>
</template>