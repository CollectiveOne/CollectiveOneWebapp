<template lang="html">
  <div class="this-container">
    <div class="w3-row sub-initiative-first-row">
      <div class="w3-col m4">
        <h5 class="w3-text-indigo w3-hide-medium w3-hide-large">Subinitiative of</h5>
        <h5 class="w3-text-indigo w3-hide-small w3-right">Subinitiative of</h5>
      </div>
      <div class="w3-col m8">
        <app-initiative-selector class="initiative-selector"
          anchor="id" label="name" :init="parentInitiative"
          url="/1/secured/initiatives/search"
          @select="parentInitiativeSelected($event)">
        </app-initiative-selector>
      </div>
    </div>
    <div class="w3-row assigner-div">
      <app-assets-assigner :assetsData="parentInitiative.ownTokens" @updated="parentOwnTokensSelected"></app-assets-assigner>
    </div>
  </div>
</template>

<script>
import InitiativeSelector from '../initiative/InitiativeSelector.vue'
import AssetsAssigner from './AssetsAssigner.vue'

export default {
  props: {
    parentInitiative: {
      type: Object
    }
  },

  components: {
    AppInitiativeSelector: InitiativeSelector,
    AppAssetsAssigner: AssetsAssigner
  },

  methods: {
    parentInitiativeSelected (initiative) {
      this.axios.get('/1/secured/initiative/' + initiative.id, {
        params: {
          level: 'withAssets'
        }
      }).then((response) => {
        this.$emit('parent-initiative-updated', response.data.data)
      })
    },

    parentOwnTokensSelected (data) {
      this.$emit('selected', [data])
    }
  }
}
</script>

<style scoped>

</style>
