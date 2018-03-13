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
      <app-model-nav @section-selected="sectionSelected($event)"></app-model-nav>
    </div>
    <div class="vision-content w3-cell w3-container">
      <div class="w3-row w3-margin-top router-container">
        <transition name="fadeenter">
          <app-cards-list :sectionId="selectedSection.id" :levels="selectedLevels">
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

export default {
  components: {
    'app-model-card-modal': ModelCardModal,
    'app-model-nav': ModelNav,
    'app-cards-list': CardsList
  },

  data () {
    return {
      showNewCardModal: false,
      selectedSection: null,
      selectedLevels: 1
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    }
  },

  methods: {
    sectionSelected (event) {
      this.selectedSection = event.section
      this.selectedLevels = event.levels
    }
  },

  created () {
    this.selectedSection = this.initiative.topModelSection
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
