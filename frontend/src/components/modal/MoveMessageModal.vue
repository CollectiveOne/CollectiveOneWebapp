<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4 app-modal-card">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>{{ $t('notifications.MOVE_MESSAGE') }}</h2>
        </div>

        <div class="w3-container form-container">

          <div class="w3-row">
            <label for=""><b>move:</b></label>
          </div>

          <div class="w3-row">
            <vue-markdown
              class="marked-text move-message-container w3-border w3-round-large w3-padding"
              :source="message.text"
              :anchorAttributes="{target: '_blank'}">
            </vue-markdown>
          </div>

          <div class="w3-row-padding w3-margin-top bottom-btns-row">
            <div class="w3-col m6 ">
              <button class="w3-button w3-padding"
                :class="{ 'app-button': toSection, 'app-button-light': !toSection }"
                @click="toSection = true">
                {{ $t('notifications.TO_SECTION_CM') }}
              </button>
            </div>

            <div class="w3-col m6 ">
              <button class="w3-button w3-padding"
                :class="{ 'app-button': !toSection, 'app-button-light': toSection }"
                @click="toSection = false">
                {{ $t('notifications.TO_CARD_CM') }}
              </button>
            </div>

          </div>

          <div v-if="toSection" class="w3-row">
            <app-model-section-selector
              :sectionId="initiative.topModelSection.id"
              @select="sectionSelected($event)">
            </app-model-section-selector>
            <app-error-panel
              :show="noSectionSelectedShow"
              message="please select one section from above">
            </app-error-panel>
          </div>
          <div v-else class="w3-row">
            <app-model-card-selector
              :inSectionId="activity.modelSection.id"
              @select="cardSelected($event)">
            </app-model-card-selector>
            <app-error-panel
              :show="noCardWrapperSelectedError"
              message="please select one card from above">
            </app-error-panel>
          </div>

          <hr>

          <div class="bottom-btns-row w3-row-padding">
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button-light" @click="closeThis()">Cancel</button>
            </div>
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button" @click="accept()">Accept</button>
            </div>
          </div>

        </div>

      </div>
    </div>
  </div>
</template>

<script>
import ModelSectionSelector from '@/components/model/ModelSectionSelector.vue'
import ModelCardSelector from '@/components/model/cards/ModelCardSelector.vue'

export default {

  components: {
    'app-model-section-selector': ModelSectionSelector,
    'app-model-card-selector': ModelCardSelector
  },

  props: {
    activity: {
      type: Object
    }
  },

  data () {
    return {
      toSection: true,
      noSectionSelectedError: false,
      noCardWrapperSelectedError: false,
      section: null,
      cardWrapper: null
    }
  },

  computed: {
    message () {
      return this.activity.message
    },
    initiative () {
      return this.$store.state.initiative.initiative
    },
    noSectionSelected () {
      return this.section === null
    },
    noSectionSelectedShow () {
      return this.noSectionSelected && this.noSectionSelectedError
    },
    fromContextType () {
      return this.activity.modelCardWrapper != null ? 'MODEL_CARD' : 'MODEL_SECTION'
    },
    fromContextElementId () {
      return this.activity.modelCardWrapper != null ? this.activity.modelCardWrapper.id : this.activity.modelSection.id
    },
    toContextType () {
      return this.toSection ? 'MODEL_SECTION' : 'MODEL_CARD'
    },
    toContextElementId () {
      return this.toSection ? this.section.id : this.cardWrapper.id
    },
    messageId () {
      return this.activity.message.id
    }
  },

  methods: {
    closeThis () {
      this.$emit('close')
    },
    sectionSelected (section) {
      this.section = section
    },
    cardSelected (cardWrapper) {
      this.cardWrapper = cardWrapper
    },
    accept () {
      var ok = true
      if (this.toSection && this.section === null) {
        ok = false
        this.noSectionSelectedError = true
      }

      if (!this.toSection && this.cardWrapper === null) {
        ok = false
        this.noCardWrapperSelectedError = true
      }

      if (ok) {
        this.axios.put('/1/message/' + this.fromContextType + '/' + this.fromContextElementId + '/' + this.messageId + '/move', {}, {
          params: {
            toSectionId: this.toSection ? this.section.id : '',
            toCardWrapperId: !this.toSection ? this.cardWrapper.id : ''
          }
        }).then((response) => {
          this.closeThis()
        })
      }
    }
  },

  mounted () {
  }
}
</script>

<style scoped>

.form-container {
  padding-top: 20px;
  padding-bottom: 35px;
}

.move-message-container {

}


</style>
