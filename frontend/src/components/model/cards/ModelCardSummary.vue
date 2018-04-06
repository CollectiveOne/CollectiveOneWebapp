<template lang="html">
  <div class="card-summary-container w3-card-2 w3-row"
    @mouseover="hovering = true"
    @mouseleave="hovering = false">

    <div class="w3-col m8 left-div">
      <div @click="cardClicked()" class="w3-left expand-btn cursor-pointer">
        <i class="fa fa-expand" aria-hidden="true"></i>
      </div>

      <div class="w3-left text-div">
        {{ cardShortTitle }}
      </div>

      <div v-if="cardWrapper.inSections.length > 0" class="w3-right text-div">
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
    </div>

    <div class="w3-col m4 right-div">

      <div v-if="creator !== null" class="w3-left text-div">
        {{ creator.nickname }}
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
  </div>
</template>

<script>
import { cardMixin } from '@/components/model/cards/cardMixin.js'

export default {

  name: 'model-card-summary',

  mixins: [ cardMixin ],

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
      hovering: false,
      creator: null
    }
  },

  computed: {
    cardShortTitle () {
      return this.card.title !== '' ? this.card.title : (this.card.text.length < 40 ? this.card.text : this.card.text.slice(0, 40) + '...')
    },
    card () {
      return this.cardWrapper.card
    },
    hasImage () {
      return this.card.imageFile !== null
    }
  },

  methods: {
    showThisTag (inSection) {
      return inSection.id !== this.inSection.id
    },
    hoverEnter () {
      this.showActionButton = true
      this.highlight = true
    },
    hoverLeave () {
      this.showActionButton = false
      this.highlight = false
    }
  },

  created () {
    this.getAuthors()
  }
}
</script>

<style scoped>

.card-summary-container {
  padding: 6px 0px;
}

.left-div {
  padding: 0px 12px;
  border-right-style: solid;
  border-width: 1px;
  border-color: #d0d0d0;
}

.right-div {
  padding: 0px 12px;
}

.expand-btn {
  width: 28px;
  margin-right: 10px;
  background-color: #d6d6d6;
  text-align: center;
  padding: 3px 6px;
  border-radius: 3px;
}

.expand-btn:hover {
  background-color: #a3a0a0;
}

.text-div {
  padding-top: 3px;
}



.insection-tag-container {
  display: inline-block;
  margin-right: 5px;
  margin-bottom: 2px;
}

.indicator-comp {
  margin-right: 6px;
  margin-left: 4px;
}

</style>
