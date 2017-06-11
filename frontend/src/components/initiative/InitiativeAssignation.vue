<template lang="html">
  <div class="w3-card-2 w3-padding">
    <div class="w3-row">
      <div class="w3-col m4">
          <div v-for="bill in assignation.assets" class="">
            {{ bill.value }} {{ bill.assetName }}
          </div>
      </div>
      <div class="w3-col m8">
        <div class="w3-row">
          {{ assignation.motive }}
        </div>
        <div class="w3-row">
          {{ assignation.state }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    assignationInit: {
      type: Object
    }
  },

  data () {
    return {
      assignation: null
    }
  },

  methods: {
    extend () {
      this.axios.get('/1/secured/initiative/' + this.initiative.id + '/assignation/' + this.assignationId).then((response) => {
        this.assignation = response.data.data
      })
    }
  },

  mounted () {
    this.assignation = JSON.parse(JSON.stringify(this.assignationInit))
  }
}
</script>

<style lang="css">
</style>
