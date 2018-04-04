<template lang="html">
  <div class="section-content">
    <div class="w3-row control-row w3-border-bottom">
      <div v-if="currentSection !== null" class="w3-col m6">
        <div class="w3-row">
          <transition name="fadeenter">
            <div :key="currentSectionId" class="w3-left this-section-title">
              <div class="w3-left title-div">
                {{ currentSection.title }}
              </div>
              <div class="w3-left btn-div">
                <app-section-control-buttons :section="currentSection" :inSection="null">
                </app-section-control-buttons>
              </div>
              <div @click="showIn = !showIn" class="w3-left btn-div cursor-pointer">
                <i class="fa fa-info-circle" aria-hidden="true"></i>
              </div>
            </div>
          </transition>
        </div>
        <div class="slider-container">
          <transition name="slideDownUp">
            <div v-if="showIn" class="w3-row breadcrumb">
              <div v-if="currentSectionPaths[0].length > 0" class="w3-row">
                <small>This is section is under:</small>
              </div>
              <div v-if="currentSectionPaths[0].length > 0" class="w3-row" v-for="currentSectionPath in currentSectionPaths">
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
              <div class="w3-row description-container w3-margin-top w3-margin-bottom">
                <span v-if="currentSection.description !== null && currentSection.description !== ''">{{ currentSection.description }}</span>
                <span v-else>empty</span>
              </div>
            </div>
          </transition>
        </div>
      </div>
      <div class="w3-col m6 section-tabs w3-center">
        <router-link :to="{ name: 'ModelSectionMessages', query: {levels: this.$route.query.levels ? this.$route.query.levels : 1} }"
          class="tab-btn-space">
          <div class="tab-btn noselect" :class="{'bold-text': isChat, 'button-blue': isChat}">
            <span class="w3-hide-small w3-hide-medium tab-btn-text">Chat</span>
            <span class="w3-hide-large"><i class="fa fa-home" aria-hidden="true"></i></span>
          </div>
        </router-link>
        <router-link :to="{ name: 'ModelSectionCards', query: {levels: this.$route.query.levels ? this.$route.query.levels : 1}}"
          class="tab-btn-space">
          <div class="tab-btn noselect" :class="{'bold-text': isCards, 'button-blue': isCards}">
            <span class="w3-hide-small w3-hide-medium tab-btn-text">Cards</span>
            <span class="w3-hide-large"><i class="fa fa-lightbulb-o" aria-hidden="true"></i></span>
          </div>
        </router-link>
        <router-link :to="{ name: 'ModelSectionDoc', query: {levels: this.$route.query.levels ? this.$route.query.levels : 1} }"
          class="tab-btn-space">
          <div class="tab-btn noselect" :class="{'bold-text': isDoc, 'button-blue': isDoc}">
            <span class="w3-hide-small w3-hide-medium tab-btn-text">Doc</span>
            <span class="w3-hide-large"><i class="fa fa-users" aria-hidden="true"></i></span>
          </div>
        </router-link>
      </div>
    </div>
    <div class="w3-row w3-margin-top w3-container">
      <transition name="fadeenter">
        <router-view :cardsAsCards="isCards"></router-view>
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
      showSubmenu: false,
      showIn: false,
      showNewSubsectionModal: false,
      showSectionModal: false,
      showNewCardModal: false
    }
  },

  computed: {
    menuItems () {
      return [
        { text: 'add card', value: 'addCard', faIcon: 'fa-plus' },
        { text: 'add subsection', value: 'addSubsection', faIcon: 'fa-plus' },
        { text: 'edit', value: 'edit', faIcon: 'fa-pencil' },
        { text: 'remove', value: 'remove', faIcon: 'fa-times' }
      ]
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
    },
    isChat () {
      return this.$route.name === 'ModelSectionMessages'
    },
    isCards () {
      return this.$route.name === 'ModelSectionCards' || this.$route.name === 'ModelSectionCard'
    },
    isDoc () {
      return this.$route.name === 'ModelSectionDoc'
    },
    redirect () {
      if (this.$route.name === 'ModelSectionContent') {
        console.log('redirecting from ModelSectionContent to ModelSectionCards')
        this.$router.replace({name: 'ModelSectionCards', query: {levels: this.$route.query.levels ? this.$route.query.levels : 1}})
      }
    }
  },

  watch: {
    '$route' () {
      this.$store.dispatch('updateCurrentSection', this.$route.params.sectionId)
      this.redirect()
    }
  },

  methods: {
    clickOutsideMenu () {
      this.showSubmenu = false
    }
  },

  created () {
    this.redirect()
  }
}
</script>

<style scoped>

.w3-col {
  min-height: 1px;
}

.section-content {
}

.control-row {
  padding: 6px 18px;
}

.control-div {
  margin-left: 12px;
}

.this-section-title {
  font-size: 22px;
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

.btn-div:hover {
  background-color: rgb(171, 171, 171);
}

.description-container {
  border-radius: 3px;
  padding: 3px 12px;
  background-color: #cfcfcf;
}

.section-tabs {
  display: flex;
  flex-direction: row;
}

.tab-btn-space {
  display: block;
  padding: 6px 10px 0px 10px;
  width: 33%;
}

.tab-btn {
  padding: 0px 6px;
  border-radius: 6px;
}

.tab-btn-text {
  font-size: 14px;
}

.bold-text {
  font-weight: bold;
}

</style>
