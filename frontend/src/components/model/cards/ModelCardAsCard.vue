<template lang="html">
  <div class="w3-display-container w3-card-4 w3-topbar border-blue w3-round-large"
    @mouseover="hovering = true"
    @mouseleave="hovering = false">

    <div class="w3-row card-container cursor-pointer w3-display-container"
      ref="cardContent">

      <div class="w3-col s12" @click="cardClicked()">
        <div v-if="hasImage"
          class="w3-row w3-center w3-display-container image-container">
          <img class="" :src="card.imageFile.url + '?lastUpdated=' + card.imageFile.lastUpdated" alt="">
        </div>

        <div class="card-container-padded">
          <div v-if="card.title !== ''" class="w3-row">
            <b>{{ card.title }}</b>
          </div>

          <div ref="cardText"
            class="w3-row card-text"
            :style="cardTextStyle">
            <vue-markdown class="marked-text" :source="card.text"></vue-markdown>
          </div>
        </div>
      </div>

      <div v-if="textTooLong()" class="">
        <div @click="showMoreClick()"
          class="w3-padding model-action-button gray-2-color w3-display-bottomright cursor-pointer">
          <i class="fa fa-arrows-v" aria-hidden="true"></i>
        </div>
      </div>

    </div>

    <div class="w3-row bottom-row light-grey">

        <div v-if="cardWrapper.inSections.length > 0" class="">
          <div v-for="inSection in cardWrapper.inSections" :key="inSection.id"
            v-if="showThisTag(inSection)" class="w3-left insection-tag-container">
            <div class="">
              <router-link :to="{ name: 'ModelSectionContent', params: { sectionId: inSection.id } }"
                class="gray-1 w3-tag w3-round w3-small">
                {{ inSection.title }}
              </router-link>
            </div>
          </div>
        </div>

        <div class="w3-right cursor-pointer indicator-comp"
          @click="cardClicked()">
          <app-indicator
            contextType="MODEL_CARD"
            :contextElementId="cardWrapper.id"
            :size="18"
            type="messages"
            :forceUpdate="forceUpdate">
          </app-indicator>
        </div>

        <div class="w3-right cursor-pointer indicator-comp"
          @click="toggleLike()">
          <app-indicator
            contextType="MODEL_CARD"
            :contextElementId="cardWrapper.id"
            :size="18"
            type="likes"
            :selected="cardWrapper.userLiked"
            :autoUpdate="false"
            :countInit="cardWrapper.nLikes">
          </app-indicator>
        </div>

    </div>

    <transition name="fadeenter">
      <div v-if="hovering && enableExpand"
        class="w3-padding model-action-button gray-2-color w3-display-topright cursor-pointer"
        @click="cardClicked()">
        <i class="fa fa-expand" aria-hidden="true"></i>
      </div>
    </transition>

  </div>
</template>

<script>
export default {

  name: 'model-card-as-card',

  props: {
    cardWrapper: {
      type: Object,
      default: null
    }
  },

  computed: {
    cardTextStyle () {
      if (!this.showFull) {
        if (this.hasImage) {
          return {
            maxHeight: '100px'
          }
        } else {
          return {
            maxHeight: '250px'
          }
        }
      } else {
        return {}
      }
    }
  },

  methods: {
    card () {
      return this.cardWrapper.card
    },
    hasImage () {
      return this.card.imageFile !== null
    }
  }
}
</script>

<style scoped>
</style>
