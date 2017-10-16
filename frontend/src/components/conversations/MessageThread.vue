<template lang="html">
  <div class="">
    <div class="w3-row history-container">
      <app-activity-getter
        :url="url"
        :reverse="true"
        :triggerUpdate="triggerUpdate">
      </app-activity-getter>
    </div>
    <div class="w3-row w3-margin-top">
      <app-markdown-editor
        v-model="newMessageText"
        placeholder="say something"
        :showToolbar="false"
        :showSend="true"
        @send="send($event)">
      </app-markdown-editor>
    </div>
  </div>
</template>

<script>
import ActivityGetter from '@/components/notifications/ActivityGetter.vue'

export default {
  components: {
    'app-activity-getter': ActivityGetter
  },

  props: {
    contextType: {
      type: String,
      default: ''
    },
    contextElementId: {
      type: String,
      default: ''
    },
    url: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      newMessageText: '',
      triggerUpdate: true
    }
  },

  methods: {
    send () {
      var message = {
        text: this.newMessageText
      }
      this.axios.post('/1/messages/' + this.contextType + '/' + this.contextElementId, message).then((response) => {
        if (response.data.result === 'success') {
          this.newMessageText = ''
          this.triggerUpdate = !this.triggerUpdate
        }
      })
    }
  }
}
</script>

<style scoped>

.CodeMirror, .CodeMirror-scroll {
	min-height: 50px !important;
}

.history-container {
  max-height: 50vh;
  overflow: auto;
}

</style>
