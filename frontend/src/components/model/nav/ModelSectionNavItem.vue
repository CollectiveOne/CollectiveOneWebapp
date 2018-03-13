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

    <div class="w3-row">
      <div @click="showSubsections = !showSubsections" class="circle-div cursor-pointer">
        <div v-if="hasSubsections">
          <i v-if="!showSubsections" class="fa fa-chevron-circle-right" aria-hidden="true"></i>
          <i v-else class="fa fa-chevron-circle-down" aria-hidden="true"></i>
        </div>
        <div v-else>
          <i class="fa fa-circle" aria-hidden="true"></i>
        </div>
      </div>
      <div @click="sectionSelected()" class="title-div cursor-pointer">
        {{ section.title }}
      </div>
      <div @click="showSubmenu = !showSubmenu" class="control-div cursor-pointer">
        <i class="fa fa-bars" aria-hidden="true"></i>
      </div>
      <div v-if="showSubmenu" class="control-btns w3-card w3-white"
        v-click-outside="clickOutsideMenu">
        <app-drop-down-menu :items="menuItems"
          @addSubsection="addSubsection()"
          @edit="edit()"
          @remove="remove()">
        </app-drop-down-menu>
      </div>
    </div>

    <div v-if="showSubsections" class="w3-row subsections-container">
      <app-model-section-nav-item v-for="subsection in subsections" :section="subsection" :key="subsection.id"
        class="subsection-row">
      </app-model-section-nav-item>
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
    }
  },

  data () {
    return {
      showSubsections: false,
      showSubmenu: false,
      showNewSubsectionModal: false,
      subsections: []
    }
  },

  watch: {
    showSubsections () {
      if (this.showSubsections) {
        this.updateSubsections()
      }
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
    }
  },

  methods: {
    sectionSelected () {
      this.$store.dispatch('updateCurrentSection', this.section)
    },
    clickOutsideMenu () {
      this.showSubmenu = false
    },
    addSubsection () {
      this.showNewSubsectionModal = true
    },
    updateSubsections () {
      this.axios.get('/1/model/section/' + this.section.id, { params: { level: 1 } }).then((response) => {
        if (response.data.result === 'success') {
          this.subsections = response.data.data.subsections
        }
      })
    }
  }
}
</script>

<style scoped>

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

.control-btns {
  margin-top: 25px;
  margin-left: 135px;
  position: absolute;
  width: 180px;
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
