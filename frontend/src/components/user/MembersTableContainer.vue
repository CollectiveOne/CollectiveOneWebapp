<template lang="html">
  <app-members-table
    :members="members"
    :canEdit="canEdit"
    @remove="removeMember($event)"
    @role-updated="roleUpdated($event)"
    @add="addMember($event)">
  </app-members-table>
</template>

<script>
import MembersTable from '@/components/user/MembersTable.vue'

export default {
  components: {
    'app-members-table': MembersTable
  },

  props: {
    members: Array,
    canEdit: {
      type: Boolean,
      default: false
    }
  },

  methods: {
    addMember (member) {
      var index = this.indexOfMember(member.user.c1Id)
      if (index === -1) {
        this.members.push(JSON.parse(JSON.stringify(member)))
      }
    },

    removeMember (member) {
      var index = this.indexOfMember(member.user.c1Id)
      if (index > -1) {
        this.members.splice(index, 1)
      }
    },

    roleUpdated (member) {
      var index = this.indexOfMember(member.user.c1Id)
      if (index > -1) {
        this.members[index].role = member.role
      }
    },

    indexOfMember (c1Id) {
      for (var ix in this.members) {
        if (this.members[ix].user.c1Id === c1Id) {
          return ix
        }
      }
      return -1
    }
  }
}
</script>

<style scoped>
</style>
