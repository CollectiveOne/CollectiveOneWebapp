<template lang="html">
  <app-user-profile
    :userId="$route.params.userId">
  </app-user-profile>
</template>

<script>
import UserProfile from '@/components/user/UserProfile.vue'

export default {
  components: {
    'app-user-profile': UserProfile
  }
}
</script>

<style scoped>
</style>
