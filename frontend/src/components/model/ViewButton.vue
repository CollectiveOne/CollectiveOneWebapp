<template lang="html">
  <div class="w3-row">
    <router-link class="w3-col" :to="{ name: 'ModelView', params: { viewId: view.id } }">
      <div class="w3-row">
        <div class="message-indicator">
          <app-indicator
            contextType="MODEL_VIEW"
            :contextElementId="view.id"
            :size="16"
            :hideWhenZero="false"
            type="messages">
          </app-indicator>
          <!-- <app-likes-indicator
            contextType="MODEL_VIEW"
            :contextElementId="view.id"
            :size="16">
          </app-likes-indicator>
          <app-cards-indicator
            contextType="MODEL_VIEW"
            :contextElementId="view.id"
            :size="16">
          </app-cards-indicator> -->
        </div>
      </div>
      <div class="w3-row">
        <div class="w3-button"
          :class="{'app-button-light': !selected, 'app-button': selected}">
          {{ view.title }}
        </div>
      </div>
    </router-link>
  </div>
</template>

<script>
export default {
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
}

</style>
