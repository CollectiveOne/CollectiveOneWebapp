<template lang="html">
  <router-link :to="{name: 'Initiative', params: {'initiativeId': initiative.id}}"
    tag="div" class="w3-panel w3-round-large w3-padding cursor-pointer"
    :style="{'background-color': $store.getters.colorOfInitiative(initiative.id)}">
    <h4><b>{{ initiative.meta.name }}</b></h4>
    <p>{{ initiative.meta.driver }}</p>
  </router-link>
</template>

<script>
export default {
  props: {
    initiative: Object
  }
}
</script>

<style scoped>

.w3-panel {
  color: white;
}

</style>
