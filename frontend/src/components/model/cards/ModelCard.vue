<template lang="html">
  <div v-if="cardWrapper" class="card-base-div">

    <div class="card-content-container"
      :draggable="$store.state.support.triggerCardDraggingState && isDraggable"
      @dragstart="dragStart($event)">

      <component
        :is="cardComponent"
        :cardWrapper="cardWrapper"
        :inSection="inSection"
        :forceUpdate="forceUpdate"
        :inCardSelector="inCardSelector"
        :isNew="cardWrapper.type === 'newCard'"
        :cardRouteName="cardRouteName"
        :hideCardControls="hideCardControls"
        :type="type"
        @update="update()"
        @edit="$emit('edit')"
        @setConsent="setConsent($event)"
        @createNew="$emit('createNew')"
        @updateCards="$emit('updateCards')">
      </component>

      <transition name="fadeenter">
        <div v-if="$store.state.support.triggerCardDraggingState" class="cover-when-draggable">
          <div class="cover-when-draggable-content">
            <span>
              <i class="fa fa-arrows" aria-hidden="true"></i> move
            </span>
          </div>
        </div>
      </transition>

    </div>

  </div>
</template>

<script>
import ModelCardEditor from '@/components/model/cards/ModelCardEditor.vue'
import ModelCardSummary from '@/components/model/cards/ModelCardSummary.vue'
import ModelCardAsCard from '@/components/model/cards/ModelCardAsCard.vue'
import ModelCardAsPar from '@/components/model/cards/ModelCardAsPar.vue'

export default {
  name: 'model-card',

  components: {
    'app-model-card-editor': ModelCardEditor,
    'app-model-card-summary': ModelCardSummary,
    'app-model-card-as-card': ModelCardAsCard,
    'app-model-card-as-par': ModelCardAsPar
  },

  props: {
    cardWrapperInit: {
      type: Object,
      default: null
    },
    inSection: {
      type: Object,
      default: null
    },
    type: {
      type: String,
      default: 'card'
    },
    forceUpdate: {
      type: Boolean,
      default: true
    },
    inCardSelector: {
      type: Boolean,
      default: false
    },
    hideCardControls: {
      type: Boolean,
      default: false
    },
    cardRouteName: {
      type: String,
      default: 'ModelSectionCard'
    }
  },

  watch: {
    cardWrapperInit () {
      this.cardWrapper = this.cardWrapperInit
    }
  },

  data () {
    return {
      cardWrapper: null
    }
  },

  computed: {
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    authorIsLoggedUser () {
      if (this.$store.state.user.profile) {
        return this.$store.state.user.profile.c1Id === this.cardWrapper.creator.c1Id
      }
      return false
    },
    isDraggable () {
      if (this.cardWrapper.scope === 'COMMON') {
        return this.isLoggedAnEditor
      } else {
        return this.authorIsLoggedUser
      }
    },
    cardComponent () {
      if (this.cardWrapper.type === 'newCard' || this.cardWrapper.type === 'edit' || this.type === 'new') {
            return 'app-model-card-editor'
      }

      switch (this.type) {
        case 'summary':
          return 'app-model-card-summary'

        case 'card':
          return 'app-model-card-as-card'

        case 'doc':
          return 'app-model-card-as-par'

        default:
          return 'app-model-card-as-card'
      }
    }
  },

  methods: {
    update () {
      console.log('updating card ' + this.cardWrapper.id)
      this.axios.get('/1/model/cardWrapperAddition/' + this.cardWrapper.id, {
        params: {
          inSectionId: this.inSection ? this.inSection.id : ''
        }
      }).then((response) => {
        this.cardWrapper = response.data.data
      })
    },
    setConsent (state) {
      this.axios.put('/1/model/cardAddition/' + this.cardWrapper.additionId + '/setConsent', {}, {
        params: {
          elementType: 'CARD_WRAPPER_ADDITION',
          simpleConsentState: state ? 'OPENED' : 'CLOSED'
        }
      }).then((response) => {
        this.update()
        this.$emit('updateCards')
      })
    },
    dragStart (event) {
      var moveCardData = {
        type: 'MOVE_CARD',
        cardWrapperId: this.cardWrapper.id,
        fromSectionId: this.inSection.id,
        scope: this.cardWrapper.scope
      }
      event.dataTransfer.setData('text/plain', JSON.stringify(moveCardData))

      /* have an extended version of the dragged elements */
      var moveCardElement = {
        type: 'MOVE_CARD',
        cardWrapper: this.cardWrapper,
        fromSection: this.inSection
      }
      this.$store.commit('setDraggingElement', moveCardElement)
    }
  },

  created () {
    this.cardWrapper = this.cardWrapperInit
  }
}
</script>

<style>

.card-content-container {
  position: relative;
}

.cover-when-draggable {
  top: 0px;
  left: 0px;
  border-radius: 6px;
  position: absolute;
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
  background-color: rgba(164, 164, 164, 0.4);
  padding: 0px 16px;
  font-size: 16px;
  cursor: move;
}

.cover-when-draggable-content {
  max-width: 80%;
  max-height: 80%;
  margin: 0 auto;
  background-color: #15a5cc;
  color: white;
  border-radius: 12px;
  padding: 3px 12px;
}


.card-content-container,
.card-content-container h1,
.card-content-container h2,
.card-content-container h3,
.card-content-container h4,
.card-content-container h5
{
  font-family: 'Open Sans', sans-serif;
}

.card-content-container h1 {
  font-size: 19px;
}

.card-content-container h2 {
  font-size: 17px;
}

.card-content-container h3,
.card-content-container h4,
.card-content-container h5 {
  font-weight: bold;
  font-size: 15px;
}

.card-title {
  color: #15a5cc;
}

</style>


<style scoped>

</style>
