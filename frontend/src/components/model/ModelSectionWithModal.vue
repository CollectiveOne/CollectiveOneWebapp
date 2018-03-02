<!-- This wrapper is needed because the model card modal uses
needs the model card component inside, and would crate a recursion -->
<template lang="html">

  <div class="">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-section-modal
          v-if="showSectionModal"
          :isNew="false"
          :initiativeId="initiativeId"
          :sectionId="section.id"
          :inElementId="inElementId"
          :inElementTitle="inElementTitle"
          :onlyMessages="onlyMessages"
          @close="showSectionModal = false">
        </app-model-section-modal>
      </transition>
    </div>

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-section-modal
          v-if="showNewSubsectionModal"
          :isNew="true"
          :initiativeId="initiativeId"
          :inElementId="section.id"
          :inElementTitle="section.title"
          @close="showNewSubsectionModal = false">
        </app-model-section-modal>
      </transition>
    </div>

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal
          v-if="showNewCardModal"
          :isNew="true"
          :ixInSection="ixInSection"
          :initiativeId="initiativeId"
          :inSectionId="section.id"
          :inSectionTitle="section.title"
          @close="showNewCardModal = false">
        </app-model-card-modal>
      </transition>
    </div>

    <div class="w3-display-container"
      @mouseover="showActionButton = true"
      @mouseleave="showActionButton = false">

      <!-- forward all props  -->
      <app-model-section
        :sectionId="sectionId"
        :sectionInit="sectionInit"
        :basicPreloaded="basicPreloaded"
        :subElementsPreloaded="subElementsPreloaded"
        :initiativeId="initiativeId"
        :inElementId="inElementId"
        :inElementTitle="inElementTitle"
        :level="level"
        :dragType="dragType"
        :floating="floating"
        :cardsAsCards="cardsAsCards"
        :expandInit="expandInit"
        :expandSubSubsecInit="expandSubSubsecInit"
        :forceUpdate="forceUpdate"
        :sortByLikes="sortByLikes"
        @updated="updated($event)"
        @new-card="newCard($event)"
        @new-subsection="newSubsection()"
        @show-messages="expandSectionModal(true)"
        @header-clicked="expandSectionModal(true)">
      </app-model-section>

      <transition name="fadeenter">
        <div v-if="showActionButton" class="w3-display-topright action-buttons-container"
          @mouseleave="showSubActionButtons = false">
          <div
            class="w3-button model-action-button gray-1-color w3-right tooltip"
            @click="expandSectionModal()">
            <i class="fa fa-expand" aria-hidden="true"></i>
            <span class="tooltiptext gray-1">expand</span>
          </div>
          <div
            v-if="isLoggedAnEditor"
            class="w3-button model-action-button gray-1-color w3-right tooltip"
            @click="showSubActionButtons = !showSubActionButtons">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
            <span class="tooltiptext gray-1">add elements</span>
          </div>
          <div v-if="showSubActionButtons" class="sub-action-buttons-container w3-card w3-white">
            <div class="w3-button" @click="newCard()">
              <i class="fa fa-plus w3-margin-right" aria-hidden="true"></i> add card
            </div>
            <div class="w3-button" @click="newSubsection()">
              <i class="fa fa-plus w3-margin-right" aria-hidden="true"></i> add sub-section
            </div>
          </div>
          <div class="w3-button model-action-button gray-1-color w3-right tooltip"
            @click="cardsAsCards = !cardsAsCards">
            <i class="fa" :class="{ 'fa-bars': cardsAsCards, 'fa-th': !cardsAsCards }" aria-hidden="true"></i>
            <span class="tooltiptext gray-1">toggle card/doc view</span>
          </div>
          <div class="w3-button model-action-button gray-1-color w3-right tooltip"
            @click="sortByLikes = !sortByLikes">
            <i class="fa" :class="{'fa-sort-numeric-asc': sortByLikes, ' fa-heartbeat': !sortByLikes}"
              aria-hidden="true">
            </i>
            <span class="tooltiptext gray-1">sort by likes</span>
          </div>
          <div class="w3-button model-action-button gray-1-color w3-right tooltip"
            @click="forceUpdate = !forceUpdate">
            <i class="fa fa-refresh" aria-hidden="true"></i>
            <span class="tooltiptext gray-1">refresh contents</span>
          </div>
        </div>
      </transition>

    </div>

  </div>

</template>

<script>
import ModelSectionModal from '@/components/model/modals/ModelSectionModal.vue'
import ModelCardModal from '@/components/model/modals/ModelCardModal.vue'
import ModelSection from '@/components/model/ModelSection.vue'

export default {
  name: 'model-section-with-modal',

  components: {
    'app-model-section-modal': ModelSectionModal,
    'app-model-card-modal': ModelCardModal,
    'app-model-section': ModelSection
  },

  props: {
    sectionInit: {
      type: Object,
      default: null
    },
    basicPreloaded: {
      type: Boolean,
      default: false
    },
    subElementsPreloaded: {
      type: Boolean,
      default: false
    },
    initiativeId: {
      type: String,
      default: ''
    },
    sectionId: {
      type: String,
      default: ''
    },
    inElementId: {
      type: String,
      default: ''
    },
    inElementTitle: {
      type: String,
      default: ''
    },
    level: {
      type: Number,
      default: 0
    },
    dragType: {
      type: String
    },
    floating: {
      type: Boolean,
      default: false
    },
    cardsAsCardsInit: {
      type: Boolean,
      default: true
    },
    expandInit: {
      type: Boolean,
      default: false
    },
    expandSubSubsecInit: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      section: null,
      showSubActionButtons: false,
      cardsAsCards: true,
      showActionButton: false,
      showSectionModal: false,
      showNewSubsectionModal: false,
      showNewCardModal: false,
      forceUpdate: false,
      onlyMessages: false,
      sortByLikes: false
    }
  },

  computed: {
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    }
  },

  methods: {
    updated (section) {
      this.section = section
    },
    expandSectionModal (onlyMessages) {
      this.onlyMessages = onlyMessages || false
      this.showSectionModal = true
    },
    newSubsection () {
      this.showNewSubsectionModal = true
    },
    newCard (ix) {
      this.ixInSection = ix
      this.showNewCardModal = true
    }
  },

  watch: {
    cardsAsCardsInit () {
      this.cardsAsCards = this.cardsAsCardsInit
    }
  },

  created () {
    this.cardsAsCards = this.cardsAsCardsInit
    this.section = this.sectionInit
  }
}
</script>

<style scoped>

.tooltip .tooltiptext {
    top: 100%;
    right: 50%;
}

.action-buttons-container {
  margin-top: 2px;
  margin-right: 1px;
}

.sub-action-buttons-container {
  position: absolute;
  margin-top: 38px;
  margin-left: -120px;
}

.sub-action-buttons-container .fa {
  font-size: 12px;
}

.sub-action-buttons-container .w3-button {
  width: 100%;
  text-align: left !important;
}

</style>
