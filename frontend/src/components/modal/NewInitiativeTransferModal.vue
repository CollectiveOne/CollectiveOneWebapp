<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">
        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-theme">
          <h2>Direct transfer to another initiative</h2>
        </div>

        <div class="this-container w3-container">

          <div class="w3-row">
            <label class="w3-text-indigo"><b>Transfer to</b></label>
            <select v-model="transfer.receiverId" class="w3-select initiative-selector" name="transferto">
              <option v-for="subinitiative in subinitiatives" :value="subinitiative.id">{{ subinitiative.name }}</option>
            </select>
          </div>

          <br>

          <div class="w3-row">
            <label class="w3-text-indigo"><b>Motive</b></label>
            <input v-model="transfer.motive" class="w3-input w3-hover-light-gray" type="text">
            <br>

            <label class="w3-text-indigo"><b>Notes</b></label>
            <textarea v-model="transfer.notes" class="w3-input w3-border w3-round w3-hover-light-gray"></textarea>
            <br>
          </div>

          <div class="w3-row">
            <app-initiative-assets-assigner
              :initInitiativeId="initiative.id"
              @updated="assetsSelected($event)">
            </app-initiative-assets-assigner>
          </div>

          <hr>

          <div class="bottom-btns-row w3-row-padding">
            <div class="w3-col m6">
              <button type="button" class="w3-button w3-light-gray w3-round" @click="closeThis()">Cancel</button>
            </div>
            <div class="w3-col m6">
              <button type="button" class="w3-button w3-theme w3-round" @click="accept()">Accept</button>
            </div>
          </div>

        </div>

      </div>
    </div>
  </div>
</template>
receiverId
<script>
import { mapActions } from 'vuex'
import InitiativeAssetsAssigner from '@/components/transfers/InitiativeAssetsAssigner.vue'

export default {

  components: {
    'app-initiative-assets-assigner': InitiativeAssetsAssigner
  },

  data () {
    return {
      transfer: {
        motive: '',
        notes: '',
        receiverId: ''
      }
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    subinitiatives () {
      return this.initiative.subInitiatives
    }
  },

  methods: {
    ...mapActions(['showOutputMessage']),

    closeThis () {
      this.$store.commit('showNewInitiativeTransferModal', false)
    },
    assetsSelected (assets) {
      this.transfer.assetId = assets[0].assetId
      this.transfer.value = assets[0].value
      this.transfer.senderId = assets[0].senderId
    },
    accept () {
      this.axios.post('/1/secured/initiative/' + this.transfer.senderId + '/transferToInitiative', this.transfer)
      .then((response) => {
        this.$store.commit('triggerUpdateAssets')
        this.closeThis()
      })
    }
  }

}
</script>



<style scoped>

.w3-modal {
  display: block;
}

.this-container {
  padding-top: 10px;
  padding-bottom: 30px;
}

.close-div {
  width: 70px;
  height: 70px;
  cursor: pointer;
  text-align: right;
}

.fa-times {
  color: rgb(255, 255, 255);
  margin-right: 20px;
  margin-top: 20px;
}

form {
  padding-top: 0px;
  padding-bottom: 35px;
}

.initiative-selector {
  margin-top: 5px;
  padding-left: 10px;
}

.assset-assigner {
  margin-bottom: 10px;
}

.bottom-btns-row button {
  width: 100%;
}

</style>
