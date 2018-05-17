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
    containerClass () {
      let cClass = {}
      switch (this.cardWrapper.scope) {
        case 'PRIVATE':
          cClass['border-red'] = true
          break

        case 'PERSONAL':
          cClass['border-yellow'] = true
          break

        default:
          cClass['border-blue'] = true
          break
      }
      return cClass
    }
  },
  methods: {
    showThisTag (inSection) {
      /* hide the card in which this section is from tags */
      if (this.inSection) {
        return inSection.id !== this.inSection.id
      }
      return true
    },
    cardClicked () {
      if (!this.inCardSelector) {
        this.$router.push({name: this.cardRouteName, params: { cardId: this.cardWrapper.id }})
      }
    },
    toggleLike () {
      this.axios.put('/1/model/card/' + this.cardWrapper.id + '/like',
        {}, {
          params: {
            likeStatus: !this.cardWrapper.userLiked
          }
        }).then((response) => {
          this.$emit('update')
        })
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
