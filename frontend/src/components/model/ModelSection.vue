<template lang="html">
  <div class="">

    <div v-if="section" class="section-container w3-display-container"
      @mouseover="showActionButton = true"
      @mouseleave="showActionButton = false">

      <div class="section-title-container w3-leftbar w3-border-top w3-border-right gray-1-border light-grey"
        draggable="true"
        @dragstart="dragStart($event)">

        <div class="w3-row title-row">
          <router-link tag="a" :to="{ name: 'ModelSection', params: { sectionId: section.id } }"
            class="section-title cursor-pointer w3-left" :style="sectionTitleStyle">
            {{ section.title }}
          </router-link>
        </div>

        <div v-if="floating" class="w3-row">
          <div class="w3-left">
            <i>in:</i>
          </div>
          <div v-for="parentSection in section.inSections" class="in-tag-container w3-left">
            <router-link :to="{ name: 'ModelSection', params: { sectionId: parentSection.id } }"
              class="gray-1 w3-tag w3-round w3-small">
              {{ parentSection.title }}
            </router-link>
          </div>
          <div v-for="parentView in section.inViews" class="in-tag-container w3-left">
            <router-link :to="{ name: 'ModelView', params: { viewId: parentView.id } }"
              class="gray-1 w3-tag w3-round w3-small">
              {{ parentView.title }}
            </router-link>
          </div>
        </div>

        <div class="w3-row gray-1-border section-description w3-small">
          <vue-markdown class="marked-text" :source="section.description"></vue-markdown>
        </div>

        <div v-if="!floating" class="w3-row w3-small also-in-row">
          <div v-if="sectionIsInOtherPlaces" class="">
            <div v-for="parentSection in section.inSections" class="in-tag-container w3-right">
              <div v-if="parentSection.id !== parentSectionId" class="">
                <router-link :to="{ name: 'ModelSection', params: { sectionId: parentSection.id } }"
                  class="gray-1 w3-tag w3-round w3-small">
                  {{ parentSection.title }}
                </router-link>
              </div>
            </div>
            <div v-for="parentView in section.inViews" class="in-tag-container w3-right">
              <div v-if="parentView.id !== viewId" class="">
                <router-link :to="{ name: 'ModelView', params: { viewId: parentView.id } }"
                  class="gray-1 w3-tag w3-round w3-small">
                  {{ parentView.title }}
                </router-link>
              </div>
            </div>
            <div class="w3-right">
              <i>also in:</i>
            </div>
          </div>
        </div>

      </div>

      <div v-if="expanded" class="w3-row expand-row">
        <button
          class="w3-button gray-1 w3-small"
          @click="toggleExpand()">
          <span><i class="fa fa-chevron-up" aria-hidden="true"></i> Collapse</span>
        </button>
      </div>

      <div class="gray-1-border" :class="{'w3-border-bottom': !showExpandButton, 'slider-container': animatingSubsections}">
        <transition name="slideDownUp"
          @before-enter="animatingSubsections = true"
          @after-enter="animatingSubsections = false"
          @before-leave="animatingSubsections = true"
          @after-leave="animatingSubsections = false">
          <!-- hooks are needed to enable overflow when not animating -->

          <div v-if="expanded" class="w3-row w3-leftbar w3-border-right subelements-container">

            <div v-if="section.cardsWrappers.length > 0" class="w3-row-padding cards-container">
              <div v-for="cardWrapper in section.cardsWrappers"
                :class="{'section-card-col': cardsAsCards, 'section-card-par': !cardsAsCards}"
                :key="cardWrapper.id"
                @dragover.prevent
                @drop="cardDroped(cardWrapper.id, $event)">

                <app-model-card
                  :cardWrapper="cardWrapper"
                  :initiativeId="initiativeId"
                  :sectionId="section.id"
                  :cardEffect="cardsAsCards"
                  @show-card-modal="$emit('show-card-modal', $event)">
                </app-model-card>
              </div>
              <div :class="{'section-card-col': cardsAsCards, 'section-card-par': !cardsAsCards}"
                @dragover.prevent
                @drop="cardDroped('', $event)">

                <div class="gray-1-color" :class="{'w3-card-2': cardsAsCards}">
                  <button class="w3-button" style="width: 100%"
                    @click="newCard()">
                    <i class="fa fa-plus w3-margin-right" aria-hidden="true"></i>
                    add card
                  </button>
                </div>
              </div>
            </div>

            <div v-if="section.nSubsections > 0 && section.cardsWrappers.length > 0" class="bottom-bar light-grey w3-small">
              <button
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
            </div>

            <div class="" :class="{'slider-container': animatingSubsections}">
              <transition name="slideDownUp"
                @before-enter="animatingSubsections = true"
                @after-enter="animatingSubsections = false"
                @before-leave="animatingSubsections = true"
                @after-leave="animatingSubsections = false">

                <div v-if="showSubsections" class="subsections-container" :style="subsectionsContainerStyle">
                  <div v-for="subsection in section.subsections"
                    :key="subsection.id"
                    class="subsection-container"
                    @dragover.prevent
                    @drop="subsectionDroped(subsection.id, $event)">

                    <app-model-section
                      :preloaded="subsection.subElementsLoaded"
                      :sectionInit="subsection"
                      :sectionId="subsection.id"
                      :parentSectionId="section.id"
                      :initiativeId="initiativeId"
                      :level="level + 1"
                      dragType="MOVE_SUBSECTION"
                      @show-section-modal="$emit('show-section-modal', $event)"
                      @show-card-modal="$emit('show-card-modal', $event)">
                    </app-model-section>
                  </div>

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
        <div v-if="showActionButton && !asListItem" class="w3-display-topright action-buttons-container"
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
    parentSectionId: {
      type: String,
      default: ''
    },
    viewId: {
      type: String,
      default: ''
    },
    level: {
      type: Number,
      default: 0
    },
    dragType: {
      type: String
    },
    asListItem: {
      type: Boolean,
      default: false
    },
    floating: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      section: null,
      showSubsections: false,
      showCards: false,
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
      if (!this.asListItem) {
        return ((this.section.nSubsections > 0) || (this.section.nCards > 0))
      } else {
        return false
      }
    },
    sectionTitleStyle () {
      var fontsize = this.level < 5 ? 22 - 4 * this.level : 12
      if (!this.asListItem) {
        return {'font-size': fontsize + 'px'}
      } else {
        return {'font-size': '22px'}
      }
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
    },
    sectionIsInOtherPlaces () {
      var ix
      for (ix in this.section.inSections) {
        if (this.section.inSections[ix].id !== this.parentSectionId) {
          return true
        }
      }

      for (ix in this.section.inViews) {
        if (this.section.inViews[ix].id !== this.viewId) {
          return true
        }
      }

      return false
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
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/section/' + this.section.id, {
        params: {
          level: 1
        }
      }).then((response) => {
        this.section = response.data.data
        this.checkExpands()
      })
    },
    checkExpands () {
      if (this.section.nSubsections > 0 && this.section.cardsWrappers.length === 0) {
        this.showSubsections = true
      }
    },
    expandSectionModal () {
      this.$emit('show-section-modal', {
        isSubsection: true,
        parentSectionId: this.parentSectionId,
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
    },
    cardDroped (onCardWrapperId, event) {
      var dragData = JSON.parse(event.dataTransfer.getData('text/plain'))

      if (dragData.type === 'MOVE_CARD') {
        var url = '/1/initiative/' + this.initiativeId +
        '/model/section/' + dragData.fromSectionId +
        '/moveCard/' + dragData.cardWrapperId

        this.axios.put(url, {}, {
          params: {
            onSectionId: this.section.id,
            onCardWrapperId: onCardWrapperId
          }
        }).then((response) => {
          this.$store.commit('triggerUpdateModel')
        })
      }
    },
    dragStart (event) {
      var moveSectionData = {
        type: this.dragType,
        sectionId: this.section.id,
        fromSectionId: this.parentSectionId,
        fromViewId: this.viewId
      }
      event.dataTransfer.setData('text/plain', JSON.stringify(moveSectionData))
    },
    subsectionDroped (onSubsectionId, event) {
      var dragData = JSON.parse(event.dataTransfer.getData('text/plain'))
      var url = ''

      if (dragData.type === 'MOVE_SECTION') {
        /* move section to view section */
        url = '/1/initiative/' + this.initiativeId +
        '/model/view/' + dragData.fromViewId +
        '/moveSection/' + dragData.sectionId

        this.axios.put(url, {}, {
          params: {
            onSectionId: this.section.id,
            onSubsectionId: onSubsectionId
          }
        }).then((response) => {
          this.$store.commit('triggerUpdateModel')
        })
      }

      if (dragData.type === 'MOVE_SUBSECTION') {
        /* move section to view section */
        url = '/1/initiative/' + this.initiativeId +
        '/model/section/' + dragData.fromSectionId +
        '/moveSubsection/' + dragData.sectionId

        this.axios.put(url, {}, {
          params: {
            onSectionId: this.section.id,
            onSubsectionId: onSubsectionId
          }
        }).then((response) => {
          this.$store.commit('triggerUpdateModel')
        })
      }
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
      this.checkExpands()
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
  padding-top: 8px;
  padding-left: 8px;
  padding-right: 8px;
  padding-bottom: 4px;
}

.title-row {
  margin-bottom: 4px;
}

.also-in-row {
  margin-top: 8px;
}

.section-title:hover {
  color: #15a5cc;
}

.in-tag-container {
  display: inline-block;
  margin-left: 5px;
  margin-bottom: 5px;
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

@media screen and (max-width: 991px) {
  .section-card-col {
    width: calc(100% - 16px);
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
