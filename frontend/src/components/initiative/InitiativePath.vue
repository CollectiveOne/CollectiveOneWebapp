<template lang="html">
  <div class="">
    <div v-for="(parent, ix) in reversedParents" class="w3-left">
      <router-link :to="'/inits/' + parent.id" tag="div" class="w3-left cursor-pointer" :class="{ 'parent-2': ix > 0 }">
        {{ parent.name }}
      </router-link>
      <div class="w3-left separator">
        <i class="fa fa-arrow-right" aria-hidden="true"></i>
      </div>
    </div>
    <div class="w3-left initiative">
      {{ initiative.name }}
    </div>
  </div>
</template>

<script>
export default {
  props: {
    initiative: Object
  },

  computed: {
    reversedParents () {
      return JSON.parse(JSON.stringify(this.initiative.parents.reverse()))
    }
  }
}
</script>

<style scoped>

.parent-2 {
  margin-left: 5px;
}

.separator {
  font-size: 12px;
  padding-top: 10px;
  margin-left: 5px;
}

.initiative {
  margin-left: 5px;
}

</style>
