<template lang="html">
  <div class="card-container-doc"
    @mouseover="hovering = true"
    @mouseleave="hovering = false">

    <div class="w3-row">

      <div class="w3-col s12">

        <div class="card-container-padded">
          <div v-if="card.title !== ''" class="w3-row card-title">
            {{ card.title }}
          </div>

          <div ref="cardText"
            class="w3-row card-text">
            <vue-markdown class="marked-text" :source="card.text"></vue-markdown>
          </div>
        </div>

        <div v-if="hasImage"
          class="w3-row w3-center w3-display-container image-container">
          <img class="" :src="card.imageFile.url + '?lastUpdated=' + card.imageFile.lastUpdated" alt="">
        </div>

      </div>
    </div>
  </div>
</template>

<script>
export default {

  name: 'model-card-as-card',

  props: {
    cardWrapper: {
      type: Object,
      default: null
    },
    forceUpdate: {
      type: Boolean,
      default: true
    },
    inSection: {
      type: Object,
      default: null
    }
  },

  data () {
    return {
      hovering: false
    }
  },

  computed: {
    card () {
      return this.cardWrapper.card
    },
    hasImage () {
      return this.card.imageFile !== null
    }
  },

  methods: {
    hoverEnter () {
      this.showActionButton = true
      this.highlight = true
    },
    hoverLeave () {
      this.showActionButton = false
      this.highlight = false
    }
  },

  mounted () {
  }
}
</script>

<style>

</style>

<style scoped>

.card-container-padded {
}

.image-container {
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  min-height: 80px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  margin-top: 16px;
  margin-bottom: 16px;
}

.image-container img {
  max-height: 100%;
  max-width: 100%;
}

.image-container-doc {
  height: 350px;
  margin-bottom: 16px;
}

.image-container-doc img {
  height: 100%;
}

.card-text {
  overflow-y: hidden;
}

.card-text-ascard-no-image {
  max-height: 250px;
}

.card-text p {
  margin-top: 0px;
  margin-bottom: 0px;
}

.bottom-row {
  border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px;
  padding: 3px 6px;
  min-height: 30px;
}

.insection-tag-container {
  display: inline-block;
  margin-right: 5px;
  margin-bottom: 5px;
}

.indicator-comp {
  margin-right: 6px;
  margin-left: 4px;
}

</style>
