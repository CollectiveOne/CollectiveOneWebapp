<template lang="html">
  <div class="">
    <router-view></router-view>
    <div v-if="$store.state.support.myInitiativesFirstLoaded" class="w3-container">
      <h1>Browse Initiatives</h1>
      <label for=""><b>Filter by tag:</b></label>
      <div class="w3-row-padding">
        <div class="w3-col m4">
          <app-initiative-tag-selector
            class="tag-selector"
            @select="tagSelected($event)"
            :resetTrigger="resetTrigger">
          </app-initiative-tag-selector>
        </div>
        <div class="w3-col m8 tags-column">
          <app-initiative-tag
            v-for="tag in filters.tags"
            :tag="tag"
            :key="tag.id"
            class="tags-containers"
            :showRemove="true"
            @remove="removeTag($event)">
          </app-initiative-tag>
        </div>
      </div>

    </div>
    <div v-else class="w3-row w3-center loader-gif-container">
      <img class="loader-gif" src="../assets/loading.gif" alt="">
    </div>
  </div>

</template>

<script>
import InitiativeCard from '@/components/initiative/InitiativeCard.vue'
import InitiativeTagSelector from '@/components/initiative/InitiativeTagSelector.vue'
import InitiativeTag from '@/components/initiative/InitiativeTag.vue'

const getIndexOfTag = function (list, id) {
  for (var ix in list) {
    if (list[ix].id === id) {
      return ix
    }
  }
  return -1
}

export default {
  components: {
    'app-initiative-card': InitiativeCard,
    'app-initiative-tag-selector': InitiativeTagSelector,
    'app-initiative-tag': InitiativeTag
  },
  data () {
    return {
      filters: {
        tags: []
      },
      resetTrigger: true
    }
  },
  computed: {
    userInitiatives () {
      return this.$store.state.support.initiativesTree
    }
  },
  methods: {
    tagSelected (tag) {
      this.resetTrigger = !this.resetTrigger
      if (getIndexOfTag(this.filters.tags, tag.id) === -1) {
        this.filters.tags.push(tag)
        this.updateInitiatives()
      }
    },
    removeTag (tag) {
      var ix = getIndexOfTag(this.filters.tags, tag.id)
      if (ix !== -1) {
        this.filters.tags.splice(ix, 1)
        this.updateInitiatives()
      }
    }
  }

}
</script>

<style scoped>

.tags-column {
  padding-top: 8px;
}

</style>
