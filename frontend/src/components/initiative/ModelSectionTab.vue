<template lang="html">
  <div class="w3-cell-row">

    <div class="vision-nav light-grey w3-cell">
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

  watch: {
    '$route' () {
      this.redirect()
    }
  },

  methods: {
    redirect () {
      if (this.$route.name === 'InitiativeModel') {
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
}

.vision-content {
  vertical-align: top;
}

.router-container {
  font-family: 'Open Sans', sans-serif !important;
}

.router-container h1 {
  font-size: 32px;
}

.router-container {
  padding-bottom: 20px;
}

.router-container .model-button {
  background-color: #eff3f6;
  padding-top: 2px !important;
  padding-bottom: 3px !important;
}

</style>
