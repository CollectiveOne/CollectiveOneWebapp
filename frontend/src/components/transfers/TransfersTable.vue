<template lang="html">
  <table class="w3-table w3-striped w3-bordered w3-centered">
    <thead>
      <tr>
        <th>status</th>
        <th>value</th>
        <th class="w3-hide-small">type</th>
        <th class="w3-hide-small">motive</th>
        <th>to</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="assignation in assignations" :key="assignation.id">
        <td>
          <router-link tag="div"
            :to="{ name: 'InitiativeAssignation', params: { assignationId: assignation.id } }"
            class="cursor-pointer w3-tag w3-small w3-round-large w3-padding-small"
            :class="stateTagClass(assignation)">
            {{ assignation.state }}
          </router-link>
        </td>
        <td>{{ tokensString(assignation.assets[0].value) }} {{ assignation.assets[0].assetName }}</td>
        <td class="w3-hide-small">{{ assignation.type }}</td>
        <td class="w3-hide-small">{{ assignation.motive }}</td>
        <td class="avatar-col">
          <div class="avatar-container" v-for="receiver in assignation.receivers">
            <app-user-avatar :user="receiver.user" :showName="false" :small="true"></app-user-avatar>
          </div>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'
import { tokensString } from '@/lib/common.js'

export default {
  components: {
    'app-user-avatar': UserAvatar
  },

  props: {
    transfers: Array
  },

  methods: {
    tokensString (v) {
      return tokensString(v)
    },
    stateTagClass (assignation) {
      if (assignation.state === 'OPEN') {
        return {
          'open-color': true
        }
      } else {
        return {
          'done-color': true
        }
      }
    }
  }
}
</script>

<style scoped>

.avatar-col {
  white-space: nowrap;
  text-align: left !important;
}

.avatar-container {
  display: inline-block;
  margin-left: 5px;
}

</style>
