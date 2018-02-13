<template lang="html">
  <div class="w3-display-container">
    <router-link
      :to="{ name: 'ModelView', params: { viewId: view.id } }"
      class="w3-button w3-right"
      :class="{'app-button-light': !selected, 'app-button': selected}">
      {{ view.title }}
    </router-link>
    <div class="w3-display-topright message-indicator">
      <app-messages-indicator
        contextType="MODEL_VIEW"
        :contextElementId="view.id"
        :size="16">
      </app-messages-indicator>
    </div>
  </div>
</template>

<script>
import MessagesIndicator from '@/components/notifications/MessagesIndicator.vue'

export default {
  components: {
    'app-messages-indicator': MessagesIndicator
  },

  props: {
    view: {
      type: Object
    },
    selected: {
      type: Boolean,
      default: false
    }
  }
}
</script>

<style scoped>

.message-indicator {
  margin-top: -10px;
  margin-right: -15px;
}

</style>
