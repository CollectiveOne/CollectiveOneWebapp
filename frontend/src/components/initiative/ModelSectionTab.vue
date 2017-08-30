<template lang="html">

<div>

  <div class="slider-container">
    <transition name="slideDownUp">
      <app-model-view-modal
        v-if="showViewModal"
        :pars="viewModalPars"
        @close="showViewModal = false">
      </app-model-view-modal>
    </transition>
  </div>

  <div class="slider-container">
    <transition name="slideDownUp">
      <app-model-section-modal
        v-if="showSectionModal"
        :pars="sectionModalPars"
        @close="showSectionModal = false">
      </app-model-section-modal>
    </transition>
  </div>

  <div class="slider-container">
    <transition name="slideDownUp">
      <app-model-card-modal
        v-if="showCardModal"
        :pars="cardModalPars"
        @close="showCardModal = false">
      </app-model-card-modal>
    </transition>
  </div>

  <div class="w3-container">
    <div v-if="views" class="w3-row w3-margin-top">
      <div class="view-selector w3-left app-margin-right app-margin-bottom" v-for="view in views">
        <router-link
          :to="{ name: 'ModelView', params: { viewId: view.id } }"
          class="w3-button w3-right"
          :class="{'app-button-light': !isViewSelected(view.id), 'app-button': isViewSelected(view.id)}">
          {{ view.title }}
        </router-link>
      </div>
      <div @click="newView()" class="w3-button w3-right">
        <i class="fa fa-plus-circle" aria-hidden="true"></i>
      </div>
    </div>
    <div class="w3-row w3-margin-top">
      <transition name="fadeenter">
        <router-view
          @show-view-modal="showViewModalFun($event)"
          @show-section-modal="showSectionModalFun($event)"
          @show-card-modal="showCardModalFun($event)">
        </router-view>
      </transition>
    </div>
  </div>
</div>

</template>

<script>
import ModelViewModal from '@/components/model/modals/ModelViewModal.vue'
import ModelSectionModal from '@/components/model/modals/ModelSectionModal.vue'
import ModelCardModal from '@/components/model/modals/ModelCardModal.vue'

export default {
  components: {
    'app-model-view-modal': ModelViewModal,
    'app-model-section-modal': ModelSectionModal,
    'app-model-card-modal': ModelCardModal
  },

  data () {
    return {
      showView: 0,
      showModalInitiativeId: '',
      showViewModal: false,
      viewModalPars: null,
      showSectionModal: false,
      sectionModalPars: null,
      showCardModal: false,
      cardModalPars: null
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    views () {
      if (this.$store.state.initiative.initiativeModelViews) {
        return this.$store.state.initiative.initiativeModelViews.views
      } else {
        return []
      }
    }
  },

  methods: {
    isViewSelected (viewId) {
      if (this.$route.params.viewId) {
        if (this.$route.params.viewId === viewId) {
          return true
        }
      }
    },
    showViewModalFun (event) {
      this.viewModalPars = event
      this.showViewModal = true
    },
    showSectionModalFun (event) {
      this.sectionModalPars = event
      this.showSectionModal = true
    },
    showCardModalFun (event) {
      this.cardModalPars = event
      this.showCardModal = true
    },
    newView () {
      this.showViewModalFun({
        new: true,
        initiativeId: this.initiative.id
      })
    }
  },

  mounted () {
    this.$store.dispatch('refreshModelViews')
  }
}
</script>

<style scoped>

</style>
