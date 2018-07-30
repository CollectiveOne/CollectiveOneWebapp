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
      <div v-if="$store.state.user.authenticated" class="w3-row">
        <app-model-nav-control-buttons>
        </app-model-nav-control-buttons>
      </div>
      <div class="w3-row">
        <div v-if="$store.state.sectionsTree.hasParents" class="parentContextContainer">
          in: <button type="button" class="w3-button app-button parentContexts" 
          v-for="(parentContext,i) in parentContexts" @click="expandParentContext(i)">{{ parentContext.name }}</button>
        </div>
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
      showSectionModal: false
      // parentContexts:  [{name: "Test 0", id: "saswat"}, {name: "Test 1", id: "sahoo"}]
    }
  },

  computed: {
    parentContexts () {
      return this.$store.getters.getParentsData(true)
    },
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
    expandParentContext (parentContextPosition) {
      console.log('clicked = ', this.parentContexts[parentContextPosition].id)
    }
  },

  created () {
  }
}
</script>

<style scoped>

.model-nav-container-large {
  height: 100%;
}

.model-nav-container {
}

.controls-row {
  background-color: #f9f9f9;
  padding: 6px;
}

.parentContextContainer {
  padding-left: 5px;
  padding-top: 5px;

}

.parentContexts {
  margin-left: 5px;
}

</style>
