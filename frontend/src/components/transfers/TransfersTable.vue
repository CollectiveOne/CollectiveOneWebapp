<template lang="html">
  <div class="">
    <table v-if="transfers.length > 0" class="w3-table w3-striped w3-bordered w3-centered">
      <thead>
        <tr>
          <th>date</th>
          <th v-if="showFrom">from</th>
          <th>value</th>
          <th>to</th>
          <th class="w3-hide-small">motive</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="transfer in transfers" :key="transfer.id">
          <td>{{ dateString(transfer.orderDate) }}</td>
          <td v-if="showFrom">{{ transfer.senderName }}</td>
          <td>{{ tokensString(transfer.value) }} {{ transfer.assetName }}</td>
          <td>
            <div class="w3-tag w3-round-large" :style="{'background-color': $store.getters.colorOfInitiative(transfer.receiverId)}">
              {{ transfer.receiverName }}
            </div>
          </td>
          <td class="w3-hide-small">{{ transfer.motive }}</td>
        </tr>
      </tbody>
    </table>
    <div v-else class="empty-div">
      no transfers have been made to any initiative
    </div>
  </div>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'
import { tokensString, dateString } from '@/lib/common.js'

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
    },
    dateString (v) {
      return dateString(v)
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

.empty-div {
  padding-top: 20px;
  text-align: center;
  font-size: 18px;
}

</style>
