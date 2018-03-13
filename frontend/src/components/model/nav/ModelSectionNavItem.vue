<template lang="html">
  <div class="cursor-pointer">
    <div class="circle-div">
      <div v-if="hasSubsections">
        <i v-if="!showSubsections"class="fa fa-chevron-circle-right" aria-hidden="true"></i>
        <i v-else class="fa fa-chevron-circle-down" aria-hidden="true"></i>
      </div>
      <div v-else>
        <i class="fa fa-circle" aria-hidden="true" :style="{'color': color}"></i>
      </div>
    </div>
    <div class="title-div">
      {{ section.title }}
    </div>
    <div class="control-div">
      <i class="fa fa-bars" aria-hidden="true"></i>
      <div class="control-btns w3-card w3-white">
        <app-drop-down-menu></app-drop-down-menu>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'model-section-nav-item',

  props: {
    section: {
      type: Object
    }
  },

  data () {
    return {
      showSubsections: false
    }
  },

  computed: {
    hasSubsections () {
      return this.section.nSubsections > 0
    }
  }
}
</script>

<style scoped>

.circle-div {
  width: 30px;
  float: left;
  text-align: center;
}

.title-div {
  width: calc(100% - 30px - 30px);
  float: left;
}

.control-div {
  width: 30px;
  float: left;
  text-align: center;
}

.control-btns {
  margin-top: 5px;
  margin-left: -150px;
  position: absolute;
  width: 180px;
  text-align: left;
}

.control-btns .w3-row:hover {
  background-color: #CCCCCC;
}

.control-btns .w3-row {
  padding: 6px 16px;
}

</style>
