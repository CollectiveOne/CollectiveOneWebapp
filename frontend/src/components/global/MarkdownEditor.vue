<template lang="html">
  <div class="w3-cell-row">
    <div class="w3-cell markdown-container">
      <markdown-editor
        :configs="editorConfigs"
        :value="value"
        @input="$emit('input', $event)">
      </markdown-editor>
    </div>
    <div v-if="showSend" class="w3-cell send-button-column">
      <div class="send-button-container">
        <button class="w3-button app-button" name="button"
          @click="$emit('send', value)">
          <i class="fa fa-paper-plane" aria-hidden="true"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    value: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: ''
    },
    showToolbar: {
      type: Boolean,
      default: true
    },
    showSend: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
    }
  },

  computed: {
    editorConfigs () {
      var toolbarIcons
      if (this.showToolbar) {
        toolbarIcons = ['bold', 'italic', 'quote', 'unordered-list', 'ordered-list', 'link', '|', 'preview', 'guide']
      } else {
        toolbarIcons = []
      }
      return {
        spellChecker: false,
        placeholder: this.placeholder,
        toolbar: toolbarIcons,
        status: false
      }
    }
  },

  methods: {
  }
}
</script>

<style scoped>

.send-button-column {
  width: 60px;
  height: 100%;
}

.send-button-container {
  height: 100%;
  padding-left: 10px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}

.markdown-container {
}

</style>
