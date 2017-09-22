<template lang="html">
  <div class="">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-view-modal
          v-if="showViewModal"
          :isNew="isNew"
          :viewId="view.id"
          :initiativeId="initiativeId"
          @close="showViewModal = false">
        </app-model-view-modal>
      </transition>
    </div>

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-section-modal
          v-if="showSectionModal"
          :isNew="true"
          :initiativeId="initiativeId"
          :inView="true"
          :inElementId="view.id"
          :inElementTitle="view.title"
          @close="showSectionModal = false">
        </app-model-section-modal>
      </transition>
    </div>

    <div v-if="view" class="model-view-container w3-display-container">
      <div class="view-title"
        @mouseover="showActionButton = true"
        @mouseleave="showActionButton = false">

        <transition name="fadeenter">
          <div v-if="showActionButton" class="w3-display-topright buttons-container gray-1-color" style="width:100px">
            <div @click="expandViewModal()" class="w3-button model-action-button w3-right">
              <i class="fa fa-expand" aria-hidden="true"></i>
            </div>
          </div>
        </transition>

        <div class="w3-rest">
          <div class="w3-row">
            <h1 class="">{{ view.title }}</h1>
          </div>
          <div class="w3-row w3-padding light-grey">
            <vue-markdown class="marked-text" :source="view.description"></vue-markdown>
          </div>
        </div>
      </div>

      <div v-for="section in view.sections"
        :key="section.id"
        class="view-sections">

        <div class="section-drop-area"
          @dragover.prevent
          @drop="sectionDroped(section.id, $event)">
        </div>

        <app-model-section
          :preloaded="false"
          :sectionId="section.id"
          :viewId="view.id"
          :sectionInit="section"
          :initiativeId="initiativeId"
          :level="0"
          dragType="MOVE_SECTION">
        </app-model-section>
      </div>

      <div class="view-sections">
        <div class="section-drop-area"
          @dragover.prevent
          @drop="sectionDroped('', $event)">
        </div>

        <button class="w3-button w3-border gray-1-color" style="width: 100%; text-align: left"
          @click="newSection()">
          <i class="fa fa-plus w3-margin-right" aria-hidden="true"></i>
          add section
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import ModelSection from '@/components/model/ModelSection.vue'
import ModelViewModal from '@/components/model/modals/ModelViewModal.vue'
import ModelSectionModal from '@/components/model/modals/ModelSectionModal.vue'

export default {
  name: 'app-model-view',

  components: {
    'app-model-view-modal': ModelViewModal,
    'app-model-section-modal': ModelSectionModal,
    'app-model-section': ModelSection
  },

  data () {
    return {
      view: null,
      showActionButton: false,
      showViewModal: false,
      showSectionModal: false,
      isNew: false
    }
  },

  watch: {
    '$store.state.support.triggerUpdateModel' () {
      this.update()
    }
  },

  props: {
    initiativeId: {
      type: String,
      default: ''
    },
    viewId: {
      type: String,
      default: ''
    }
  },

  computed: {
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    }
  },

  methods: {
    update () {
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/view/' + this.viewId, {
        params: {
          level: 1
        }
      }).then((response) => {
        this.view = response.data.data
      })
    },
    expandViewModal () {
      this.isNew = false
      this.showViewModal = true
    },
    newSection () {
      this.showSectionModal = true
    },
    sectionDroped (onSectionId, event) {
      var dragData = JSON.parse(event.dataTransfer.getData('text/plain'))
      var url = ''

      if (dragData.type === 'MOVE_SECTION') {
        url = '/1/initiative/' + this.initiativeId +
        '/model/view/' + this.view.id +
        '/moveSection/' + dragData.sectionId

        this.axios.put(url, {}, {
          params: {
            onViewSectionId: onSectionId
          }
        }).then((response) => {
          this.$store.commit('triggerUpdateModel')
        })
      }

      if (dragData.type === 'MOVE_SUBSECTION') {
        url = '/1/initiative/' + this.initiativeId +
        '/model/section/' + dragData.fromSectionId +
        '/moveSubsection/' + dragData.sectionId

        this.axios.put(url, {}, {
          params: {
            onViewId: this.view.id,
            onSubsectionId: onSectionId
          }
        }).then((response) => {
          this.$store.commit('triggerUpdateModel')
        })
      }
    }
  },

  mounted () {
    this.update()
  }
}
</script>

<style scoped>

.view-title .buttons-container {
  padding-top: 15px;
}

.view-title h1 {
  margin-bottom: 0px;
}

.view-sections {
  /*margin-bottom: 30px;*/
}

.section-drop-area {
  height: 30px;
  width: 100%;
}

</style>
