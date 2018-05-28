<template lang="html">

  <div class="">
    <div class="w3-row controls-row small-scroll">

      <div class="control-group">
        <div @click="draggable()" class="w3-left control-btn" :class="{'control-btn-selected': isDraggable}">
          <img src="./../../../assets/move-icon.svg" alt="">
        </div>
        <div @click="filter()" class="w3-left control-btn" :class="{'control-btn-selected': isFilter}">
          <img src="./../../../assets/filter-icon.svg" alt="">
        </div>

        <div class="control-group">
          <div class="">
            <div @click="search()" class="w3-left control-btn" :class="{'control-btn-selected': isShowSearchInput}">
              <img src="./../../../assets/search-icon.svg" alt="">
            </div>
          </div>
        </div>

        <div v-if="isShowSearchInput" class="control-group slider-container">
          <transition name="slideRightLeft">
            <div class="">
              <div class="">
                <div class="">
                  <input ref="inputQuery" class="w3-input"
                  type="text" name="" value="" placeholder="search">
                </div>
              </div>
            </div>
          </transition>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

  export default {
    components: {
    },
    props: {
    },

    data () {
      return {
        showSearchInput: false,
        toggleFilter: false
      }
    },

    computed: {
      isShowSearchInput () {
        return this.showSearchInput
      },
      isDraggable () {
        return this.$store.state.support.triggerSectionDraggingState
      },
      isFilter () {
        return this.toggleFilter
      }
      },

      methods: {
        draggable () {
          this.$store.commit('triggerSectionDraggingState')
        },
        filter () {
          this.toggleFilter = !this.toggleFilter
        },
        search () {
          this.showSearchInput = !this.showSearchInput
        }
      }

    }
  </script>

  <style scoped>


</style>
