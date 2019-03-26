export const sectionElementsMixin = {

  computed: {
    isMessagesContent () {
      return this.$route.name === 'ModelSectionMessages'
    },
    isCardsContent () {
      return this.$route.name === 'ModelSectionCards' ||
        this.$route.name === 'ModelSectionCard'
    },
    levels () {
      return this.$store.getters.getActualLevels
    },
    infiniteLevels () {
      if (this.isSectionsOrder) {
        return this.$store.state.viewParameters.isInfiniteLevels
      } else {
        return true
      }
    },
    cardsType () {
      return this.$store.state.viewParameters.cardsType
    },
    showPrivate () {
      return this.$store.state.viewParameters.showPrivate
    },
    showShared () {
      return this.$store.state.viewParameters.showShared
    },
    showCommon () {
      return this.$store.state.viewParameters.showCommon
    },
    isSummary () {
      return this.cardsType === 'summary' && this.isCardsContent
    },
    isCard () {
      return this.cardsType === 'card' && this.isCardsContent
    },
    isDoc () {
      return this.cardsType === 'doc' && this.isCardsContent
    },
    isSectionsOrder () {
      return this.orderType === 'sections'
    },
    isDraggable () {
      return this.$store.state.support.enableCardDraggingState
    },
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    popperOptions () {
      return {
        placement: 'bottom',
        modifiers: {
          preventOverflow: {
            enabled: true,
            boundariesElement: 'viewport'
          }
        }
      }
    }
  }
}
