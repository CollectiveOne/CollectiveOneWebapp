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
            <div :key="currentSectionId" class="w3-left">
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
      </div>
      <div class="w3-row w3-margin-top w3-container">
        <transition name="fadeenter">
          <router-view></router-view>
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
      showNewCardModal: false
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    currentSectionId () {
      return this.$route.params.sectionId
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

  watch: {
    '$route' () {
      this.$store.dispatch('updateCurrentSection', this.$route.params.sectionId)
    }
  },

  methods: {
  },

  created () {
    // if (this.$store.state.model.currentSection === null) {
    //   this.$store.dispatch('updateCurrentSection', this.initiative.topModelSection)
    // }

    if (this.$route.name === 'InitiativeModel') {
      console.log('redirecting to top section')
      this.$router.replace({name: 'ModelSection', params: {sectionId: this.initiative.topModelSection.id}})
    }

    if (this.$route.name === 'ModelSection') {
       this.$store.dispatch('updateCurrentSection', this.$route.params.sectionId)
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
