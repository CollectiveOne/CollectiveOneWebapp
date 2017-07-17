<template lang="html">
  <div class="members-table-container">
    <div class="w3-row table-row">
      <table class="w3-table w3-striped w3-bordered w3-centered table-element">
        <thead>
          <tr>
            <th class="avatar-col">avatar</th>
            <th>nickname</th>
            <th class="role-col">role</th>
            <th>delete</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="member in members">
            <td class="avatar-col">
              <app-user-avatar :user="member.user" :showName="false"></app-user-avatar>
            </td>
            <td>{{ member.user.nickname }}</td>
            <td class="noselect w3-center role-col">
              <select v-if="canEdit" v-model="member.role" @change="$emit('role-updated', member)" class="role-select w3-select w3-round">
                <option value="" disabled>Choose role</option>
                <option value="ADMIN">ADMIN</option>
                <option value="MEMBER" selected>MEMBER</option>
              </select>
              <p v-else>
                <span class="role-tag w3-tag w3-round w3-theme">
                  {{ member.role ? member.role : 'MEMBER' }}
                </span>
              </p>
            </td>
            <td @click="$emit('remove', member)" class="w3-button">
              <i class="fa fa-times-circle-o w3-xlarge gray-1-color" aria-hidden="true"></i>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-if="canEdit" class="new-member-row">
      <div class="w3-row w3-container">
        <label class="">
          <b>add user:</b>
        </label>
      </div>

      <div class="w3-row user-selector-row">
        <div class="w3-col m7">
          <app-user-selector
            class="user-selector"
            anchor="c1Id" label="nickname" :resetTrigger="resetUserSelector"
            url="/1/secured/users/suggestions"
            @select="userSelected($event)">
          </app-user-selector>
        </div>
        <div class="w3-col m5">
          <div class="w3-row-padding">
            <div class="w3-col s8 w3-center">
              <select class="role-select w3-select w3-round" v-model="newMember.role">
                <option value="" disabled>Choose role</option>
                <option value="ADMIN">ADMIN</option>
                <option value="MEMBER" selected>MEMBER</option>
              </select>
            </div>
            <div class="w3-col s4 w3-center">
              <button type="button" class="add-btn w3-button app-button" @click="add()">add</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'
import UserSelector from '@/components/user/UserSelector.vue'

export default {
  components: {
    'app-user-avatar': UserAvatar,
    'app-user-selector': UserSelector
  },

  props: {
    members: Array,
    canEdit: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      newMember: {
        user: null,
        role: 'MEMBER'
      },
      resetUserSelector: false
    }
  },

  methods: {
    add () {
      this.resetUserSelector = !this.resetUserSelector
      this.$emit('add', this.newMember)
    },
    userSelected (user) {
      this.newMember.user = user
    }
  }
}
</script>

<style scoped>

.members-table-container {
}

.table-row {
  margin: 0 auto;
  margin-bottom: 25px;
}

.avatar-col {
  width: 50px;
}

.role-col {
  width: 120px;
}

.user-selector-row .w3-col {
  margin-bottom: 10px;
}

</style>
