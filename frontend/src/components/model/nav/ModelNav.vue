<template lang="html">
  <div class="">

    <transition name="slideDownUp">
      <app-model-section-modal
        v-if="showSectionModal"
        :isNew="true"
        :initiativeId="initiative.id"
        :inInitiative="true"
        :inElementId="initiative.id"
        :inElementTitle="initiative.meta.name"
        @close="showSectionModal = false">
      </app-model-section-modal>
    </transition>

    <div class="model-nav-container w3-container">
      <div class="w3-row w3-margin-bottom">
        <div class="zoom-controls gray-1-color">
          <div class="cursor-pointer">
            <i @click="levelDown()" class="fa fa-minus-circle" aria-hidden="true"></i>
          </div>
          <div class="">
            {{ levels }}
          </div>
          <div class="cursor-pointer">
            <i @click="levelUp()" class="fa fa-plus-circle" aria-hidden="true"></i>
          </div>
        </div>
      </div>
      <div class="w3-row">
        <app-model-section-nav-item
          :section="initiative.topModelSection">
        </app-model-section-nav-item>
      </div>
    </div>
  </div>
</template>

<script>
import ModelSectionModal from '@/components/model/modals/ModelSectionModal.vue'
import ModelSectionNavItem from '@/components/model/nav/ModelSectionNavItem.vue'

export default {
  components: {
    'app-model-section-modal': ModelSectionModal,
    'app-model-section-nav-item': ModelSectionNavItem
  },
  data () {
    return {
      showSectionModal: false
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    levels () {
      return parseInt(this.$route.query.levels)
    },
    expandModelNav () {
      return this.$store.state.support.expandModelNav
    }
  },
  methods: {
    levelUp () {
      this.$router.replace({name: this.$route.name, query: {levels: this.levels + 1}})
    },
    levelDown () {
      if (this.levels > 1) {
        this.$router.replace({name: this.$route.name, query: {levels: this.levels - 1}})
      }
    }
  }
}
</script>

<style scoped>

.model-nav-container {
  padding-top: 16px;
  min-height: calc(100vh - 131px);
}

.zoom-controls {
  margin: 0 auto;
  width: 90px;
  font-size: 20px;
}

.zoom-controls > div {
  width: 30px;
  float: left;
  text-align: center;
}

</style>
