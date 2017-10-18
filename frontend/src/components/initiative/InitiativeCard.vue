<template lang="html">
  <div class="this-card-container w3-card-2 w3-round-large w3-border w3-leftbar cursor-pointer"
    :style="{'border-left-color': initiative.meta.color + ' !important'}"
    @click="goToInitiative()">

    <div v-if="initiative.meta.imageFile" class="w3-row image-container w3-center w3-display-container">
      <img :src="initiative.meta.imageFile.url + '?lastUpdated=' + initiative.meta.imageFile.lastUpdated" alt="">
    </div>

    <div class="w3-container">
      <a class="w3-row initiative-title">
        <h4><b>{{ initiative.meta.name }}</b></h4>
      </a>

      <div class="w3-row driver-row">
        <vue-markdown class="marked-text" :source="initiative.meta.driver"></vue-markdown>
      </div>
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

.image-container {
  min-height: 80px;
  max-height: 250px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
}

.image-container img {
  max-height: 100%;
  max-width: 100%;
}

</style>
