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

    <div v-if="addUserEnabled">
      <hr v-if="users.length > 1">
    </div>
    <div v-if="addUserEnabled" class="w3-row">
      <app-member-selector class="user-selector"
        :members="members" :resetAfterSelect="true"
        @select="userSelected($event)">
      </app-member-selector>
    </div>
  </div>
</template>

<script>
import UserAvatar from './UserAvatar.vue'
import MemberSelector from './MemberSelector.vue'

export default {
  props: {
    users: {
      type: Array,
      default: () => {
        return []
      }
    },
    addUserEnabled: {
      type: Boolean,
      default: true
    }
  },

  components: {
    'app-user-avatar': UserAvatar,
    'app-member-selector': MemberSelector
  },

  computed: {
    members () {
      return this.$store.getters.initiativeMembersUsers()
    }
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
