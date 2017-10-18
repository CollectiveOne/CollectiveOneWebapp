<template lang="html">
  <div class="">
    <div class="w3-row">
      <div class="w3-margin-right" style="display: inline-block"><b>search in:</b></div>
      <button
        @click="separate = false"
        class="w3-button" type="button" name="button"
        :class="{'app-button-light': separate, 'app-button': !separate}">
        single query
      </button>
      <button
        @click="separate = true"
        class="w3-button app-button" type="button" name="button"
        :class="{'app-button-light': !separate, 'app-button': separate}">
        side-by-side
      </button>
    </div>
    <hr>
    <div class="w3-row">
      <div v-if="separate" class="">
        <div class="w3-col s6">
          <div class="w3-row">
            <app-model-search
              :exclusive="true"
              initType="SECTIONS"
              :queryInit='query'
              @query-updated="queryUpdated($event)">
            </app-model-search>
          </div>
        </div>
        <div class="w3-col s6">
          <div class="w3-ro2">
            <app-model-search
              :exclusive="true"
              initType="CARDS"
              :queryInit='query'
              @query-updated="queryUpdated($event)">
            </app-model-search>
          </div>
        </div>
      </div>
      <div v-else class="">
        <app-model-search
          :exclusive="false"
          :queryInit='query'
          @query-updated="queryUpdated($event)">
        </app-model-search>
      </div>
    </div>
  </div>
</template>

<script>
import ModelSearch from '@/components/model/ModelSearch.vue'

export default {
  components: {
    'app-model-search': ModelSearch
  },

  data () {
    return {
      separate: false,
      query: ''
    }
  },

  methods: {
    queryUpdated (query) {
      this.query = query
    }
  }
}
</script>

<style scoped>

button {
  width: 150px;
}

</style>
