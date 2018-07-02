<template lang="html">
  <div class="popper">
    <div class="">
      {{ title }}
    </div>
    <div v-if="showDetails" class="">
      {{ details }}
    </div>

  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String
    },
    details: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      showDetails: true
    }
  }
}
</script>

<style scoped>

.help-popper-container {
  background-color: white;
  border-style: solid;
  border-width: 2px;
}

</style>
