<template lang="html">
  <div class="item-container">
    <router-link :to="'/inits/'+initiative.id+'/overview'">
      <h5>{{ initiative.name }}</h5>
      <app-initiative-menu-item class="sub-initiative-element" v-for="subinitiative in initiative.subInitiatives" :initiative="subinitiative" :key="subinitiative.id"></app-initiative-menu-item>
    </router-link>
  </div>
</template>

<script>
export default {
  name: 'app-initiative-menu-item',

  props: {
    initiative: {
      type: Object,
      default: () => {
        return {
          name: 'Initiative Name',
          driver: 'Initiative Driver',
          subInitiatives: []
        }
      }
    }
  }
}
</script>

<style scoped>

.sub-initiative-element {
  margin-left: 15px;
}

</style>
