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
            <app-initiative-selector class="initiative-selector"
              anchor="id" label="name"
              url="/1/secured/initiatives/suggestions"
              @select="receiverSelected($event)">
            </app-initiative-selector>
          </div>

          <br>

          <div class="w3-row">
            <label class="w3-text-indigo"><b>Motive</b></label>
            <input v-model="assignation.motive" class="w3-input w3-hover-light-gray" type="text">
            <br>

            <label class="w3-text-indigo"><b>Notes</b></label>
            <textarea v-model="assignation.notes" class="w3-input w3-border w3-round w3-hover-light-gray"></textarea>
            <br>
          </div>

          <div class="w3-row">
            <app-initiative-assets-assigner
              :initiativeId="initiativeId" type='member-assigner'
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

<script>
import { mapActions } from 'vuex'
import InitiativeSelector from '../initiative/InitiativeSelector.vue'
import InitiativeAssetsAssigner from './InitiativeAssetsAssigner.vue'

export default {

  components: {
    'app-initiative-selector': InitiativeSelector,
    'app-initiative-assets-assigner': InitiativeAssetsAssigner
  },

  props: {
    initiativeId: {
      type: String
    }
  },

  data () {
    return {
      peerReview: false,
      assignation: {
        motive: '',
        notes: ''
      }
    }
  },

  computed: {
  },

  methods: {
    ...mapActions(['showOutputMessage']),

    closeThis () {
      this.$emit('close-this')
    },
    assetsSelected (assets) {
      this.assignation.assets = assets
    },
    receiverSelected (initiative) {
      this.assignation.receiver = initiative
    },
    accept () {
      this.axios.post('/1/secured/initiative/' + this.assignation.initiativeId + '/transferToInitiative', this.assignation)
      .then((response) => {
        this.$emit('assignation-done')
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

.section-tabs {
  text-align: center;
  user-select: none;
  cursor: pointer;
}

.assset-assigner {
  margin-bottom: 10px;
}

.bottom-btns-row button {
  width: 100%;
}

</style>
