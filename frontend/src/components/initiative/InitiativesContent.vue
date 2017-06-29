<template lang="html">
  <div class="">

    <transition name="slideDownUp">
      <app-edit-initiative-modal v-if="showEditInitiativeModal" :initiative="initiative"
        @close-this="showEditInitiativeModal = false"
        @initiative-updated="updateThisInitiative()">
      </app-edit-initiative-modal>
    </transition>

    <div v-if="initiative" class="w3-row">
      <div class="w3-white">

      <div class="header-container">
        <transition name="fadeenter" mode="out-in">
            <header class="w3-theme" :key="initiative.name">
              <div class="w3-row" style="height:55px">
                <div v-if="isLoggedAnAdmin" class="w3-col w3-right edit-btn-div w3-button w3-large"
                  style="width:55px; height:100%">
                  <i class="fa fa-ellipsis-v" aria-hidden="true"></i>
                </div>
                <div class="w3-rest w3-container">
                  <h3 class="noselect">{{ initiative.name }}</h3>
                </div>
              </div>
            </header>
          </transition>
        </div>

        <div class="section-tabs w3-row w3-center">
          <router-link tag="div" to="overview" class="w3-col s4 tablink w3-bottombar w3-hover-light-grey w3-padding"
            :class="{'w3-border-blue': isOverview, 'w3-theme-l3': isOverview}"
            @click="">
            <h5 class="d2-color noselect" :class="{'bold-text': isOverview}">Overview</h5>
          </router-link>
          <router-link tag="div" to="people" class="w3-col s4 tablink w3-bottombar w3-hover-light-grey w3-padding"
            :class="{'w3-border-blue': isPeople, 'w3-theme-l3': isPeople}"
            @click="">
            <h5 class="d2-color noselect" :class="{'bold-text': isPeople}">People</h5>
          </router-link>
          <router-link tag="div" to="assignations" class="w3-col s4 tablink w3-bottombar w3-hover-light-grey w3-padding"
            :class="{'w3-border-blue': isAssignations, 'w3-theme-l3': isAssignations}"
            @click="">
            <h5 class="d2-color noselect" :class="{'bold-text': isAssignations}">Transfers</h5>
          </router-link>
        </div>

        <div class="w3-row content-container">
          <transition :name="animationType" mode="out-in">
            <router-view :initiative="initiative" :key="initiative.id"
              @new-initiative="$emit('new-initiative', $event)"
              @new-member="newMember($event)"
              @please-update="updateInitiative(initiative.id)">
            </router-view>
          </transition>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import EditInitiativeModal from '../modal/EditInitiativeModal.vue'

export default {

  components: {
    'app-edit-initiative-modal': EditInitiativeModal
  },

  data () {
    return {
      initiative: null,
      showEditInitiativeModal: false
    }
  },

  computed: {
    isLoggedAnAdmin () {
      return this.initiative.loggedMember.role === 'ADMIN'
    },
    isOverview () {
      var res = false
      this.$route.matched.forEach((e) => {
        if (e.name === 'InitiativeOverview') {
          res = true
        }
      })
      return res
    },
    isPeople () {
      var res = false
      this.$route.matched.forEach((e) => {
        if (e.name === 'InitiativePeople') {
          res = true
        }
      })
      return res
    },
    isAssignations () {
      var res = false
      this.$route.matched.forEach((e) => {
        if (e.name === 'InitiativeAssignations') {
          res = true
        }
      })
      return res
    },
    animationType () {
      return this.$store.state.support.contentAnimationType
    }
  },

  methods: {
    updateThisInitiative () {
      this.updateInitiative(this.initiative.id)
    },
    updateInitiative (id) {
      this.axios.get('/1/secured/initiative/' + id, {
        params: {
          addAssets: true,
          addSubinitiatives: true,
          addMembers: true,
          addLoggedUser: true
        }
      }).then((response) => {
        this.initiative = response.data.data
      }).catch((error) => {
        console.log(error)
      })
    },
    newMember () {
      this.showNewMemberModal = true
    }
  },

  watch: {
    '$route' (to, from) {
      this.updateInitiative(this.$route.params.initiativeId)
    }
  },

  mounted () {
    this.updateInitiative(this.$route.params.initiativeId)
  }
}
</script>

<style scoped>

.header-container {
  overflow: hidden;
}

.tablink {
  cursor: pointer;
}

.bold-text {
  font-weight: bold;
}

.edit-btn-div {
  padding-top: 15px;
}

.content-container {
  overflow: hidden;
}


</style>
