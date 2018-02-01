<template lang="html">
  <div class="slider-container">
    <transition name="slideDownUp">
      <div v-if="show" class="w3-row w3-tag error-row w3-round" :class="{'error-panel': isError, 'success-panel': isSuccess}">
        {{ message }}
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  props: {
    show: Boolean,
    message: String,
    panelType: {
      type: String,
      default: 'error'
    }
  },

  computed: {
    isError () {
      return this.panelType === 'error'
    },
    isSuccess () {
      return this.panelType === 'success'
    }
  }
}
</script>

<style lang="css">
</style>
