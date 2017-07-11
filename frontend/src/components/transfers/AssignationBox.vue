<template lang="html">
  <div v-if="assignation" class="w3-card-2 assignation-container w3-display-container">

    <div class="top-right-div w3-display-topright">
      <router-link tag="div" :to="{ name: 'InitiativeAssignation', params: { assignationId: assignation.id } }"
        class="w3-button d2-color">
        <i class="fa fa-window-maximize" aria-hidden="true"></i>
      </router-link>
    </div>

    <div class="w3-row">

      <div class="w3-col l4 seal-container">
        <app-value-seal :value="assignation.assets[0].value" :assetName="assignation.assets[0].assetName"></app-value-seal>
      </div>

      <div class="w3-col l8 data-container w3-container">
        <div class="w3-row tags-row">
          <div class="w3-left w3-tag w3-theme-l2 w3-round noselect w3-small">
            <b>{{ assignation.type }}</b>
          </div>
          <div class="w3-left w3-tag w3-theme-l2 w3-round noselect w3-small">
            <b>{{ assignation.state }}</b>
          </div>
        </div>

        <div class="w3-row from-row">
          <div class="w3-left d2-color">
            <label class="noselect"><b>from:</b></label> {{ assignation.initiativeName }}
          </div>
        </div>

        <div class="w3-row motive-row">
          <div class="w3-left d2-color data-label">
            <label class="noselect"><b>motive:</b></label> {{ assignation.motive }}
          </div>
        </div>

        <div class="w3-row receivers-row">
          <div class="w3-left d2-color">
            <label class="noselect"><b>to:</b></label>
          </div>
          <div class="w3-left receivers-container" v-for="receiver in assignation.receivers" :key="receiver.id" >
            <app-user-avatar :user="receiver.user"
              class="w3-left"
              :small="true" :showName="false">
            </app-user-avatar>
          </div>
        </div>

      </div>
    </div>
  </div>

</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'
import ValueSeal from '@/components/transfers/ValueSeal.vue'

export default {
  components: {
    'app-user-avatar': UserAvatar,
    'app-value-seal': ValueSeal
  },

  props: {
    assignation: {
      type: Object
    }
  },

  data () {
    return {
    }
  },

  computed: {
    isDirect () {
      return (this.assignation.type === 'DIRECT')
    },
    isPeerReviewed () {
      return (this.assignation.type === 'PEER_REVIEWED')
    }
  },

  methods: {
  }
}
</script>

<style scoped>

.assignation-container {
  padding-top: 10px;
  padding-bottom: 10px;
}

.top-right-div {
}

.seal-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.data-container {
  padding-top: 5px;
}

.tags-row {
  margin-bottom: 5px;
}

.tags-row .w3-tag {
  width: 100px;
  margin-right: 10px;
}

.from-row {
  margin-bottom: 0px;
}

.motive-row {
  margin-bottom: 0px;
}

.receivers-row {
  margin-bottom: 5px;
}

.receivers-container {
  margin-left: 15px;
  padding-top: 6px;
}

.action-btn {
  width: 100%;
  max-width: 220px;
  padding-top: 2px !important;
  padding-bottom: 2px !important;
}

.percent-input {
  max-width: 100px;
}

.fa-percent {
  font-size: 22px;
  margin-top: 7.5px;
  margin-left: 5px;
}

.not-sure-col {
  margin-left: 20px;
}

.not-sure-col button {
  width: 120px;
}

.bottom-btns-row {
  margin-top: 10px;
  margin-bottom: 20px;
}

.bottom-btns-row button {
  width: 100%;
}


</style>
