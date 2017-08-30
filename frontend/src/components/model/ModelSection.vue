<template lang="html">
  <div class="">
    <div class="section-container w3-display-container"
      @mouseover="showActionButton = true"
      @mouseleave="showActionButton = false">

      <div class="section-title-container w3-border-top">
        <div class="section-title w3-row w3-tag gray-1" :style="sectionTitleStyle">{{ section.title }}</div>
        <div class="w3-leftbar section-description w3-row w3-small light-grey w3-padding">
          {{ section.description }}
        </div>
      </div>

      <div class="w3-row-padding w3-leftbar cards-container">
        <app-model-card
          v-for="cardWrapper in section.cardsWrappers"
          :key="cardWrapper.id"
          :cardWrapper="cardWrapper"
          :initiativeId="initiativeId"
          :cardEffect="cardsAsCards"
          class="w3-col section-card"
          :class="{'l4': cardsAsCards, 'm6': cardsAsCards, 's12': !cardsAsCards}"
          @show-card-modal="$emit('show-card-modal', $event)">
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
              :initiativeId="initiativeId"
              :level="level + 1"
              class="subsection-container"
              @show-section-modal="$emit('show-section-modal', $event)"
              @show-card-modal="$emit('show-card-modal', $event)">
            </app-model-section>
          </div>
        </transition>
      </div>

      <transition name="fadeenter">
        <div v-if="showActionButton" class="w3-display-topright action-buttons-container"
          @mouseleave="showSubActionButtons = false">
          <div
            class="w3-button model-action-button gray-1-color w3-right"
            @click="expandSectionModal()">
            <i class="fa fa-expand" aria-hidden="true"></i>
          </div>
          <div
            class="w3-button model-action-button gray-1-color w3-right"
            @click="showSubActionButtons = !showSubActionButtons">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
          </div>
          <div v-if="showSubActionButtons" class="sub-action-buttons-container w3-card w3-white">
            <div class="w3-button" @click="newCard()">
              <i class="fa fa-plus w3-margin-right" aria-hidden="true"></i> new card
            </div>
            <div class="w3-button" @click="newSubsection()">
              <i class="fa fa-plus w3-margin-right" aria-hidden="true"></i> new sub-section
            </div>
          </div>
          <div
            class="w3-button model-action-button gray-1-color w3-right"
            @click="cardsAsCards = !cardsAsCards">
            <i class="fa" :class="{ 'fa-bars': cardsAsCards, 'fa-th': !cardsAsCards }" aria-hidden="true"></i>
          </div>
        </div>
      </transition>

    </div>
  </div>
</template>

<script>
import ModelCard from '@/components/model/ModelCard.vue'

export default {
  name: 'app-model-section',

  components: {
    'app-model-card': ModelCard
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
      showSubActionButtons: false,
      cardsAsCards: true
    }
  },

  computed: {
    sectionTitleStyle () {
      var fontsize = this.level < 5 ? 26 - 4 * this.level : 16
      return {'font-size': fontsize + 'px'}
    },
    subsectionsContainerStyle () {
      var paddingLeft = this.level < 5 ? 25 * (this.level + 1) : 100
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
  },

  methods: {
    expandSectionModal () {
      this.$emit('show-section-modal', {
        new: false,
        sectionId: this.section.id,
        initiativeId: this.initiativeId
      })
    },
    newSubsection () {
      this.$emit('show-section-modal', {
        new: true,
        isSubsection: true,
        initiativeId: this.initiativeId,
        parentSectionId: this.section.id,
        parentSectionTitle: this.section.title
      })
    },
    newCard () {
      this.$emit('show-card-modal', {
        new: true,
        initiativeId: this.initiativeId,
        sectionId: this.section.id,
        sectionTitle: this.section.title
      })
    }
  }
}
</script>

<style scoped>

.section-container {
  min-height: 100px;
}

.action-buttons-container {
  margin-top: 2px;
}

.sub-action-buttons-container {
  position: absolute;
  margin-top: 38px;
  margin-left: -120px;
}

.sub-action-buttons-container .fa {
  font-size: 12px;
}

.sub-action-buttons-container .w3-button {
  width: 100%;
  text-align: left !important;
}

.section-title-container {
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
  padding: 20px 10px 16px 10px;
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
