<template lang="html">
  <span
    @click="clicked()"
    class="badge"
    :class="{
       'badge-info': !selected,
       'badge-success': selected
      }"
    >
      <slot></slot>
  </span>
</template>

<script>
export default {
  props: {
    id: {
      type: String,
      default: 'id'
    },

    selectedInit: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      selected: false
    }
  },

  methods: {
    clicked () {
      this.selected = !this.selected

      if (this.selected) this.$emit('selected', {selected: true, id: this.id})
      else this.$emit('selected', {selected: false, id: this.id})
    }
  },

  created () {
    this.selected = this.selectedInit
  }
}
</script>

<style scoped>

.badge {

  float: left;
  padding-top: 5px;
  padding-bottom: 5px;

  cursor: pointer;

  -webkit-touch-callout: none; /* iOS Safari */
  -webkit-user-select: none; /* Safari */
  -khtml-user-select: none; /* Konqueror HTML */
  -moz-user-select: none; /* Firefox */
  -ms-user-select: none; /* Internet Explorer/Edge */
  user-select: none; /* Non-prefixed version, currently
                                supported by Chrome and Opera */
}

</style>
