<template lang="html">
  <div class="this-card-container w3-card-2 w3-round-large w3-padding w3-border w3-leftbar" :style="{'border-left-color': initiative.meta.color + ' !important'}">
    <a class="w3-row initiative-title cursor-pointer"
      @click="goToInitiative()">
      <h4><b>{{ initiative.meta.name }}</b></h4>
    </a>

    <div class="w3-row driver-row">
      <vue-markdown class="marked-text" :source="initiative.meta.driver"></vue-markdown>
    </div>

    <div class="w3-row w3-margin-top">
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
  },
  methods: {
    goToInitiative () {
      this.$emit('clicked', this.initiative.id)
      this.$router.push({ name: 'Initiative', params: { 'initiativeId': this.initiative.id } })
    }
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
