<template lang="html">
  <div class="">
    <div class="w3-row">
      <transition name="fadeenter">
        <div :key="currentSectionId" v-if="currentSection" class="this-section-title small-scroll">
          <div class="title-div">
            {{ sectionTitle }}
          </div>
          <div v-if="$store.state.user.authenticated" class="btn-div fa-button">
            <app-section-control-buttons :section="currentSection" :inSection="null" :hideAdd="true">
            </app-section-control-buttons>
          </div>
          <div v-if="isLoggedAnEditor" class="btn-div fa-button">
            <app-section-control-buttons :section="currentSection" :inSection="null" :onlyAdd="true">
            </app-section-control-buttons>
          </div>
          <div @click="showIn = !showIn" class="btn-div fa-button">
            <i class="fa fa-info-circle" aria-hidden="true"></i>
          </div>
          <div class="btn-div fa-button">
            <router-link :to="{ name: 'ModelSectionRead', params: {sectionId: this.currentSection.id} }">
              <img src="./../../assets/shareable-view.svg" alt="">
            </router-link>
          </div>
        </div>
      </transition>
    </div>
    <div class="slider-container">
      <transition name="slideDownUp">
        <div v-if="showIn && currentSection && currentSectionPaths.length > 0" class="w3-row breadcrumb">
          <div class="w3-row description-container light-grey">
            <span v-if="currentSection.description !== null && currentSection.description !== ''">{{ currentSection.description }}</span>
            <span v-else>empty</span>
          </div>

          <div v-if="currentSectionPaths[0].length > 0" class="w3-row">
            <small>This is section is under:</small>
          </div>
          <div v-if="currentSectionPaths[0].length > 0" class="w3-row"
            v-for="currentSectionPath in currentSectionPaths">

            <div class="w3-left fa-container">
              <i class="fa fa-circle " aria-hidden="true"></i>
            </div>
            <div v-for="(parent, ix) in currentSectionPath" class="w3-left">
              <div class="w3-left">
                {{ parent.title }}
              </div>
              <div v-if="ix < currentSectionPath.length - 1" class="w3-left fa-container">
                <i class="fa fa-chevron-right" aria-hidden="true"></i>
              </div>
            </div>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script>
import SectionControlButtons from '@/components/model/SectionControlButtons.vue'

export default {
  components: {
    'app-section-control-buttons': SectionControlButtons
  },

  data () {
    return {
      showIn: false
    }
  },

  computed: {
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    menuItems () {
      return [
        { text: 'add card', value: 'addCard', faIcon: 'fa-plus' },
        { text: 'add subsection', value: 'addSubsection', faIcon: 'fa-plus' },
        { text: 'edit', value: 'edit', faIcon: 'fa-pencil' },
        { text: 'remove', value: 'remove', faIcon: 'fa-times' }
      ]
    },
    sectionTitle () {
      if (this.currentSection) {
        return this.currentSection.title
      }
      return ''
    },
    currentSectionId () {
      return this.$route.params.sectionId
    },
    currentSection () {
      if (this.currentSectionGenealogy) {
        return this.currentSectionGenealogy.section
      }
      return null
    },
    currentSectionGenealogy () {
      return this.$store.state.model.currentSectionGenealogy
    },
    currentSectionPaths () {
      if (this.$store.getters.currentSectionPaths !== null) {
        return this.$store.getters.currentSectionPaths
      }
      return []
    }
  },

  watch: {
    '$route.params.sectionId' () {
      this.$store.dispatch('updateCurrentSection', this.$route.params.sectionId)
    }
  },

  methods: {
    clickOutsideMenu () {
      this.showSubmenu = false
    }
  }
}
</script>

<style scoped>

.this-section-title {
  font-size: 22px;
  white-space: nowrap;
  overflow-x: auto;
}

.this-section-title > div {
  display: inline-block;
}

.fa-button img {
  height: 24px;
}

.title-div {
  margin-right: 16px;
}

.breadcrumb {
  padding-top: 0px;
}

.breadcrumb .fa {
  font-size: 8px;
  margin-left: 6px;
  margin-right: 4px;
}

.breadcrumb .fa-container .fa {
  vertical-align: middle;
}

.btn-div {
  width: 35px;
  text-align: center;
}

.description-container {
  border-radius: 3px;
  padding: 3px 12px;
}

</style>
