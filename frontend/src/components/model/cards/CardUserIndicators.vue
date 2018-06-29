<template lang="html">
  <div class="w3-right">
    <div class="w3-right notification-div">
      <app-notifications-list
        :element="cardWrapper"
        contextType="MODEL_CARD_WRAPPER">
      </app-notifications-list>
    </div>

    <div v-if="!hideCardControls && this.isLoggedAnEditor" class="w3-right gray-1-color control-div">
      <app-card-control-buttons
        :cardWrapper="cardWrapper"
        :inSection="inSection"
        @update="$emit('update')"
        @createNew="$emit('createNew')"
        @edit="$emit('edit')"
        @updateCards="$emit('updateCards')">
      </app-card-control-buttons>
    </div>

    <div v-if="!isPrivate" class="w3-right cursor-pointer indicator-comp"
      @click="cardClicked()">
      <app-indicator
        contextType="MODEL_CARD"
        :contextElementId="cardWrapper.id"
        :size="18"
        type="messages"
        :forceUpdate="forceUpdate">
      </app-indicator>
    </div>

    <div v-if="!isPrivate" class="w3-right cursor-pointer indicator-comp"
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
</template>

<script>
import { cardMixin } from '@/components/model/cards/cardMixin.js'
import CardControlButtons from '@/components/model/cards/CardControlButtons.vue'
import NotificationsList from '@/components/notifications/NotificationsList.vue'

export default {

  mixins: [ cardMixin ],

  components: {
    'app-card-control-buttons': CardControlButtons,
    'app-notifications-list': NotificationsList
  },

  props: {
  },

  computed: {
  },

  methods: {
    toggleLike () {
      this.axios.put('/1/model/card/' + this.cardWrapper.id + '/like',
        {}, {
          params: {
            likeStatus: !this.cardWrapper.userLiked
          }
        }).then((response) => {
          this.$emit('update')
        })
    }
  }

}
</script>

<style scoped>

.indicator-comp {
  margin-right: 6px;
  margin-left: 4px;
}

.control-div {
  padding: 3px 6px;
}

</style>
