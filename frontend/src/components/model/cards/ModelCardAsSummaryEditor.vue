<template lang="html">
  <div class="card-summary-container w3-leftbar w3-card w3-row" :class="cClass"
    @mouseover="hovering = true"
    @mouseleave="hovering = false">

    <div class="w3-col m8 left-div">
      <div class="text-div">
        <input type="text" class="w3-input w3-hover-light-grey" placeholder="Enter card title"  v-model="editedCard.title">
          <app-markdown-editor placeholder="Enter text here" v-model="editedCard.text"></app-markdown-editor>
      </div>

<!--       <div v-if="cardWrapper.inModelSections.length > 0 && !hideCardControls" class="w3-right text-div">
        <app-in-model-sections-tags
          :inModelSections="cardWrapper.inModelSections"
          :hideSectionId="inSectionId">
        </app-in-model-sections-tags>
      </div> -->
    </div>

    <div class="w3-col m4 right-div controls">

<!--       <div v-if="cardWrapper.creator !== null" class="w3-left text-div">
        <app-user-avatar :user="cardWrapper.creator" :showName="false" :small="true"></app-user-avatar>
      </div>
 -->
<!--       <div v-if="editors.length > 0" class="w3-left editors-div">
        <div class="w3-left" v-for="editor in editors">
          <app-user-avatar v-if="editor.c1Id !== cardWrapper.creator.c1Id" :user="editor" :showName="false" :small="true"></app-user-avatar>
        </div>
      </div> -->

      <div v-if="!inCardSelector" class="w3-left scope-controls">

        <div  class="w3-right cursor-pointer indicator-comp"
          @click="scopeSelected('common')">
          <i class="fa fa-circle select-common highlight" aria-hidden="true"></i>
        </div>

        <div class="w3-right cursor-pointer indicator-comp"
          @click="scopeSelected('shared')">
          <i class="fa fa-circle select-shared highlight" aria-hidden="true"></i>
        </div>

        <div class="w3-right cursor-pointer indicator-comp"
          @click="scopeSelected('private')">
          <i class="fa fa-circle select-private highlight" aria-hidden="true"></i>
        </div>
      </div>

      <div v-if="!inCardSelector" class="w3-right control-buttons">
        <div class="w3-right cursor-pointer indicator-comp">
          <i class="fa fa-upload highlight" aria-hidden="true"></i>
        </div>
        <div class="w3-right cursor-pointer indicator-comp" @click="cardClicked()">
          <i class="fa fa-expand highlight" aria-hidden="true"></i>
        </div>
      </div>
        <div class="send-button-container">
        <button v-show="!sendingData" class="w3-button app-button" name="button" @click="createCard()">Create new card</button>
        <div v-show="sendingData" class="sending-accept light-grey">
          <img class="" src="../../../assets/loading.gif" alt="">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { cardMixin } from '@/components/model/cards/cardMixin.js'
import CardControlButtons from '@/components/model/cards/CardControlButtons.vue'
import InModelSectionsTags from '@/components/model/cards/InModelSectionsTags.vue'

export default {

  name: 'model-card-summary',

  mixins: [ cardMixin ],

  components: {
    'app-card-control-buttons': CardControlButtons,
    'app-in-model-sections-tags': InModelSectionsTags
  },

  props: {
    inModelSection: {
      type: Object,
      default: null
    },
    cardWrapper: {
      type: Object,
      default: null
    }
  },

  data () {
    return {
      hovering: false,
      cClass: {
        'border-yellow': true
      },
      editedCard: {
        title: '',
        text: '',
        newScope: 'SHARED'
      }
    }
  },

  computed: {
    cardShortTitle () {
      return 'banda'
    }
    // card () {
    //   return this.cardWrapper.card
    // },
    // hasImage () {
    //   return this.card.imageFile !== null
    // }
  },

  methods: {
    scopeSelected (scope) {
      console.log(scope)
      this.cClass = {}
      if (scope === 'private') {
        this.editedCard.newScope = 'PRIVATE'
        this.cClass['border-red'] = true
      } else if (scope === 'shared') {
        this.editedCard.newScope = 'SHARED'
        this.cClass['border-yellow'] = true
      } else if (scope === 'common') {
        this.editedCard.newScope = 'COMMON'
        this.cClass['border-blue'] = true
      }
      console.log(this.cClass)
    },
    hoverEnter () {
      this.showActionButton = true
      this.highlight = true
    },
    hoverLeave () {
      this.showActionButton = false
      this.highlight = false
    }
  },

  created () {
  }
}
</script>

<style scoped>

.card-summary-container {
  overflow: hidden;
  position: relative;
  width: 100%;
}

.controls {
    height: 100%;
    position: absolute;
    right: 0;
    top: 0;
}

.right-div {
  height: 100%;
}

.card-title {
}

.highlight:hover {
   text-shadow: 0px 0px 5px #000000;
}

.select-clicked { 
   text-shadow: 0px 0px 5px #000000;
}
.app-button {
  border-radius: 0px;
}

.send-button-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  position: absolute;
  right:    0;
  bottom:   0;
}

.scope-controls, .control-buttons {
  padding: 5px 0px 5px 0px;
}

.select-private {
  color: #980458;
}
.select-shared {
  color: #c6bb21;
}
.select-common {
  color: #0c708b;
}

.card-container {
  max-height: 750px;
  transition: max-height 1000ms ease;
  overflow-y: auto;
}

.left-div {
  border-right-style: solid;
  border-width: 1px;
  border-color: #d0d0d0;
}

.expand-btn {
  width: 28px;
  margin-right: 10px;
  background-color: #d6d6d6;
  text-align: center;
  padding: 3px 6px;
  border-radius: 3px;
}

.expand-btn:hover {
  background-color: #a3a0a0;
}

.control-div {
  padding: 3px 6px;
}

.text-div {
  padding-top: 3px;
}

.editors-div {
  margin-left: 10px;
  padding-left: 8px;
  border-left-style: dotted;
  border-color: #c1c1c1;
  border-width: 1px;
}

.indicator-comp {
  margin-right: 6px;
  margin-left: 4px;
}

</style>
