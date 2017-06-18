<template lang="html">
  <div class="w3-container this-container">
    <div class="w3-card">
      <header class="w3-container w3-theme-l2">
        <h4>Members</h4>
      </header>

      <div class="w3-container members-div">
        <app-initiative-member
          v-for="member in initiative.members"
          :key="member.user.c1Id"
          :member="member",
          @remove="removeMember($event)">
        </app-initiative-member>
        <div v-if="isLoggedAnAdmin">
          <div class="w3-row" :style="{'margin-bottom': '5px'}">
            <label class="w3-text-indigo" :style="{'margin-bottom': '10px'}"><b>add member:</b></label>
          </div>
          <app-initiative-new-member @add="addMember($event)"></app-initiative-new-member>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import InitiativeNewMember from './InitiativeNewMember.vue'
import InitiativeMember from './InitiativeMember.vue'

export default {
  components: {
    'app-initiative-member': InitiativeMember,
    'app-initiative-new-member': InitiativeNewMember
  },

  props: {
    initiative: {
      type: Object
    }
  },

  computed: {
    isLoggedAnAdmin () {
      return this.initiative.loggedMember.role === 'ADMIN'
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
}

.members-div {
  padding-top: 20px;
  padding-bottom: 20px;
}

</style>
