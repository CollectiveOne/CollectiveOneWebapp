<template lang="html">
  <div class="section-content">
    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-section-modal
          v-if="showNewSubsectionModal"
          :isNew="true"
          :inElementId="currentSection.id"
          :inElementTitle="currentSection.title"
          @close="showNewSubsectionModal = false">
        </app-model-section-modal>
      </transition>
    </div>

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal
          v-if="showNewCardModal"
          :isNew="true"
          :inSectionId="currentSection.id"
          :inSectionTitle="currentSection.title"
          @close="showNewCardModal = false">
        </app-model-card-modal>
      </transition>
    </div>

    <div class="w3-row control-row w3-border-bottom">
      <div class="w3-col m6 breadcrumb">
        <transition name="fadeenter">
          <div :key="currentSectionId" class="w3-left">
            <div v-for="(parent, ix) in currentSectionPath" class="w3-left">
              <div class="w3-left">
                {{ parent.title }}
              </div>
              <div v-if="ix < currentSectionPath.length - 1" class="w3-left">
                <i class="fa fa-chevron-right" aria-hidden="true"></i>
              </div>
            </div>
            <div @click="showSubmenu = !showSubmenu" class="w3-left w3-margin-left cursor-pointer">
              <i class="fa fa-bars" aria-hidden="true"></i>
              <div v-if="showSubmenu" class="control-btns w3-card w3-white">
                <app-drop-down-menu :items="menuItems"
                  @addCard="showNewCardModal = true"
                  @addSubsection="showNewSubsectionModal = true">
                </app-drop-down-menu>
              </div>
            </div>
          </div>
        </transition>
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
export default {

  data () {
    return {
      showSubmenu: false,
      showNewSubsectionModal: false,
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
      return this.currentSectionGenealogy.section
    },
    currentSectionGenealogy () {
      return this.$store.state.model.currentSectionGenealogy
    },
    currentSectionPath () {
      if (this.$store.getters.currentSectionPath !== null) {
        var path = JSON.parse(JSON.stringify(this.$store.getters.currentSectionPath))
        return path.reverse()
      }
      return []
    },
    isChat () {
      return this.$route.name === 'ModelSectionMessages'
    },
    isCards () {
      return this.$route.name === 'ModelSectionCards'
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
  font-size: 22px;
  min-height: 65px;
}

.control-btns {
  width: 200px;
  position: absolute;
  margin-top: 10px;
  z-index: 1;
  font-size: 14px;
}

.breadcrumb {
  padding-top: 11px;
}

.breadcrumb .fa {
  font-size: 18px;
  margin-left: 6px;
  margin-right: 6px;
}

.section-tabs {
  display: flex;
  flex-direction: row;
}

.tab-btn-space {
  display: block;
  padding: 9px 10px 0px 10px;
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
