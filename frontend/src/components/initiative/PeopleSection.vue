<template lang="html">
  <div class="this-container">
    <div v-if="showWantToContribute" class="w3-row w3-center top-button-row">
      <div class="w3-row">
        <button @click="wantToContributeSelected = !wantToContributeSelected"
          class="w3-button app-button" type="button" name="button">
          Want to contribute?
        </button>
      </div>
      <div class="w3-row w3-margin-top">
        <div class="slider-container">
          <transition name="slideDownUp">
            <div v-if="wantToContributeSelected" class="w3-row tags-row w3-center">
              <div class="w3-padding w3-round light-grey w3-margin-bottom">
                <p>
                  The administrators of this initiative will be notified of your
                  interest and will be able to add you as a contributor to this initiative.
                </p>
              </div>

              <button
                class="w3-button app-button-light button-pair"
                @click="wantToContributeSelected = false">cancel
              </button>
              <button
                class="w3-button app-button button-pair"
                @click="wantToContribute()">confirm
              </button>
            </div>
          </transition>
        </div>
      </div>
    </div>
    <div class="w3-row own-members-div">
      <h3 class="section-header">Members of {{ initiative.meta.name }}:</h3>
      <app-members-table
        :members="initiative.initiativeMembers.members"
        :canEdit="isLoggedAnAdmin || isLoggedAParentAdmin"
        @remove="removeMember($event)"
        @role-updated="roleUpdated($event)"
        @add="addMember($event)">
      </app-members-table>
      <div v-if="noOtherAdminError" class="w3-row w3-tag error-panel error-row w3-round">
        there should be at least one admin per initiative
      </div>
    </div>
    <br>
    <div v-if="allSubmembers.length > 0" class="sub-members-div">
      <hr>
      <h3 class="section-header">Members of subinitiatives of {{ initiative.meta.name }}:</h3>
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
      noOtherAdminError: false,
      wantToContributeSelected: true
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    isLoggedAnAdmin () {
      return this.$store.getters.isLoggedAnAdmin
    },
    isLoggedAParentAdmin () {
      return this.$store.getters.isLoggedAParentAdmin
    },
    isLoggedAMember () {
      return this.$store.getters.isLoggedAMember
    },
    allSubmembers () {
      return getAllSubmembers(this.initiative.initiativeMembers)
    },
    showWantToContribute () {
      return !this.isLoggedAMember && !this.isLoggedAParentAdmin
    }
  },

  methods: {
    ...mapActions(['showOutputMessage']),

    addMember (member) {
      var index = this.indexOfMember(member.user.c1Id)
      member.initiativeId = this.initiative.id

      if (index === -1) {
        this.axios.post('/1/initiative/' + this.initiative.id + '/member', member).then((response) => {
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
        this.axios.delete('/1/initiative/' + this.initiative.id + '/member/' + member.user.c1Id,
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
          this.axios.put('/1/initiative/' + this.initiative.id + '/member/' + member.user.c1Id, member
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

.top-button-row {
  padding-top: 30px;
  padding-bottom: 30px;
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
