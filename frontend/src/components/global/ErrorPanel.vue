<template lang="html">
  <div class="slider-container">
    <transition name="slideDownUp">
      <div v-if="show" class="w3-row w3-tag error-panel error-row w3-round">
        {{ message }}
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  props: {
    show: Boolean,
    message: String
  }
}
</script>

<style lang="css">
</style>
