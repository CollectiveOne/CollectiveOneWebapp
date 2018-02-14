<template lang="html">
  <div class="gray-1-color" :class="">
    <div v-if="show" class="w3-left noselect">
      <div class="">
        <span :style="iconStyle"><i class="fa" :class="iconClass"></i></span>
        <span v-if="!loading" :style="numberStyle"><i>{{ count }}</i></span>
        <span v-else :style="numberStyle">-</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    initiativeId: {
      type: String
    },
    contextType: {
      type: String
    },
    contextElementId: {
      type: String
    },
    size: {
      type: Number,
      default: 24
    },
    hideWhenZero: {
      type: Boolean,
      default: false
    },
    type: {
      type: String
    }
  },

  computed: {
    iconClass () {
      switch (this.type) {
        case 'messages':
          return 'fa-comments'
        case 'likes':
          return 'fa-heart'
      }
    },
    requestType () {
      switch (this.type) {
        case 'messages':
          return 'countMessages'
        case 'likes':
          return 'countLikes'
      }
    },
    show () {
      if (this.hideWhenZero) {
        return this.count !== 0
      } else {
        return true
      }
    },
    iconStyle () {
      return {
        fontSize: this.size + 'px'
      }
    },
    numberStyle () {
      return {
        fontSize: this.size * 2.0 / 3.0 + 'px'
      }
    }
  },

  data () {
    return {
      count: 0,
      loading: false,
      hide: false
    }
  },

  watch: {
    contextElementId () {
      this.update()
    }
  },

  methods: {
    update () {
      var url = ''

      switch (this.contextType) {
        case 'MODEL_VIEW':
          url = '/1/initiative/' + this.initiativeId + '/model/view/' + this.contextElementId + '/' + this.requestType
          break

        case 'MODEL_SECTION':
          url = '/1/initiative/' + this.initiativeId + '/model/section/' + this.contextElementId + '/' + this.requestType
          break

        case 'MODEL_CARD':
          url = '/1/initiative/' + this.initiativeId + '/model/card/' + this.contextElementId + '/' + this.requestType
          break
      }

      this.loading = true
      this.axios.get(url).then((response) => {
        this.loading = false
        if (response.data.result === 'success') {
          this.count = response.data.data
        }
      })
    }
  },

  mounted () {
    this.update()
  }
}
</script>

<style scoped>

</style>
