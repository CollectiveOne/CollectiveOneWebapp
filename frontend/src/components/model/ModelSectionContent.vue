<template lang="html">
  <div :class="{'section-content-fixed': isMessages, 'section-content-scroll': !isMessages}">
    <div class="w3-row control-row w3-border-bottom">
      <app-section-info></app-section-info>
    </div>
    <div class="w3-row section-elements">
      <transition name="fadeenter">
        <router-view></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
import SectionInfo from '@/components/model/SectionInfo'

export default {

  components: {
    'app-section-info': SectionInfo
  },

  computed: {
    isMessages () {
      return this.$route.name === 'ModelSectionMessages'
    }
  },

  methods: {
    redirect () {
      if (this.$route.name === 'ModelSectionContent') {
        this.$router.replace({ name: 'ModelSectionCards' })
      }
    }
  },

  watch: {
    '$route.name' () {
      this.redirect()
    }
  },

  created () {
    this.$store.dispatch('updateCurrentSection', this.$route.params.sectionId)
    this.redirect()
  }
}
</script>

<style scoped>

.section-content-scroll {
  overflow-y: auto;
}

.section-content-fixed {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.control-row {
  padding: 16px 18px 12px 36px;
  min-height: 62px;
}

.section-elements {
  display: flex;
  flex-direction: column;
}

</style>
