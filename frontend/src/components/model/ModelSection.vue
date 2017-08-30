<template lang="html">
  <div class="">
    <div class="section-container w3-display-container"
      @mouseover="showActionButton = true"
      @mouseleave="showActionButton = false">

      <div class="section-title-container w3-border-top gray-1-border ">
        <div class="w3-row section-title w3-tag gray-1" :style="sectionTitleStyle">{{ section.title }}</div>
        <div class="w3-row w3-leftbar gray-1-border section-description w3-small light-grey w3-padding">
          {{ section.description }}
        </div>
      </div>

      <div class="slider-container">
        <transition name="slideDownUp">
          <div v-if="expanded" class="w3-row subelements-container">
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

            <div class="w3-leftbar bottom-bar light-grey w3-small">
              <button
                v-if="section.nSubsections > 0"
                class="w3-button model-button"
                @click="showSubsections = !showSubsections">
                <div v-if="showSubsections" >
                  <i class="w3-left fa fa-caret-up" aria-hidden="true"></i>
                  <div class="w3-left button-text">
                    hide subsections
                  </div>
                </div>
                <div v-else>
                  <i class="w3-left fa fa-caret-right" aria-hidden="true"></i>
                  <div class="w3-left button-text">
                    <b>{{ section.nSubsections }}</b> subsections
                  </div>
                </div>
              </button>
            </div>
            <div class="slider-container w3-leftbar">
              <transition name="slideDownUp">
                <div v-if="showSubsections" class="subsections-container" :style="subsectionsContainerStyle">
                  <app-model-section
                    v-for="subsection in section.subsections"
                    :key="subsection.id"
                    :preloaded="section.subElementsLoaded"
                    :sectionInit="subsection"
                    :initiativeId="initiativeId"
                    :level="level + 1"
                    class="subsection-container"
                    @show-section-modal="$emit('show-section-modal', $event)"
                    @show-card-modal="$emit('show-card-modal', $event)">
                  </app-model-section>
                </div>
              </transition>
            </div>
          </div>
        </transition>
      </div>

      <div class="w3-row expand-row">
        <button
          class="w3-button gray-1 w3-small"
          @click="toggleExpand()">
          <span v-if="!expanded">Expand (contains <b>{{ section.nSubsections }}</b> subsections and <b>{{ section.nCards }}</b> cards)</span>
          <span v-else>Collapse "{{  section.title }}" section</span>
        </button>
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
    sectionInit: {
      type: Object,
      default: null
    },
    preloaded: {
      type: Boolean,
      default: false
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
      section: null,
      showSubsections: false,
      showActionButton: false,
      showSubActionButtons: false,
      cardsAsCards: true,
      expanded: false
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
    toggleExpand () {
      if (!this.expanded) {
        if (!this.preloaded) {
          this.update()
        }
        this.expanded = true
      } else {
        this.expanded = false
      }
    },
    update () {
      this.axios.get('/1/secured/initiative/' + this.initiativeId + '/model/section/' + this.section.id).then((response) => {
        this.section = response.data.data
      })
    },
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
  },

  created () {
    if (this.load) {
      this.update()
    } else {
      this.section = this.sectionInit
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
  border-top-width: 2px !important;
}

.section-title {
  margin-bottom: 0px;
}

.expand-row button {
  width: 100%;
  text-align: left;
  padding-left: 10px;
  padding-top: 5px;
  padding-bottom: 5px;
}

.cards-container {
  padding: 20px 10px 16px 10px;
}

.section-card {
  margin-top: 0px;
  margin-bottom: 6px;
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
