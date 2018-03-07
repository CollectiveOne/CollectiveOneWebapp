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
    sectionId: {
      type: String
    },
    levels: {
      type: Number
    }
  },

  watch: {
    sectionId () {
      this.update()
    },
    levels () {
      this.update()
    }
  },

  data () {
    return {
      section: null,
      allCardWrappers: []
    }
  },

  methods: {
    update () {
      if (this.sectionId) {
        if (this.sectionId !== '') {
          this.axios.get('/1/model/section/' + this.sectionId, {params: {level: this.levels}}).then((response) => {
            if (response.data.result === 'success') {
              this.section = response.data.data
              this.updateAllCardWrappers()
            }
          })
        }
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
