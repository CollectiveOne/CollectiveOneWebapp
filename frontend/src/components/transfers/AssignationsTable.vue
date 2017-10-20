<template lang="html">
  <div class="full-table-container">
    <table v-if="assignations.length > 0" class="w3-table w3-striped w3-bordered w3-centered table-element">
      <thead>
        <tr>
          <th class="w3-hide-small">DATE</th>
          <th>STATUS</th>
          <th v-if="showFrom">FROM</th>
          <th>VALUE</th>
          <th>TO</th>
          <th class="w3-hide-small">TYPE</th>
          <th class="w3-hide-small">MOTIVE</th>
          <th>more</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="assignation in assignations" :key="assignation.id">
          <td class="w3-hide-small">{{ dateString(assignation.creationDate) }}</td>
          <td>
            <router-link tag="div"
              class="w3-tag w3-small w3-round-large w3-padding-small noselect cursor-pointer"
              :to="{ name: 'InitiativeAssignation', params: { assignationId: assignation.id } }"
              :class="stateTagClass(assignation)">
              {{ assignationState(assignation) }}
            </router-link>
          </td>
          <td v-if="showFrom">
            <router-link tag="div" :to="{name: 'Initiative', params: {'initiativeId': assignation.initiativeId}}"
              class="w3-tag w3-round-large cursor-pointer noselect"
              :style="{'background-color': $store.getters.colorOfInitiative(assignation.initiativeId)}">
              {{ assignation.initiativeName }}
            </router-link>
          </td>
          <td>{{ tokensString(assignation.assets[0].value) }} {{ assignation.assets[0].assetName }}</td>
          <td class="avatar-col">
            <div class="avatars-container">
              <div class="avatar-container" v-for="receiver in assignation.receivers">
                <app-user-avatar :user="receiver.user" :showName="false" :small="true"></app-user-avatar>
              </div>
            </div>
          </td>
          <td class="w3-hide-small">{{ assignationType(assignation) }}</td>
          <td class="w3-hide-small">{{ assignation.motive }}</td>
          <router-link
            tag="td"
            :to="{ name: 'InitiativeAssignation', params: { assignationId: assignation.id } }"
            class="cursor-pointer gray-1-color w3-button">
            <i class="fa fa-external-link" aria-hidden="true"></i>
          </router-link>
        </tr>
      </tbody>
    </table>
    <div v-else class="empty-div">
      no transfers have been made to any user
    </div>
    <div class="w3-row w3-center w3-margin-top">
      <button v-if="showMore"
       @click="showMoreClick()"
       class="w3-button app-button-light" type="button" name="button">
        show more...
      </button>
    </div>
  </div>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'
import { tokensString, dateString } from '@/lib/common.js'

export default {
  name: 'assignationsTable',

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
    }
  },

  data () {
    return {
      assignations: [],
      currentPage: 0,
      showMore: true
    }
  },

  methods: {
    getData () {
      this.axios.get(this.url, {
        params: {
          page: this.currentPage,
          size: 10,
          sortDirection: 'DESC',
          sortProperty: 'creationDate'
        }
      }).then((response) => {
        if (response.data.data.length < 10) {
          this.showMore = false
        }
        this.assignations = this.assignations.concat(response.data.data)
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
    },
    assignationType (assignation) {
      switch (assignation.type) {
        case 'PEER_REVIEWED':
          return 'PR'

        case 'DIRECT':
          return 'D'
      }

      return ''
    },
    assignationState (assignation) {
      switch (assignation.state) {
        case 'OPEN':
          if (assignation.thisEvaluation) {
            if (assignation.thisEvaluation.evaluationState === 'PENDING') {
              return 'EVALUATE'
            } else {
              return 'REVIEW'
            }
          } else {
            return 'OPEN'
          }

        default:
          return assignation.state
      }
    },
    stateTagClass (assignation) {
      switch (assignation.state) {
        case 'ON_HOLD':
          return {
            'on-hold-color': true
          }

        case 'OPEN':
          return {
            'open-color': true
          }

        case 'DONE':
          return {
            'done-color': true
          }

        case 'REVERT_ORDERED':
        case 'REVERTED':
        case 'DELETED':
          return {
            'revert-color': true
          }
      }
    }
  },

  created () {
    this.getData()
  }
}
</script>

<style scoped>

.full-table-container {
  max-height: 80vh;
  overflow-y: auto;
}

.table-element {
  overflow: auto;
}

.avatar-col {
  white-space: nowrap;
  text-align: left !important;
}

.avatars-container {
  max-width: 20vw;
  overflow: auto;
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
