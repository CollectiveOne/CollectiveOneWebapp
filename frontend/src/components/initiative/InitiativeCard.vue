<template lang="html">
  <div class="this-card-container w3-card-2 w3-round-large w3-padding w3-border w3-leftbar" :style="{'border-left-color': initiative.meta.color + ' !important'}">
    <router-link :to="{name: 'Initiative', params: {'initiativeId': initiative.id}}"
      tag="a" class="initiative-title">
      <h4><b>{{ initiative.meta.name }}</b></h4>
    </router-link>
    <p>{{ initiative.meta.driver }}</p>
    <div class="">
      <app-initiative-tag
        class="tags-containers"
        v-for="tag in initiative.meta.tags"
        :tag="tag"
        :key="tag.id">
      </app-initiative-tag>
    </div>
  </div>
</template>

<script>
import InitiativeTag from '@/components/initiative/InitiativeTag.vue'
export default {
  components: {
    'app-initiative-tag': InitiativeTag
  },
  props: {
    initiative: Object
  }
}
</script>

<style scoped>

.this-card-container {
  border-left-width: 10px !important;
}

.initiative-title:hover {
  color: #15a5cc;
}

</style>
