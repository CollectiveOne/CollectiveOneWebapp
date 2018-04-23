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

  computed: {
    isChat () {
      return this.$route.name === 'ModelSectionMessages'
    },
    isCards () {
      return this.$route.name === 'ModelSectionCards' || this.$route.name === 'ModelSectionCard'
    },
    isDoc () {
      return this.$route.name === 'ModelSectionDoc'
    }
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
    '$route' () {
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

.w3-col {
  min-height: 1px;
}

.section-content {
}

.control-row {
  padding: 6px 18px;
}

.section-tabs {
  display: flex;
  flex-direction: row;
}

.tab-btn-space {
  display: block;
  padding: 6px 10px 0px 10px;
  width: 50%;
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
