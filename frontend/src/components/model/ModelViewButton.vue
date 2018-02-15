<template lang="html">
  <div class="w3-row container">
    <router-link class="w3-col" :to="{ name: 'ModelView', params: { viewId: view.id } }">
      <div class="w3-row light-grey">
        <div class="message-indicator w3-right">
          <app-indicator
            :initiativeId="view.initiativeId"
            contextType="MODEL_VIEW"
            :contextElementId="view.id"
            type="messages"
            :size="16"
            :hideWhenZero="false">
          </app-indicator>
        </div>
      </div>
      <div class="w3-row">
        <div class="w3-padding w3-center"
          :class="{'light-grey-d1': !selected, 'button-blue': selected}">
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

.container {
  min-width: 100px;
  overflow: hidden;
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
  border-bottom-left-radius: 10px;
  border-bottom-right-radius: 10px;
}

.message-indicator {
  margin-right: 5px;
}

</style>
