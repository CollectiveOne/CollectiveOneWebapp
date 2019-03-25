<template lang="html">
  <div class="">
    <router-link :to="{ name: 'ModelSectionContent', params: { initiativeId: section.initiativeId, sectionId: section.id } }"
      class="w3-tag w3-round w3-small" :class="tagClass">
      {{ section.title }}
    </router-link>
  </div>
</template>

<script>
export default {
  props: {
    section: {
      type: Object
    }
  },
  computed: {
    tagClass () {
      let cClass = {}
      switch (this.section.scope) {
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

<style lang="css">
</style>
