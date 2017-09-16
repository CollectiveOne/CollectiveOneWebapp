<template lang="html">
  <div class="slider-container">
    <transition name="slideDownUp">
      <div v-if="show"class="w3-panel w3-padding light-grey">
        <h3>{{ message }}</h3>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  data () {
    return {
      show: false
    }
  },

  methods: {
    showNow () {
      this.show = true
      setTimeout(() => {
        this.show = false
      }, 5000)
    }
  },

  mounted () {
    if (this.$route.query.fromAll) {
      this.axios.put('/1/notifications/unsubscribeFromAll').then((response) => {
        this.message = 'Thats it! you wont receive further email notifications from CollectiveOne. ' +
          'Please contact CollectiveOne to enable them back.'
        this.showNow()
      })
    }

    if (this.$route.query.fromInitiativeId) {
      this.axios.put('/1/notifications/unsubscribeFromInitiative/' + this.$route.query.fromInitiativeId).then((response) => {
        this.message = 'Thats it! you wont receive further email notifications for activity in ' +
          this.$route.query.fromInitiativeName + '. Please visit that initiative page to enable them back.'
        this.showNow()
      })
    }
  }
}
</script>

<style lang="css">
</style>
