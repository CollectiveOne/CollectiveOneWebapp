<template lang="html">
  <div class="popper">
    <div @click="showDetails = !showDetails" class="header cursor-pointer">
      {{ title }}<br>
      <small class="more-button">{{ showDetails ? 'less' : 'more' }}</small>
    </div>
    <hr v-if="showDetails">
    <div v-if="showDetails" class="details-div" v-html="details">
    </div>

  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String
    },
    details: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      showDetails: false
    }
  }
}
</script>

<style scoped>

.popper {
  padding: 6px 12px;
}

.header {
  line-height: 10px;
}

.more-button {
  text-decoration: underline;
  color: blue;
}

.details-div {
  width: 250px;
  white-space: normal;
  padding-bottom: 10px;
  text-align: left;
}

</style>
