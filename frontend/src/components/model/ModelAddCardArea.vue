<template lang="html">
  <div class="">
    <div class="cursor-pointer"
      @mouseover="hoverIn()"
      @mouseleave="hoverOut()">

      <div v-if="showEmptySpace" class="empty-div w3-center">
        <i>add-card</i>
      </div>

      <div class="slider-container">
        <transition name="slideDownUp"
          @before-enter="showEmptySpace = false"
          @after-leave="showEmptySpace = true">

          <div v-if="showAddButton" class="button-container">
            <div class="gray-1-color w3-card-2">
              <button class="w3-button" style="width: 100%"
                @click="$emit('new-card', ix)">
                <i class="fa fa-plus w3-margin-right" aria-hidden="true"></i>
                add card
              </button>
            </div>
          </div>
        </transition>
      </div>

    </div>
  </div>
</template>

<script>
export default {

  props: {
    ix: {
      type: Number,
      default: 0
    }
  },

  data () {
    return {
      showEmptySpace: true,
      showAddButton: false
    }
  },

  methods: {
    hoverIn () {
      this.showAddButton = true
    },
    hoverOut () {
      this.showAddButton = false
    }
  }
}
</script>

<style scoped>

.empty-div {
  min-height: 22px;
  color: #bfc1c3;
}

.button-container {
  padding-top: 10px;
  padding-bottom: 10px;
}
</style>
