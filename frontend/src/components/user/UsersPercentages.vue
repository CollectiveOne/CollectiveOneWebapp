<template lang="html">
  <div class="percentages-container">
    <div v-for="(userData, ix) in usersData" class="w3-row-padding">
      <div class="w3-col l6 w3-center">
        <app-user-avatar :user="userData.receiverUser" class="user-container"></app-user-avatar>
      </div>
      <div class="w3-col l6">
        <div class="w3-row input-div">
          <div class="w3-col s6 w3-center">
            <div class="slider-container">
              <transition name="slideDownUp" mode="out-in">
                <div key="1" class="w3-row" v-if="!isDontKnow(userData)">
                  <i class="fa fa-percent d2-color w3-right" aria-hidden="true"></i>
                  <input
                    v-model.number="userData.percent"
                    class="percent-input w3-right w3-input w3-border w3-hover-light-gray w3-round"
                    type="number" step="5" min="0"
                    :disabled="disable">
                </div>
                <div v-else key="2" class="">
                  <div class="w3-tag w3-padding w3-round w3-theme noselect">
                    DK
                  </div>
                </div>
              </transition>
            </div>
          </div>
          <div class="w3-col s6 not-sure-col">
            <button v-if="!disable"
              class="w3-button w3-theme w3-round w3-left"
              @click="toggleDontKnow(userData)">
              {{ isDontKnow(userData) ? 'set' : 'dont know' }}
            </button>
          </div>
        </div>
      </div>
      <div class="w3-col l12">
        <hr>
      </div>
    </div>
    <div class="w3-row-padding total-row">
      <div class="w3-col l6">
        <div class="w3-right">
          <b>total assigned:</b>
        </div>
      </div>
      <div class="w3-col l2">
        <div class="w3-left">
          <b class="d2-color">{{ sumOfPercents }}%</b>
        </div>
      </div>
      <div v-if="missingPercent != 0" class="w3-col l4">
        <div class="w3-left">
          <b class="warning-panel">{{ missingPercent }}% {{ missingPercent > 0 ? 'missing' : 'in excess'}}</b>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'

export default {
  components: {
    'app-user-avatar': UserAvatar
  },

  props: {
    usersDataInit: {
      type: Array,
      default: () => { return [] }
    },
    disable: {
      type: Boolean,
      defualt: false
    }
  },

  data () {
    return {
      usersData: []
    }
  },

  computed: {
    sumOfPercents () {
      var sum = 0.0
      for (var ix in this.usersData) {
        var thisUserData = this.usersData[ix]
        if (!this.isDontKnow(thisUserData)) {
          sum += thisUserData.percent
        }
      }
      return sum
    },
    missingPercent () {
      return 100 - this.sumOfPercents
    },
    arePercentagesOk () {
      if (this.sumOfPercents === 100) {
        return true
      } else {
        return false
      }
    }
  },

  methods: {
    isDontKnow (userData) {
      if (userData.type) {
        if (userData.type === 'SET') {
          return false
        }
        if (userData.type === 'DONT_KNOW') {
          return true
        }
      } else {
        return false
      }
    },
    toggleDontKnow (userData) {
      if (this.isDontKnow(userData)) {
        userData.type = 'SET'
      } else {
        userData.percent = 0
        userData.type = 'DONT_KNOW'
      }
    }
  },

  mounted () {
    this.usersData = this.usersDataInit
  }
}
</script>

<style scoped>

.percent-input {
  max-width: 70px;
}

.input-div .fa-percent {
  margin-left: 5px;
  margin-top: 10px;
  font-size: 20px;
}

.not-sure-col button {
  margin-left: 10px;
  width: 100px;
}

.total-row {
  font-size: 20px;
}

.bottom-btns-row {
  margin-top: 10px;
  margin-bottom: 20px;
}

.bottom-btns-row button {
  width: 100%;
}

</style>
