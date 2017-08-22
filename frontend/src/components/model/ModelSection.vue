<template lang="html">
  <div class="">
    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-section-modal
          v-if="showSectionModal" :sectionId="section.id"
          :key="section.id"
          @close="showSectionModal = false">
        </app-model-section-modal>
      </transition>
    </div>

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-new-card-modal
          v-if="showNewCardModal"
          :sectionId="section.id" :initiativeId="initiativeId"
          :key="section.id"
          @close="showNewCardModal = false">
        </app-model-new-card-modal>
      </transition>
    </div>

    <div class="section-container w3-display-container"
      @mouseover="showActionButton = true"
      @mouseleave="showActionButton = false">

      <div class="section-title-container w3-border-top">
        <div class="section-title w3-row w3-tag gray-1" :style="sectionTitleStyle">{{ section.title }}</div>
        <div class="w3-leftbar section-description w3-row w3-small light-grey w3-padding">
          {{ section.description }}
        </div>
      </div>

      <div class="cards-container">
        <app-model-card
          v-for="card in section.cards"
          :key="card.id"
          :card="card"
          class="section-card">
        </app-model-card>
      </div>

      <div class="w3-leftbar bottom-bar light-grey w3-small" :class="{'bottom-bar-bottom': !showSubsections}">
        <button
          v-if="section.subsections.length > 0"
          class="w3-button model-button"
          @click="showSubsections = !showSubsections">
          <div v-if="showSubsections" >
            <i class="w3-left fa fa-caret-up" aria-hidden="true"></i>
            <div class="w3-left button-text">
              hide
            </div>

          </div>
          <div v-else>
            <i class="w3-left fa fa-caret-right" aria-hidden="true"></i>
            <div class="w3-left button-text">
              <b>{{ section.subsections.length }}</b> subsections
            </div>
          </div>
        </button>
      </div>
      <div class="slider-container">
        <transition name="slideDownUp">
          <div v-if="showSubsections" class="subsections-container" :style="subsectionsContainerStyle">
            <app-model-section
              v-for="subsection in section.subsections"
              :key="subsection.id"
              :section="subsection"
              :level="level + 1"
              class="subsection-container">
            </app-model-section>
          </div>
        </transition>
      </div>

      <transition name="fadeenter">
        <div v-if="showActionButton" class="w3-display-topright action-buttons-container">
          <div
            class="w3-button model-action-button gray-1-color w3-right"
            @click="showSectionModal = true">
            <i class="fa fa-expand" aria-hidden="true"></i>
          </div>
          <div
            class="w3-button model-action-button gray-1-color w3-right"
            @click="showNewCardModal = true">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
          </div>
        </div>
      </transition>

    </div>
  </div>
</template>

<script>
import ModelCard from '@/components/model/ModelCard.vue'
import ModelSectionModal from '@/components/model/modals/ModelSectionModal.vue'
import ModelNewCardModal from '@/components/model/modals/ModelNewCardModal.vue'

export default {
  name: 'app-model-section',

  components: {
    'app-model-card': ModelCard,
    'app-model-section-modal': ModelSectionModal,
    'app-model-new-card-modal': ModelNewCardModal
  },

  props: {
    section: {
      type: Object,
      default: null
    },
    initiativeId: {
      type: String,
      default: ''
    },
    level: {
      type: Number,
      default: 0
    }
  },

  data () {
    return {
      showSubsections: false,
      showActionButton: false,
      showSectionModal: false,
      showNewCardModal: false
    }
  },

  computed: {
    sectionTitleStyle () {
      var fontsize = this.level < 5 ? 26 - 4 * this.level : 16
      return {'font-size': fontsize + 'px'}
    },
    subsectionsContainerStyle () {
      var paddingLeft = this.level < 5 ? 20 * (this.level + 1) : 100
      var marginTop = this.level < 5 ? 110 - 20 * (5 - this.level) : 10

      return {
        'padding-left': paddingLeft + 'px',
        'margin-top': marginTop + ' px'
      }
    },
    showSubsectionsButtonContent () {
      var text = ''
      if (this.showSubsections) {
        text = ' hide subsection'
        if (this.section.subsections.length > 1) {
          text += 's'
        }
      } else {
        text = '<i class="fa fa-caret-right" aria-hidden="true"></i><b> ' + this.section.subsections.length + '</b> subsection'
        if (this.section.subsections.length > 1) {
          text += 's'
        }
      }
      return text
    }
  }
}
</script>

<style scoped>

.section-container {
}

.action-buttons-container {
  margin-top: 2px;
}

.section-title-container {
  margin-bottom: 5px;
  border-color: #637484 !important;
  border-top-width: 2px !important;
}

.section-title {
  margin-bottom: 0px;
}

.section-description {
  border-color: #637484 !important;
}

.cards-container {
}

.section-card {
  margin-top: 0px;
  margin-bottom: 6px;
}

.bottom-bar {
  border-color: #637484 !important;
}

.bottom-bar-bottom {
  border-bottom-style: solid;
  border-bottom-width: 2px !important;
}

.bottom-bar .fa {
  margin-top: 5px;
  font-size: 16px !important;
  margin-right: 5px;
}

.bottom-bar .button-text {
  padding-top: 5px;
}

.subsections-container {
  padding-top: 20px;
}

.subsection-container {
  margin-bottom: 20px;
}

</style>
