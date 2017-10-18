<template lang="html">
  <table v-if="submembers.length > 0" class="w3-table w3-striped w3-bordered w3-centered table-element">
    <thead>
      <tr>
        <th class="avatar-col">AVATAR</th>
        <th>NICKNAME</th>
        <th class="subinitiatives-col">SUBINITIATIVES</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="submember in submembers">
        <td class="avatar-col">
          <app-user-avatar :user="submember.user" :showName="false"></app-user-avatar>
        </td>
        <td>{{ submember.user.nickname }}</td>
        <td>
          <router-link tag="div" :to="{name: 'Initiative', params: {'initiativeId': subinitiative.id}}"
            v-for="subinitiative in submember.subinitiatives"
            :key="subinitiative.id"
            class="subinitiative-tag w3-tag w3-round-large cursor-pointer noselect"
            :style="{'background-color': $store.getters.colorOfInitiative(subinitiative.id)}">
            {{ subinitiative.name }}
          </router-link>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'

export default {
  components: {
    'app-user-avatar': UserAvatar
  },

  props: {
    submembers: Array
  }
}
</script>

<style scoped>

.avatar-col {
  /*width: 50px;*/
}

.subinitiative-tag {
  margin-left: 5px;
  margin-bottom: 3px;
}

</style>
