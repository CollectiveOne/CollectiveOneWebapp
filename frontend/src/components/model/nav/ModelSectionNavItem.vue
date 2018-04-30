<template lang="html">
  <div class="w3-row section-nav-item-container">

    <div class="w3-row section-nav-item-first-row"
      :class="rowClass"
      draggable="true"
      @dragstart="dragStart($event)"
      @dragover.prevent="draggingOver($event)"
      @dragleave.prevent="draggingLeave()"
      @drop.prevent="dragDroped($event)">

      <div v-if="draggingOverWithSectionSameLevelFlag" class="w3-row dragging-same-level-clue">

      </div>

      <div @click="toggleSubsections()" class="circle-div cursor-pointer">
        <div v-if="hasSubsections">
          <i v-if="!showSubsections" class="fa fa-chevron-circle-right" aria-hidden="true"></i>
          <i v-else class="fa fa-chevron-circle-down" aria-hidden="true"></i>
        </div>
        <div v-else>
          <i class="fa fa-circle" aria-hidden="true"></i>
        </div>
      </div>
      <div @click="sectionSelected()" @dblclick="toggleSubsections()" class="title-div cursor-pointer noselect">
        {{ sectionTitle }}
      </div>
      <div v-if="section" class="notification-div">
        <app-notifications-list
          :section="section"
          :forceUpdate="forceUpdateNotifications">
        </app-notifications-list>
      </div>
      <div class="control-div" :class="{'fa-button': !highlight, 'fa-button-dark': highlight}">
        <app-section-control-buttons :section="section" :inSection="inSection">
        </app-section-control-buttons>
      </div>
    </div>

    <div :class="{'slider-container': animating}">
      <transition name="slideDownUp"
        @before-enter="animating = true"
        @after-enter="animating = false"
        @before-leave="animating = true"
        @after-leave="animating = false">

        <div v-if="showSubsections && subsections.length > 0" class="w3-row subsections-container"
          :class="{'subsection-container-selected': highlight}" >
          <app-model-section-nav-item
            v-for="(subsection, ix) in subsections"
            :inSection="section"
            :section="subsection" :key="subsection.id"
            :highlightLevel="highlightLevelUse - 1"
            :coordinate="coordinate.concat(ix)"
            class="subsection-row">
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
    inSection: {
      type: Object,
      default: null
    },
    highlightLevel: {
      type: Number,
      default: 0
    },
    coordinate: {
      type: Array,
      default: () => { return [] }
    }
  },

  data () {
    return {
      showSubsections: false,
      animating: false,
      draggingOverWithCardFlag: false,
      draggingOverWithSectionSameLevelFlag: false,
      draggingOverWithSectionInsideFlag: false,
      resetIntervalId: 0,
      forceUpdateNotifications: false
    }
  },

  watch: {
  },

  computed: {
    sectionData () {
      return this.$store.getters.getSectionDataAtCoord(this.coordinate)
    },
    section () {
      if (this.sectionData) {
        return this.sectionData.section
      }
      return null
    },
    subsections () {
      if (this.section) {
        return this.section.subsections
      }
      return []
    },
    highlight () {
      return this.highlightLevelUse > 0
    },
    rowClass () {
      return {
        'section-selected': this.highlight,
        'dragging-over-with-card': this.draggingOverWithCardFlag,
        'dragging-over-with-section-same-level': this.draggingOverWithSectionSameLevelFlag,
        'dragging-over-with-section-inside': this.draggingOverWithSectionInsideFlag
      }
    },
    sectionTitle () {
      if (this.section) {
        return this.section.isTopModelSection ? 'All' : this.section.title
      }
      return ''
    },
    hasSubsections () {
      if (this.section) {
        return this.section.nSubsections > 0
      }
      return false
    },
    isSelected () {
      if (this.section) {
        return this.$route.params.sectionId === this.section.id
      }
      return false
    },
    levels () {
      return this.$route.query.levels ? parseInt(this.$route.query.levels) : 1
    },
    highlightLevelUse () {
      if (this.isSelected) {
        /* if this is the selected section, start counting from levels */
        return this.levels
      } else {
        /* if this is not the selected section, decrease the highlightLevel property */
        return this.highlightLevel
      }
    }
  },

  methods: {
    toggleSubsections () {
      this.$store.dispatch('toogleExpandSubsection', this.coordinate)
      this.showSubsections = !this.showSubsections
    },
    checkExpandSubsections () {
      if (this.highlightLevelUse > 1) {
        this.showSubsections = true
      } else {
        if (this.sectionData) {
          this.showSubsections = this.sectionData.expand
        }
      }
    },
    sectionSelected () {
      if (this.section) {
        this.$store.commit('setLastSectionVisited', this.section)
        this.$router.push({name: 'ModelSectionContent', params: {sectionId: this.section.id}})
      }
    },
    dragStart (event) {
      var moveSectionData = {
        type: 'MOVE_SECTION',
        sectionId: this.section.id,
        fromSectionId: this.inSection.id
      }
      event.dataTransfer.setData('text/plain', JSON.stringify(moveSectionData))

      /* have an extended version of the dragged elements */
      var moveSectionElement = {
        type: 'MOVE_SECTION',
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
              onSectionId: this.section.id
            }
          }).then((response) => {
            this.$store.commit('triggerUpdateSectionCards')
          })
        } else {
          /* copy card without removing it */
          this.axios.put('/1/model/section/' + this.section.id +
            '/cardWrapper/' + dragData.cardWrapperId, {}).then((response) => {
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
              onSectionId: onSectionId,
              beforeSubsectionId: beforeSubsectionId
            }
          }).then((response) => {
            this.$store.commit('triggerUpdateSectionsTree')
          })
        } else {
          /* copy card without removing it */
          this.axios.put('/1/model/section/' + onSectionId +
            '/subsection/' + dragData.sectionId, {}, {
              params: {
                beforeSubsectionId: beforeSubsectionId
              }
            }).then((response) => {
            this.$store.commit('triggerUpdateSectionsTree')
          })
        }
      }

      this.$store.commit('setDraggingElement', null)
    }
  },

  mounted () {
    this.checkExpandSubsections()
  }
}
</script>

<style scoped>

.section-nav-item-container {
  color: #57636f;
}

.section-nav-item-first-row {
}

.subsections-container {
  padding-left: 20px;
}

.subsection-container-selected {
  background-color: #313942;
}

.subsection-row {
}

.section-selected {
  background-color: #313942;
  transition: all 300ms ease;
  color: white;
  border-color: #3e464e;
}

.dragging-over-with-card {
  background-color: #637484;
}

.dragging-same-level-clue {
  height: 5px;
  background-color: #637484;
  border-radius: 2.5px;
}

.dragging-over-with-section-inside {
  background-color: #637484;
}

.circle-div {
  width: 30px;
  float: left;
  text-align: center;
  padding: 3px 0px;
}

.title-div {
  width: calc(100% - 30px - 30px - 30px);
  float: left;
  padding: 3px 0px;
}

.control-div {
  text-align: center;
  width: 30px;
  float: left;
  padding: 3px 0px;
}

.notification-div {
  min-height: 1px;
  width: 30px;
  float: left;
}

</style>
