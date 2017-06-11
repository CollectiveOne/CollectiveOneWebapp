<template lang="html">
  <div class="">
    <app-new-assignation-modal
      v-if="showNewAssignationModal"
      :initiativeId="initiative.id"
      @close-this="showNewAssignationModal = false"
      @assignation-done="$emit('please-update')">
    </app-new-assignation-modal>
    <div v-if="initiative" class="this-container w3-container w3-padding">
    <div class="w3-card">
      <header class="w3-container w3-theme-l2">
        <h4>Driver</h4>
      </header>
      <div class="w3-container">
        <p>{{ initiative.driver }}</p>
      </div>
    </div>
    <br>
    <div class="w3-card">
      <header class="w3-container w3-theme-l2">
        <h4>Assets</h4>
      </header>
      <div v-if="hasOwnTokens">
        <app-tokens-distribution-chart
          :assetId="initiative.ownTokens.assetId" :initiativeId="initiative.id"
          @new-initiative="$emit('new-initiative', $event)"
          @new-assignment="newAssignment($event)">
        </app-tokens-distribution-chart>
      </div>
      <div v-if="hasOtherAssets">
        <app-tokens-distribution-chart v-for="asset in initiative.otherAssets"
          :key="asset.assetId" :assetId="asset.assetId" :initiativeId="initiative.id"
          @new-initiative="$emit('new-initiative', $event)"
          @new-assignment="newAssignment($event)">
        </app-tokens-distribution-chart>
      </div>
    </div>

    <br>
    <div class="w3-card">
      <header class="w3-container w3-theme-l2">
        <h4>Members</h4>
      </header>

      <div class="w3-container members-div">
        <app-initiative-contributor
          v-for="contributor in initiative.contributors"
          :key="contributor.user.c1Id"
          :contributor="contributor"
          @remove="removeContributor($event)">
        </app-initiative-contributor>
        <div class="w3-row" :style="{'margin-bottom': '5px'}">
          <label class="w3-text-indigo" :style="{'margin-bottom': '10px'}"><b>add member:</b></label>
        </div>
        <app-initiative-new-contributor @add="addContributor($event)"></app-initiative-new-contributor>
      </div>

    </div>
    <br>
  </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import InitiativeTokensDistributionChart from './InitiativeTokensDistributionChart.vue'
import InitiativeNewContributor from './InitiativeNewContributor.vue'
import InitiativeContributor from './InitiativeContributor.vue'
import NewAssignationModal from '../modal/NewAssignationModal.vue'

export default {
  components: {
    'app-tokens-distribution-chart': InitiativeTokensDistributionChart,
    'app-initiative-contributor': InitiativeContributor,
    'app-initiative-new-contributor': InitiativeNewContributor,
    'app-new-assignation-modal': NewAssignationModal
  },

  props: {
    initiative: {
      type: Object
    }
  },

  data () {
    return {
      showNewAssignationModal: false,
      parentInitiativeIdForModal: null
    }
  },

  computed: {
    hasOwnTokens () {
      if (this.initiative.ownTokens) {
        return true
      } else {
        return false
      }
    },
    hasOtherAssets () {
      if (this.initiative.otherAssets) {
        if (this.initiative.otherAssets.length > 0) {
          return true
        }
      }
      return false
    }
  },

  methods: {
    ...mapActions(['showOutputMessage']),

    newAssignment (data) {
      this.showNewAssignationModal = true
    },

    addContributor (contributor) {
      var index = this.indexOfContributor(contributor.user.c1Id)
      contributor.initiativeId = this.initiative.id

      if (index === -1) {
        this.axios.post('/1/secured/initiative/' + this.initiative.id + '/contributor', contributor).then((response) => {
          if (response.data.result === 'success') {
            this.$emit('please-update')
          } else {
            this.showOutputMessage(response.data.message)
          }
        })
      } else {
        this.showOutputMessage('user has been already included')
      }
    },

    removeContributor (contributor) {
      var index = this.indexOfContributor(contributor.user.c1Id)
      if (index > -1) {
        this.axios.delete('/1/secured/initiative/' + this.initiative.id + '/contributor/' + contributor.id,
        ).then((response) => {
          if (response.data.result === 'success') {
            this.$emit('please-update')
          } else {
            this.showOutputMessage(response.data.message)
          }
        })
      }
    },

    indexOfContributor (c1Id) {
      for (var ix in this.initiative.contributors) {
        if (this.initiative.contributors[ix].user.c1Id === c1Id) {
          return ix
        }
      }
      return -1
    }
  }
}
</script>

<style scoped>

.this-container {
  padding-top: 25px !important;
}

.members-div {
  padding-top: 15px;
  padding-bottom: 30px;
}

</style>
