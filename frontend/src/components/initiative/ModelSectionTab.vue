<template lang="html">
  <div class="w3-cell-row nav-cell-row">
    <transition name="slideRightLeft">
      <div v-show="showNavBar" class="vision-nav-cell w3-cell light-grey drop-shadow-br w3-border-top">
        <app-model-nav ></app-model-nav>
      </div>
    </transition>
    <div @click="showNavBar = !showNavBar" class="hide-nav-div drop-shadow-br" :class="{'hide-nav-div-shift': showNavBar}">
      <i v-if="showNavBar" class="fa fa-chevron-left" aria-hidden="true"></i>
      <i v-else class="fa fa-chevron-right" aria-hidden="true"></i>
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

  data () {
    return {
      showNavBar: true
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    }
  },

  methods: {
    redirect () {
      if (this.$route.name === 'InitiativeModel') {
        let currentSection = this.$store.getters.currentSection
        if (currentSection !== null) {
          this.$router.replace({name: 'ModelSectionContent', params: {sectionId: currentSection.id}})
        } else {
          this.$router.replace({name: 'ModelSectionContent', params: {sectionId: this.initiative.topModelSection.id}})
        }
      }
    }
  },

  created () {
    this.redirect()
  }
}
</script>

<style scoped>

.nav-cell-row {
  position: relative;
}

.vision-nav-cell {
  width: 350px;
  min-width: 350px;
  min-height: 1px;
  vertical-align: top;
  border-color: #e3e6e8 !important;
}

.vision-content {
  vertical-align: top;
  min-width: 350px;
}

.hide-nav-div {
  width: 25px;
  height: 35px;
  position: absolute;
  top: 12px;
  background-color: #313942;
  border-top-right-radius: 5px;
  border-bottom-right-radius: 5px;
  color: white;
  padding-top: 7px;
  padding-left: 6px;
  cursor: pointer;
}

.hide-nav-div-shift {
  left: 350px;
}

.hide-nav-div:hover {
  background-color: #3e464e;
}

</style>
