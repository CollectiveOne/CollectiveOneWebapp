<template lang="html">
  <div class="section-content">
    <div class="w3-row control-row w3-border-bottom">
      <app-section-info></app-section-info>
    </div>
    <div class="w3-row">
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

  methods: {
    redirect () {
      if (this.$route.name === 'ModelSectionContent') {
        console.log('redirecting from ModelSectionContent to ModelSectionCards')
        this.$router.replace({name: 'ModelSectionCards'})
      }
    }
  },

  watch: {
    '$route.name' () {
      this.redirect()
    }
  },

  created () {
    console.log('ModelSectionContent created')
    this.$store.dispatch('updateCurrentSection', this.$route.params.sectionId)
    this.redirect()
  }
}
</script>

<style scoped>

.section-content {
}

.control-row {
  padding: 16px 18px 12px 36px;
}

</style>
