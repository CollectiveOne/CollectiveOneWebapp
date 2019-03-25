<template lang="html">
  <div class="w3-modal" draggable="false">
    <div class="w3-modal-content">
      <div class="w3-card-4 app-modal-card w3-display-container"
        v-click-outside="clickOutside">

        <div class="w3-display-topright">
          <div class="w3-right w3-button close-btn w3-xlarge" @click="closeThisConfirm()">
            <i class="fa fa-times" aria-hidden="true"></i>
          </div>
        </div>

        <div class="w3-container w3-border-bottom">
          <h3>{{ $t('model.MOVE_CARD') }}</h3>
        </div>

        <div class="div-modal-content w3-container">

          <div class="w3-row">
            <div class="w3-card-4 w3-topbar w3-round-large w3-margin-bottom">
              <app-model-card-as-card-content
               :card="cardWrapperToMove.card"
               :showFull="false">
              </app-model-card-as-card-content>
            </div>
          </div>

          <div class="section-tabs w3-row w3-center light-grey">
            <div class="w3-col s6 w3-bottombar w3-hover-light-grey cursor-pointer"
              :class="{'border-blue-app': !isMoveInternal}"
              @click="isMoveInternal = false">
              <h5 class="noselect" :class="{'bold-text': !isMoveInternal}">{{ $t('model.ADD_ALSO_TO') }}</h5>
            </div>
            <div class="w3-col s6 w3-bottombar w3-hover-light-grey cursor-pointer"
              :class="{'border-blue-app': isMoveInternal}"
              @click="isMoveInternal = true">
              <h5 class="noselect" :class="{'bold-text': !isMoveInternal}">{{ $t('model.MOVE_TO') }}</h5>
            </div>
          </div>

          <div class="w3-row">
            <app-model-section-selector
              :sectionId="$store.state.initiative.initiative.topModelSection.id"
              @select="sectionSelected($event)">
            </app-model-section-selector>
            <app-error-panel
              :show="noSectionSelectedShow"
              message="please select one section from above">
            </app-error-panel>
          </div>

          <div class="w3-row w3-container w3-margin-top">
            <label class="">
              <b>New scope (in "{{ inSectionTitle }}" section):</b>
            </label>
            <select v-model="newScope"
              class="w3-input w3-topbar" :class="scopeBorderClass" name="">
              <option value="PRIVATE">Private (only I can see it)</option>
              <option value="SHARED">Shared (others can see it, but its mine)</option>
              <option value="COMMON">Common (controlled by all editors)</option>
            </select>
          </div>

          <div class="modal-bottom-btns-row w3-row-padding">
            <hr>
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button-light" @click="closeThisConfirm()">
                {{ $t('general.CANCEL') }}<span><small>(Esc)</small></span>
              </button>
            </div>
            <div class="w3-col m6 w3-center">
              <button v-show="!sendingData" type="button" class="w3-button app-button" @click="accept()">
                {{ $t('general.ACCEPT') }}<span><small>(Ctr + Enter)</small></span>
              </button>
              <div v-show="sendingData" class="sending-accept light-grey">
                <img class="" src="../../../assets/loading.gif" alt="">
              </div>
            </div>
          </div>

        </div>

      </div>
    </div>
  </div>
</template>

<script>
import ModelSectionSelector from '@/components/model/ModelSectionSelector.vue'
import ModelCardAsCardContent from '@/components/model/cards/ModelCardAsCardContent.vue'

export default {

  components: {
    'app-model-section-selector': ModelSectionSelector,
    'app-model-card-as-card-content': ModelCardAsCardContent
  },

  props: {
    isMove: {
      type: Boolean,
      default: false
    },
    cardWrapperToMove: {
      type: Object
    },
    fromSection: {
      type: Object
    }
  },

  data () {
    return {
      toSection: null,
      isMoveInternal: false,
      noSectionSelectedError: false,
      newScope: 'COMMON',
      sendingData: false
    }
  },

  computed: {
    noSectionSelectedShow () {
      return this.toSection == null && this.noSectionSelectedError
    },

    inSectionTitle () {
      return this.toSection != null ? this.toSection.title : ''
    },

    scopeBorderClass () {
      let cClass = {}
      switch (this.newScope) {
        case 'PRIVATE':
          cClass['border-red'] = true
          break

        case 'SHARED':
          cClass['border-yellow'] = true
          break

        default:
          cClass['border-blue'] = true
          break
      }
      return cClass
    }
  },

  methods: {
    closeThisConfirm () {
      this.$emit('close')
    },

    clickOutside () {
        this.closeThisConfirm()
    },

    sectionSelected (section) {
      this.toSection = section
    },

    accept () {
      let ok = true
      if (this.toSection == null) {
        this.noSectionSelectedError = true
        ok = false
      }

      if (!ok) return

      this.sendingData = true
      if (this.isMove) {
        /* move from section */
        this.axios.put('/1/model/section/' + this.fromSection.id +
          '/moveCard/' + this.cardWrapperToMove.id, {}, {
          params: {
            onSectionId: this.toSection.id,
            scope: this.newScope
          }
        }).then((response) => {
          this.$emit('update')
          this.sendingData = false
          this.closeThisConfirm()
        })
      } else {
        /* copy card without removing it */
        this.axios.put('/1/model/section/' + this.toSection.id +
          '/cardWrapper/' + this.cardWrapperToMove.id, {}, {
          params: {
            scope: this.newScope
          }
        }).then((response) => {
          this.$emit('update')
          this.sendingData = false
          this.closeThisConfirm()
        })
      }
    }
  },

  created () {
    this.isMoveInternal = this.isMove
  }
}
</script>

<style scoped>

.close-btn {
  border-top-right-radius: 20px;
}

</style>
