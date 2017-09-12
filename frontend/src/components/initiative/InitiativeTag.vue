<template lang="html">
  <div class="tag-container w3-center w3-round w3-border">
    {{ tag.tagText }}
  </div>
</template>

<script>

export default {
  props: {
    tag: {
      type: Object,
      default: null
    }
  }
}
</script>

<style scoped>

.tag-container {
  padding: 3px 12px;
  background-color: #68b628;
  color: white;
}

</style>
