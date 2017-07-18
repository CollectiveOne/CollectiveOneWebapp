<template lang="html">
  <div class="">
    <table class="w3-table w3-striped w3-bordered w3-centered">
      <tbody>
        <tr v-for="(userData, ix) in usersData" :key="userData.user.c1Id">
          <td class="avatar-col">
            <app-user-avatar :user="userData.user" class="user-container" :showName="false"></app-user-avatar>
          </td>
          <td>
            {{ userData.user.nickname }}
          </td>
          <td v-if="isReceivers">
            <div class="w3-right donate-btn-container">
              <button @click="donorClicked(userData.user)" class="donate-button w3-button w3-small app-button">{{userData.isDonor ? 'receive' : 'donate'}}</button>
            </div>
          </td>
          <td>
            <div @click="removeUser(userData.user)" class="w3-right w3-button w3-xlarge remove-button">
              <div ><i class="fa fa-times-circle-o gray-1-color" aria-hidden="true"></i></div>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
    <div class="w3-row w3-margin-top">
      <app-member-selector class="w3-col m8 user-selector w3-margin-bottom"
        :members="members" :resetAfterSelect="true"
        @select="addUser($event)">
      </app-member-selector>
      <div class="w3-col m4 ">
        <button class="w3-button app-button" @click="addAllMembers()">add all</button>
      </div>
    </div>
  </div>
</template>

<script>
import UserAvatar from './UserAvatar.vue'
import MemberSelector from './MemberSelector.vue'

export default {
  props: {
    usersInit: {
      type: Array,
      default: () => {
        return []
      }
    },
    type: {
      type: String,
      default: 'none'
    }
  },

  components: {
    'app-user-avatar': UserAvatar,
    'app-member-selector': MemberSelector
  },

  data () {
    return {
      usersData: []
    }
  },

  computed: {
    isReceivers () {
      return this.type === 'receivers'
    },
    members () {
      return this.$store.getters.initiativeMembersUsers()
    }
  },

  methods: {
    addAllMembers () {
      this.usersData = []
      for (var ix in this.members) {
        this.usersData.push({
          user: this.members[ix],
          percent: 0,
          isDonor: false
        })
      }
      this.$emit('updated', this.usersData)
    },
    addUser (user) {
      if (this.indexOfUser(user) === -1) {
        this.usersData.push({
          user: user,
          percent: 0,
          isDonor: false
        })
        this.$emit('updated', this.usersData)
      }
    },
    removeUser (user) {
      var ix = this.indexOfUser(user)
      if (ix !== -1) {
        this.usersData.splice(ix, 1)
        this.$emit('updated', this.usersData)
      }
    },
    indexOfUser (user) {
      for (var ix in this.usersData) {
        if (this.usersData[ix].user.c1Id === user.c1Id) {
          return ix
        }
      }
      return -1
    },
    donorClicked (user) {
      var ix = this.indexOfUser(user)
      if (ix !== -1) {
        this.usersData[ix].isDonor = !this.usersData[ix].isDonor
        this.$emit('updated', this.usersData)
      }
    }
  },

  mounted () {
    this.usersData = JSON.parse(JSON.stringify(this.usersInit))
  }
}
</script>

<style scoped>

</style>
