<template lang="html">
  <div
    class="card-container w3-leftbar w3-border-thik w3-display-container w3-border-top"
    @mouseover="showActionButton = true"
    @mouseleave="showActionButton = false">

    <div v-if="card.title !== ''" class="">
      <b>{{ card.title }}</b>
    </div>

    <div class="card-text">
      <p>{{ card.text }}</p>
    </div>

    <transition name="fadeenter">
      <div v-if="showActionButton" class="w3-button model-action-button gray-2-color w3-display-topright">
        <i class="fa fa-expand" aria-hidden="true"></i>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'model-card',

  components: {
  },

  data () {
    return {
      showActionButton: false
    }
  },

  props: {
    card: {
      type: Object,
      default: null
    }
  }
}
</script>

<style scoped>

.card-container {
  padding: 5px 5px 5px 16px;
}

.card-text p {
  margin-top: 0px;
  margin-bottom: 0px;
}



</style>
