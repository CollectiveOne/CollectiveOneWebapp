<template lang="html">
  <div class="w3-cell-row">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal
          v-if="showNewCardModal"
          :inSectionId="selectedSection.id"
          :inSectionTitle="selectedSection.title"
          @close="showNewCardModal = false"
          :isNew="true">
        </app-model-card-modal>
      </transition>
    </div>

    <div class="vision-nav light-grey w3-cell">
      <app-model-nav></app-model-nav>
    </div>
    <div class="vision-content w3-cell">
      <div class="w3-row control-row w3-border-bottom">
        <div class="w3-left breadcrumb">
          <transition name="fadeenter">
            <div :key="currentSection.id" class="w3-left">
              <div v-for="(parent, ix) in currentSectionPath" class="w3-left">
                <div class="w3-left">
                  {{ parent.title }}
                </div>
                <div v-if="ix < currentSectionPath.length - 1" class="w3-left">
                  <i class="fa fa-chevron-right" aria-hidden="true"></i>
                </div>
              </div>
            </div>
          </transition>
        </div>
        <div class="w3-right zoom-controls gray-1-color">
          <div class="w3-left">
            <i class="fa fa-level-down" aria-hidden="true"></i>
            {{ level }}
          </div>
          <div class="w3-left cursor-pointer">
            <i @click="levelDown()" class="fa fa-minus-circle" aria-hidden="true"></i>
          </div>
          <div class="w3-left cursor-pointer">
            <i @click="levelUp()" class="fa fa-plus-circle" aria-hidden="true"></i>
          </div>
        </div>
      </div>
      <div class="w3-row w3-margin-top w3-container">
        <transition name="fadeenter">
          <app-cards-list :key="currentSection.id" :level="level">
          </app-cards-list>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
import ModelCardModal from '@/components/model/modals/ModelCardModal.vue'
import ModelNav from '@/components/model/nav/ModelNav.vue'
import CardsList from '@/components/model/CardsList.vue'
import ActivityGetter from '@/components/notifications/ActivityGetter.vue'

export default {
  components: {
    'app-model-card-modal': ModelCardModal,
    'app-model-nav': ModelNav,
    'app-cards-list': CardsList,
    'app-activity-getter': ActivityGetter
  },

  data () {
    return {
      showNewCardModal: false,
      level: 1
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    currentSection () {
      return this.$store.state.model.currentSection
    },
    currentSectionGenealogy () {
      return this.$store.state.model.currentSectionGenealogy
    },
    currentSectionPath () {
      if (this.$store.getters.currentSectionPath !== null) {
        var path = JSON.parse(JSON.stringify(this.$store.getters.currentSectionPath))
        return path.reverse()
      }
      return []
    }
  },

  methods: {
    levelDown () {
      this.level = this.level > 0 ? this.level - 1 : 0
    },
    levelUp () {
      this.level = this.level + 1
    }
  },

  created () {
    if (this.$store.state.model.currentSection === null) {
      this.$store.dispatch('updateCurrentSection', this.initiative.topModelSection)
    }
  }
}
</script>

<style scoped>

.vision-nav {
  width: 350px;
}

.control-row {
  padding: 6px 18px;
  font-size: 22px;
}

.breadcrumb .fa {
  font-size: 18px;
  margin-left: 6px;
  margin-right: 6px;
}

.zoom-controls > div {
  margin-right: 16px;
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
