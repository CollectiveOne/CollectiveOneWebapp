<template lang="html">
  <div class="this-container">
    <div class="own-members-div">
      <h3 class="section-header">members of {{ initiative.meta.name }}</h3>
      <app-members-table
        :members="initiative.initiativeMembers.members"
        :canEdit="isLoggedAnAdmin"
        @remove="removeMember($event)"
        @role-updated="roleUpdated($event)"
        @add="addMember($event)">
      </app-members-table>
      <div v-if="noOtherAdminError" class="w3-row w3-tag error-panel error-row w3-round">
        there should be at least one admin per initiative
      </div>
    </div>
    <div class="sub-members-div">
      <h3 class="section-header">members of subinitiatives of {{ initiative.meta.name }}</h3>
      <app-submembers-table
        :submembers="allSubmembers">
      </app-submembers-table>
    </div>

  </div>
</template>

<script>
import { mapActions } from 'vuex'
import MembersTable from '@/components/user/MembersTable.vue'
import SubmembersTable from '@/components/user/SubmembersTable.vue'

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
    'app-members-table': MembersTable,
    'app-submembers-table': SubmembersTable
  },

  data () {
    return {
      noOtherAdminError: false
    }
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

    roleUpdated (member) {
      var index = this.indexOfMember(member.user.c1Id)
      if (index > -1) {
        var otherAdmin = false
        for (var ix in this.initiative.initiativeMembers.members) {
          if (ix !== index) {
            if (this.initiative.initiativeMembers.members[ix].role === 'ADMIN') {
              otherAdmin = true
            }
          }
        }

        if (otherAdmin) {
          this.axios.put('/1/secured/initiative/' + this.initiative.id + '/member/' + member.user.c1Id, member
          ).then((response) => {
            if (response.data.result === 'success') {
              this.$store.dispatch('refreshInitiative')
            } else {
              this.showOutputMessage(response.data.message)
            }
          })
        } else {
          this.noOtherAdminError = true
          this.initiative.initiativeMembers.members[index].role = 'ADMIN'
          setTimeout(() => {
            this.noOtherAdminError = false
          }, 2000)
        }
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
  padding-top: 0px !important;
  padding-bottom: 25px !important;
}

.members-div {
  padding-top: 20px;
  padding-bottom: 20px;
}

.members-panel {
  margin-bottom: 25px;
}

.initiative-member-row {
  margin-bottom: 10px;
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
