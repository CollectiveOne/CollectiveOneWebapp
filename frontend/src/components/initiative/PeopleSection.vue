<template lang="html">
  <div class="w3-container this-container">
    <div class="w3-card members-panel">
      <header class="w3-container w3-theme noselect">
        <h4>Members and roles of {{ initiative.name }}</h4>
      </header>

      <div class="w3-container members-div">
        <app-initiative-member
          v-for="member in initiative.initiativeMembers.members"
          :key="member.user.c1Id"
          :member="member"
          :canEdit="isLoggedAnAdmin"
          @remove="removeMember($event)">
        </app-initiative-member>
        <div v-if="isLoggedAnAdmin">
          <div class="w3-row" :style="{'margin-bottom': '5px'}">
            <label class="d2-color noselect" :style="{'margin-bottom': '10px'}"><b>add member:</b></label>
          </div>
          <app-initiative-new-member @add="addMember($event)"></app-initiative-new-member>
        </div>
      </div>

    </div>

    <div class="w3-card members-panel" v-if="allSubmembers.length > 0" >
      <header class="w3-container w3-theme noselect">
        <h4>Members of sub-initiatives</h4>
      </header>

      <div class="w3-container members-div">
        <div class="w3-row" v-for="submember in allSubmembers" :key="submember.c1Id">
          <div class="w3-col m4">
            <app-user-avatar
              class="w3-left"
              :user="submember.user">
            </app-user-avatar>
          </div>
          <div class="w3-col m8 subinitiatives-tags-container">
            <router-link v-for="initiativeData in submember.subinitiatives"
              :to="'/inits/' + initiativeData.id" tag="div"
              :key="initiativeData.id"
              class="w3-tag w3-theme w3-round w3-left subinitiative-tag noselect cursor-pointer">
              {{ initiativeData.name }}
            </router-link>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import InitiativeNewMember from '@/components/user/InitiativeNewMember.vue'
import InitiativeMember from '@/components/user/InitiativeMember.vue'
import UserAvatar from '@/components/user/UserAvatar.vue'

const getIndexOfUser = function (list, c1Id) {
  for (var ix in list) {
    if (list[ix].user.c1Id === c1Id) {
      return ix
    }
  }
  return -1
}

const appendMembersAndSubmembers = function (initiativeMembers, allmembers) {
  allmembers = allmembers || []
  initiativeMembers.members.forEach((member) => {
    /* Add this initiative members */
    var initiativeData = {
      name: initiativeMembers.initiativeName,
      id: initiativeMembers.initiativeId
    }

    var ix = getIndexOfUser(allmembers, member.user.c1Id)
    if (ix === -1) {
      allmembers.push({
        user: member.user,
        subinitiatives: [ initiativeData ]
      })
    } else {
      allmembers[ix].subinitiatives.push(initiativeData)
    }
  })
  for (var ix in initiativeMembers.subinitiativesMembers) {
    appendMembersAndSubmembers(initiativeMembers.subinitiativesMembers[ix], allmembers)
  }
}

const getAllSubmembers = function (initiativeMembers) {
  var submembers = []
  initiativeMembers.subinitiativesMembers.forEach((subinitiativeMembers) => {
    appendMembersAndSubmembers(subinitiativeMembers, submembers)
  })
  return submembers
}

export default {
  components: {
    'app-initiative-member': InitiativeMember,
    'app-initiative-new-member': InitiativeNewMember,
    'app-user-avatar': UserAvatar
  },

  props: {
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    isLoggedAnAdmin () {
      return this.initiative.loggedMember.role === 'ADMIN'
    },
    allSubmembers () {
      return getAllSubmembers(this.initiative.initiativeMembers)
    }
  },

  methods: {
    ...mapActions(['showOutputMessage']),

    addMember (member) {
      var index = this.indexOfMember(member.user.c1Id)
      member.initiativeId = this.initiative.id

      if (index === -1) {
        this.axios.post('/1/secured/initiative/' + this.initiative.id + '/member', member).then((response) => {
          if (response.data.result === 'success') {
            this.$store.dispatch('refreshInitiative')
          } else {
            this.showOutputMessage(response.data.message)
          }
        })
      } else {
        this.showOutputMessage('user has been already included')
      }
    },

    removeMember (member) {
      var index = this.indexOfMember(member.user.c1Id)
      if (index > -1) {
        this.axios.delete('/1/secured/initiative/' + this.initiative.id + '/member/' + member.user.c1Id,
        ).then((response) => {
          if (response.data.result === 'success') {
            this.$store.dispatch('refreshInitiative')
          } else {
            this.showOutputMessage(response.data.message)
          }
        })
      }
    },

    indexOfMember (c1Id) {
      for (var ix in this.initiative.initiativeMembers.members) {
        if (this.initiative.initiativeMembers.members[ix].user.c1Id === c1Id) {
          return ix
        }
      }
      return -1
    }
  }
}
</script>

<style scoped>

.this-container {
  padding-top: 25px !important;
  padding-bottom: 25px !important;
}

.members-div {
  padding-top: 20px;
  padding-bottom: 20px;
}

.members-panel {
  margin-bottom: 25px;
}

.subinitiatives-tags-container {
  padding-top: 17px;
}

.subinitiative-tag {
  margin-right: 10px;
  margin-bottom: 10px;
  padding: 3px 10px 3px 10px;
}

</style>
