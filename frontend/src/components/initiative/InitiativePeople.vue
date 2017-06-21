<template lang="html">
  <div class="w3-container this-container">
    <div class="w3-card members-panel">
      <header class="w3-container w3-theme-l2 noselect">
        <h4>Members and roles of this initiative</h4>
      </header>

      <div class="w3-container members-div">
        <app-initiative-member
          v-for="member in initiative.members.members"
          :key="member.user.c1Id"
          :member="member"
          :canEdit="isLoggedAnAdmin"
          @remove="removeMember($event)">
        </app-initiative-member>
        <div v-if="isLoggedAnAdmin">
          <div class="w3-row" :style="{'margin-bottom': '5px'}">
            <label class="w3-text-indigo noselect" :style="{'margin-bottom': '10px'}"><b>add member:</b></label>
          </div>
          <app-initiative-new-member @add="addMember($event)"></app-initiative-new-member>
        </div>
      </div>

    </div>

    <div class="w3-card members-panel" v-if="allSubmembers.length > 0" >
      <header class="w3-container w3-theme-l2 noselect">
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
            <div class="w3-tag w3-theme w3-round w3-left subinitiative-tag noselect" v-for="initiative in submember.subinitiatives">
              {{ initiative }}
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import InitiativeNewMember from './InitiativeNewMember.vue'
import InitiativeMember from './InitiativeMember.vue'
import UserAvatar from '../user/UserAvatar.vue'

const getIndexOfUser = function (list, c1Id) {
  for (var ix in list) {
    if (list[ix].user.c1Id === c1Id) {
      return ix
    }
  }
  return -1
}

const getAllSubmembers = function (subinitiativesMembers) {
  var submembers = []
  subinitiativesMembers.forEach((subinitiativeMembers) => {
    subinitiativeMembers.members.forEach((member) => {
      var ix = getIndexOfUser(submembers, member.user.c1Id)
      if (ix === -1) {
        submembers.push({
          user: member.user,
          subinitiatives: [ subinitiativeMembers.initiativeName ]
        })
      } else {
        submembers[ix].subinitiatives.push(subinitiativeMembers.initiativeName)
      }
    })
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
    initiative: {
      type: Object
    }
  },

  computed: {
    isLoggedAnAdmin () {
      return this.initiative.loggedMember.role === 'ADMIN'
    },
    allSubmembers () {
      return getAllSubmembers(this.initiative.members.subinitiativesMembers)
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
            this.$emit('please-update')
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
            this.$emit('please-update')
          } else {
            this.showOutputMessage(response.data.message)
          }
        })
      }
    },

    indexOfMember (c1Id) {
      for (var ix in this.initiative.members) {
        if (this.initiative.members[ix].user.c1Id === c1Id) {
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
