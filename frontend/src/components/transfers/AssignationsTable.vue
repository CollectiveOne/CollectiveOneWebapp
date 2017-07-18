<template lang="html">
  <div class="">
    <table v-if="assignations.length > 0" class="w3-table w3-striped w3-bordered w3-centered table-element">
      <thead>
        <tr>
          <th class="w3-hide-small">date</th>
          <th>status</th>
          <th v-if="showFrom">from</th>
          <th>value</th>
          <th>to</th>
          <th class="w3-hide-small">type</th>
          <th class="w3-hide-small">motive</th>
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
            <div class="w3-tag w3-round-large" :style="{'background-color': $store.getters.colorOfInitiative(assignation.initiativeId)}">
              {{ assignation.initiativeName }}
            </div>
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
    assignations: Array,
    showFrom: {
      type: Boolean,
      default: false
    }
  },

  methods: {
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
        case 'DONE':
          return 'DONE'

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
      }

      return ''
    },
    stateTagClass (assignation) {
      if (assignation.state === 'OPEN') {
        return {
          'open-color': true
        }
      } else {
        return {
          'done-color': true
        }
      }
    }
  }
}
</script>

<style scoped>

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
