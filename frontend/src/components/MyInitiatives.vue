<template lang="html">
  <div class="">
    <transition name="slideDownUp">
      <app-new-initiative-modal
        v-if="showNewInitiativeModal"
        @close="showNewInitiativeModal = false">
      </app-new-initiative-modal>
    </transition>

    <div class="w3-row my-initiatives-row">
      <div class="w3-col s6" :class="{'border-blue-app': starredOnly}">
        <button @click="viewStarredOnly()" class="w3-button">
          <img v-if="starredOnly" src="./../assets/star-on.png" alt="">
          <img v-else src="./../assets/star.png" alt="">
        </button>
      </div>
      <div class="w3-col s6 all" :class="{'border-blue-app': !starredOnly}">
        <button @click="viewAll()" class="w3-button">
          <b>{{ $t('general.ALL') }}</b>
        </button>
      </div>
    </div>

    <div v-if="!loading" class="w3-row-padding">
      <div v-if="myInitiatives.length > 0" class="">
        <div v-for="initiative in myInitiatives" class="initiative-card-col">
          <app-initiative-card
            :initiative="initiative"
            :key="initiative.id"
            @clicked="updateCurrentInitiative($event)">
          </app-initiative-card>
        </div>
      </div>
      <div v-else class="w3-center">
        <div class="w3-row w3-margin-top">
          <i v-if="!starredOnly">{{ $t('initiatives.MY_INITS_EMPTY') }}</i>
          <i v-else>{{ $t('initiatives.MY_INITS_STARRED_EMPTY') }}</i>
        </div>
        <div v-if="!starredOnly" class="w3-row w3-margin-top">
          <button @click="showNewInitiativeModal = true" class="w3-button app-button">
            {{ $t('initiatives.CREATE_ONE') }}
          </button>
        </div>
      </div>
    </div>
    <div v-else class="w3-row w3-center loader-gif-container">
      <img class="loader-gif" src="../assets/loading.gif" alt="">
    </div>
  </div>
</template>

<script>
import InitiativeCard from '@/components/initiative/InitiativeCard.vue'
import NewInitiativeModal from '@/components/modal/NewInitiativeModal.vue'

export default {
  components: {
    'app-initiative-card': InitiativeCard,
    'app-new-initiative-modal': NewInitiativeModal
  },

  data () {
    return {
      showNewInitiativeModal: false
    }
  },

  computed: {
    loading () {
      return this.$store.state.initiativesTree.loadingMyInitiatives
    },
    myInitiatives () {
      return this.$store.getters.initiativesTreeTop()
    },
    starredOnly () {
      return this.$store.state.initiativesTree.starredOnly
    }
  },

  methods: {
    updateCurrentInitiative (id) {
      this.$store.dispatch('updateCurrentInitiativeTree', id)
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

.my-initiatives-row {
  text-align: center;
  margin-bottom: 16px;
}

.my-initiatives-row .w3-col {
  border-bottom: 6px solid #ccc;
  font-size: 18px;
}

.my-initiatives-row img {
  width: 30px;
}

.my-initiatives-row button {
  width: 100%;
  height: 45px;
}

.initiative-card-col {
  margin-bottom: 32px;
  display: inline-block;
}

@media screen and (min-width: 1200px) {
  .initiative-card-col {
    width: calc(50% - 26px);
    margin-left: 13px;
    margin-right: 13px;
    vertical-align: top;
  }
}

@media screen and (max-width: 1199px) {
  .initiative-card-col {
    width: calc(100% - 26px);
    margin-left: 13px;
    margin-right: 13px;
    vertical-align: top;
  }
}

</style>
