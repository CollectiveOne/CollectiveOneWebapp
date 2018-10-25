<template lang="html">
  <div class="">
    <div v-for="inSection in inModelSections" :key="inSection.id"
      v-if="showThisTag(inSection)" class="w3-left insection-tag-container">
      <app-model-section-tag :section="inSection"></app-model-section-tag>
    </div>
  </div>
</template>

<script>
import ModelSectionTag from '@/components/model/ModelSectionTag.vue'

export default {
  components: {
    'app-model-section-tag': ModelSectionTag
  },

  props: {
    inModelSections: {
      type: Array,
      default: () => []
    },
    hideSectionId: {
      type: String,
      default: ''
    }
  },

  methods: {
    showThisTag (inSection) {
      /* hide the card in which this section is from tags */
      return inSection.id !== this.hideSectionId
    }
  }
}
</script>

<style scoped>

  .insection-tag-container {
    display: inline-block;
    margin-right: 5px;
  }

</style>
