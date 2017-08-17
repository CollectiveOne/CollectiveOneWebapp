<template lang="html">

<div>
  <div class="w3-container"
    v-if="initiativeModel">
    <app-model-view :view="initiativeModel.views[showView]" :key="initiativeModel.views[showView].id"></app-model-view>
  </div>
</div>
</template>

<script>
import ModelView from '@/components/model/ModelView.vue'

export default {
  components: {
    'app-model-view': ModelView
  },

  data () {
    return {
      showView: 0
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    initiativeModel () {
      return this.initiative.initiativeModel
    }
  },

  methods: {
  },

  mounted () {
  }
}
</script>

<style scoped>

</style>
