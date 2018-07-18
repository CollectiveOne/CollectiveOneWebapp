export const cardMixin = {

  props: {
    cardWrapper: {
      type: Object,
      default: null
    },
    forceUpdate: {
      type: Boolean,
      default: true
    },
    inSection: {
      type: Object,
      default: null
    },
    hideCardControls: {
      type: Boolean,
      default: false
    },
    inCardSelector: {
      type: Boolean,
      default: false
    },
    cardRouteName: {
      type: String,
      default: 'ModelSectionCard'
    }
  },

  computed: {
    cardWrapperId () {
      return this.cardWrapper ? this.cardWrapper.id : ''
    },
    isPrivate () {
      return this.cardWrapper.scope === 'PRIVATE'
    },
    isShared () {
      return this.cardWrapper.scope === 'SHARED'
    },
    inSectionId () {
      return this.inSection ? this.inSection.id : ''
    },
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    containerClass () {
      let cClass = {}
      switch (this.cardWrapper.scope) {
        case 'PRIVATE':
          cClass['border-red'] = true
          break

        case 'SHARED':
          cClass['border-yellow'] = true
          break

        case 'COMMON':
          cClass['border-blue'] = true
          break

        default:
          cClass['border-gray'] = true
          break
      }
      return cClass
    },
    showLikes () {
      return !this.isPrivate && !this.governanceTypeIsConsent
    },
    showConsentPositions () {
      return !this.isPrivate && this.governanceTypeIsConsent
    },
    governanceTypeIsConsent () {
      return this.cardWrapper.governanceType === 'SIMPLE_CONSENT'
    },
    consentIsOpened () {
      return this.cardWrapper.simpleConsentState === 'OPENED'
    }
  },

  methods: {
    cardClicked () {
      if (!this.inCardSelector) {
        this.$router.push({name: this.cardRouteName, params: { cardId: this.cardWrapperId }})
      }
    },
    editors () {
      var editors = []
      for (var ix in this.cardWrapper.editors) {
        if (this.cardWrapper.editors[ix].c1Id !== this.cardWrapper.creator.c1Id) {
          editors.push(this.cardWrapper.editors[ix])
        }
      }
      return editors
    }
  }
}
