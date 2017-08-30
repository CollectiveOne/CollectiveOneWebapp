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

  <div class="w3-container"
    v-if="initiativeModel">
    <div class="w3-row w3-margin-top">
      <div class="view-selector w3-left app-margin-right app-margin-bottom" v-for="view in views">
        <button @click="viewSelected(view.id)"
          class="w3-button w3-right"
          :class="{'app-button-light': !isViewSelected(view.id), 'app-button': isViewSelected(view.id)}">
          {{ view.title }}
        </button>
      </div>
      <div @click="newView()" class="w3-button w3-right">
        <i class="fa fa-plus-circle" aria-hidden="true"></i>
      </div>
    </div>
    <div class="w3-row w3-margin-top">
      <transition name="fadeenter">
        <app-model-view
          @show-view-modal="showViewModalFun($event)"
          @show-section-modal="showSectionModalFun($event)"
          @show-card-modal="showCardModalFun($event)"
          :view="views[showView]" :key="showView">
        </app-model-view>
      </transition>
    </div>
  </div>
</div>

</template>

<script>
import ModelView from '@/components/model/ModelView.vue'
import ModelViewModal from '@/components/model/modals/ModelViewModal.vue'
import ModelSectionModal from '@/components/model/modals/ModelSectionModal.vue'
import ModelCardModal from '@/components/model/modals/ModelCardModal.vue'

const getViewIndex = function (views, id) {
  for (var ix in views) {
    if (views[ix].id === id) {
      return ix
    }
  }
  return -1
}

export default {
  components: {
    'app-model-view': ModelView,
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
    initiativeModel () {
      return this.$store.state.initiative.initiativeModel
    },
    views () {
      return this.initiativeModel.views
    }
  },

  methods: {
    viewSelected (id) {
      this.showView = getViewIndex(this.views, id)
    },
    isViewSelected (id) {
      return this.showView === getViewIndex(this.views, id)
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
    this.$store.dispatch('refreshModel')
  }
}
</script>

<style scoped>

</style>
