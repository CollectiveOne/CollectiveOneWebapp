<template lang="html">
  <div class="">
    Asssignation {{ assignationId }}
  </div>
</template>

<script>
export default {

  data () {
    return {
      assignation: null
    }
  },

  computed: {
    assignationId () {
      return this.$route.params.assignationId
    }
  },

  mounted () {}
}
</script>

<style lang="css">
</style>
