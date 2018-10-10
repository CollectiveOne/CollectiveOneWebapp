<template lang="html">
  <div class="w3-row card-container cursor-pointer"
    ref="cardContent">

    <div class="w3-row" @click="$emit('card-clicked')">
      <div v-if="hasImage"
        class="w3-row w3-center w3-display-container image-container">
        <img class="" :src="card.imageFile.url + '?lastUpdated=' + card.imageFile.lastUpdated" alt="">
      </div>

      <div class="card-container-padded">
        <div v-if="card.title !== ''" class="w3-row card-title">
          {{ card.title }}
        </div>

        <div
          class="w3-row card-text">
          <vue-markdown v-if="card.text" class="marked-text" :source="card.text" :anchorAttributes="{target: '_blank'}"></vue-markdown>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  props: {
    card: {
      type: Object
    }
  },
  computed: {
    hasImage () {
      return this.card.imageFile !== null
    }
  }
}
</script>

<style scoped>

.card-container {
}

.card-container-padded {
  padding: 8px 6px 12px 12px !important;
}

.image-container {
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  min-height: 80px;
  max-height: 150px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
}

.image-container img {
  max-height: 100%;
  max-width: 100%;
}

.card-text {
  overflow-y: hidden;
}


</style>
