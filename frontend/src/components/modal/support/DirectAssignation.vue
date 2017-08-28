<template lang="html">
  <div class="">
    <div class="w3-row w3-margin-bottom">
      <label for=""><b>Receiver: </b></label>
    </div>
    <div class="w3-row">
      <app-member-selector :members="members" @select="newReceiverSelected($event)"></app-member-selector>
    </div>
  </div>
</template>

<script>
import MemberSelector from '@/components/user/MemberSelector.vue'

export default {
  components: {
    'app-member-selector': MemberSelector
  },

  data () {
    return {
    }
  },

  computed: {
    members () {
      return this.$store.getters.initiativeMembersUsers()
    }
  },

  methods: {
    newReceiverSelected (user) {
      this.$emit('updated', {
        receivers: [
          {
            user: user,
            percent: 100
          }
        ]})
    }
  }
}
</script>

<style scoped>
</style>
