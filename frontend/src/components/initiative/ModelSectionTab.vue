<template lang="html">
  <div class="w3-row section-content-tab">
    <div class="section-nav-container"
      @touchstart="touchStart($event)"
      @touchend="touchEnd($event)" >
      <transition name="slideRightLeft">
        <div v-show="expandModelNav" class="vision-nav-cell light-grey drop-shadow-br w3-border-top">
          <app-model-nav @section-selected="sectionSelected()">
          </app-model-nav>
        </div>
      </transition>
    </div>
    <div class="hide-nav-div-container">
      <div @click="expandModelNavClicked()" class="hide-nav-div drop-shadow-br">
        <i v-if="expandModelNav" class="fa fa-chevron-left" aria-hidden="true"></i>
        <i v-else class="fa fa-chevron-right" aria-hidden="true"></i>
      </div>
    </div>
    <div class="vision-content">
      <router-view></router-view>
      <div class="touch-area"
        @touchstart="touchStart($event)"
        @touchend="touchEnd($event)" >
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
      touchStartX: 0,
      touchStartY: 0
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    windowIsSmall () {
      return this.$store.state.support.windowIsSmall
    },
    expandModelNav () {
      return this.$store.state.support.expandModelNav
    }
  },

  methods: {
    expandModelNavClicked () {
      this.$store.commit('toggleExpandModelNav')
    },
    sectionSelected () {
      if (this.windowIsSmall) {
        this.showNavBar = false
      }
    },
    redirect () {
      if (this.$route.name === 'InitiativeModel') {
        let currentSection = this.$store.state.model.currentSection
        if (currentSection !== null) {
          console.log('redirecting to ModelSectionContent with sectionId: ' + currentSection.id)
          this.$router.replace({ name: 'ModelSectionContent', params: { sectionId: currentSection.id } })
        } else {
          console.log('redirecting to ModelSectionContent with sectionId: ' + this.initiative.topModelSection.id)
          this.$router.replace({ name: 'ModelSectionContent', params: { sectionId: this.initiative.topModelSection.id } })
        }
      }
    },
    touchStart (event) {
      this.touchStartX = event.changedTouches[0].clientX
      this.touchStartY = event.changedTouches[0].clientY
    },
    touchEnd (event) {
      let deltaX = event.changedTouches[0].clientX - this.touchStartX
      let deltaY = event.changedTouches[0].clientY - this.touchStartY
      let eps = 30
      if (Math.abs(deltaY) < (2 * eps)) {
        if (deltaX > eps) {
          this.$store.commit('setExpandModelNav', true)
        } else {
          if (deltaX < (-1 * eps)) {
            this.$store.commit('setExpandModelNav', false)
          }
        }
      }
    }
  },

  created () {
    console.log('ModelSectionTab created')
    this.redirect()
  }
}
</script>

<style scoped>

.section-content-tab {
  height: 100%;
  display: flex;
  flex-direction: row;
  position: relative;
}

.section-nav-container {
  height: 100%;
}

.vision-nav-cell {
  max-width: calc(100vw - 90px);
  width: 350px;
  min-height: 1px;
  border-color: #e3e6e8 !important;
  overflow-y: auto;
  height: 100%;
}

.vision-content {
  flex-grow: 1;
  height: 100%;
  min-width: 350px;
  position: relative;
}

.touch-area {
  height: 100%;
  width: 20px;
  position: absolute;
  left: 0;
  top: 0;
}

.hide-nav-div-container {
  width: 0px;
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
  z-index: 2;
}

.hide-nav-div:hover {
  background-color: #3e464e;
}

</style>
