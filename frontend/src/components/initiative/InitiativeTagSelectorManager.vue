<template lang="html">
  <div class="w3-row">
    <div class="w3-col s12">


      <div class="slider-container">
        <transition name="fadeenter">
          <div v-if="!creating" class="w3-row">
            <div class="w3-col w3-right w3-center" style="width: 100px">
              <button @click="$emit('add', tag)" class="w3-button app-button" name="button">{{ $t('general.ADD') }}</button>
            </div>
            <div class="w3-rest new-tag-input-container">
              <app-initiative-tag-selector
                :init="tagInit"
                :enableCreate="true"
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
            <label><b>{{ $t('initiatives.TAG') }}:</b></label>
            <input id="T_tagName_SelectTagInitiative" type="text" class="w3-input" v-model.trim="newTag.tagText">
            <app-error-panel
              :show="tagTextEmptyErrorShow"
              :message="$t('general.FIELD_CANNOT_BE_EMPTY')">
            </app-error-panel>
            <app-error-panel
              :show="tagTextIsValidShow"
              :message="$t('general.FIELD_ONLY_LETTERS')">
            </app-error-panel>
            <app-error-panel
              :show="tagTextTooLong"
              :message="$t('general.FIELD_TOO_LONG')">
            </app-error-panel>

            <br>
            <label><b>{{ $t('general.DESCRIPTION') }}:</b></label>
            <textarea id="T_tagDescription_SelectTagInitiative" class="w3-input w3-border" rows="2" placeholder="tag description"v-model.trim="newTag.description"></textarea>
            <app-error-panel
              :show="tagDescriptionEmptyErrorShow"
              :message="$t('general.FIELD_CANNOT_BE_EMPTY')">
            </app-error-panel>
            <app-error-panel
              :show="tagDescriptionTooLong"
              :message="$t('general.FIELD_TOO_LONG')">
            </app-error-panel>

            <div class="bottom-btns-row w3-row-padding w3-margin-top">
              <div class="w3-col m6">
                <button type="button" class="w3-button app-button-light" @click="creating = false">{{ $t('general.CANCEL') }}</button>
              </div>
              <div class="w3-col m6">
                <button id="T_buttonAccept_SelectTagInitiative" type="button" class="w3-button app-button" @click="saveTag()">{{ $t('general.ACCEPT') }}</button>
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
      tag: null,
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
      this.tag = tag
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
        this.axios.post('/1/initiative/tag', this.newTag).then((response) => {
          if (response.data.result === 'success') {
            return this.axios.get('/1/initiative/tag/' + response.data.elementId)
          }
        }).then((response) => {
          if (response.data.result === 'success') {
            this.creating = false
            this.newTag = {
              tagText: '',
              description: ''
            }
            this.tagInit = response.data.data
            this.tag = response.data.data
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
