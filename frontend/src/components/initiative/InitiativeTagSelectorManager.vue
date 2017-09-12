<template lang="html">
  <div class="w3-row">
    <div class="w3-col s12">


      <div class="slider-container">
        <transition name="fadeenter">
          <div v-if="!creating" class="w3-row">
            <div class="w3-col w3-right w3-center" style="width: 100px">
              <button class="w3-button app-button" name="button">add</button>
            </div>
            <div class="w3-rest new-tag-input-container">
              <app-initiative-tag-selector
                :init="tagInit"
                @select="tagSelected($event)"
                @create-new="creating = true">
              </app-initiative-tag-selector>
            </div>
          </div>
        </transition>
      </div>

      <div class="slider-container">
        <transition name="fadeenter">
          <div v-if="creating" class="w3-row">
            <label><b>Tag:</b></label>
            <input type="text" class="w3-input" v-model.trim="newTag.tagText">
            <app-error-panel
              :show="tagTextEmptyErrorShow"
              message="please provide a tag">
            </app-error-panel>
            <app-error-panel
              :show="tagTextIsValidShow"
              message="tag can only contain letters, numbers and/or spaces">
            </app-error-panel>
            <app-error-panel
              :show="tagTextTooLong"
              message="tag too long">
            </app-error-panel>

            <br>
            <label><b>Description:</b></label>
            <textarea class="w3-input w3-border" rows="2" placeholder="tag description"v-model.trim="newTag.description"></textarea>
            <app-error-panel
              :show="tagDescriptionEmptyErrorShow"
              message="please provide a description">
            </app-error-panel>
            <app-error-panel
              :show="tagDescriptionTooLong"
              message="tag description too long">
            </app-error-panel>

            <div class="bottom-btns-row w3-row-padding w3-margin-top">
              <div class="w3-col m6">
                <button type="button" class="w3-button app-button-light" @click="creating = false">Cancel</button>
              </div>
              <div class="w3-col m6">
                <button type="button" class="w3-button app-button" @click="saveTag()">Accept</button>
              </div>
            </div>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
import InitiativeTagSelector from '@/components/initiative/InitiativeTagSelector.vue'

export default {

  props: {
    initiativeId: {
      type: String,
      default: ''
    }
  },

  components: {
    'app-initiative-tag-selector': InitiativeTagSelector
  },

  data () {
    return {
      creating: false,
      newTag: {
        tagText: '',
        description: ''
      },
      tagInit: null,
      tagDescriptionEmptyError: false,
      tagTextEmptyError: false,
      tagTextInvalidError: false
    }
  },

  computed: {
    tagTextIsEmpty () {
      return this.newTag.tagText === ''
    },
    tagTextEmptyErrorShow () {
      return this.tagTextIsEmpty && this.tagTextEmptyError
    },
    tagTextValid () {
      if (this.tagTextIsEmpty) {
        return true
      } else {
        return /^[a-z\d\-_\s]+$/i.test(this.newTag.tagText)
      }
    },
    tagTextIsValidShow () {
      return !this.tagTextValid && this.tagTextInvalidError
    },
    tagTextTooLong () {
      return this.newTag.tagText.length > 32
    },
    tagDescriptionIsEmpty () {
      return this.newTag.description === ''
    },
    tagDescriptionEmptyErrorShow () {
      return this.tagDescriptionIsEmpty && this.tagDescriptionEmptyError
    },
    tagDescriptionTooLong () {
      return this.newTag.description.length > 250
    }
  },

  methods: {
    tagSelected (tag) {
      this.$emit('selected', tag)
    },
    saveTag () {
      var ok = true

      if (this.tagTextIsEmpty) {
        this.tagTextEmptyError = true
        ok = false
      }

      if (!this.tagTextValid) {
        this.tagTextInvalidError = true
        ok = false
      }

      if (this.tagTextTooLong) {
        ok = false
      }

      if (this.tagDescriptionIsEmpty) {
        this.tagDescriptionEmptyError = true
        ok = false
      }

      if (this.tagDescriptionTooLong) {
        ok = false
      }

      if (ok) {
        /* save and get it again, then emit as selected */
        this.axios.post('/1/secured/tag', this.newTag).then((response) => {
          if (response.data.result === 'success') {
            return this.axios.get('/1/secured/initiative/tag/' + response.data.elementId)
          }
        }).then((response) => {
          debugger
          if (response.data.result === 'success') {
            this.creating = false
            this.newTag = {
              tagText: '',
              description: ''
            }
            this.tagInit = response.data.data
            this.$emit('selected', response.data.data)
          }
        })
      }
    }
  }
}
</script>

<style scoped>
</style>
