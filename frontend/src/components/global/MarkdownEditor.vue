<template lang="html">
  <div class="w3-cell-row">
    <div class="w3-cell markdown-container">
      <div class="w3-row">
        <textarea v-if="!preview" class="this-textarea" :class="textareaClasses" ref="mdArea"
          @focus="atFocus($event)" @blur="$emit('c-blur', $event)"
          v-model="text"
          :placeholder="placeholder">
        </textarea>
        <div v-if="preview || sideBySide" class="this-markdown" :class="markdownClasses">
          <vue-markdown class="marked-text" :source="text"></vue-markdown>
        </div>
      </div>
    </div>
    <div class="w3-cell buttons-column w3-cell-top">
      <div @click="togglePreview()"
        class="w3-tag cursor-pointer a-button" :class="{'gray-1': !preview, 'button-blue': preview}">
        <i class="fa fa-eye" aria-hidden="true"></i>
      </div>
      <div @click="toggleSideBySide()"
        class="w3-tag gray-1 cursor-pointer a-button" :class="{'gray-1': !sideBySide, 'button-blue': sideBySide}">
        <i class="fa fa-columns" aria-hidden="true"></i>
      </div>
      <a href="https://en.support.wordpress.com/markdown-quick-reference/" target="_blank"
        class="w3-tag gray-1 cursor-pointer a-button">
        <i class="fa fa-question-circle" aria-hidden="true"></i>
      </a>
    </div>
    <div v-if="showSend" class="w3-cell send-button-column w3-cell-top">
      <div class="send-button-container">
        <button class="w3-button app-button" name="button"
          @click="$emit('send', value)">
          <i class="fa fa-paper-plane" aria-hidden="true"></i>
          <br>
          <small>ctr + &crarr;</small>
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
    showSend: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      text: '',
      preview: false,
      sideBySide: false
    }
  },

  watch: {
    value () {
      this.text = this.value
      setTimeout(() => { this.checkHeight() }, 500)
    },
    text () {
      this.$emit('input', this.text)
      this.checkHeight()
    }
  },

  computed: {
    textareaClasses () {
      if (!this.sideBySide) {
        return {
          'w3-input': true,
          'w3-border': true
        }
      } else {
        return {
          'w3-input': true,
          'w3-border': true,
          'w3-col': true,
          'm6': true
        }
      }
    },
    markdownClasses () {
      if (!this.sideBySide) {
        return {
          'w3-input': true,
          'w3-border': true
        }
      } else {
        return {
          'w3-border': true,
          'w3-col': true,
          'm6': true
        }
      }
    }
  },

  methods: {
    atFocus (event) {
      this.$emit('c-focus', event)
      this.checkHeight()
    },
    checkHeight () {
      if (this.$refs.mdArea) {
        if (this.text !== '') {
          /* resize text area */
          this.$refs.mdArea.style.height = 'auto'
          this.$refs.mdArea.style.height = (this.$refs.mdArea.scrollHeight) + 'px'
        } else {
          this.$refs.mdArea.style.height = '0px'
        }
      }
    },
    togglePreview () {
      this.sideBySide = false
      this.preview = !this.preview
    },
    toggleSideBySide () {
      this.preview = false
      this.sideBySide = !this.sideBySide
    },
    atKeydown (e) {
      /* ctr + enter */
      if (e.keyCode === 13 && e.ctrlKey) {
        e.preventDefault()
        this.$emit('send', this.value)
      }
    }
  },

  mounted () {
    this.text = this.value
    window.addEventListener('keydown', this.atKeydown)

    /* autoresize textarea */
    this.$refs.mdArea.setAttribute('style', 'height:' + (this.$refs.mdArea.scrollHeight) + 'px;overflow-y:hidden;')

    setTimeout(() => {
      this.checkHeight()
    }, 500)
  },

  destroyed () {
    window.removeEventListener('keydown', this.atKeydown)
  }
}
</script>

<style scoped>

.this-textarea, .this-markdown {
  min-height: 68px;
}

.buttons-column {
  width: 32px;
  height: 100%;
  padding: 0px !important;
}

.a-button {
  width: 32px;
  border-top-right-radius: 3px;
  border-bottom-right-radius: 3px;
  margin-bottom: 1px;
}

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
