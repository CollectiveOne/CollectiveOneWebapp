<template lang="html">
  <div class="">

    <app-edit-initiative-modal v-if="showEditInitiativeModal" :initiative="initiative"
      @close-this="showEditInitiativeModal = false"
      @initiative-updated="updateThisInitiative()">
    </app-edit-initiative-modal>

    <div v-if="initiative" class="w3-row">
      <div class="w3-white">
        <header class="w3-container w3-theme">
          <h3 class="w3-left noselect">{{ initiative.name }}</h3>
          <div v-if="isLoggedAnAdmin" class="edit-btn-div w3-button w3-right w3-large" @click="showEditInitiativeModal = true">
            <i class="fa fa-pencil" aria-hidden="true"></i>
          </div>
        </header>

        <div class="section-tabs w3-row w3-center">
          <router-link tag="div" to="overview" class="w3-col s4 tablink w3-bottombar w3-hover-light-grey w3-padding"
            :class="{'w3-border-blue': isOverview}"
            @click="">
            <h5 class="w3-text-indigo noselect" :class="{'bold-text': isOverview}">Overview</h5>
          </router-link>
          <router-link tag="div" to="people" class="w3-col s4 tablink w3-bottombar w3-hover-light-grey w3-padding"
            :class="{'w3-border-blue': isPeople}"
            @click="">
            <h5 class="w3-text-indigo noselect" :class="{'bold-text': isPeople}">People</h5>
          </router-link>
          <router-link tag="div" to="assignations" class="w3-col s4 tablink w3-bottombar w3-hover-light-grey w3-padding"
            :class="{'w3-border-blue': isAssignations}"
            @click="">
            <h5 class="w3-text-indigo noselect" :class="{'bold-text': isAssignations}">Transfers</h5>
          </router-link>
        </div>

        <div class="w3-row content-container">
          <transition :name="animationType" mode="out-in">
            <router-view :initiative="initiative"
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

@offcanvas-transition-speed:;
@offcanvas-transition-timing: ease;

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

.slideToRight-enter-active {
  animation: slideToRight-in 0.5s ease forwards;
}

.slideToRight-leave-active {
  animation: slideToRight-out 0.5s ease forwards;
}

.slideToLeft-enter-active {
  animation: slideToLeft-in 0.5s ease forwards;
}

.slideToLeft-leave-active {
  animation: slideToLeft-out 0.5s ease forwards;
}

.slideToUp-enter-active {
  animation: slideToUp-in 0.5s ease forwards;
}

.slideToUp-leave-active {
  animation: slideToUp-out 0.5s ease forwards;
}

.slideToDown-enter-active {
  animation: slideToDown-in 0.5s ease forwards;
}

.slideToDown-leave-active {
  animation: slideToDown-out 0.5s ease forwards;
}

@keyframes slideToRight-in {
  from {
    transform: translateX(-100%);
  }
  to {
    transform: translateX(0);
  }
}

@keyframes slideToRight-out {
  from {
    transform: translateX(0);
  }
  to {
    transform: translateX(100%);
  }
}

@keyframes slideToLeft-in {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}

@keyframes slideToLeft-out {
  from {
    transform: translateX(0);
  }
  to {
    transform: translateX(-100%);
  }
}

@keyframes slideToDown-in {
  from {
    transform: translateY(-100%);
  }
  to {
    transform: translateY(0);
  }
}

@keyframes slideToDown-out {
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(100%);
  }
}

@keyframes slideToUp-in {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}

@keyframes slideToUp-out {
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(-100%);
  }
}

</style>
