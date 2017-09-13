<template lang="html">
  <div class="w3-row tag-container w3-round w3-border">
    <div class="w3-col s12">
      <div class="top-row w3-row">
        <div class="tag-text-container w3-center">
          <span v-if="showDescription"><b>{{ tag.tagText }}</b></span>
          <span v-else="showDescription">{{ tag.tagText }}</span>
        </div>
        <div v-if="showRemove" @click="$emit('remove', tag)" class="cursor-pointer tag-close-div">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>
      </div>
      <div v-if="showDescription" class="w3-row description-row">
        <i>{{ tag.description }}</i>
      </div>
    </div>
  </div>

</template>

<script>

export default {
  props: {
    tag: {
      type: Object,
      default: null
    },
    showRemove: {
      type: Boolean,
      default: false
    },
    showDescription: {
      type: Boolean,
      default: false
    }
  }
}
</script>

<style scoped>

.tag-container {
  background-color: #E1ECF4;
  color: #39739d;
}

.top-row {
  white-space: nowrap;
}

.top-row > div {
  display: inline-block;
}

.tag-text-container {
  padding: 1px 10px;
}

.tag-close-div {
  padding: 1px 5px;
}

.tag-close-div:hover {
  background-color: #bac8d2;
}

.description-row {
  padding: 2px 10px;
}

</style>
