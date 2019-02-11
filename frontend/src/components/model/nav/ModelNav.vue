<template lang="html">
  <div class="model-nav-container-large">

    <transition name="slideDownUp">
      <app-model-section-modal
        v-if="showSectionModal"
        :isNew="true"
        :initiativeId="initiative.id"
        :inInitiative="true"
        :inElementId="initiative.id"
        :inElementTitle="initiative.meta.name"
        @close="showSectionModal = false">
      </app-model-section-modal>
    </transition>

    <div class="model-nav-container">
      <div v-if="$store.state.user.authenticated"
        class="w3-row w3-right dark-gray controls-row cursor-pointer"
        @click="showControls = !showControls">
        <i class="fa" :class="{'fa-chevron-down': !showControls, 'fa-chevron-up': showControls}" aria-hidden="true"></i>
      </div>
      <div class="slider-container w3-left">
        <transition name="slideDownUp">
          <div v-if="showControls" class="controls-btns">
            <app-model-nav-control-buttons>
            </app-model-nav-control-buttons>
          </div>
        </transition>
      </div>
      <div class="w3-row">
        <app-model-section-nav-item
          :sectionData="sectionData"
          @section-selected="$emit('section-selected', $event)">
        </app-model-section-nav-item>
      </div>
    </div>

  </div>
</template>

<script>
import ModelSectionModal from '@/components/model/modals/ModelSectionModal.vue'
import ModelSectionNavItem from '@/components/model/nav/ModelSectionNavItem.vue'
import ModelNavControlButtons from '@/components/model/nav/ModelNavControlButtons.vue'

export default {
  components: {
    'app-model-section-modal': ModelSectionModal,
    'app-model-section-nav-item': ModelSectionNavItem,
    'app-model-nav-control-buttons': ModelNavControlButtons
  },

  data () {
    return {
      showSectionModal: false,
      showControls: false
    }
  },

  computed: {
    sectionData () {
      return this.$store.getters.getSectionDataAtCoord([0])
    },
    section () {
      if (this.sectionData) {
        return this.sectionData.section
      }
      return null
    },
    initiative () {
      return this.$store.state.initiative.initiative
    },
    levels () {
      return this.$store.state.viewParameters.levels
    }
  },

  methods: {
  },

  created () {
  }
}
</script>

<style scoped>

.controls-btns {
  width: 350px;
  height: 56px;
  padding: 6px 6px;
  background-color: #313942;
}

.model-nav-container-large {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.model-nav-container {
  flex: 1 0 0;
  min-height: 0;
  overflow-y: auto;
}

.controls-row {
  width: 100%;
  text-align: center;
  transition: all 300ms ease;
}

.controls-row:hover {
  color: #60656b;
}

</style>
