<template lang="html">
  <div class="">
    <div v-show="!sectionSelected" class="">
      <div class="w3-row w3-margin-top">
        <input @input="queryUpdate()" v-model="query" class="w3-input" type="text" name="" value="" placeholder="search and select a section">
      </div>
      <div class="section-results-container w3-row-padding w3-container w3-margin-top w3-border">
        <div class="section-container cursor-pointer w3-border gray-1-border w3-round"
          v-for="section in sections"
          :key="section.id"
          @click="sectionClicked(section)">

          <app-model-section-header
            :section="section"
            :level="0"
            :floating="true">
          </app-model-section-header>

        </div>
        <div v-if="sections.length == 0" class="w3-center">
          <i>no sections found</i>
        </div>
      </div>
    </div>
    <div v-show="sectionSelected !== null" class="w3-row w3-display-container w3-margin-top w3-border gray-1-border w3-round">
      <app-model-section-header
        :section="sectionSelected"
        :level="0"
        :floating="true">
      </app-model-section-header>

      <div class="delete-selected gray-1-color w3-display-topright w3-xlarge"
        @click="sectionDeselected()">
        <i class="fa fa-times-circle cursor-pointer" aria-hidden="true"></i>
      </div>
    </div>
  </div>
</template>

<script>
import ModelSectionHeader from '@/components/model/ModelSectionHeader.vue'

export default {

  components: {
    'app-model-section-header': ModelSectionHeader
  },

  props: {
    initiativeId: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      query: '',
      page: 0,
      sections: [],
      sectionSelected: null
    }
  },

  methods: {
    queryUpdate () {
      this.update()
    },
    sectionClicked (section) {
      this.sectionSelected = section
      this.$emit('select', section)
    },
    sectionDeselected () {
      this.sectionSelected = null
      this.$emit('select', null)
    },
    update () {
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/section/search', {
        params: {
          query: this.query,
          page: this.page,
          size: 10
        }
      }).then((response) => {
        this.sections = response.data.data.content
      })
    }
  },

  created () {
    this.update()
  }
}
</script>

<style scoped>

.section-results-container {
  max-height: 500px;
  overflow-y: auto;
  padding-top: 16px;
  padding-bottom: 16px;
}

.section-container {
  margin-bottom: 16px;
  display: inline-block;
}

@media screen and (min-width: 992px) {
  .section-container {
    width: calc(50% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

.delete-selected {
  position: absolute;
  margin-right: 15px;
  margin-top: 2px;
}

.delete-selected:hover {
  color: #15a5cc !important;
}

</style>
