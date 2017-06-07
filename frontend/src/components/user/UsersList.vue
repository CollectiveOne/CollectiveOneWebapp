<template lang="html">
  <div class="">
    <div class="w3-row" v-for="user in users">
      <div class="w3-col s10">
        <app-user-avatar :user="user"></app-user-avatar>
      </div>
      <div class="w3-col s2 w3-xxlarge w3-button w3-center">
        <div @click="removeClicked(user)"><i class="fa fa-times-circle-o l1-color" aria-hidden="true"></i></div>
      </div>
    </div>

    <div class="w3-row">
      <app-user-selector class="user-selector"
        anchor="c1Id" label="nickname"
        url="/1/secured/users/suggestions"
        @select="userSelected($event)">
      </app-user-selector>
    </div>
  </div>
</template>

<script>
import UserAvatar from './UserAvatar.vue'
import UserSelector from './UserSelector.vue'

export default {
  props: {
    users: {
      type: Array,
      default: () => {
        return []
      }
    }
  },

  components: {
    'app-user-avatar': UserAvatar,
    'app-user-selector': UserSelector
  },

  methods: {
    userSelected (user) {
      this.$emit('add-user', user)
    },
    removeClicked (user) {
      this.$emit('remove-user', user)
    }
  }
}
</script>

<style lang="css">
</style>
