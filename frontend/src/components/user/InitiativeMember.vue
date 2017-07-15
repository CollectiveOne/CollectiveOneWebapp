<template lang="html">
  <div class="w3-row">
    <div class="w3-col m7">
      <app-user-avatar :user="member.user"></app-user-avatar>
    </div>
    <div class="w3-col m5">
      <div class="w3-row-padding">
        <div class="w3-col noselect w3-center" :class="{'s8' : canEdit, 's12' : !canEdit}">
          <select v-if="canEdit" v-model="member.role" @change="$emit('role-updated', member)" class="role-select w3-select w3-round">
            <option value="" disabled>Choose role</option>
            <option value="ADMIN">ADMIN</option>
            <option value="MEMBER" selected>MEMBER</option>
          </select>
          <p v-else><span class="role-tag w3-tag w3-round w3-theme">{{ member.role ? member.role : 'MEMBER' }}</span></p>
        </div>
        <div v-if="canEdit" class="w3-col s4 w3-xlarge w3-button w3-center">
          <div @click="$emit('remove', member)"><i class="fa fa-times-circle-o l1-color" aria-hidden="true"></i></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UserAvatar from '../user/UserAvatar.vue'

export default {
  components: {
    'app-user-avatar': UserAvatar
  },

  props: {
    member: {
      type: Object
    },
    canEdit: {
      type: Boolean,
      default: false
    }
  }
}
</script>

<style scoped>

.role-select {
  width: 120px;
}

</style>
