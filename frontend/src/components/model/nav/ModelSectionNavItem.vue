<template lang="html">
  <div class="w3-row">

    <div class="w3-row"
      :class="rowClass"
      draggable="true"
      @dragstart="dragStart($event)"
      @dragover.prevent="draggingOver($event)"
      @dragleave.prevent="draggingLeave()"
      @drop.prevent="dragDroped($event)">

      <div v-if="draggingOverWithSectionSameLevelFlag" class="w3-row dragging-same-level-clue">

      </div>

      <div @click="showSubsections = !showSubsections" class="circle-div cursor-pointer">
        <div v-if="hasSubsections">
          <i v-if="!showSubsections" class="fa fa-chevron-circle-right" aria-hidden="true"></i>
          <i v-else class="fa fa-chevron-circle-down" aria-hidden="true"></i>
        </div>
        <div v-else>
          <i class="fa fa-circle" aria-hidden="true"></i>
        </div>
      </div>
      <div @click="sectionSelected()" class="title-div cursor-pointer noselect">
        {{ section.isTopModelSection ? 'All' : section.title }}
      </div>
      <div class="control-div">
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

        <div v-if="showSubsections && subsections.length > 0" class="w3-row subsections-container">
          <app-model-section-nav-item v-for="subsection in subsections"
            :inSection="section"
            :section="subsection" :key="subsection.id"
            :highlightLevel="highlightLevelUse - 1"
            class="subsection-row">
          </app-model-section-nav-item>
        </div>
      </transition>
    </div>
  </div>

</template>

<script>
import SectionControlButtons from '@/components/model/SectionControlButtons'
import ModelSectionModal from '@/components/model/modals/ModelSectionModal'

export default {
  name: 'app-model-section-nav-item',

  components: {
    'app-section-control-buttons': SectionControlButtons,
    'app-model-section-modal': ModelSectionModal
  },

  props: {
    section: {
      type: Object
    },
    inSection: {
      type: Object,
      default: null
    },
    highlightLevel: {
      type: Number,
      default: 0
    }
  },

  data () {
    return {
      showSubsections: false,
      subsections: [],
      animating: false,
      draggingOverWithCardFlag: false,
      draggingOverWithSectionSameLevelFlag: false,
      draggingOverWithSectionInsideFlag: false,
      resetIntervalId: 0
    }
  },

  watch: {
    showSubsections () {
      if (this.showSubsections) {
        this.updateSubsections()
      }
    },
    '$route' () {
      this.checkExpandSubsections()
    },
    '$store.state.support.triggerUpdateSectionsTree' () {
      this.updateSubsections()
    }
  },

  computed: {
    rowClass () {
      return {
        'section-selected': this.highlightLevelUse > 0,
        'dragging-over-with-card': this.draggingOverWithCardFlag,
        'dragging-over-with-section-same-level': this.draggingOverWithSectionSameLevelFlag,
        'dragging-over-with-section-inside': this.draggingOverWithSectionInsideFlag
      }
    },
    hasSubsections () {
      return this.section.nSubsections > 0
    },
    isSelected () {
      return this.$route.params.sectionId === this.section.id
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
    sectionSelected () {
      this.$router.push({name: 'ModelSectionContent', params: {sectionId: this.section.id}})
    },
    addSubsection () {
      this.showNewSubsectionModal = true
    },
    updateSubsections () {
      this.axios.get('/1/model/section/' + this.section.id, { params: { levels: 1 } }).then((response) => {
        if (response.data.result === 'success') {
          this.subsections = response.data.data.subsections
        }
      })
    },
    checkExpandSubsections () {
      if (this.highlightLevelUse > 1) {
        this.showSubsections = true
      } else {
        // this.showSubsections = false
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

.section-selected {
  background-color: #797474;
  transition: background-color 300ms ease;
  color: white;
}

.dragging-over-with-card {
  background-color: #cfcfcf;
}

.dragging-same-level-clue {
  height: 5px;
  background-color: #575757;
  border-radius: 2.5px;
}

.dragging-over-with-section-inside {
  background-color: #cfcfcf;
}

.circle-div {
  width: 30px;
  float: left;
  text-align: center;
}

.title-div {
  width: calc(100% - 30px - 30px);
  float: left;
}

.control-div {
  text-align: center;
  width: 30px;
  float: left;
}

.subsections-container {
  padding-top: 5px;
  padding-bottom: 5px;
  padding-left: 25px;
}

.subsection-row {
  padding-top: 5px;
}

</style>
