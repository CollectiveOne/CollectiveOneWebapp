<template lang="html">
  <router-link :to="{ name: 'ModelSectionContent', params: { initiativeId: section.initiativeId, sectionId: section.id }, query: {menu: 'CD'} }"
    class="gray-1 w3-tag w3-round w3-small">
    {{ section.title }}
  </router-link>
</template>

<script>
export default {
  props: {
    section: {
      type: Object
    }
  }
}
</script>

<style lang="css">
</style>
