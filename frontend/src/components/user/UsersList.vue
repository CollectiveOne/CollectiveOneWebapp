<template lang="html">
  <div class="">
    <transition-group name="fade" tag="div">
      <div class="" v-for="(userData, ix) in usersData" :key="userData.user.c1Id">
        <div class="w3-row">
          <div class="w3-col w3-right user-controls w3-container">
            <div class="w3-row">
              <div class="w3-right w3-button w3-xlarge remove-button">
                <div @click="removeUser(userData.user)"><i class="fa fa-times-circle-o gray-1-color" aria-hidden="true"></i></div>
              </div>
              <div v-if="isReceivers" class="w3-right donate-btn-container">
                <button @click="donorClicked(userData.user)" class="donate-button w3-button w3-small app-button">{{userData.isDonor ? 'receive' : 'donate'}}</button>
              </div>
            </div>
          </div>
          <div class="w3-rest user-avatar-container">
            <div v-if="donorOverlay(userData)" class="donor-overlay">
              <b>IS DONOR</b>
            </div>
            <div class="w3-row" :class="{'covered-div': donorOverlay(userData)}">
              <app-user-avatar :user="userData.user"></app-user-avatar>
            </div>
          </div>
        </div>
        <hr class="user-hr">
      </div>
    </transition-group>

    <div class="w3-row">
      <app-member-selector class="user-selector"
        :members="members" :resetAfterSelect="true"
        @select="addUser($event)">
      </app-member-selector>
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
    donorOverlay (userData) {
      return userData.isDonor && this.isReceivers
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

.user-avatar-container {
  padding-top: 3px;
}

.donor-overlay {
  position: absolute;
  text-align: center;
  font-size: 29px;
  margin-left: 50px;
  color: rgba(255, 255, 255, 0.78);
  z-index: 100;
}

.user-controls {
  width: 160px;
}

.user-controls .w3-right {
  margin-left: 10px;
}

.donate-btn-container {
  padding-top: 10px;
}

.donate-button {
  padding: 2px 10px 2px 10px !important;
}

.remove-button {
  padding: 5px 10px 5px 10px !important;
}

.user-hr {
  margin: 2px 0px 2px 0px !important;
}

.user-selector {
  margin-top: 10px;
}

</style>
