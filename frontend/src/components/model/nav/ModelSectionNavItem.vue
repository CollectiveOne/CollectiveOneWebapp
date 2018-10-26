<template lang="html">

  <div class="w3-row section-nav-item-container">
    <div class="w3-row section-nav-item-first-row"
      :class="rowClass"
      :draggable="$store.state.support.triggerSectionDraggingState"
      @dragstart="dragStart($event)"
      @dragover.prevent="draggingOver($event)"
      @dragleave.prevent="draggingLeave()"
      @drop.prevent="dragDroped($event)">

      <div v-if="draggingOverWithSectionSameLevelFlag" class="dragging-same-level-clue">

      </div>

      <div :class="arrowAndTitleClass" class="arrow-and-title">
        <div @click="toggleSubsections()" class="circle-div cursor-pointer">
          <div v-if="hasSubsections">
            <i v-if="!showSubsections" class="fa fa-chevron-circle-right" aria-hidden="true"></i>
            <i v-else class="fa fa-chevron-circle-down" aria-hidden="true"></i>
          </div>
          <div v-else>
            <i @click="sectionSelected()" class="fa fa-circle" aria-hidden="true"></i>
          </div>
        </div>
        <div @click="sectionSelected()" @dblclick="toggleSubsections()"
          class="title-div cursor-pointer noselect" :class="titleClass"
          @mouseover="hoveringOnTitle = true"
          @mouseenter="hoveringOnTitle = true"
          @mouseleave="hoveringOnTitle = false">
          {{ sectionTitle }}
        </div>
      </div>

      <div v-if="isSelected && isCardsView" class="zoom-controls">
        <div @click="levelDown()" class="w3-left cursor-pointer arrow-div">
          <img src="./../../../assets/zoom-in-icon.svg" alt="">
        </div>

        <div @click="levelUp()" class="w3-left cursor-pointer arrow-div">
          <img src="./../../../assets/zoom-out-icon.svg" alt="">
        </div>

        <div class="levels-div-container">
          <transition name="slideDownUp">
            <div v-if="showLevel" class="levels-div noselect">
              {{ levels }}
            </div>
          </transition>
        </div>
      </div>

      <div v-if="section" class="notification-div">
        <app-notifications-list
          :element="section"
          :forceUpdate="forceUpdateNotifications"
          :isSelected="isSelected">
        </app-notifications-list>
      </div>

      <div v-if="$store.state.user.authenticated && !draggingEnabled"
        class="control-div" :class="{'fa-button': !parentIsSelected, 'fa-button-dark': parentIsSelected || highlight}">

        <app-section-control-buttons
          :section="section"
          :inSection="inSection"
          :draggable="$store.state.support.triggerSectionDraggingState"
          @section-removed="sectionRemoved()"
          @addCard="addCard">
        </app-section-control-buttons>
      </div>

      <div v-if="draggingEnabled" class="drag-message-div">
        <span><i class="fa fa-arrows" aria-hidden="true"></i></span>
      </div>
    </div>

    <div :class="{'slider-container': animating}">
      <transition name="slideDownUp"
        @before-enter="animating = true"
        @after-enter="animating = false"
        @before-leave="animating = true"
        @after-leave="animating = false">

        <div v-if="showSubsections && subsectionsDataFiltered.length > 0" class="w3-row subsections-container"
          :class="{'subsection-container-selected': highlight}" >
          <app-model-section-nav-item
            class="subsection-row"
            v-for="(subsectionData, ix) in subsectionsDataFiltered"
            :inSection="section"
            :sectionData="subsectionData"
            :key="subsectionData.section.id"
            :highlightLevel="highlightLevelUse - 1"
            :parentIsSelected="isSelected || parentIsSelected"
            @section-selected="$emit('section-selected', $event)">
          </app-model-section-nav-item>
        </div>
      </transition>
    </div>
  </div>

</template>

<script>
import NotificationsList from '@/components/notifications/NotificationsList.vue'
import SectionControlButtons from '@/components/model/SectionControlButtons'
import ModelSectionModal from '@/components/model/modals/ModelSectionModal'

export default {
  name: 'app-model-section-nav-item',

  components: {
    'app-section-control-buttons': SectionControlButtons,
    'app-model-section-modal': ModelSectionModal,
    'app-notifications-list': NotificationsList
  },

  props: {
    sectionData: {
      type: Object,
      default: null
    },
    inSection: {
      type: Object,
      default: null
    },
    highlightLevel: {
      type: Number,
      default: 0
    },
    parentIsSelected: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      showSubsections: false,
      subsectionsDataFiltered: [],
      animating: false,
      draggingOverWithCardFlag: false,
      draggingOverWithSectionSameLevelFlag: false,
      draggingOverWithSectionInsideFlag: false,
      resetIntervalId: 0,
      forceUpdateNotifications: false,
      subscription: null,
      hoveringOnTitle: false,
      showLevel: false
    }
  },

  watch: {
    '$store.state.sectionsTree.triggerUpdateExpands' () {
      // console.log('checking expand subsections due to triggerUpdateExpands in ' + this.section.title + ' id:' + this.section.id)
      this.checkExpandSubsections()
      this.updateSubsectionsDataFiltered()
    },
    levels () {
      this.checkExpandSubsections()
    },
    section () {
      this.subscribeSocket()
    },
    sectionData () {
      this.updateSubsectionsDataFiltered()
    }
  },

  computed: {
    section () {
      if (this.sectionData) {
        return this.sectionData.section
      }
      return null
    },
    showPrivate () {
      return this.$store.state.viewParameters.showPrivateSections
    },
    showShared () {
      return this.$store.state.viewParameters.showSharedSections
    },
    showCommon () {
      return this.$store.state.viewParameters.showCommonSections
    },
    highlight () {
      return this.highlightLevelUse > 0
    },
    rowClass () {
      let thisClasses = {
        'section-selected': this.highlight,
        'dragging-over-with-card': this.draggingOverWithCardFlag,
        'dragging-over-with-section-same-level': this.draggingOverWithSectionSameLevelFlag,
        'dragging-over-with-section-inside': this.draggingOverWithSectionInsideFlag
      }

      if (this.section) {
        switch (this.section.scope) {
          case 'PRIVATE':
          thisClasses['border-red'] = true
          break

          case 'SHARED':
          thisClasses['border-yellow'] = true
          break

          default:
          thisClasses['border-blue'] = true
          break
        }
      }

      return thisClasses
    },
    arrowAndTitleClass () {
      let thisClasses = {}

      if (this.highlight) {
        thisClasses['title-selected'] = true
      } else {
        if (this.parentIsSelected) {
          thisClasses['title-inside-section-selected'] = true
        }
      }

      return thisClasses
    },
    titleClass () {
      return {
        'title-hover': this.hoveringOnTitle && !this.highlight
      }
    },
    sectionTitle () {
      if (this.section) {
        return this.section.title
        // return this.section.isTopModelSection ? 'All' : this.section.title
      }
      return ''
    },
    hasSubsections () {
      return this.subsectionsDataFiltered.length > 0
    },
    isSelected () {
      if (this.section) {
        return this.$route.params.sectionId === this.section.id
      }
      return false
    },
    levels () {
      return this.$store.getters.getActualLevels
    },
    infiniteLevels () {
      return this.levels === 999
    },
    highlightLevelUse () {
      if (this.isSelected) {
        /* if this is the selected section, start counting from levels */
        return this.levels
      } else {
        /* if this is not the selected section, decrease the highlightLevel property */
        return this.highlightLevel
      }
    },
    draggingEnabled () {
      return this.$store.state.support.triggerSectionDraggingState
    },
    popperOptions () {
      return {
        placement: 'bottom',
        modifiers: {
          preventOverflow: {
            enabled: true,
            boundariesElement: 'viewport'
          }
        }
      }
    },
    isCardsView () {
      return this.$route.name === 'ModelSectionCards'
    }
  },

  methods: {
    addCard () {
      if (!this.isSelected) {
        this.$router.push({name: 'ModelSectionCards', params: {sectionId: this.section.id}, query: {createCard: this.section.id}})
      }
    },
    updateSubsectionsDataFiltered () {
      if (this.sectionData) {
        let subsectionsDataFiltered = this.sectionData.subsectionsData.filter((subsectionData) => {
          switch (subsectionData.section.scope) {
            case 'PRIVATE':
              return this.showPrivate

            case 'SHARED':
              return this.showShared

            case 'COMMON':
              return this.showCommon
          }
          return true
        })

        this.subsectionsDataFiltered = subsectionsDataFiltered
      } else {
        this.subsectionsDataFiltered = []
      }
    },
    toggleSubsections () {
      if (this.showSubsections) {
        this.collapseSubsections()
      } else {
        this.expandSubsections()
      }
    },
    expandSubsections () {
      this.showSubsections = true
      this.$store.dispatch('expandSubsection', this.sectionData.coordinate)
    },
    collapseSubsections () {
      this.showSubsections = false
      this.$store.dispatch('collapseSubsection', this.sectionData.coordinate)
    },
    checkExpandSubsections () {
      if (this.highlightLevelUse > 1) {
        if (!this.infiniteLevels) {
          console.log('expanding subsections of ' + this.section.title + ' id: ' + this.section.id + ' due to levels')
          this.expandSubsections()
        }
      } else {
        if (this.sectionData) {
          if (this.sectionData.expand && !this.showSubsections) {
            console.log('expanding subsections of ' + this.section.title + ' id: ' + this.section.id + ' due to expand flag')
            this.expandSubsections()
          }
        }
      }
    },
    sectionSelected () {
      this.$emit('section-selected', this.section)
      if (this.section) {
        this.$router.push({name: 'ModelSectionContent', params: {sectionId: this.section.id}})
      }
    },
    levelDown () {
      this.showLevel = true
      setTimeout(() => {
        this.showLevel = false
      }, 1000)
      this.$store.commit('levelDown')
    },
    levelUp () {
      this.showLevel = true
      setTimeout(() => {
        this.showLevel = false
      }, 1000)
      this.$store.commit('levelUp')
    },
    updateInTree () {
      // if (this.section) {
      //   this.$store.dispatch('updateSectionDataInTree', {sectionId: this.section.id})
      // }
    },
    updateParentInTree () {
      if (this.inSection) {
        this.$store.dispatch('updateSectionDataInTree', {sectionId: this.inSection.id})
      }
    },
    sectionRemoved () {
      if (this.isSelected) {
        this.$router.push({name: 'ModelSectionContent', params: {sectionId: this.inSection.id}})
      }
      this.updateParentInTree()
    },
    subscribeSocket () {
      if (this.subscription === null) {
        this.subscription = this.$store.dispatch('subscribe', {
          url: '/channel/activity/model/section/' + this.section.id,
          onMessage: (tick) => {
            var message = tick.body
            if (message === 'UPDATE') {
              this.updateInTree()
            }
          }
        })
      }
    },
    dragStart (event) {
      var moveSectionData = {
        type: 'MOVE_SECTION',
        scope: this.section.scope,
        sectionId: this.section.id,
        fromSectionId: this.inSection.id
      }
      event.dataTransfer.setData('text/plain', JSON.stringify(moveSectionData))

      /* have an extended version of the dragged elements */
      var moveSectionElement = {
        type: 'MOVE_SECTION',
        scope: this.section.scope,
        section: this.section,
        fromSection: this.inSection
      }
      this.$store.commit('setDraggingElement', moveSectionElement)
    },
    draggingOver (event) {
      var draggingElement = this.$store.state.support.draggingElement
      if (draggingElement !== null) {
        if (draggingElement.type === 'MOVE_CARD') {
          this.draggingOverWithCardFlag = true
        }

        if (draggingElement.type === 'MOVE_SECTION') {
          let ratioX = (event.clientX - event.currentTarget.offsetLeft) / event.currentTarget.offsetWidth
          // let ratioY = (event.clientY - event.currentTarget.offsetTop) / event.currentTarget.offsetHeight
          if (ratioX < 0.5) {
            this.draggingOverWithSectionInsideFlag = false
            this.draggingOverWithSectionSameLevelFlag = true
          } else {
            this.draggingOverWithSectionSameLevelFlag = false
            this.draggingOverWithSectionInsideFlag = true
          }
        }
      }
    },
    draggingLeave () {
      clearTimeout(this.resetIntervalId)
      this.resetIntervalId = setTimeout(() => {
        this.draggingOverWithCardFlag = false
        this.draggingOverWithSectionSameLevelFlag = false
        this.draggingOverWithSectionInsideFlag = false
      }, 100)
    },
    dragDroped (event) {
      this.draggingOverWithCardFlag = false
      this.draggingOverWithSectionSameLevelFlag = false
      this.draggingOverWithSectionInsideFlag = false

      var dragData = JSON.parse(event.dataTransfer.getData('text/plain'))
      var moveFlag = false

      if (dragData.type === 'MOVE_CARD') {
        if (!event.ctrlKey && dragData.fromSectionId !== '') {
          moveFlag = true
        } else {
          moveFlag = false
        }

        if (moveFlag) {
          /* move from section */
          this.axios.put('/1/model/section/' + dragData.fromSectionId +
            '/moveCard/' + dragData.cardWrapperId, {}, {
            params: {
              onSectionId: this.section.id,
              scope: dragData.scope
            }
          }).then((response) => {
            this.$store.commit('triggerUpdateSectionCards')
          })
        } else {
          /* copy card without removing it */
          this.axios.put('/1/model/section/' + this.section.id +
            '/cardWrapper/' + dragData.cardWrapperId, {}, {
            params: {
              scope: dragData.scope
            }
          }).then((response) => {
            this.$store.commit('triggerUpdateSectionCards')
          })
        }
      }

      if (dragData.type === 'MOVE_SECTION') {
        if (!event.ctrlKey && dragData.fromSectionId !== '') {
          moveFlag = true
        } else {
          moveFlag = false
        }

        let inside = false
        let ratioX = (event.clientX - event.currentTarget.offsetLeft) / event.currentTarget.offsetWidth

        if (ratioX > 0.5) {
          inside = true
        }

        let onSectionId = ''
        let beforeSubsectionId = ''

        if (inside) {
          onSectionId = this.section.id
          beforeSubsectionId = ''
        } else {
          onSectionId = this.inSection.id
          beforeSubsectionId = this.section.id
        }

        if (moveFlag) {
          /* move from section */
          this.axios.put('/1/model/section/' + dragData.fromSectionId +
            '/moveSubsection/' + dragData.sectionId, {}, {
            params: {
              toSectionId: onSectionId,
              onSectionId: beforeSubsectionId,
              isBefore: false
            }
          }).then((response) => {
            this.$store.dispatch('updateSectionDataInTree', {sectionId: dragData.fromSectionId})
            this.$store.dispatch('updateSectionDataInTree', {sectionId: onSectionId})
          })
        } else {
          /* copy section without removing it */
          this.axios.put('/1/model/section/' + onSectionId +
            '/subsection/' + dragData.sectionId, {}, {
              params: {
                scope: dragData.scope,
                onSubsectionId: beforeSubsectionId,
                isBefore: false
              }
            }).then((response) => {
              this.$store.dispatch('updateSectionDataInTree', {sectionId: onSectionId})
              this.$store.dispatch('updateSectionDataInTree', {sectionId: dragData.fromSectionId})
            })
        }
      }

      this.$store.commit('setDraggingElement', null)
    }
  },

  created () {
    if (this.section) {
      this.subscribeSocket()
    }
  },

  mounted () {
    this.checkExpandSubsections()
    this.updateSubsectionsDataFiltered()
  },

  beforeDestroy () {
    if (this.subscription) {
      this.$store.dispatch('unsubscribe', this.subscription)
    }
  }

}
</script>

<style scoped>

.section-nav-item-container {
  color: #313942;
}

.section-selected {
  background-color: #313942;
  transition: all 300ms ease;
  border-color: #3e464e;
}

.title-selected {
  font-weight: bold;
  color:  #15a5cc;
}

.title-inside-section-selected {
  color: #9ba7a8;
}

.section-nav-item-first-row {
  position: relative;
  border-left-style: solid;
  border-width: 2px;
}

.subsections-container {
  padding-left: 20px;
}

.subsection-container-selected {
  background-color: #313942;
}

.subsection-row {
}

.dragging-over-with-card {
  background-color: #637484;
}

.dragging-same-level-clue {
  position: absolute;
  bottom: 0px;
  left: 0px;
  width: 100%;
  height: 5px;
  background-color: #637484;
  border-radius: 2.5px;
}

.dragging-over-with-section-inside {
  background-color: #637484;
}

.arrow-and-title {
  padding: 3px 0px;
  font-size: 16px;
  float: left;
  width: calc(100% - 30px - 30px);
}

.zoom-controls {
  position: absolute;
  right: 60px;
  top: 6px;
  width: 55px;
  height: 25px;
}

.zoom-controls > div {

}

.zoom-controls .arrow-div {
  width: 25px;
  height: 25px;
  float: left;
  text-align: center;
  padding-top: 1px;
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 6px;
  transition: all 300ms ease;
}

.zoom-controls .arrow-div:hover {
  background-color: rgba(21, 165, 204, 0.5);
}

.zoom-controls > div:first-child {
  margin-right: 5px;
}

.zoom-controls .arrow-div img {
  width: 14px;
}

.levels-div-container {
  position: absolute;
  left: 15px;
  top: 2px;
  overflow: hidden;
}

.levels-div {
  text-align: center;
  background-color: #b35454;
  width: 25px;
  border-radius: 6px;
}

.circle-div {
  width: 30px;
  float: left;
  text-align: center;
  padding: 3px 0px;
  transition: all 300ms ease;
}

.circle-div:hover {
  color: #106e87;
}

.title-hover {
  color: #106e87;
}

.title-div {
  width: calc(100% - 30px);
  float: left;
  padding: 3px 0px;
  transition: all 300ms ease;
}

.control-div {
  text-align: center;
  width: 30px;
  float: left;
  padding: 7.5px 0px 6px 0px;
}

.notification-div {
  min-height: 1px;
  width: 30px;
  float: left;
  padding-top: 6px;
}

.drag-message-div {
  top: 4px;
  right: 12px;
  position: absolute;
  background-color: rgba(21, 165, 204, 0.8);
  color: white;
  border-radius: 6px;
  padding: 3px 6px;
  cursor: move;
}

</style>
