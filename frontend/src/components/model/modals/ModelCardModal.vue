<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="div-close-modal w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times fa-close-modal" aria-hidden="true"></i>
        </div>

        <div v-if="card" class="">
          <div class="w3-container w3-border-bottom">
            <h2>Card deatils</h2>
          </div>

          <div class="w3-container div-modal-content">
            <label class=""><b>Title:</b></label>
            <div class="w3-padding light-grey">
              {{ card.title }}
            </div>

            <label class=""><b>Text:</b></label>
            <div class="w3-padding light-grey">
              {{ card.text }}
            </div>
          </div>
        </div>


      </div>
    </div>
  </div>
</template>

<script>
export default {

  props: {
    initiativeId: {
      type: String,
      default: ''
    },
    cardId: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      card: null
    }
  },

  computed: {
  },

  methods: {
    updateCardData () {
      this.axios.get('/1/secured/initiative/' + this.initiativeId + '/model/card/' + this.cardId).then((response) => {
        this.card = response.data.data
      })
    },
    closeThis () {
      this.$emit('close')
    }
  },

  mounted () {
    this.updateCardData()
  }
}
</script>

<style scoped>

</style>
