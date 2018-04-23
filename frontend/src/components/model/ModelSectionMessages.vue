<template lang="html">
  <div class="">
    <div class="w3-row controls-row">
      <div class="w3-left zoom-controls">
        <div class="w3-left cursor-pointer">
          <i @click="levelDown()" class="fa fa-minus-circle" aria-hidden="true"></i>
        </div>
        <div class="w3-left number-div">
          {{ levels }}
        </div>
        <div class="w3-left cursor-pointer">
          <i @click="levelUp()" class="fa fa-plus-circle" aria-hidden="true"></i>
        </div>
      </div>
      <div class="w3-left text-details">
        <span >
          you are seeing the timeline events up to {{ levels }} levels under
          <br>"{{ currentSection.title }}" section .
        </span>
      </div>
    </div>

    <div class="w3-row card-messages-container">
      <app-message-thread
        contextType="MODEL_SECTION"
        :contextElementId="currentSectionId"
        :onlyMessagesInit="true"
        :levels="levels">
      </app-message-thread>
    </div>
  </div>

</template>

<script>
import MessageThread from '@/components/notifications/MessageThread'

export default {
  components: {
    'app-message-thread': MessageThread
  },

  computed: {
    currentSectionId () {
      return this.$route.params.sectionId
    },
    currentSectionGenealogy () {
      return this.$store.state.model.currentSectionGenealogy
    },
    currentSection () {
      if (this.currentSectionGenealogy) {
        return this.currentSectionGenealogy.section
      }
      return {'title': 'section'}
    },
    levels () {
      return this.$route.query.levels ? parseInt(this.$route.query.levels) : 1
    }
  },

  methods: {
    levelUp () {
      this.$router.replace({name: this.$route.name, query: {levels: this.levels + 1}})
    },
    levelDown () {
      if (this.levels > 1) {
        this.$router.replace({name: this.$route.name, query: {levels: this.levels - 1}})
      }
    }
  }
}
</script>

<style scoped>

.controls-row {
  margin: 6px 12px;
}

.card-messages-container {
  padding: 0px 12px 12px 12px;
}

.zoom-controls {
  width: 90px;
  font-size: 28px;
  text-align: center;
}

.zoom-controls > .w3-left {
  width: 30px;
}

.zoom-controls > .number-div {
  font-size: 22px;
}

.text-details {
  padding-left: 12px;
  font-size: 12px;
}

</style>
