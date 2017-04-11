<template lang="html">

  <form @submit.prevent class="col">
    <h2>Propose a new contribution</h2>
    <br />

    <div class="form-group">
      <app-project-selector v-model="projectName"></app-project-selector>
    </div>

    <div class="form-group">
      <label>Goal:</label>
      <app-input-autocomplete
        :initValue="goalTag"
        :onSelect="(val) => {goalTag = val.anchor}"
        anchor="anchor"
        url="/1/goals/suggestions" :customParams="{'projectName': projectName}">
      </app-input-autocomplete>
      <span v-if="goalTagError" class="alert alert-danger">{{ goalTagError }}</span>
    </div>

    <div class="form-group">
      <label>Title:</label>
      <input v-model="title" type="text" class="form-control"></input>
      <span v-if="titleError" class="alert alert-danger">{{ titleError }}</span>
    </div>

    <div class="form-group">
      <label>Description:</label>
      <markdown-editor id="description-editor" v-model="description" :configs="markdownConfig"></markdown-editor>
      <span v-if="descriptionError" class="alert alert-danger">{{ descriptionError }}</span>
    </div>

    <div class="form-group">
      <label>Product:</label>
      <markdown-editor id="product-editor" v-model="product" :configs="markdownConfig"></markdown-editor>
      <span v-if="productError" class="alert alert-danger">{{ productError }}</span>
    </div>

    <div class="form-group">
      <label>Suggested Bid:</label>
      <input v-model="suggestedBid" type="number" class="form-control"></input>
      <span v-if="suggestedBidError" class="alert alert-danger">{{ suggestedBidError }}</span>
    </div>
    <br>
    <button class="btn btn-default" @click="cancel()">Cancel</button>
    <button class="btn btn-primary" @click="create()">Create</button>

  </form>

</template>

<script>

import { mapGetters } from 'vuex'
import ProjectSelector from '@/components/ProjectSelector.vue'
import Autocomplete from '@/components/external/Autocomplete.vue'

export default {
  props: {

  },

  data () {
    return {
      markdownConfig: {
        spellChecker: false,
        promptURLs: true,
        toolbar: ['bold', 'italic', 'strikethrough', '|', 'code', 'unordered-list', 'ordered-list', 'link', '|', 'preview', 'side-by-side', 'fullscreen', 'guide']
      },
      projectName: '',
      goalTag: '',
      goalTagError: null,
      title: '',
      titleError: null,
      description: '',
      descriptionError: null,
      product: '',
      productError: null,
      suggestedBid: '',
      suggestedBidError: null
    }
  },

  components: {
    AppProjectSelector: ProjectSelector,
    AppInputAutocomplete: Autocomplete
  },

  methods: {
    ...mapGetters(['activeProject']),

    cancel () {
      this.$emit('cancel')
    },

    create () {
      var cbtion = {
        projectName: this.projectName,
        goalTag: this.goalTag,
        title: this.title,
        description: this.description,
        product: this.product,
        suggestedBid: this.suggestedBid
      }

      this.axios.post('/1/cbtion', cbtion).then((response) => {
      })
    }
  },

  created () {
    this.$root.$on('show::modal', (id) => {
      if (id === 'cbtionNewModal') {
        this.projectName = this.activeProject()
      }
    })
  }

}
</script>

<style>

#description-editor.markdown-editor .CodeMirror,
#description-editor.markdown-editor .CodeMirror-scroll {
  min-height: 200px;
}

#product-editor.markdown-editor .CodeMirror,
#product-editor.markdown-editor .CodeMirror-scroll {
  min-height: 50px;
}

</style>
