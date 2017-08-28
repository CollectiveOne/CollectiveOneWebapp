<template lang="html">

<div>
  <div class="slider-container">
    <transition name="slideDownUp">
      <app-model-new-view-modal
        v-if="showNewViewModal" :initiativeId="initiative.id"
        :key="initiative.id"
        @close="showNewViewModal = false">
      </app-model-new-view-modal>
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
      <div @click="showNewViewModal = true" class="w3-button w3-right">
        <i class="fa fa-plus-circle" aria-hidden="true"></i>
      </div>
    </div>
    <div class="w3-row w3-margin-top">
      <transition name="fadeenter">
        <app-model-view :view="views[showView]" :key="showView"></app-model-view>
      </transition>
    </div>
  </div>
</div>

</template>

<script>
import ModelView from '@/components/model/ModelView.vue'
import ModelNewViewModal from '@/components/model/modals/ModelNewViewModal.vue'

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
    'app-model-new-view-modal': ModelNewViewModal
  },

  data () {
    return {
      showView: 0,
      showNewViewModal: false
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
    }
  },

  mounted () {
    this.$store.dispatch('refreshModel')
  }
}
</script>

<style scoped>

</style>
