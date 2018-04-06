export const cardMixin = {
  methods: {
    cardClicked () {
      this.$router.push({name: 'ModelSectionCard', params: { cardId: this.cardWrapper.id }})
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
    getAuthors () {
      this.axios.get('/1/model/cardWrapper/' + this.cardWrapper.id + '/creator').then((response) => {
        if (response.data.result === 'success') {
          this.creator = response.data.data
        }
      })
    }
  }
}
