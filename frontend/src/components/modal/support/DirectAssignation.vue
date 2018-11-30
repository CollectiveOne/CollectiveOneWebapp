<template lang="html">
  <div class="">
    <div class="w3-row w3-margin-bottom">
      <label for=""><b>Receiver: </b></label>
    </div>
    <div class="w3-row">
      <app-member-selector :members="members" @select="newReceiverSelected($event)" :resetAfterSelect="multiple"></app-member-selector>
    </div>
    <div v-if="multiple" class="w3-row w3-margin-top w3-border w3-round">
      <div class="percentages-container">
        <app-user-percentages
          :usersData="receiversData"
          :showDontKnow="false"
          :showTotal="false"
          :showRemove="true"
          @remove="removeReceiver($event)"
          @users-data-updated="receiversUpdated($event)">
        </app-user-percentages>
      </div>
      <div v-if="receiversData.length === 0" class="w3-padding">
        <i>{{ $t('tokens.NO_MEMBERS_SELECTED') }}</i>
      </div>
    </div>
    <div class="w3-row w3-margin-top">
      or <button @click="multiple = !multiple" class="w3-button app-button" type="button" name="button">
        {{ multiple ? $t('$tokens.ONE_RECEIVER') : $t('tokens.MULTIPLE_RECEIVERS') }}
      </button>
    </div>
  </div>
</template>

<script>
import MemberSelector from '@/components/user/MemberSelector.vue'
import UsersPercentages from '@/components/user/UsersPercentages.vue'

export default {
  components: {
    'app-member-selector': MemberSelector,
    'app-user-percentages': UsersPercentages
  },

  data () {
    return {
      multiple: false,
      receiversData: []
    }
  },

  computed: {
    members () {
      return this.$store.getters.initiativeMembersUsers()
    }
  },

  methods: {
    indexOfUser (user) {
      for (var ix in this.receiversData) {
        if (this.receiversData[ix].user.c1Id === user.c1Id) {
          return ix
        }
      }
      return -1
    },
    removeReceiver (userData) {
      var ix = this.indexOfUser(userData.user)
      if (ix > -1) {
        this.receiversData.splice(ix, 1)
      }
    },
    newReceiverSelected (user) {
      if (!this.multiple) {
        this.$emit('updated', {
          receivers: [
            {
              user: user,
              percent: 100
            }
          ] })
      } else {
        var ix = this.indexOfUser(user)
        if (ix === -1) {
          this.receiversData.push({
            user: user,
            percent: 0,
            isDonor: false
          })
        }
      }
    },
    receiversUpdated () {
      this.$emit('updated', {
        receivers: this.receiversData
      })
    }
  }
}
</script>

<style scoped>

.percentages-container {
  padding-bottom: 10px;
}

</style>
