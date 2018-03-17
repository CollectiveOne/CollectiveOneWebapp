<template lang="html">
  <div v-if="usersData.length > 0" class="percentages-container">
    <table class="w3-table w3-striped w3-bordered w3-centered table-class">
      <thead>
        <tr>
          <th class="avatar-col middle" colspan="2">USER</th>
          <th v-if="hasDonors">DONOR</th>
          <th v-if="amEvaluator" class="percent-col">MINE %</th>
          <th v-if="hasDonors" class="percent-col">PRE-DONOR %</th>
          <th class="percent-col">{{ disable ? 'FINAL %' : '%' }}</th>
          <th v-if="showTotal" class="value-col">{{ disable ? 'FINAL TOKENS' : 'TOTAL' }}</th>
          <th class="bar-col w3-hide-small w3-hide-medium"></th>
          <th v-if="!disable && showDontKnow">KNOW / DON'T</th>
          <th v-if="showSelfBiases" class="self-bias-col">SELF-BIAS</th>
          <th v-if="showRemove" class="self-bias-col">REMOVE</th>
        </tr><i></i>
      </thead>
      <tbody>
        <tr v-for="(userData, ix) in usersData">
          <td class="avatar-col">
            <app-user-avatar :user="userData[userAnchor]" class="user-container" :showName="false"></app-user-avatar>
          </td>
          <td>
            {{ userData[userAnchor].nickname }}
          </td>
          <td v-if="hasDonors">{{ userData.isDonor ? 'yes' : 'no' }}</td>
          <td v-if="myEvaluations.length > 0" class="percent-col">
            {{ myEvaluationOf(userData) }}
          </td>
          <td v-if="hasDonors"  class="percent-col">
            {{ userData.evaluatedPercent.toFixed(1) }} %
          </td>
          <td class="percent-col">
            <div v-if="disable" class="">
              <div v-if="!isDontKnow(userData)" class="">
                <b>{{ userData.percent.toFixed(1) }} %</b>
              </div>
              <div v-else class="">
                <b>don't know</b>
              </div>
            </div>
            <div v-else class="slider-container">
              <transition name="slideDownUp" mode="out-in">
                <div key="1" v-if="!isDontKnow(userData)">
                  <input
                    @blur="percentUpdated($event, userData)"
                    :value="userData.percent"
                    class="percent-input w3-input w3-border w3-hover-light-grey w3-round"
                    type="number" step="5" min="0"
                    :disabled="disable">
                </div>
                <div v-else key="2">
                  <div class="w3-tag w3-padding w3-round gray-1 noselect">
                    DK
                  </div>
                </div>
              </transition>
            </div>
          </td>
          <td v-if="showTotal" class="value-col">
            <div v-if="disable" class="">
              <div v-if="!isDontKnow(userData)" class="">
                <b>{{ (userData.percent * totalTokens / 100).toFixed(2) }}</b>
              </div>
              <div v-else class="">
                <b>don't know</b>
              </div>
            </div>
            <div v-else class="slider-container">
              <transition name="slideDownUp" mode="out-in">
                <div key="1" v-if="!isDontKnow(userData)">
                  <input
                    :value="userData.percent * totalTokens / 100"
                    @blur="valueUpdated($event, userData)"
                    class="percent-input w3-input w3-border w3-hover-light-grey w3-round"
                    type="number" step="5" min="0"
                    :disabled="disable">
                </div>
                <div v-else key="2">
                  <div class="w3-tag w3-padding w3-round gray-1 noselect">
                    DK
                  </div>
                </div>
              </transition>
            </div>
          </td>
          <td class="bar-col w3-hide-small w3-hide-medium">
            <div class="light-grey w3-round">
              <div class="bar-div app-blue w3-center w3-round" :style="{'width': userData.percent +'%'}">
              </div>
            </div>
          </td>
          <td v-if="!disable && showDontKnow">
            <button
              class="w3-button app-button dont-know-button"
              @click="toggleDontKnow(userData)">
              {{ isDontKnow(userData) ? 'set' : 'dont know' }}
            </button>
          </td>
          <td v-if="showSelfBiases" class="self-bias-col" :class="{'self-bias-good': userData.selfBias <= 0, 'self-bias-bad': userData.selfBias > 0}">
            <b>{{ Math.abs(userData.selfBias) < 100 ? userData.selfBias.toFixed(1) + ' %' : '-'}}</b>
          </td>
          <td v-if="showRemove" class="w3-xlarge w3-button"
            @click="$emit('remove', userData)">
            <i class="fa fa-times-circle-o gray-1-color" aria-hidden="true"></i>
          </td>
        </tr>
      </tbody>
      <tfoot v-if="!disable">
        <tr>
          <td></td>
          <td>total assigned:</td>
          <td>{{ sumOfPercents.toFixed() }}%</td>
          <td v-if="showTotal">{{ sumOfPercents * totalTokens / 100 }}</td>
          <td>{{ autoScaled ? '( auto-rounded )' : ''}}</td>
        </tr>
      </tfoot>
    </table>
    <div v-if="!disable" class="slider-container">
      <transition name="slideDownUp">
        <div v-if="!arePercentagesOk" class="w3-row missing-row">
          <div>
            {{ missingPercent > 0 ? 'missing' : 'excess of'}}: <b>{{ Math.abs(missingPercent.toFixed(0)) }}%<span v-if="showTotal"> / {{ Math.abs(missingPercent/100) * totalTokens }}</span> </b>
            <button
              class="w3-button app-button w3-margin-left"
              @click="autoScale()">
              autoscale
            </button>
          </div>
        </div>
      </transition>
    </div>

  </div>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'
import { floatToChar } from '@/lib/common.js'

export default {
  components: {
    'app-user-avatar': UserAvatar
  },

  props: {
    /* WARNING: percents are updated directly in this property! */
    usersData: {
      type: Array,
      default: () => { return [] }
    },
    disable: {
      type: Boolean,
      defualt: false
    },
    userAnchor: {
      type: String,
      default: 'user'
    },
    showSelfBiases: {
      type: Boolean,
      default: false
    },
    myEvaluations: {
      type: Array,
      default: () => { return [] }
    },
    totalTokens: {
      type: Number,
      default: 1
    },
    showDontKnow: {
      type: Boolean,
      default: true
    },
    showTotal: {
      type: Boolean,
      default: true
    },
    showRemove: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      autoScaled: false
    }
  },

  computed: {
    amEvaluator () {
      if (this.myEvaluations) {
        return this.myEvaluations.length > 0
      } else {
        return false
      }
    },
    hasDonors () {
      for (var ix in this.usersData) {
        if (this.usersData[ix].isDonor) {
          return true
        }
      }
      return false
    },
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
      if (Math.abs(this.sumOfPercents - 100.0) < 0.001) {
        return true
      } else {
        return false
      }
    },
    feedbackText () {
      if (Math.abs(this.missingPercent) > 0) {
        if (this.missingPercent > 0) {
          return 'missing'
        } else {
          return 'in excess'
        }
      }
    },
    totalClass () {
      if (this.arePercentagesOk) {
        return {
          'success-panel': true
        }
      } else {
        return {
          'error-panel': true
        }
      }
    }
  },

  methods: {
    floatToChar (v) {
      return floatToChar(v)
    },
    percentUpdated (event, userData) {
      userData.percent = parseFloat(event.target.value)
      this.checkRounding()
      this.$emit('users-data-updated')
    },
    valueUpdated (event, userData) {
      userData.percent = parseFloat(event.target.value) / this.totalTokens * 100
      this.checkRounding()
      this.$emit('users-data-updated')
    },
    checkRounding () {
      if ((Math.abs(this.missingPercent) > 0) && (Math.abs(this.missingPercent) < 1)) {
        this.autoScale()
      } else {
        this.autoScaled = false
      }
    },
    autoScale () {
      if (this.sumOfPercents > 0) {
        var scale = this.sumOfPercents / 100
        for (var ix in this.usersData) {
          this.usersData[ix].percent *= 1 / scale
        }
        this.autoScaled = true
      }
    },
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
    },
    myEvaluationOf (userData) {
      for (var ix in this.myEvaluations) {
        if (this.myEvaluations[ix].receiverUser.c1Id === userData.user.c1Id) {
          if (this.myEvaluations[ix].type === 'DONT_KNOW') {
            return 'DK'
          } else {
            return this.myEvaluations[ix].percent.toFixed(1) + ' %'
          }
        }
      }
    }
  }
}
</script>

<style scoped>

.table-class th {
  vertical-align: middle;
}

.avatar-col {
  /*width: 50px;*/
}

.percent-col {
  width: 100px;
}

.value-col {
  width: 110px;
}

.bar-col {
  width: 200px;
}

.bar-div {
  height: 20px;
}

.self-bias-col {
  width: 100px;
}

.dont-know-button {
  width: 100px;
}

.missing-panel {
  max-width: 100px;
}

.user-hr {
  margin: 5px 0px 5px 0px !important;
}

.total-row {
  margin-top: 10px;
  font-size: 18px;
}

.total-class {
  max-width: 100px;
}

.missing-row {
  margin-top: 20px;
  text-align: center;
}

.missing-row div {
  display: inline-block;
}

.self-bias-good {
  color: rgb(39, 187, 131);
}

.self-bias-bad {
  color: rgb(170, 0, 0);
}

.bottom-btns-row {
  margin-top: 10px;
  margin-bottom: 20px;
}

.bottom-btns-row button {
  width: 100%;
}

</style>
