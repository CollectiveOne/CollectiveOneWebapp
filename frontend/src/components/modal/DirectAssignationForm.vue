<template lang="html">
  <div class="w3-container">
    <div class="w3-row">
      <div class="w3-col m8 assset-assigner">
        <app-initiative-assets-assigner
          v-if="initiative" :initiativeId="initiativeId" type='member-assigner'
          @updated="assetsSelected($event)">
        </app-initiative-assets-assigner>
      </div>
      <div class="w3-col m4">
        <div class="w3-left">
          <h5 class="w3-text-indigo w3-left"><b>Transfer to </b></h5>
        </div>
        <app-user-selector class="w3-left"
          anchor="c1Id" label="nickname"
          url="/1/secured/users/suggestions"
          @select="receiver = $event">
        </app-user-selector>
      </div>
    </div>

    <div class="w3-row">
      <div v-if="transferReady" class="w3-panel w3-theme w3-round-xlarge">
        <h5><b>Summary</b></h5>
        <p>
          {{ receiver.nickname }} will receive <span v-for="transfer in assetsTransfers">
          <span v-if="transfer.value ">
            <b>{{ tokensString(transfer.value) }} {{ transfer.assetName }}</b> from {{ initiative.name }}
          </span>
        </span>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import InitiativeAssetsAssigner from './InitiativeAssetsAssigner.vue'
import UserSelector from '../user/UserSelector.vue'

/* eslint-disable no-unused-vars */
import { tokensString } from '../../lib/common'
/* eslint-enable no-unused-vars */

export default {
  components: {
    'app-initiative-assets-assigner': InitiativeAssetsAssigner,
    'app-user-selector': UserSelector
  },

  props: {
    initiativeId: {
      type: String
    }
  },

  methods: {
    tokensString (v) {
      return tokensString(v)
    }
  },

  computed: {
    transferReady () {
      if (this.receiver != null) {
        if (this.assetsTransfers.length > 0) {
          for (var ix in this.assetsTransfers) {
            if (this.assetsTransfers[ix].value > 0) {
              return true
            } else {
              return false
            }
          }
        }
      }
      return false
    }
  }
}
</script>

<style lang="css">
</style>
