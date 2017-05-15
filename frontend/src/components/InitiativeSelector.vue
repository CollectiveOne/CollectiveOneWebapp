<template lang="html">
  <input class="w3-input w3-hover-light-gray" type="text" v-model="selectedInitiative.name">
</template>

<script>
export default {
  data () {
    return {
      selectedInitiative: null
    }
  },

  props: {
    init: {
      type: Object,
      default: () => {
        return {
          name: 'Selected Initiative Name',
          id: 'selectedInitiativeId'
        }
      }
    }
  },

  mounted () {
    this.selectedInitiative = this.init
  }
}
</script>

<style lang="css">
</style>
