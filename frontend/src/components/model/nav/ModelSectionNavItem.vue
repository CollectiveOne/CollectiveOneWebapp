<template lang="html">
  <div class="cursor-pointer">
    <div class="w3-left circle-div">
      <div v-if="hasSubsections">
        <i v-if="!showSubsections"class="fa fa-chevron-circle-right" aria-hidden="true" :style="{'color': color}"></i>
        <i v-else class="fa fa-chevron-circle-down" aria-hidden="true" :style="{'color': color}"></i>
      </div>
      <div v-else>
        <i class="fa fa-circle" aria-hidden="true" :style="{'color': color}"></i>
      </div>
    </div>
    <div class="w3-left title-div w3-margin-left">
      {{ section.title }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'model-section-nav-item',

  props: {
    section: {
      type: Object
    }
  },

  data () {
    return {
      showSubsections: false
    }
  },

  computed: {
    hasSubsections () {
      return this.section.hasSubsections
    }
  }
}
</script>

<style scoped>
</style>
