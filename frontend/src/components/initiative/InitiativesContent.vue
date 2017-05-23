<template lang="html">
  <div v-if="initiative" class="w3-row">
    <div class="w3-white">
      <header class="w3-container w3-theme">
        <h3>{{ initiative.name }}</h3>
      </header>

      <div class="w3-row">
        <router-view :initiative="initiative"></router-view>
      </div>
    </div>
  </div>
</template>

<script>
export default {

  data () {
    return {
      initiative: null
    }
  },

  methods: {
    updateInitiative (id) {
      this.axios.get('/1/secured/initiative/' + id, {
        params: {
          addAssets: true,
          addSubinitiatives: true
        }
      }).then((response) => {
        this.initiative = response.data.data
      }).catch((error) => {
        console.log(error)
      })
    }
  },

  watch: {
    '$route' () {
      this.updateInitiative(this.$route.params.initiativeId)
    }
  },

  mounted () {
    this.updateInitiative(this.$route.params.initiativeId)
  }
}
</script>

<style scoped>

.tablink {
  cursor: pointer;
}
</style>
