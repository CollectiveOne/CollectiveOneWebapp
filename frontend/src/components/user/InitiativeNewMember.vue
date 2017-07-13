<template lang="html">
  <div class="w3-row">
    <div class="w3-col m7" v-if="showSelector">
      <app-user-selector
        class="user-selector"
        anchor="c1Id" label="nickname"
        url="/1/secured/users/suggestions"
        @select="userSelected($event)">
      </app-user-selector>
    </div>
    <div class="w3-col m5">
      <div class="w3-row-padding">
        <div class="w3-col s8 w3-center">
          <select class="role-select w3-select w3-round" v-model="member.role">
            <option value="" disabled>Choose role</option>
            <option value="ADMIN">ADMIN</option>
            <option value="MEMBER" selected>MEMBER</option>
          </select>
        </div>
        <div class="w3-col s4 w3-center">
          <button type="button" class="add-btn w3-button w3-theme w3-round" @click="add()">add</button>
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
      showSelector: true,
      member: {
        role: 'MEMBER',
        user: null
      }
    }
  },

  methods: {
    add () {
      this.$emit('add', this.member)
    },
    userSelected (user) {
      this.$children[0].$destroy()
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
