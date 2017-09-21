<template lang="html">
  <markdown-editor
    :configs="{ spellChecker: false, hideIcons: ['image', 'heading', 'quote']}"
    :value="value"
    @input="$emit('input', $event)">
  </markdown-editor>
</template>

<script>
export default {
  props: ['value']
}
</script>

<style scoped>
</style>
