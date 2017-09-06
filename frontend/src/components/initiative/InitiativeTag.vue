<template lang="html">
  <div class="tag-container">
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
  background-color: #b12db0;
  padding: 3px 6px;
}

</style>
