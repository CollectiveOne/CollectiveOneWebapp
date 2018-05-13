export const cardMixin = {
  props: {
    inCardSelector: {
      type: Boolean,
      default: false
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
        this.$router.push({name: 'ModelSectionCard', params: { cardId: this.cardWrapper.id }})
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
