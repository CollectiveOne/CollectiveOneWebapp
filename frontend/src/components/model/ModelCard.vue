<template lang="html">
  <div class="">
    <div class="w3-display-container"
      :class="{ 'w3-card-2': cardEffect }"
      @mouseover="showActionButton = true"
      @mouseleave="showActionButton = false">

      <div class="w3-row"
        :class="{'card-container-padded': cardEffect, 'card-container-slim': !cardEffect }">

        <div v-if="card.title !== ''" class="">
          <b>{{ card.title }}</b>
        </div>

        <div class="card-text">
          <p>{{ card.text }}</p>
        </div>

      </div>

      <div v-if="showDetails" class="w3-row light-grey">
        <div class="w3-col s6">
          <div class="w3-tag light-grey-d1">
            {{ cardWrapper.state }}
          </div>
        </div>
        <div class="w3-col s6">
          {{ dateString(cardWrapper.targetDate) }}
        </div>
      </div>

      <transition name="fadeenter">
        <div v-if="showActionButton"
          @click="showCardModal()"
          class="w3-button model-action-button gray-2-color w3-display-topright">
          <i class="fa fa-expand" aria-hidden="true"></i>
        </div>
      </transition>
    </div>
  </div>
</template>

<script>
import { dateString } from '@/lib/common.js'

export default {
  name: 'model-card',

  components: {
  },

  props: {
    cardWrapper: {
      type: Object,
      default: null
    },
    initiativeId: {
      type: String,
      default: ''
    },
    cardEffect: {
      type: Boolean,
      default: true
    }
  },

  data () {
    return {
      showActionButton: false
    }
  },

  computed: {
    card () {
      return this.cardWrapper.card
    },
    showDetails () {
      if (this.cardEffect) {
        return this.cardWrapper.stateControl
      }
    }
  },

  methods: {
    dateString (v) {
      return dateString(v)
    },
    showCardModal () {
      this.$emit('show-card-modal', {
        new: false,
        cardWrapperId: this.cardWrapper.id,
        initiativeId: this.initiativeId
      })
    }
  }

}
</script>

<style scoped>

.card-container-padded {
  padding: 8px 16px 10px 16px !important;
}

.card-container-slim {
  padding: 0px 0px 0px 0px !important;
}

.card-text p {
  margin-top: 0px;
  margin-bottom: 0px;
}

</style>
