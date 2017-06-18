<template lang="html">
  <div class="">
    <div v-if="initiative" class="w3-row">
      <div class="w3-white">
        <header class="w3-container w3-theme">
          <h3>{{ initiative.name }}</h3>
        </header>

        <div class="section-tabs w3-row">
          <router-link tag="div" to="overview" class="w3-col s4 tablink w3-bottombar w3-hover-light-grey w3-padding"
            :class="{'w3-border-blue': isOverview}"
            @click="">
            <h5 class="w3-text-indigo" :class="{'bold-text': isOverview}">Overview</h5>
          </router-link>
          <router-link tag="div" to="people" class="w3-col s4 tablink w3-bottombar w3-hover-light-grey w3-padding"
            :class="{'w3-border-blue': isPeople}"
            @click="">
            <h5 class="w3-text-indigo" :class="{'bold-text': isPeople}">People</h5>
          </router-link>
          <router-link tag="div" to="assignations" class="w3-col s4 tablink w3-bottombar w3-hover-light-grey w3-padding"
            :class="{'w3-border-blue': isAssignations}"
            @click="">
            <h5 class="w3-text-indigo" :class="{'bold-text': isAssignations}">Assignations</h5>
          </router-link>
        </div>

        <div class="w3-row">
          <router-view :initiative="initiative"
            @new-initiative="$emit('new-initiative', $event)"
            @new-member="newMember($event)"
            @please-update="updateInitiative(initiative.id)">
          </router-view>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {

  components: {
  },

  data () {
    return {
      initiative: null
    }
  },

  computed: {
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
    }
  },

  methods: {
    updateInitiative (id) {
      this.axios.get('/1/secured/initiative/' + id, {
        params: {
          addAssets: true,
          addSubinitiatives: true,
          addMembers: true
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
    '$route' () {
      this.updateInitiative(this.$route.params.initiativeId)
    }
  },

  mounted () {
    this.updateInitiative(this.$route.params.initiativeId)
  }
}
</script>

<style scoped>

.tablink {
  cursor: pointer;
}

.bold-text {
  font-weight: bold;
}

</style>
