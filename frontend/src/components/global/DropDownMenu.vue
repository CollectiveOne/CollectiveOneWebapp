<template lang="html">
  <div class="menu-container">
    <div v-for="(item, ix) in items" :key="ix"
      class="w3-row menu-row" @click="$emit(item.value)">
      <div class="icon-cont">
        <i class="fa" :class="item.faIcon" aria-hidden="true"></i>
      </div>
      <div class="text-cont noselect">
        {{ item.text }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    items: {
      type: Array,
      default: () => {
        return [
          {
            faIcon: 'fa-bars',
            value: 'textId',
            text: 'text'
          }
        ]
      }
    }
  }
}
</script>

<style scoped>

.menu-row:hover {
  background-color: #CCCCCC;
}

.menu-row {
  padding: 4px 0px;
}

.icon-cont {
  width: 30px;
  float: left;
  text-align: center;
}

.text-cont {
  float: left;
  width: calc(100% - 30px);
  padding-left: 5px;
}

</style>
