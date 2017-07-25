<template lang="html">
  <div class="">
    <table v-if="transfers.length > 0" class="w3-table w3-striped w3-bordered w3-centered">
      <thead>
        <tr>
          <th>DATE</th>
          <th v-if="showFrom">FROM</th>
          <th>VALUE</th>
          <th>TO</th>
          <th class="w3-hide-small">MOTIVE</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="transfer in transfers" :key="transfer.id">
          <td>{{ dateString(transfer.orderDate) }}</td>
          <td v-if="showFrom">
            <router-link tag="div" :to="'/inits/' + transfer.receiverId"
              class="w3-tag w3-round-large cursor-pointer noselect"
              :style="{'background-color': $store.getters.colorOfInitiative(transfer.senderId)}">
              {{ transfer.senderName }}
            </router-link>
          </td>
          <td>{{ tokensString(transfer.value) }} {{ transfer.assetName }}</td>
          <td>
            <router-link tag="div" :to="'/inits/' + transfer.receiverId"
              class="w3-tag w3-round-large cursor-pointer noselect"
              :style="{'background-color': $store.getters.colorOfInitiative(transfer.receiverId)}">
              {{ transfer.receiverName }}
            </router-link>
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
