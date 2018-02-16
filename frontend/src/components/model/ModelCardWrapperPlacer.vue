<template lang="html">
  <div class="">

    <div class="">
      <h1 class="">Card Page</h1>
    </div>

    <app-model-card-with-modal
      :cardWrapperInit="cardWrapper"
      :initiativeId="$route.params.initiativeId"
      inSectionId=""
      inSectionTitle=""
      :cardEffect="true"
      :floating="false"
      @please-update="update()">
    </app-model-card-with-modal>

  </div>
</template>

<script>
import ModelCardWithModal from '@/components/model/ModelCardWithModal.vue'

export default {
  components: {
    'app-model-card-with-modal': ModelCardWithModal
  },

  data () {
    return {
      cardWrapper: null
    }
  },

  methods: {
    update () {
      this.axios.get('/1/initiative/' + this.$route.params.initiativeId + '/model/cardWrapper/' + this.$route.params.cardWrapperId).then((response) => {
        this.cardWrapper = response.data.data
      })
    }
  },

  created () {
    this.update()
  }

}
</script>

<style scoped>
</style>
