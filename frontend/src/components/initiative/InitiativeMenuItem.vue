<template lang="html">
  <div class="w3-container d2-color">
    <div class="w3-row">
      <div v-if="hasSubinitiatives" class="w3-col s3 w3-center expand-container">
        <i v-if="!showSubinitiatives" @click="showSubinitiatives = true" class="fa fa-chevron-circle-down w3-button" aria-hidden="true"></i>
        <i v-else @click="showSubinitiatives = false" class="fa fa-chevron-circle-up w3-button" aria-hidden="true"></i>
      </div>
      <div class="w3-col s7">
        <h5>
          <router-link :to="'/inits/'+initiative.id+'/overview'" class="w3-left">
            {{ initiative.name }}
          </router-link>
        </h5>
      </div>
      <div class="w3-col s2 btns l2-color w3-button" @click="$emit('new-subinitiative', initiative.id)">
        <i class="fa fa-external-link" aria-hidden="true"></i>
      </div>
    </div>
    <div class="w3-row" v-if="showSubinitiatives" v-for="subinitiative in initiative.subInitiatives">
      <div class="w3-col s10">
        <app-initiative-menu-item
          class="sub-initiative-element" :initiative="subinitiative" :key="subinitiative.id"
          @new-subinitiative="$emit('new-subinitiative', $event)">
        </app-initiative-menu-item>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'app-initiative-menu-item',

  props: {
    initiative: {
      type: Object,
      default: () => {
        return {
          name: 'Initiative Name',
          driver: 'Initiative Driver',
          subInitiatives: []
        }
      }
    }
  },

  computed: {
    hasSubinitiatives () {
      return this.initiative.subInitiatives.length > 0
    }
  },

  data () {
    return {
      showSubinitiatives: false
    }
  }
}
</script>

<style scoped>

</style>
