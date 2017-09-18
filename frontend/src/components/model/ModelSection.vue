<template lang="html">
  <div class="">
    <div v-if="section" class="section-container w3-display-container"
      @mouseover="showActionButton = true"
      @mouseleave="showActionButton = false">

      <div class="section-title-container w3-border-top w3-border-right gray-1-border ">
        <router-link tag="a" :to="{ name: 'ModelSection', params: { sectionId: section.id } }" class="w3-row section-title w3-tag gray-1 cursor-pointer" :style="sectionTitleStyle">{{ section.title }}</router-link>
        <div class="w3-row w3-leftbar gray-1-border section-description w3-small light-grey w3-padding">
          {{ section.description }}
        </div>
      </div>

      <div class="gray-1-border" :class="{'w3-border-bottom': !showExpandButton, 'slider-container': animatingSubsections}">
        <transition name="slideDownUp"
          @before-enter="animatingSubsections = true"
          @after-enter="animatingSubsections = false"
          @before-leave="animatingSubsections = true"
          @after-leave="animatingSubsections = false">
          <!-- hooks are needed to enable overflow when not animating -->

          <div v-if="expanded" class="w3-row w3-leftbar w3-border-right subelements-container">

            <div class="w3-row expand-row">
              <button
                class="w3-button gray-1 w3-small"
                @click="toggleExpand()">
                <span><i class="fa fa-chevron-up" aria-hidden="true"></i> Collapse</span>
              </button>
            </div>

            <div class="w3-row-padding cards-container">
              <app-model-card
                v-for="cardWrapper in section.cardsWrappers"
                :key="cardWrapper.id"
                :cardWrapper="cardWrapper"
                :initiativeId="initiativeId"
                :sectionId="section.id"
                :cardEffect="cardsAsCards"
                class=""
                :class="{'section-card-col': cardsAsCards, 'section-card-par': !cardsAsCards}"
                @show-card-modal="$emit('show-card-modal', $event)">
              </app-model-card>
              <div :class="{'section-card-col': cardsAsCards, 'section-card-par': !cardsAsCards}">
                <div class="gray-1-color" :class="{'w3-card-2': cardsAsCards}">
                  <button class="w3-button" style="width: 100%"
                    @click="newCard()">
                    <i class="fa fa-plus w3-margin-right" aria-hidden="true"></i>
                    add card
                  </button>
                </div>
              </div>
            </div>

            <div class="bottom-bar light-grey w3-small">
              <button v-if="section.nSubsections > 0"
                class="w3-button model-button show-sections-button"
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
              <div v-else class="">
                <button class="w3-button" style="width: 100%; text-align: left"
                  @click="newSubsection()">
                  <i class="fa fa-plus w3-margin-right" aria-hidden="true"></i>
                  add subsection
                </button>
              </div>
            </div>

            <div class="" :class="{'slider-container': animatingSubsections}">
              <transition name="slideDownUp"
                @before-enter="animatingSubsections = true"
                @after-enter="animatingSubsections = false"
                @before-leave="animatingSubsections = true"
                @after-leave="animatingSubsections = false">

                <div v-if="showSubsections" class="subsections-container" :style="subsectionsContainerStyle">
                  <app-model-section
                    v-for="subsection in section.subsections"
                    :key="subsection.id"
                    :preloaded="subsection.subElementsLoaded"
                    :sectionInit="subsection"
                    :sectionId="subsection.id"
                    :initiativeId="initiativeId"
                    :level="level + 1"
                    class="subsection-container"
                    @show-section-modal="$emit('show-section-modal', $event)"
                    @show-card-modal="$emit('show-card-modal', $event)">
                  </app-model-section>

                  <div class="subsection-container gray-1-color w3-border">
                    <button class="w3-button" style="width: 100%; text-align: left"
                      @click="newSubsection()">
                      <i class="fa fa-plus w3-margin-right" aria-hidden="true"></i>
                      add subsection
                    </button>
                  </div>
                </div>
              </transition>
            </div>
          </div>
        </transition>
      </div>

      <div v-if="showExpandButton" class="w3-row expand-row">
        <button
          class="w3-button gray-1 w3-small"
          @click="toggleExpand()">
          <span v-if="!expanded">
            <i class="fa fa-chevron-down" aria-hidden="true"></i> Expand (contains
            <span v-if="section.nSubsections > 0 && section.nCards > 0"><b>{{ section.nSubsections }}</b> subsections and <b>{{ section.nCards }}</b> cards)</span>
            <span v-else>
              <span v-if="section.nSubsections > 0"><b>{{ section.nSubsections }}</b>)</span>
              <span v-if="section.nCards > 0"><b>{{ section.nCards }}</b> cards)</span>
            </span>
          </span>
          <span v-else><i class="fa fa-chevron-up" aria-hidden="true"></i> Collapse "{{  section.title }}" section</span>
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
            v-if="isLoggedAnEditor"
            class="w3-button model-action-button gray-1-color w3-right"
            @click="showSubActionButtons = !showSubActionButtons">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
          </div>
          <div v-if="showSubActionButtons" class="sub-action-buttons-container w3-card w3-white">
            <div class="w3-button" @click="newCard()">
              <i class="fa fa-plus w3-margin-right" aria-hidden="true"></i> add card
            </div>
            <div class="w3-button" @click="newSubsection()">
              <i class="fa fa-plus w3-margin-right" aria-hidden="true"></i> add sub-section
            </div>
          </div>
          <div v-if="expanded" class="w3-button model-action-button gray-1-color w3-right"
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
    sectionId: {
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
      expanded: false,
      animatingSubsections: false
    }
  },

  computed: {
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    showExpandButton () {
      return ((this.section.nSubsections > 0) || (this.section.nCards > 0))
    },
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

  watch: {
    '$store.state.support.triggerUpdateModel' () {
      this.update()
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
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/section/' + this.section.id).then((response) => {
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
    if (!this.preloaded) {
      this.section = {
        id: this.sectionId
      }
      this.update()
    } else {
      this.section = this.sectionInit
    }
  }
}
</script>

<style scoped>

.section-container {
}

.action-buttons-container {
  margin-top: 2px;
  margin-right: 1px;
  z-index: 90;
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

.section-card-col {
  margin-bottom: 22px;
  display: inline-block;
}

@media screen and (min-width: 1200px) {
  .section-card-col {
    width: calc(33% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

@media screen and (min-width: 992px) and (max-width: 1199px) {
  .section-card-col {
    width: calc(50% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

.section-card-par {
  margin-bottom: 16px;
}

.bottom-bar .fa {
  margin-top: 5px;
  font-size: 16px !important;
  margin-right: 5px;
}

.bottom-bar .button-text {
  padding-top: 5px;
}

.show-sections-button {
  width: 180px;
}

.subsections-container {
  padding-top: 20px;
  padding-right: 10px;
}

.subsection-container {
  margin-bottom: 20px;
}

</style>
