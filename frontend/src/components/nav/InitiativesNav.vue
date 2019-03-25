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

      <div v-if="$store.state.user.authenticated" class="w3-row my-initiatives-row">
        <div class="w3-col s6" :class="{'border-blue-app': starredOnly}">
          <button @click="viewStarredOnly()" class="w3-button">
            <img v-if="starredOnly" src="./../../assets/star-on.png" alt="">
            <img v-else src="./../../assets/star.png" alt="">
          </button>
        </div>
        <div class="w3-col s6 all" :class="{'border-blue-app': !starredOnly}">
          <button @click="viewAll()" class="w3-button">
            <b>{{ $t('general.ALL') }}</b>
          </button>
        </div>
      </div>
      <div v-else class="w3-row">
        <h6 class="w3-center white-text noselect">
            <i>{{ $t('initiatives.CURRENT_INITIATIVE') }}</i>
        </h6>
      </div>

      <div v-if="!loading" class="initiatives-nav-tree">
        <app-initiative-menu-item v-for="(initiative, ix) in menuInitiatives"
          :initiative="initiative" :key="initiative.id"
          :coord="[ ix ]" class="top-menu-item"
          @initiative-selected="$emit('initiative-selected')">
        </app-initiative-menu-item>
        <div v-if="menuInitiatives.length === 0" class="w3-center">
          <i v-if="!starredOnly">{{ $t('initiatives.MY_INITS_EMPTY') }}</i>
          <i v-else>{{ $t('initiatives.MY_INITS_STARRED_EMPTY') }}</i>
        </div>
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
    },
    starredOnly () {
      return this.$store.state.initiativesTree.starredOnly
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
    },
    viewAll () {
      this.$store.dispatch('viewAllInitiatives')
    },
    viewStarredOnly () {
      this.$store.dispatch('viewStarredOnlyInitiatives')
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
  text-align: center;
}

.my-initiatives-row .w3-col {
  border-bottom: 3px solid;
  font-size: 18px;
}

.my-initiatives-row .w3-col button {
  height: 46px;
}

.my-initiatives-row .w3-button {
  width: 100%;
}

.my-initiatives-row img {
  width: 25px;
}

.initiatives-nav-tree {
  padding-top: 16px;
}

</style>
