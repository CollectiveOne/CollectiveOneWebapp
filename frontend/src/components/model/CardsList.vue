<template lang="html">
  <div class="">
    <div v-for="cardWrapper in allCardWrappers" class="">
      <app-model-card-with-modal
        :cardWrapperInit="cardWrapper"
        :cardEffect="true">
      </app-model-card-with-modal>
    </div>
  </div>
</template>

<script>

const appendAllCards = function (section, cardsWrappers) {
  cardsWrappers = cardsWrappers.concat(section.cardsWrappers)
  for (var ixS in section.subsections) {
    appendAllCards(section.subsections[ixS], cardsWrappers)
  }
  return cardsWrappers
}

export default {
  props: {
    level: {
      type: Number,
      default: 1
    }
  },
  data () {
    return {
      sectionContent: null,
      allCardWrappers: []
    }
  },

  computed: {
    currentSection () {
      return this.$store.state.model.currentSection
    }
  },

  watch: {
    '$store.state.model.currentSection' () {
      this.update()
    }
  },

  methods: {
    update () {
      if (this.currentSection) {
          this.axios.get('/1/model/section/' + this.currentSection.id, {params: {level: this.level}}).then((response) => {
            if (response.data.result === 'success') {
              this.section = response.data.data
              this.updateAllCardWrappers()
            }
          })
      }
    },
    updateAllCardWrappers () {
      this.allCardWrappers = appendAllCards(this.section, [])
    }
  },

  created () {
    this.update()
  }
}
</script>

<style section>
</style>
