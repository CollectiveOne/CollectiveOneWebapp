<template lang="html">
  <div class="full-table-container">
    <table v-if="transfers.length > 0" class="w3-table w3-striped w3-bordered w3-centered">
      <thead>
        <tr>
          <th>{{ $t('tokens.DATE_UC') }}</th>
          <th v-if="showFrom">{{ $t('tokens.FROM_UC') }}</th>
          <th>{{ $t('tokens.VALUE_UC') }}</th>
          <th>{{ $t('tokens.TO_UC') }}</th>
          <th class="w3-hide-small">{{ $t('tokens.MOTIVE_UC') }}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="transfer in transfers" :key="transfer.id">
          <td>{{ dateString(transfer.orderDate) }}</td>
          <td v-if="showFrom">
            <router-link tag="div" :to="{name: 'Initiative', params: {'initiativeId': transfer.receiverId}}"
              class="w3-tag w3-round-large cursor-pointer noselect"
              :style="{'background-color': $store.getters.colorOfInitiative(transfer.senderId)}">
              {{ transfer.senderName }}
            </router-link>
          </td>
          <td>{{ tokensString(transfer.value) }} {{ transfer.assetName }}</td>
          <td>
            <router-link tag="div" :to="{name: 'Initiative', params: {'initiativeId': transfer.receiverId}}"
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
      {{ $t('general.NO_RESULTS_FOUND') }}
    </div>
    <div class="w3-row w3-center w3-margin-top">
      <button v-if="showMore"
       @click="showMoreClick()"
       class="w3-button app-button-light" type="button" name="button">
        {{ $t('general.SHOW_MORE') }}...
      </button>
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
    url: {
      type: String,
      default: ''
    },
    showFrom: {
      type: Boolean,
      default: false
    },
    triggerUpdate: {
      type: Boolean,
      default: false
    }
  },

  watch: {
    triggerUpdate () {
      this.resetData()
    }
  },

  data () {
    return {
      transfers: [],
      currentPage: 0,
      showMore: true
    }
  },

  methods: {
    resetData () {
      this.transfers = []
      this.currentPage = 0
      this.getData()
    },
    getData () {
      this.axios.get(this.url, {
        params: {
          page: this.currentPage,
          size: 10,
          sortDirection: 'DESC',
          sortProperty: 'orderDate'
        }
      }).then((response) => {
        if (response.data.data.length < 10) {
          this.showMore = false
        }
        this.transfers = this.transfers.concat(response.data.data)
      })
    },
    showMoreClick () {
      this.currentPage += 1
      this.getData()
    },
    tokensString (v) {
      return tokensString(v)
    },
    dateString (v) {
      return dateString(v)
    }
  },

  created () {
    this.resetData()
  }
}
</script>

<style scoped>

.full-table-container {
  max-height: 80vh;
  overflow-y: auto;
}

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
