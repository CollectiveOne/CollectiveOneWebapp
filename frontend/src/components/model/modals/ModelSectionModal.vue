<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="div-close-modal w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times fa-close-modal" aria-hidden="true"></i>
        </div>

        <div v-if="section" class="w3-container w3-border-bottom">
          <h2>{{ section.title }} section</h2>
        </div>

        <div v-if="section" class="w3-container div-modal-content">
          <label class=""><b>Section description:</b></label>
          <div class="w3-padding light-grey">
            {{ section.description }}
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
    sectionId: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      section: null
    }
  },

  computed: {
  },

  methods: {
    updateSectionData () {
      this.axios.get('/1/secured/initiative/' + this.initiativeId + '/model/section/' + this.sectionId).then((response) => {
        this.section = response.data.data
      })
    },
    closeThis () {
      this.$emit('close')
    }
  },

  mounted () {
    this.updateSectionData()
  }
}
</script>

<style scoped>

</style>
