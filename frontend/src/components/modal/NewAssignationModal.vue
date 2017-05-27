<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">
        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-theme">
          <h2>{{ peerReview ? 'New Peer-Reviewed assignation' : 'New Direct Assignation' }}</h2>
        </div>

        <form class="w3-container">
          <div class="section-tabs w3-row">
            <div class="w3-half tablink w3-bottombar w3-hover-light-grey w3-padding"
              :class="{'w3-border-blue': !peerReview}"
              @click="peerReview = false">
              <h5 class="w3-text-indigo" :class="{'bold-text': !peerReview}">Direct</h5>
            </div>
            <div class="w3-half tablink w3-bottombar w3-hover-light-grey w3-padding"
              :class="{'w3-border-blue': peerReview}"
              @click="peerReview = true">
              <h5 class="w3-text-indigo" :class="{'bold-text': peerReview}">Peer Reviewed</h5>
            </div>
          </div>
          <br>

          <app-initiative-assets-assigner
            v-if="initiative" :initiative="initiative" type='member-assigner'
            @updated="parentAssetsSelected($event)" @parent-initiative-updated="parentInitiativeUpdated($event)">
          </app-initiative-assets-assigner>
        </form>

      </div>
    </div>
  </div>
</template>

<script>
import InitiativeAssetsAssigner from './InitiativeAssetsAssigner.vue'
export default {

  components: {
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
      initiative: null
    }
  },

  methods: {
    closeThis () {
      this.$emit('close-this')
    },
    updateInitiative () {
      this.axios.get('/1/secured/initiative/' + this.initiativeId, {
        params: {
          addAssets: true
        }
      }).then((response) => {
        this.initiative = response.data.data
      })
    }
  },

  mounted () {
    this.updateInitiative()
  }

}
</script>

<style scoped>

.w3-modal {
  display: block;
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

</style>
