<template lang="html">
  <div class="w3-cell-row">
    <div class="vision-nav light-grey w3-cell light-grey drop-shadow-br w3-border-top">
      <app-model-nav></app-model-nav>
    </div>
    <div class="vision-content w3-cell">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import ModelNav from '@/components/model/nav/ModelNav.vue'

export default {
  components: {
    'app-model-nav': ModelNav
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    }
  },

  methods: {
    redirect () {
      if (this.$route.name === 'InitiativeModel') {
        console.log('redirecting from InitiativeModel to ModelSectionContent with section id: ' + this.initiative.topModelSection.id)
        this.$router.replace({name: 'ModelSectionContent', params: {sectionId: this.initiative.topModelSection.id}})
      }
    }
  },

  created () {
    this.redirect()
  }
}
</script>

<style scoped>

.vision-nav {
  width: 350px;
  vertical-align: top;
  border-color: #e3e6e8 !important;
}

.vision-content {
  vertical-align: top;
}

</style>
