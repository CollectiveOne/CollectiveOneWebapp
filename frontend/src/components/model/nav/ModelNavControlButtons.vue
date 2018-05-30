<template lang="html">

  <div class="w3-row controls-row small-scroll">

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
      }
    },

    methods: {
      toggleDraggable () {
        this.$store.commit('triggerSectionDraggingState')
      }
    }

  }
</script>

  <style scoped>


</style>
