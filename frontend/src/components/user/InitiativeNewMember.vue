<template lang="html">

</template>

<script>
import UserSelector from '../user/UserSelector.vue'
import UserAvatar from '../user/UserAvatar.vue'

export default {
  components: {
    'app-user-selector': UserSelector,
    'app-user-avatar': UserAvatar
  },

  data () {
    return {
      showSelector: true,
      member: {
        role: 'MEMBER',
        user: null
      },
      resetUserSelector: true
    }
  },

  methods: {
    add () {
      this.resetUserSelector = !this.resetUserSelector
      this.$emit('add', this.member)
    },
    userSelected (user) {
      this.member.user = user
    }
  }
}
</script>

<style scoped>

.user-selector {
  margin-bottom: 10px;
}

.role-select {
  width: 120px;
}

</style>
