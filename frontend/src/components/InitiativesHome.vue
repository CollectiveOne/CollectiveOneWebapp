<template lang="html">
  <div class="">
    <router-view></router-view>
    <div class="w3-row">
      <app-header :inInitiative="false"></app-header>
    </div>
    <div class="w3-row">
      <div v-if="$store.state.user.authenticated" class="w3-row w3-center light-grey">
        <div class="w3-col m6 border-blue cursor-pointer tablink" :class="{'w3-bottombar': !publicInitiatives}"
          @click="publicInitiatives = false">
          <h3>My Initiatives</h3>
        </div>
        <div class="w3-col m6 border-blue cursor-pointer tablink" :class="{'w3-bottombar': publicInitiatives}"
          @click="publicInitiatives = true">
          <h3>Browse Public Initiatives</h3>
        </div>
      </div>
      <div v-else class="">
        <h3>Browse Public Initiatives</h3>
      </div>
    </div>
    <div class="w3-container inits-container">
      <app-public-initiatives v-if="publicInitiatives"></app-public-initiatives>
      <app-my-initiatives v-else></app-my-initiatives>
    </div>
  </div>

</template>

<script>
import Header from '@/components/Header.vue'
import PublicInitiatives from '@/components/PublicInitiatives.vue'
import MyInitiatives from '@/components/MyInitiatives.vue'

export default {
  components: {
    'app-header': Header,
    'app-public-initiatives': PublicInitiatives,
    'app-my-initiatives': MyInitiatives
  },

  data () {
    return {
      publicInitiatives: true
    }
  },

  created () {
    if (this.$store.state.user.authenticated) {
      this.publicInitiatives = false
    }
  }
}
</script>

<style scoped>

.tablink:hover {
  background-color: #cccccc;
}

.inits-container {
  padding-top: 16px;
}

</style>
