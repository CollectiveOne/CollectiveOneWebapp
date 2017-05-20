<template lang="html">
  <div class="w3-col s12">
    <div class="w3-row">
      <h5 class="w3-left">
        <router-link :to="'/inits/'+initiative.id+'/overview'">
          {{ initiative.name }}
        </router-link>
      </h5>
      <div class="btns w3-button w3-right" @click="$emit('new-subinitiative', initiative.id)">
        <i class="fa fa-external-link" aria-hidden="true"></i>
      </div>
    </div>
    <div v-if="initiative.subInitiatives.length > 0" class="w3-row">
      <div class="w3-col s1">
        <h5>
          <i class="fa fa-minus" aria-hidden="true"></i>
        </h5>
      </div>
      <div class="w3-col s11">
        <app-initiative-menu-item
          class="sub-initiative-element" v-for="subinitiative in initiative.subInitiatives"
          :initiative="subinitiative" :key="subinitiative.id"
          @new-subinitiative="$emit('new-subinitiative', $event)">
        </app-initiative-menu-item>
      </div>
    </div>
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

.btns {
  margin-top: 5px;
  color: rgb(110, 110, 110);
  margin-bottom: 5px;
}

</style>
