<template lang="html">
  <div class="gray-1-color" :class="">
    <div class="w3-left">
      <div class="">
        <span :style="iconStyle"><i class="fa fa-comments"></i></span>
        <span v-if="!loading" :style="numberStyle"><i>{{ count }}</i></span>
        <span v-else :style="numberStyle">-</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    contextType: {
      type: String
    },
    contextElementId: {
      type: String
    },
    size: {
      type: Number,
      default: 24
    }
  },

  computed: {
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
      loading: false
    }
  },

  methods: {
    update () {
      var url = ''

      switch (this.contextType) {
        case 'MODEL_VIEW':
          url = '/1/activity/model/view/' + this.contextElementId + '/count'
          break

        case 'MODEL_SECTION':
          url = '/1/activity/model/section/' + this.contextElementId + '/count'
          break

        case 'MODEL_CARD':
          url = '/1/activity/model/card/' + this.contextElementId + '/count'
          break
      }

      this.loading = true
      this.axios.get(url, { params: { onlyMessages: true } }).then((response) => {
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
