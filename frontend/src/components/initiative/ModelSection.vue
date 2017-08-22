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
      <button @click="showNewViewModal = true" class="w3-button app-button">new</button>
    </div>
    <div class="w3-row w3-margin-top">
      <app-model-view :view="initiativeModel.views[showView]" :key="showView"></app-model-view>
    </div>
  </div>
</div>

</template>

<script>
import ModelView from '@/components/model/ModelView.vue'
import ModelNewViewModal from '@/components/model/modals/ModelNewViewModal.vue'

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
    }
  },

  methods: {
  },

  mounted () {
    this.$store.dispatch('refreshModel')
  }
}
</script>

<style scoped>

</style>
