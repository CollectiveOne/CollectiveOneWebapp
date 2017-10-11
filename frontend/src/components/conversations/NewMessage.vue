<template lang="html">
  <div class="">
    <app-markdown-editor
      v-model="newMessage"
      placeholder="say something"
      :showToolbar="false"
      :showSend="true"
      @send="send($event)">
    </app-markdown-editor>
  </div>
</template>

<script>
export default {
  data () {
    return {
      newMessage: ''
    }
  },

  methods: {
    send (message) {
      this.$emit('send', message)
    }
  }
}
</script>

<style scoped>

.CodeMirror, .CodeMirror-scroll {
	min-height: 50px !important;
}

</style>
