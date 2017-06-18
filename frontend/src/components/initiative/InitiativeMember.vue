<template lang="html">
  <div class="w3-row">
    <div class="w3-col m7">
      <app-user-avatar :user="member.user"></app-user-avatar>
    </div>
    <div class="w3-col m5">
      <div class="w3-row-padding">
        <div class="w3-col s8 w3-center">
          <p><span class="role-tag w3-tag w3-round" :class="getRoleClasses(member)">{{ member.role ? member.role : 'MEMBER' }}</span></p>
        </div>
        <div v-if="canEdit" class="w3-col w3-xxlarge w3-button w3-center" :class="{'s4' : canEdit}">
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
  },

  methods: {
    getRoleClasses (member) {
      return {
        'w3-theme': member.role === 'ADMIN',
        'w3-theme-l2': member.role !== 'ADMIN'
      }
    }
  }
}
</script>

<style scoped>

.role-tag {
  width: 120px;
}

</style>
