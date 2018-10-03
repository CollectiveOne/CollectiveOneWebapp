<template lang="html">
  <nav class="nav-container w3-border-right">

    <div class="">
      <div class="w3-row-padding btns-row">
        <div class="w3-col s4">
          <div @click="closeNav()" class="w3-xlarge cursor-pointer noselect">
            <i class="fa fa-chevron-circle-left"></i>
          </div>
        </div>
        <div v-if="$store.state.user.authenticated" class="w3-col s8 "
          @click="newInitiative()">
          <div class="create-new w3-button light-grey w3-round-large w3-center">
            <i class="fa fa-plus-circle"></i>  {{ $t('initiatives.NEW') }}
          </div>
        </div>
        <div v-else class="w3-col s8 ">
          <button @click="login()"
            class="create-new w3-button light-grey w3-round-large w3-center" name="button">
            {{ $t('general.LOGIN_SIGNUP') }}
          </button>
        </div>
      </div>

      <div class="w3-row my-initiatives-row">
        <h6 class="w3-center white-text noselect">
          <span v-if="$store.state.user.authenticated">
            <i>{{ $t('initiatives.MY_INITIATIVES') }}</i>
          </span>
          <span v-else="$store.state.user.authenticated">
            <i>{{ $t('initiatives.CURRENT_INITIATIVE') }}</i>
          </span>
        </h6>
      </div>

      <div v-if="!loading" class="">
        <app-initiative-menu-item v-for="(initiative, ix) in menuInitiatives"
          :initiative="initiative" :key="initiative.id"
          :coord="[ ix ]" class="top-menu-item"
          @initiative-selected="$emit('initiative-selected')">
        </app-initiative-menu-item>
      </div>
      <div v-else class="w3-row w3-center loader-gif-container">
        <img class="loader-gif" src="../../assets/loading.gif" alt="">
      </div>
    </div>
  </nav>
</template>

<script>
import InitiativeMenuItem from './InitiativeMenuItem.vue'

export default {

  components: {
    'app-initiative-menu-item': InitiativeMenuItem
  },

  data () {
    return {
    }
  },

  computed: {
    loading () {
      return this.$store.state.initiativesTree.loadingMyInitiatives
    },
    menuInitiatives () {
      return this.$store.getters.initiativesTree()
    }
  },

  methods: {
    newInitiative () {
      this.$emit('new-initiative')
    },
    login () {
      this.$store.state.user.lock.show()
    },
    closeNav () {
      this.$store.commit('toggleExpandNav')
    },
    swipeLeft () {
      console.log('swiped left detected')
      this.$store.commit('setExpandNav', false)
    }
  }
}
</script>

<style scoped>

.nav-container {
}

.btns-row {
  padding: 16px 12px;
}

.btns-row .w3-button {
  width: 100%;
}

.create-new {
  text-align: left;
}

.section-header {
  margin-top: 10px;
}

.w3-display-topright {
  width: 80px;
  height: 40px;
  cursor: pointer;
  z-index: 1300;
  text-align: right;
}

.fa-times {
  margin-right: 10px;
}

.top-menu-item {
  margin-bottom: 15px;
}

.my-initiatives-row {
}

</style>
