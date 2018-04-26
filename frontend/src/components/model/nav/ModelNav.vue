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

    <div class="model-nav-container">
      <div class="w3-row">
        <app-model-section-nav-item
          :section="initiative.topModelSection"
          :expandSubsections="menuExpandArray">
        </app-model-section-nav-item>
      </div>
    </div>

  </div>
</template>

<script>
import ModelSectionModal from '@/components/model/modals/ModelSectionModal.vue'
import ModelSectionNavItem from '@/components/model/nav/ModelSectionNavItem.vue'

const extractMenuExpands = function (codedString) {
  console.log(codedString)
  let jsonStr = codedString.replace(/C/g, '[').replace(/D/g, ']').replace(/Q/g, ',')
  console.log(jsonStr)
  let arr = JSON.parse(jsonStr)
  console.log(arr)
  return arr
}

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
      return this.$route.query.levels ? parseInt(this.$route.query.levels) : 1
    },
    menu () {
      return 'C1QCC1QCC1DDDQC4DDD' // this.$route.query.menu ? this.$route.query.menu : ''
    },
    menuExpandArray () {
      return extractMenuExpands(this.menu)
    }
  },

  methods: {
  },

  created () {
  }
}
</script>

<style scoped>

.model-nav-container {
  min-height: calc(100vh - 50px - 1px);
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
