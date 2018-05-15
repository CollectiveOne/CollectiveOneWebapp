<template lang="html">
  <div class="model-nav-container-large">

    <div class="model-nav-container">
      <div class="w3-row">
        <app-model-section-nav-item-simple
          :section="section"
          @section-selected="sectionSelected($event)">
        </app-model-section-nav-item-simple>
      </div>
    </div>

  </div>
</template>

<script>
import ModelSectionNavItemSimple from '@/components/model/nav/ModelSectionNavItemSimple.vue'

export default {
  components: {
    'app-model-section-nav-item-simple': ModelSectionNavItemSimple
  },

  props: {
    section: {
      type: Object,
      default: null
    }
  },

  methods: {
    sectionSelected (section) {
      let sectionDiv = document.getElementById(section.id)
      sectionDiv.scrollIntoView()
    }
  }
}
</script>

<style scoped>

.model-nav-container-large {
  height: 100%;
  padding: 16px 12px;
  font-size: 16px;
}

.model-nav-container {
}

</style>
