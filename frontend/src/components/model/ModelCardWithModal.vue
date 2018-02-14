<!-- This wrapper is needed because the model card modal uses
needs the model card component inside, and would crate a recursion -->
<template lang="html">

  <div class="">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal v-if="showCardModal"
          :isNew="false"
          :initiativeId="initiativeId"
          :cardWrapperId="cardWrapper.id"
          :inSectionId="inSectionId"
          :inSectionTitle="inSectionTitle"
          :onlyMessages="onlyMessages"
          @close="modalClosed()">
        </app-model-card-modal>
      </transition>
    </div>

    <div class=""
      draggable="true"
      @dragstart="dragStart($event)">

      <!-- forward all props  -->
      <app-model-card
        :cardWrapper="cardWrapper"
        :initiativeId="initiativeId"
        :inSectionId="inSectionId"
        :inSectionTitle="inSectionTitle"
        :cardEffect="cardEffect"
        :floating="floating"
        :enableExpand="enableExpand"
        :forceUpdate="forceUpdate"
        @please-update="update()"
        @expand-modal="showCard($event)">
      </app-model-card>

    </div>

  </div>

</template>

<script>
import ModelCardModal from '@/components/model/modals/ModelCardModal.vue'
import ModelCard from '@/components/model/ModelCard.vue'

export default {
  components: {
    'app-model-card-modal': ModelCardModal,
    'app-model-card': ModelCard
  },

  props: {
    cardWrapperInit: {
      type: Object,
      default: null
    },
    initiativeId: {
      type: String,
      default: ''
    },
    inSectionId: {
      type: String,
      default: ''
    },
    inSectionTitle: {
      type: String,
      default: ''
    },
    cardEffect: {
      type: Boolean,
      default: true
    },
    floating: {
      type: Boolean,
      default: false
    },
    enableExpand: {
      type: Boolean,
      default: true
    },
    expandLocally: {
      type: Boolean,
      default: true
    },
    forceUpdate: {
      type: Boolean,
      default: true
    }
  },

  data () {
    return {
      cardWrapper: null,
      showCardModal: false,
      onlyMessages: false
    }
  },

  methods: {
    update () {
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/cardWrapper/' + this.cardWrapper.id).then((response) => {
        this.cardWrapper = response.data.data
      })
    },
    modalClosed () {
      this.showCardModal = false
      this.update()
    },
    showCard (onlyMessages) {
      if (this.expandLocally) {
        this.onlyMessages = onlyMessages
        this.showCardModal = true
      } else {
        this.$router.push({
          name: 'ModelCardInSection',
          params: {
            initiativeId: this.initiativeId,
            sectionId: this.inSectionId,
            cardId: this.cardWrapper.id,
            onlyMessages: onlyMessages
          }
        })
      }
    },
    dragStart (event) {
      var moveCardData = {
        type: 'MOVE_CARD',
        cardWrapperId: this.cardWrapper.id,
        fromSectionId: this.inSectionId
      }
      event.dataTransfer.setData('text/plain', JSON.stringify(moveCardData))
    }
  },

  created () {
    this.cardWrapper = this.cardWrapperInit
  }
}
</script>

<style scoped>

.click-area {
  position: absolute;
  min-height: 40px;
  height: calc(100% - 40px);
  width: 100%;
  z-index: 10;
}

.messages-indicator {
  margin-right: 46px;
  margin-bottom: 6px;
  z-index: 11;
}

</style>
