<template lang="html">
  <table v-if="transfers.length > 0" class="w3-table w3-striped w3-bordered w3-centered">
    <thead>
      <tr>
        <th v-if="showFrom">from</th>
        <th>value</th>
        <th>to</th>
        <th class="w3-hide-small">motive</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="transfer in transfers" :key="transfer.id">
        <td v-if="showFrom">{{ transfer.senderName }}</td>
        <td>{{ tokensString(transfer.value) }} {{ transfer.assetName }}</td>
        <td>{{ transfer.receiverName }}</td>
        <td class="w3-hide-small">{{ transfer.motive }}</td>
      </tr>
    </tbody>
  </table>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'
import { tokensString } from '@/lib/common.js'

export default {
  name: 'transfersTable',

  components: {
    'app-user-avatar': UserAvatar
  },

  props: {
    transfers: Array,
    showFrom: {
      type: Boolean,
      default: false
    }
  },

  methods: {
    tokensString (v) {
      return tokensString(v)
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
