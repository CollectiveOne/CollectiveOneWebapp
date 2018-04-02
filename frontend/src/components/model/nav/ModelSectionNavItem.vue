<template lang="html">
  <div class="w3-row">
    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-section-modal
          v-if="showNewSubsectionModal"
          :isNew="true"
          :inElementId="section.id"
          :inElementTitle="section.title"
          @close="showNewSubsectionModal = false">
        </app-model-section-modal>
      </transition>
    </div>

    <div class="w3-row" :class="{'section-selected': highlightLevelUse > 0}">
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
        {{ section.title }}
      </div>
      <div @click="showSubmenu = !showSubmenu" class="control-div cursor-pointer"
        v-click-outside="clickOutsideMenu">
        <i class="fa fa-bars" aria-hidden="true"></i>
      </div>
      <div v-if="showSubmenu" class="control-btns-container">
        <app-drop-down-menu :items="menuItems"
          class="control-btns"
          @addSubsection="addSubsection()"
          @edit="edit()"
          @remove="remove()">
        </app-drop-down-menu>
      </div>
    </div>
    <div :class="{'slider-container': animating}">
      <transition name="slideDownUp"
        @before-enter="animating = true"
        @after-enter="animating = false"
        @before-leave="animating = true"
        @after-leave="animating = false">
        <div v-if="showSubsections" class="w3-row subsections-container">
          <app-model-section-nav-item v-for="subsection in subsections"
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
import ModelSectionModal from '@/components/model/modals/ModelSectionModal'

export default {
  name: 'app-model-section-nav-item',

  components: {
    'app-model-section-modal': ModelSectionModal
  },

  props: {
    section: {
      type: Object
    },
    highlightLevel: {
      type: Number,
      default: 0
    }
  },

  data () {
    return {
      showSubsections: false,
      showSubmenu: false,
      showNewSubsectionModal: false,
      subsections: [],
      animating: false
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
    }
  },

  computed: {
    menuItems () {
      return [
        { text: 'add subsection', value: 'addSubsection', faIcon: 'fa-plus' },
        { text: 'edit', value: 'edit', faIcon: 'fa-pencil' },
        { text: 'remove', value: 'remove', faIcon: 'fa-times' }
      ]
    },
    hasSubsections () {
      return this.section.nSubsections > 0
    },
    isSelected () {
      return this.$route.params.sectionId === this.section.id
    },
    levels () {
      return parseInt(this.$route.query.levels)
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
      this.$router.push({name: 'ModelSectionContent', params: {sectionId: this.section.id}, query: {levels: this.$route.query.levels ? this.$route.query.levels : 1}})
    },
    clickOutsideMenu () {
      this.showSubmenu = false
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
    }
  },

  mounted () {
    this.checkExpandSubsections()
  }
}
</script>

<style scoped>

.section-selected {
  background-color: #5d5d5d;
  transition: background-color 300ms ease;
  color: white;
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
  width: 30px;
  float: left;
  text-align: center;
}

.control-div:hover {
  background-color: #CCCCCC;
}

.control-btns-container {
  position: relative;
  float: left;
}

.control-btns {
  width: 180px;
  margin-top: 25px;
  margin-left: -180px;
  position: absolute;
  text-align: left;
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
