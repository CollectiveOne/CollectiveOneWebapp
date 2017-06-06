<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">
        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-theme">
          <h2>{{ peerReview ? 'New Peer-Reviewed Assignation' : 'New Direct Assignation' }}</h2>
        </div>

        <div class="w3-container">
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

          <app-direct-assignation-form
            v-if="initiative" :initiative="initiative" @updated="assignationUpdated($event)">
          </app-direct-assignation-form>

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
import DirectAssignationForm from './DirectAssignationForm.vue'

export default {

  components: {
    'app-direct-assignation-form': DirectAssignationForm
  },

  props: {
    initiativeId: {
      type: String
    }
  },

  data () {
    return {
      peerReview: false,
      initiative: null,
      assignation: null
    }
  },

  methods: {
    ...mapActions(['showOutputMessage']),

    closeThis () {
      this.$emit('close-this')
    },
    assignationUpdated (assignation) {
      this.assignation = assignation
    },
    accept () {
      this.axios.post('/1/secured/initiative/' + this.initiative.id + '/assignation', this.assignation)
      .then((response) => {
      })
    }
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

.assset-assigner {
  margin-bottom: 10px;
}

.bottom-btns-row button {
  width: 100%;
}

</style>
