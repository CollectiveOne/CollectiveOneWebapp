<template lang="html">
  <div class="w3-row">
    <div class="w3-col m7">
      <app-user-selector class="user-selector"
        anchor="c1Id" label="nickname"
        url="/1/secured/users/suggestions"
        @select="contributor.user = $event">
      </app-user-selector>
    </div>
    <div class="w3-col m5">
      <div class="w3-row-padding">
        <div class="w3-col s8 w3-center">
          <select class="role-select w3-select w3-round" v-model="contributor.role">
            <option value="" disabled>Choose role</option>
            <option value="ADMIN">ADMIN</option>
            <option value="MEMBER" selected>MEMBER</option>
          </select>
        </div>
        <div class="w3-col s4 w3-center">
          <button type="button" class="add-btn w3-button w3-theme-l1 w3-round" @click="$emit('add', contributor)">add</button>
        </div>
      </div>

    </div>

  </div>
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
      contributor: {
        role: 'MEMBER',
        user: null
      }
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
