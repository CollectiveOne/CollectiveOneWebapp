<template lang="html">

  <div class="w3-row controls-row small-scroll">

    <div v-if="this.$store.state.user.authenticated" class="control-group">
      <div class="">
        <div tooltip="Private Cards" @click="showPrivateClick()" class="w3-left control-btn border-red" :class="{'control-btn-selected': showPrivate, 'w3-bottombar': showPrivate}">
          <img src="./../../../assets/private-icon.svg" alt="">
        </div>
        <div tooltip="Shared Cards" @click="showSharedClick()" class="w3-left control-btn border-yellow" :class="{'control-btn-selected': showShared, 'w3-bottombar': showShared}">
          <img src="./../../../assets/shared-icon.svg" alt="">
        </div>
        <div tooltip="Common Cards"  @click="showCommonClick()" class="w3-left control-btn border-blue" :class="{'control-btn-selected': showCommon, 'w3-bottombar': showCommon}">
          <img src="./../../../assets/common-icon.svg" alt="">
        </div>
      </div>
    </div>

    <div class="control-group">
      <div tooltip="Move Sections" @click="toggleDraggable()" class="w3-right control-btn" :class="{'control-btn-selected': isDraggable}">
        <img src="./../../../assets/move-icon.svg" alt="">
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
      }
    },

    computed: {
      isDraggable () {
        return this.$store.state.support.triggerSectionDraggingState
      },
      showPrivate () {
        return this.$store.state.viewParameters.showPrivateSections
      },
      showShared () {
        return this.$store.state.viewParameters.showSharedSections
      },
      showCommon () {
        return this.$store.state.viewParameters.showCommonSections
      }
    },

    methods: {
      toggleDraggable () {
        this.$store.commit('triggerSectionDraggingState')
      },
      showPrivateClick () {
        this.$store.commit('toggleShowPrivateSections')
      },
      showSharedClick () {
        this.$store.commit('toggleShowSharedSections')
      },
      showCommonClick () {
        this.$store.commit('toggleShowCommonSections')
      }
    }

  }
</script>

<style scoped>

.control-group {
  margin-right: 20px;
  margin-bottom: 3px;
  display: inline-block;
  vertical-align: top;
}

.control-group div {
  display: inline-block;
  position: relative;
}

.control-btn {
  margin-right: 4px;
}

</style>
