<template lang="html">

  <div v-if="section" class="section-container w3-border gray-1-border w3-round" ref="sectionContainer">

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
        :expand="expanded"
        :showMessagesIndicator="true"
        @show-messages="$emit('show-messages')">
      </app-model-section-header>
    </div>

    <div v-if="expanded" class="w3-row expand-row w3-leftbar w3-border-bottom w3-border-top">
      <div class="w3-col l4">
        <button class="w3-button light-grey w3-small"
          @click="toggleExpand()">
          <span><i class="fa fa-chevron-up" aria-hidden="true"></i> Collapse section</span>
        </button>
      </div>
      <div class="w3-col l4 w3-border-left">
        <div class="w3-row">
          <div class="w3-col s6">
            <button class="w3-button light-grey w3-small" :disabled="nCardWrappers === 0"
              @click="showCards =!showCards">
              <span v-if="showCards"><i class="fa fa-chevron-up" aria-hidden="true"></i> Hide cards</span>
              <span v-if="!showCards && nCardWrappers > 0"><i class="fa fa-chevron-down" aria-hidden="true"></i> Show <b>{{ nCardWrappers }}</b> cards</span>
              <span v-if="!showCards && nCardWrappers === 0">No cards</span>
            </button>
          </div>
          <div class="w3-col s6">
            <button class="w3-button light-grey w3-small"
              @click="newCardFromBar()">
              <i class="fa fa-plus" aria-hidden="true"></i> new card
            </button>
          </div>
        </div>
      </div>
      <div class="w3-col l4 w3-border-left">
        <div class="w3-row">
          <div class="w3-col s6">
            <button class="w3-button light-grey w3-small" :disabled="section.nSubsections === 0"
              @click="showSubsections =!showSubsections">
              <span v-if="showSubsections"><i class="fa fa-chevron-up" aria-hidden="true"></i> Hide subsections</span>
              <span v-if="!showSubsections && section.nSubsections > 0"><i class="fa fa-chevron-down" aria-hidden="true"></i> Show <b>{{ section.nSubsections }}</b> subsections</span>
              <span v-if="!showSubsections && section.nSubsections === 0">no subsections</span>
            </button>
          </div>
          <div class="w3-col s6">
            <button class="w3-button light-grey w3-small"
              @click="newSubsectionFromBar()">
              <i class="fa fa-plus" aria-hidden="true"></i> new subsection
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="gray-1-border slider-container">

        <div v-if="expanded" class="w3-row w3-leftbar subelements-container">
          <div class="w3-col">

            <div v-if="section.cardsWrappers.length > 0 && showSubsections && !showCards" class="w3-center w3-margin-top">
              <button class="w3-button light-grey w3-small w3-round"
                @click="showCards =!showCards">
                <span v-if="showCards"><i class="fa fa-chevron-up" aria-hidden="true"></i> Hide cards</span>
                <span v-else="showCards"><b>{{ section.cardsWrappers.length }}</b> cards hidden</span>
              </button>
            </div>

            <div class="w3-row slider-container">
              <transition name="slideDownUp">
                <div v-if="showCards" class="cards-container">
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
                  <div v-if="cardsAsCards" class="last-add section-card-col"
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
              </transition>
            </div>

            <div v-if="section.nSubsections > 0 && showCards && !showSubsections" class="w3-center w3-margin-bottom">
              <button  class="w3-button light-grey w3-small w3-round"
                @click="showSubsections =!showSubsections">
                <span><b>{{ section.nSubsections }}</b> subsections hidden</span>
              </button>
            </div>

            <div v-if="section.nSubsections > 0 && showSubsections" class="w3-row subsection-controls-row">
              <div class="w3-tag gray-1 w3-small w3-left cursor-pointer"
                @click="expandSubSubsecInit =!expandSubSubsecInit">
                {{ expandSubSubsecInit ? 'collapse' : 'expand' }} all subsections
              </div>
            </div>

            <div class="slider-container">
              <transition name="slideDownUp">

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
        </div>
      </transition>
    </div>

    <div v-if="!expanded" class="w3-row expand-row w3-leftbar expand-row-collapsed">
      <button class="w3-button light-grey w3-small"
        @click="toggleExpand()">
        <span>
          <i class="fa fa-chevron-down" aria-hidden="true"></i> Expand
          <span v-if="section.nSubsections > 0 && section.nCards > 0"> (contains <b>{{ section.nSubsections }}</b> subsections and <b>{{ section.nCards }}</b> cards)</span>
          <span v-else>
            <span v-if="section.nSubsections > 0"> (contains <b>{{ section.nSubsections }}</b> subsections)</span>
            <span v-if="section.nCards > 0"> (contains <b>{{ section.nCards }}</b> cards)</span>
          </span>
        </span>
      </button>
    </div>
    <div v-if="expanded && (showCards || showSubsections)" class="w3-row expand-row w3-leftbar">
      <button class="w3-button light-grey w3-small"
        @click="toggleExpand()">
        <span><i class="fa fa-chevron-up" aria-hidden="true"></i> Collapse "{{  section.title }}" section</span>
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
    basicPreloaded: {
      type: Boolean,
      default: false
    },
    subElementsPreloaded: {
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
      loaded: false,
      showSubsections: true,
      showCards: true,
      expanded: false,
      showCardModal: false,
      showCardId: '',
      expandSubSubsecInit: false
    }
  },

  computed: {
    nCardWrappers () {
      if (this.section) {
        if (this.section.cardsWrappers) {
          return this.section.cardsWrappers.length
        } else {
          return 0
        }
      } else {
        return 0
      }
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
      if (this.expandInit) {
        this.startExpand()
      } else {
        this.startCollapse()
      }
    }
  },

  methods: {
    toggleExpand () {
      if (!this.expanded) {
        if (!this.loaded) {
          this.update(true)
        } else {
          this.startExpand()
        }
      } else {
        this.startCollapse()
      }
    },
    startExpand () {
      this.showCards = false
      this.showSubsections = false
      this.expanded = true

      this.$nextTick(() => {
        if (this.nCardWrappers > 0) {
          this.showCards = true
          setTimeout(() => {
            if (this.section.nSubsections > 0) {
              this.showSubsections = true
            }
          }, 350)
        } else {
          this.showSubsections = true
        }
      })
    },
    startCollapse () {
      if (this.showSubsections) {
        this.showSubsections = false
        setTimeout(() => {
          if (this.showCards) {
            this.showCards = false
            setTimeout(() => {
              this.expanded = false
            }, 500)
          } else {
            this.expanded = false
          }
        }, 500)
      } else {
        if (this.showCards) {
          this.showCards = false
          setTimeout(() => {
            this.expanded = false
          }, 500)
        } else {
          this.expanded = false
        }
      }
    },
    update (expandFlag) {
      expandFlag = expandFlag || false
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/section/' + this.section.id, {
        params: {
          level: 1
        }
      }).then((response) => {
        // console.log(response.data)
        this.loaded = true
        this.section = response.data.data
        this.$emit('updated', this.section)
        if (expandFlag) {
          this.startExpand()
        }
      })
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
          /* only open the model when rendering the section in the url */
          if (this.$route.params.sectionId === this.section.id) {
            this.showCardId = this.$route.params.cardId
            this.showCardModal = true
            found = true
          }
        }
      }
      if (!found) {
        this.showCardModal = false
      }
    },
    newCardFromBar () {
      this.showCards = true
      this.$emit('new-card')
    },
    newSubsectionFromBar () {
      this.showSubsections = true
      this.$emit('new-subsection')
    }
  },

  created () {
    if (!this.basicPreloaded) {
      this.section = {
        id: this.sectionId
      }
      this.update()
    } else {
      this.section = this.sectionInit
      if (this.subElementsPreloaded) {
        this.loaded = true
      } else {
        this.update()
      }
    }

    if (this.expandInit) {
      this.toggleExpand()
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
  text-align: center;
  padding-left: 10px;
  padding-top: 5px;
  padding-bottom: 5px;
}

.cards-container {
  padding: 6px 10px 22px 10px;
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

.subsection-controls-row {
  padding-left: 16px;
  margin-top: 6px;
  margin-bottom: 6px;
}

.subsections-container {
  padding-left: 16px;
  padding-top: 12px;
  padding-right: 10px;
}

.subsection-container {
  margin-bottom: 20px;
}

</style>
