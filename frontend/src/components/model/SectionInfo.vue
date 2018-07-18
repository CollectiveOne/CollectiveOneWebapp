<template lang="html">
  <div class="">
    <div class="w3-row">
      <transition name="fadeenter">
        <div :key="currentSectionId" v-if="currentSection" class="this-section-title small-scroll">
          <div class="title-div">
            {{ sectionTitle }}
          </div>

          <div class="title-buttons">
            <popper trigger="hover":options="popperOptions" delay-on-mouse-in="1200" class="btn-div">
              <app-help-popper
                :title="(showIn ? $t('general.HIDE') : $t('general.SHOW')) + ' ' + $t('help.SECTION-DETAILS-TT')"
                :details="$t('help.SECTION-DETAILS-DET')">
              </app-help-popper>

              <div slot="reference" @click="showIn = !showIn" class="fa-button">
                <i v-if="showIn" class="fa fa-chevron-up" aria-hidden="true"></i>
                <i v-else class="fa fa-chevron-down" aria-hidden="true"></i>
              </div>
            </popper>

            <div v-if="isLoggedAnEditor" class="btn-div fa-button">
              <app-section-control-buttons :section="currentSection" :inSection="null">
              </app-section-control-buttons>
            </div>

            <popper trigger="hover":options="popperOptions" delay-on-mouse-in="1200" class="btn-div">
              <app-help-popper
                :title="$t('help.READ-FRIENDLY-URL-TT')"
                :details="$t('help.READ-FRIENDLY-URL-DET')">
              </app-help-popper>

              <div slot="reference" class="fa-button">
                <router-link :to="{ name: 'ModelSectionRead', params: {sectionId: this.currentSection.id} }">
                  <img src="./../../assets/shareable-view.svg" alt="">
                </router-link>
              </div>
            </popper>

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
                <router-link :to="{ name: 'ModelSectionContent', params: {'sectionId': parent.id, 'initiativeId': parent.initiativeId } }">
                  {{ parent.title }}
                </router-link>
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
    },
    popperOptions () {
      return {
        placement: 'bottom',
        modifiers: {
          preventOverflow: {
            enabled: false
          }
        }
      }
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

.fa-button img {
  height: 24px;
}

.title-div {
  margin-right: 16px;
  display: inline-block;
}

.title-buttons {
  display: inline-block;
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
  display: inline-block;
}

.description-container {
  border-radius: 3px;
  padding: 3px 12px;
}

</style>
