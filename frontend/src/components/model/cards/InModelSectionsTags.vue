<template lang="html">
  <div class="">
    <div v-for="inSection in inModelSections" :key="inSection.id"
      v-if="showThisTag(inSection)" class="w3-left insection-tag-container">
      <div class="">
        <router-link :to="{ name: 'ModelSectionContent', params: { sectionId: inSection.id } }"
          class="w3-tag w3-round w3-small" :class="tagClass(inSection)">
          {{ inSection.title }}
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
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
    },
    tagClass (inSection) {
      let cClass = {}
      switch (inSection.scope) {
        case 'PRIVATE':
          cClass['tag-red'] = true
          break

        case 'SHARED':
          cClass['tag-yellow'] = true
          break

        default:
          cClass['tag-blue'] = true
          break
      }
      return cClass
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
