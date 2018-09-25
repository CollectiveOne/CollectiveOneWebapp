<template lang="html">
  <div class="">
    <div class="w3-row header-row">

      <app-header :inInitiative="false" :withInitiativeNav="false">
      </app-header>

      <app-user-profile
        :userId="$route.params.userId">
      </app-user-profile>

    </div>
  </div>

</template>

<script>
import Header from '@/components/Header.vue'
import UserProfile from '@/components/user/UserProfile.vue'

export default {
  components: {
    'app-header': Header,
    'app-user-profile': UserProfile
  }
}
</script>

<style scoped>
</style>
