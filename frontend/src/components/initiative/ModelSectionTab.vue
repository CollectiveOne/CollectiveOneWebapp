<template lang="html">
  <div class="w3-cell-row">
    <div class="vision-nav light-grey w3-cell">
      <app-model-nav></app-model-nav>
    </div>
    <div class="vision-content w3-cell w3-container">
      <div class="w3-row w3-margin-top router-container">
        <transition name="fadeenter">
          <router-view>
          </router-view>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
import ModelNav from '@/components/model/nav/ModelNav.vue'

export default {
  components: {
    'app-model-nav': ModelNav
  },

  data () {
    return {
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    sections () {
      if (this.$store.state.initiative.initiativeModelSections) {
        return this.$store.state.initiative.initiativeModelSections.sections
      } else {
        return []
      }
    },
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    searching () {
      return this.query !== ''
    },
    isSearch () {
      return this.$route.name === 'ModelSearch'
    }
  },

  methods: {
  },

  created () {
    if (!this.initiative.meta.modelEnabled) {
      /* redirect to overview if model is not enabled */
      this.$router.replace({ name: 'InitiativeOverview', params: { initiativeId: this.initiative.id } })
    }
  }
}
</script>

<style scoped>

.vision-nav {
  width: 350px;
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
