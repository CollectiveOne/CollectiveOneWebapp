<template lang="html">
  <div class="">
    <div class="section-title" :style="sectionTitleStyle">{{ section.title }}</div>

    <app-model-card
      v-for="card in section.cards"
      :key="card.id"
      :card="card">
    </app-model-card>

    <button
      v-if="section.subsections.length > 0"
      class="w3-button model-button w3-margin-bottom"
      @click="showSubsections = !showSubsections">
      {{ showSubsectionsButtonContent }}
    </button>
    <div class="slider-container">
      <transition name="slideDownUp">
        <div v-if="showSubsections" class="subsections-container" :style="subsectionsContainerStyle">
          <app-model-section
            v-for="subsection in section.subsections"
            :key="subsection.id"
            :section="subsection"
            :level="level + 1">
          </app-model-section>
        </div>
      </transition>
    </div>

  </div>
</template>

<script>
import ModelCard from '@/components/model/ModelCard.vue'

export default {
  name: 'app-model-section',

  components: {
    'app-model-card': ModelCard
  },

  props: {
    section: {
      type: Object,
      default: null
    },
    level: {
      type: Number,
      default: 0
    }
  },

  data () {
    return {
      showSubsections: false
    }
  },

  computed: {
    sectionTitleStyle () {
      var fontsize = this.level < 5 ? 26 - 4 * this.level : 16
      return {'font-size': fontsize + 'px'}
    },
    subsectionsContainerStyle () {
      var paddingLeft = this.level < 5 ? 20 * (this.level + 1) : 75
      return {'padding-left': paddingLeft + 'px'}
    },
    showSubsectionsButtonContent () {
      var text = ''
      if (this.showSubsections) {
        text = 'hide ' + this.section.subsections.length + ' subsection'
        if (this.section.subsections.length > 1) {
          text += 's'
        }
      } else {
        text = 'show ' + this.section.subsections.length + ' subsection'
        if (this.section.subsections.length > 1) {
          text += 's'
        }
      }
      return text
    }
  }
}
</script>

<style scoped>
</style>
