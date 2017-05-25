<template lang="html">
  <div class="">
    <app-new-member-modal v-if="showNewMemberModal" :initId="initiativeIdForModal" @close-this="showNewMemberModal = false"></app-new-member-modal>
    <div v-if="initiative" class="w3-row">
      <div class="w3-white">
        <header class="w3-container w3-theme">
          <h3>{{ initiative.name }}</h3>
        </header>

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
import NewMemberModal from '../modal/NewMemberModal.vue'

export default {

  components: {
    'app-new-member-modal': NewMemberModal
  },

  data () {
    return {
      initiative: null,
      showNewMemberModal: false
    }
  },

  methods: {
    updateInitiative (id) {
      this.axios.get('/1/secured/initiative/' + id, {
        params: {
          addAssets: true,
          addSubinitiatives: true,
          addContributors: true
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
</style>
