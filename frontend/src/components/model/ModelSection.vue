<template lang="html">

  <div v-if="section" class="section-container w3-border gray-1-border w3-round">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal v-if="showCardModal"
          :isNew="false"
          :initiativeId="initiativeId"
          :cardWrapperId="showCardId"
          :inSectionId="section.id"
          :inSectionTitle="section.Title"
          @close="showCardModal = false">
        </app-model-card-modal>
      </transition>
    </div>

    <div class=""
      draggable="true"
      @dragstart="dragStart($event)">

      <app-model-section-header
        :section="section"
        :level="level"
        :floating="floating"
        :inElementId="inElementId"
        :expand="expanded">
      </app-model-section-header>
    </div>


    <div v-if="expanded" class="w3-row expand-row w3-leftbar">
      <button
        class="w3-button light-grey w3-small"
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

        <div v-if="expanded" class="w3-row w3-leftbar subelements-container">

          <div class="w3-row-padding cards-container">
            <div v-for="(cardWrapper, ix) in section.cardsWrappers"
              :class="{'section-card-col': cardsAsCards, 'section-card-par': !cardsAsCards}"
              :key="cardWrapper.id"
              @dragover.prevent
              @drop.prevent="cardDroped(cardWrapper.id, $event)">

              <div v-if="cardsAsCards"
                @click="$emit('new-card', ix)" class="empty-div cursor-pointer w3-row w3-round">
                <div class="w3-right">
                  <i>add card here</i>
                </div>
              </div>

              <div class="">
                <app-model-card-with-modal
                  :cardWrapper="cardWrapper"
                  :initiativeId="initiativeId"
                  :inSectionId="section.id"
                  :inSectionTitle="section.title"
                  :cardEffect="cardsAsCards">
                </app-model-card-with-modal>
              </div>

            </div>
            <div class="last-add" :class="{'section-card-col': cardsAsCards, 'section-card-par': !cardsAsCards}"
              @dragover.prevent
              @drop.prevent="cardDroped('', $event)">

              <div class="gray-1-color" :class="{'w3-card-2': cardsAsCards}">
                <button class="w3-button" style="width: 100%"
                  @click="$emit('new-card')">
                  <i class="fa fa-plus w3-margin-right" aria-hidden="true"></i>
                  add card
                </button>
              </div>
            </div>
          </div>

          <div class="bottom-bar light-grey w3-small">
            <button v-if="!showSubsections"
              class="w3-button model-button show-sections-button"
              @click="showSubsections =true">
              <i class="w3-left fa fa-caret-right" aria-hidden="true"></i>
              <div class="w3-left button-text">
                <b>{{ section.nSubsections }}</b> subsections
              </div>
            </button>
            <div v-else class="w3-row light-grey">

              <div class="w3-left">
                <button class="w3-button" type="button" name="button"
                  @click="showSubsections = false">
                  <i class="w3-left fa fa-caret-up" aria-hidden="true"></i>
                  <div class="w3-left button-text">
                    hide subsections
                  </div>
                </button>
              </div>

              <div class="w3-left w3-margin-left">
                <button class="w3-button" type="button" name="button"
                  @click="expandSubSubsecInit = !expandSubSubsecInit">
                  <i class="w3-left fa fa-caret-up" aria-hidden="true"></i>
                  <div class="w3-left button-text">
                    <span v-if="!expandSubSubsecInit">expand all</span>
                    <span v-else>collapse all</span>
                  </div>
                </button>
              </div>

            </div>
          </div>

          <div class="" :class="{'slider-container': animatingSubsections}">
            <transition name="slideDownUp"
              @before-enter="animatingSubsections = true"
              @after-enter="animatingSubsections = false"
              @before-leave="animatingSubsections = true"
              @after-leave="animatingSubsections = false">

              <div v-if="showSubsections" class="subsections-container">
                <div v-for="subsection in section.subsections"
                  :key="subsection.id"
                  class="subsection-container"
                  @dragover.prevent
                  @drop="subsectionDroped(subsection.id, $event)">

                  <app-model-section-with-modal
                    :preloaded="subsection.subElementsLoaded"
                    :sectionInit="subsection"
                    :sectionId="subsection.id"
                    :initiativeId="initiativeId"
                    :inView="false"
                    :inElementId="section.id"
                    :inElementTitle="section.title"
                    :level="level + 1"
                    dragType="MOVE_SUBSECTION"
                    :cardsAsCardsInit="cardsAsCards"
                    :expandInit="expandSubSubsecInit">
                  </app-model-section-with-modal>
                </div>

                <div class="subsection-container gray-1-color w3-border w3-round"
                  @dragover.prevent
                  @drop="subsectionDroped('', $event)">

                  <button class="w3-button" style="width: 100%; text-align: left"
                    @click="$emit('new-subsection')">
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

    <div v-if="showExpandButton" class="w3-row expand-row w3-leftbar" :class="{'expand-row-collapsed': !expanded}">
      <button
        class="w3-button light-grey w3-small"
        @click="toggleExpand()">
        <span v-if="!expanded">
          <i class="fa fa-chevron-down" aria-hidden="true"></i> Expand
          <span v-if="section.nSubsections > 0 && section.nCards > 0"> (contains <b>{{ section.nSubsections }}</b> subsections and <b>{{ section.nCards }}</b> cards)</span>
          <span v-else>
            <span v-if="section.nSubsections > 0"> (contains <b>{{ section.nSubsections }}</b> subsections)</span>
            <span v-if="section.nCards > 0"> (contains <b>{{ section.nCards }}</b> cards)</span>
          </span>
        </span>
        <span v-else><i class="fa fa-chevron-up" aria-hidden="true"></i> Collapse "{{  section.title }}" section</span>
      </button>
    </div>

  </div>

</template>

<script>
import ModelSectionHeader from '@/components/model/ModelSectionHeader.vue'
import ModelCardModal from '@/components/model/modals/ModelCardModal.vue'

export default {
  name: 'app-model-section',

  components: {
    'app-model-section-header': ModelSectionHeader,
    'app-model-card-modal': ModelCardModal
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
    inView: {
      type: Boolean,
      default: false
    },
    inElementId: {
      type: String,
      default: ''
    },
    inElementTitle: {
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
    floating: {
      type: Boolean,
      default: false
    },
    cardsAsCards: {
      type: Boolean,
      default: true
    },
    forceUpdate: {
      type: Boolean,
      default: true
    },
    expandInit: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      section: null,
      showSubsections: false,
      showCards: false,
      expanded: false,
      animatingSubsections: false,
      showCardModal: false,
      showCardId: '',
      expandSubSubsecInit: false
    }
  },

  computed: {
    showExpandButton () {
      return true
    }
  },

  watch: {
    '$store.state.support.triggerUpdateModel' () {
      this.update()
    },
    '$route' () {
      this.checkCardSubroute()
    },
    forceUpdate () {
      this.update()
    },
    expandInit () {
      this.expanded = this.expandInit
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
        // console.log(response.data)
        this.section = response.data.data
        this.checkExpands()
      })
    },
    checkExpands () {
      if (this.section.nSubsections > 0 && this.section.cardsWrappers.length === 0) {
        this.showSubsections = true
      }
    },
    cardDroped (onCardWrapperId, event) {
      var dragData = JSON.parse(event.dataTransfer.getData('text/plain'))

      if (dragData.type === 'MOVE_CARD') {
        if (dragData.fromSectionId !== '') {
          /* move from section */
          this.axios.put('/1/initiative/' + this.initiativeId +
            '/model/section/' + dragData.fromSectionId +
            '/moveCard/' + dragData.cardWrapperId, {}, {
              params: {
                onSectionId: this.section.id,
                onCardWrapperId: onCardWrapperId
              }
            }).then((response) => {
              this.$store.commit('triggerUpdateModel')
            })
        } else {
          /* add existing card */
          this.axios.put('/1/initiative/' + this.initiativeId +
            '/model/section/' + this.section.id +
            '/cardWrapper/' + dragData.cardWrapperId, {}, {
              params: {
                beforeCardWrapperId: onCardWrapperId
              }
            }).then((response) => {
              this.$store.commit('triggerUpdateModel')
            })
        }
      }
    },
    dragStart (event) {
      var moveSectionData = {
        type: this.dragType,
        sectionId: this.section.id,
        fromView: this.inView,
        fromElementId: this.inElementId
      }
      event.dataTransfer.setData('text/plain', JSON.stringify(moveSectionData))
    },
    subsectionDroped (onSubsectionId, event) {
      var dragData = JSON.parse(event.dataTransfer.getData('text/plain'))
      var url = ''

      if (dragData.type === 'MOVE_SECTION') {
        /* move section to view section */
        url = '/1/initiative/' + this.initiativeId +
          '/model/view/' + dragData.fromElementId +
          '/moveSection/' + dragData.sectionId
      }

      if (dragData.type === 'MOVE_SUBSECTION') {
        url = '/1/initiative/' + this.initiativeId +
          '/model/section/' + dragData.fromElementId +
          '/moveSubsection/' + dragData.sectionId
      }

      if (url !== '') {
        this.axios.put(url, {}, {
          params: {
            onSectionId: this.section.id,
            onSubsectionId: onSubsectionId
          }
        }).then((response) => {
          this.$store.commit('triggerUpdateModel')
        })
      }
    },
    checkCardSubroute () {
      var found = false
      for (var ix in this.$route.matched) {
        if (this.$route.matched[ix].name === 'ModelCardInSection') {
          this.showCardId = this.$route.params.cardId
          this.showCardModal = true
          found = true
        }
      }
      if (!found) {
        this.showCardModal = false
      }
    }
  },

  created () {
    this.expanded = this.expandInit

    if (!this.preloaded) {
      this.section = {
        id: this.sectionId
      }
      this.update()
    } else {
      this.section = this.sectionInit
      this.checkExpands()
    }

    /* check if open card modal */
    this.checkCardSubroute()
  }
}
</script>

<style scoped>

.section-container {
}

.expand-row-collapsed {
  border-bottom-left-radius: 5px;
  border-bottom-right-radius: 5px;
  overflow: hidden;
}

.expand-row button {
  width: 100%;
  text-align: left;
  padding-left: 10px;
  padding-top: 5px;
  padding-bottom: 5px;
}

.cards-container {
  padding: 20px 10px 4px 10px;
  max-height: 500px;
  overflow: auto;
}

.section-card-col {
  margin-bottom: 0px;
  display: inline-block;
}

@media screen and (min-width: 1700px) {
  .section-card-col {
    width: calc(25% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

@media screen and (min-width: 1200px) and (max-width: 1699px) {
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

.empty-div {
  min-height: 22px;
  color: #d5d6d7;
  padding-right: 10px;
  margin: 3px 0px;
}

.last-add {
  margin-top: 16px;
  margin-bottom: 16px;
}

.empty-div:hover {
  background-color: #bfc1c3;
  color: white;
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
  width: 100%;
}

.subsections-container {
  padding-left: 20px;
  padding-top: 20px;
  padding-right: 10px;
}

.subsection-container {
  margin-bottom: 20px;
}

</style>
