<template lang="html">
  <div class="w3-white">
    <div class="w3-display-topright w3-xlarge" @click="$emit('close-navbar')"><i class="fa fa-times" aria-hidden="true"></i></div>
    <div class="w3-container w3-padding">
      <h4 class="w3-opacity">My Initiatives</h4>
      <button type="button" class="new-initiative-btn w3-button w3-teal w3-round" @click="newInitiative()"><i class="fa fa-plus-circle"></i>  create new</button>
      <app-initiative-menu-item v-for="initiative in userInitiatives" :initiative="initiative" :key="initiative.id"></app-initiative-menu-item>
    </div>
  </div>
</template>

<script>
import InitiativeMenuItem from './InitiativeMenuItem.vue'

export default {

  components: {
    AppInitiativeMenuItem: InitiativeMenuItem
  },

  data () {
    return {
      userInitiatives: null
    }
  },

  methods: {
    newInitiative () {
      this.$emit('new-initiative', '')
    },

    updatedMyInitiatives () {
      this.axios.get('/1/secured/initiatives/mines').then((response) => {
        this.userInitiatives = response.data.data
      }).catch((error) => {
        console.log(error)
      })
    }
  },

  mounted () {
    this.updatedMyInitiatives()
  }

}
</script>

<style scoped>

.new-initiative-btn {
  margin-top: 20px;
}

.w3-display-topright {
  width: 80px;
  height: 40px;
  cursor: pointer;
  z-index: 1300;
  text-align: right;
}

.fa-times {
  color: #607d8b;
  margin-right: 10px;
}

</style>
