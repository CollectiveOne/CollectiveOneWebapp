<template lang="html">
  <div class="w3-row controls-row small-scroll">

    <div class="control-group">
      <popper trigger="hover":options="popperOptions" delay-on-mouse-in="1200">
        <app-help-popper
          :title="$t('help.MESSAGES-TAB-TT')"
          :details="$t('help.MESSAGES-TAB-DET')">
        </app-help-popper>

        <div slot="reference" @click="$emit('messagesContent')" class="w3-left control-btn" :class="{'control-btn-selected': isMessagesContent}">
          <img src="./../../assets/chat-icon.svg" alt="">
        </div>
      </popper>

      <popper trigger="hover" :options="popperOptions" delay-on-mouse-in="1200">
        <app-help-popper
          :title="$t('help.CARDS-SUMMARY-TAB-TT')"
          :details="$t('help.CARDS-SUMMARY-TAB-DET')">
        </app-help-popper>

        <div slot="reference" @click="$emit('summaryView')" class="w3-left control-btn" :class="{'control-btn-selected': isSummary}">
          <img src="./../../assets/rows-icon.svg" alt="">
        </div>
      </popper>

      <popper trigger="hover" :options="popperOptions" delay-on-mouse-in="1200">
        <app-help-popper
          :title="$t('help.CARDS-TAB-TT')"
          :details="$t('help.CARDS-TAB-DET')">
        </app-help-popper>

        <div slot="reference" @click="$emit('cardView')" class="w3-left control-btn" :class="{'control-btn-selected': isCard}">
          <img src="./../../assets/cards-icon.svg" alt="">
        </div>
      </popper>

      <popper trigger="hover" :options="popperOptions" delay-on-mouse-in="1200">
        <app-help-popper
          :title="$t('help.CARDS-DOC-VIEW-TT')"
          :details="$t('help.CARDS-DOC-VIEW-DET')">
        </app-help-popper>

        <div slot="reference" @click="$emit('docView')" class="w3-left control-btn" :class="{'control-btn-selected': isDoc}">
          <img src="./../../assets/doc-icon.svg" alt="">
        </div>
      </popper>

    </div>

    <div v-if="isCardsContent" class="control-group noselect">
      <div class="w3-left zoom-controls">
        <div class="w3-left zoom-controls-enabled">
          <popper trigger="hover":options="popperOptions" delay-on-mouse-in="1200">
            <app-help-popper
              :title="$t('help.REDUCE-LEVELS-TT')"
              :details="$t('help.REDUCE-LEVELS-DET')">
            </app-help-popper>

            <div slot="reference" @click="$emit('levelDown')" class="w3-left cursor-pointer arrow-div">
              <img src="./../../assets/zoom-in-icon.svg" alt="">
            </div>
          </popper>

          <div class="w3-left number-div">
            {{ levels !== 999 ? levels : '&#x221e;' }}
          </div>

          <popper trigger="hover":options="popperOptions" delay-on-mouse-in="1200">
            <app-help-popper
              :title="$t('help.INCREASE-LEVELS-TT')"
              :details="$t('help.INCREASE-LEVELS-DET')">
            </app-help-popper>

            <div slot="reference" @click="$emit('levelUp')" class="w3-left cursor-pointer arrow-div">
              <img src="./../../assets/zoom-out-icon.svg" alt="">
            </div>
          </popper>

          <!-- <div v-if="infiniteLevels" class="zoom-controls-cover">
          </div> -->
        </div>

        <popper trigger="hover":options="popperOptions" delay-on-mouse-in="1200">
          <app-help-popper
            :title="$t('help.SEE-ALL-LEVELS-TT')"
            :details="$t('help.SEE-ALL-LEVELS-DET')">
          </app-help-popper>

          <div slot="reference" @click="$emit('toggleInifinteLevels')" class="w3-left cursor-pointer arrow-div w3-border-left" :class="{'control-btn-selected': infiniteLevels}">
            <img src="./../../assets/infinite-icon.svg" alt="">
          </div>
        </popper>

      </div>
    </div>

    <div v-if="isCardsContent && this.$store.state.user.authenticated" class="control-group">
      <div class="">
        <popper trigger="hover":options="popperOptions" delay-on-mouse-in="1200">
          <app-help-popper
            :title="(showPrivate ? $t('general.HIDE') : $t('general.SHOW')) + ' ' + $t('help.PRIVATE-CARDS-TT')"
            :details="$t('help.PRIVATE-CARDS-DET')">
          </app-help-popper>

          <div slot="reference" @click="$emit('showPrivateClick')" class="w3-left control-btn border-red" :class="{'control-btn-selected': showPrivate, 'w3-bottombar': showPrivate}">
            <img src="./../../assets/private-icon.svg" alt="">
          </div>
        </popper>

        <popper trigger="hover":options="popperOptions" delay-on-mouse-in="1200">
          <app-help-popper
            :title="(showShared ? $t('general.HIDE') : $t('general.SHOW')) + ' ' + $t('help.SHARED-CARDS-TT')"
            :details="$t('help.SHARED-CARDS-DET')">
          </app-help-popper>

          <div slot="reference" @click="$emit('showSharedClick')" class="w3-left control-btn border-yellow" :class="{'control-btn-selected': showShared, 'w3-bottombar': showShared}">
            <img src="./../../assets/shared-icon.svg" alt="">
          </div>
        </popper>

        <popper trigger="hover":options="popperOptions" delay-on-mouse-in="1200">
          <app-help-popper
            :title="(showCommon ? $t('general.HIDE') : $t('general.SHOW')) + ' ' + $t('help.COMMON-CARDS-TT')"
            :details="$t('help.COMMON-CARDS-DET')">
          </app-help-popper>

          <div slot="reference" @click="$emit('showCommonClick')" class="w3-left control-btn border-blue" :class="{'control-btn-selected': showCommon, 'w3-bottombar': showCommon}">
            <img src="./../../assets/common-icon.svg" alt="">
          </div>
        </popper>

      </div>
    </div>

    <div v-if="isCardsContent" class="control-group">
      <div class="">
        <popper trigger="hover":options="popperOptions" delay-on-mouse-in="1200">
          <app-help-popper
            :title="$t('help.SHOW-SECTION-ORDER-TT')"
            :details="$t('help.SHOW-SECTION-ORDER-DET')">
          </app-help-popper>

          <div slot="reference"  @click="$emit('sectionOrder')" class="w3-left control-btn" :class="{'control-btn-selected': isSectionsOrder}">
            <img src="./../../assets/network-icon.svg" alt="">
          </div>
        </popper>

        <popper trigger="hover":options="popperOptions" delay-on-mouse-in="1200">
          <app-help-popper
            :title="$t('help.SEARCH-CARDS-TT')"
            :details="$t('help.SEARCH-CARDS-DET')">
          </app-help-popper>

          <div slot="reference" @click="$emit('aggregatedOrder')" class="w3-left control-btn" :class="{'control-btn-selected': !isSectionsOrder}">
            <img src="./../../assets/search-icon.svg" alt="">
          </div>
        </popper>

      </div>
    </div>

    <div v-if="!isCardsContent" class="control-group" delay-on-mouse-in="1200">
      <popper trigger="hover":options="popperOptions">
        <app-help-popper
          :title="(showMessages ? $t('general.HIDE') : $t('general.SHOW')) + ' ' + $t('help.SHOW-MESSAGES-TT')"
          :details="$t('help.SHOW-MESSAGES-DET')">
        </app-help-popper>

        <div slot="reference" @click="$emit('showMessages')" class="w3-left control-btn" :class="{'control-btn-selected': showMessages}">
          <img src="./../../assets/chat-icon-2.svg" alt="">
        </div>
      </popper>

      <popper trigger="hover":options="popperOptions" delay-on-mouse-in="1200">
        <app-help-popper
        :title="(showEvents ? $t('general.HIDE') : $t('general.SHOW')) + ' ' + $t('help.SHOW-EVENTS-TT')"
        :details="$t('help.SHOW-EVENTS-DET')">
        </app-help-popper>

        <div slot="reference" @click="$emit('showEvents')" class="w3-left control-btn" :class="{'control-btn-selected': showEvents}">
          <img src="./../../assets/all-events-icon.svg" alt="">
        </div>
      </popper>

    </div>

    <div class="control-group slider-container">
      <transition name="slideRightLeft">
        <div v-if="isCardsContent && !isSectionsOrder" class="">
          <div class="">
            <div class="">
              <input ref="inputQuery" @input="$emit('searchQueryUpdated', $event.target.value)" class="w3-input"
                type="text" name="" value="" placeholder="search">
            </div>
            <div @click="$emit('updateQuery')" class="control-btn control-btn-selected">
              <i class="fa fa-refresh" aria-hidden="true"></i>
            </div>
          </div>

          <div class="w3-margin-left">
            <select @input="$emit('cardsSortByUpdated', $event.target.value)" class="w3-input">
              <option value="CREATION_DATE_DESC">Last Created</option>
              <option value="EDITION_DATE_DESC">Last Edited</option>
              <option value="CREATOR">Author</option>
            </select>
          </div>
        </div>
      </transition>
    </div>

    <div v-if="isCardsContent && isSectionsOrder" class="control-group">
      <div class="">

        <popper trigger="hover":options="popperOptions" delay-on-mouse-in="1200">
          <app-help-popper
            :title="$t('help.DOWNLOAD-CONTENT-TT')"
            :details="$t('help.DOWNLOAD-CONTENT-DET')">
          </app-help-popper>

          <div slot="reference" @click="$emit('downloadContent')" class="w3-left control-btn">
            <img src="./../../assets/download-icon.svg" alt="">
          </div>
        </popper>

        <popper trigger="hover":options="popperOptions" delay-on-mouse-in="1200">
          <app-help-popper
            :title="(isDraggable ? $t('general.DISABLE') : $t('general.ENABLE')) + ' ' + $t('help.ENABLE-DRAG-AND-DROP-TT')"
            :details="$t('help.ENABLE-DRAG-AND-DROP-CARDS-DET')">
          </app-help-popper>

          <div slot="reference" v-if="isLoggedAnEditor" @click="$emit('enableDraggable')" class="w3-left control-btn" :class="{'control-btn-selected': isDraggable}">
            <img src="./../../assets/move-icon.svg" alt="">
          </div>
        </popper>

      </div>
    </div>

    <div v-if="false" class="control-group text-details">
      <span v-if="isCardsContent">
        <span v-if="!isSectionsOrder">
          you are
          <span v-if="cardQuery !== ''" class="">
            searching by
            <span class="cursor-pointer" @click="$emit('resetQuery')">
              <i><b>"{{ cardQuery }}"</b></i> <i class="fa fa-times-circle" aria-hidden="true"></i>
            </span>
          </span>
          <span v-else class="">
            searching all cards
          </span>
          under the
          <br>"{{ sectionTitle }}" section.
        </span>
        <span v-else>
          you are seeing all cards up to {{ levels }} levels under the <br>"{{ sectionTitle }}" section and respecting their in-section order.
        </span>
      </span>
      <span v-else>
        you are seeing
        <span v-if="isOnlyMessages">all messages</span>
        <span v-else>all events</span>
        up to {{ levels }} levels under the <br>"{{ sectionTitle }}" section.
      </span>

    </div>
  </div>
</template>

<script>
import { sectionElementsMixin } from '@/components/model/SectionElementsMixin.js'

export default {

  mixins: [sectionElementsMixin],

  props: {
    orderType: {
      type: String,
      default: 'sections'
    },
    showEvents: {
      type: Boolean,
      default: true
    },
    showMessages: {
      type: Boolean,
      default: true
    }
  },

  computed: {
  }
}

</script>

<style scoped>

.controls-row {
  margin: 12px 12px 6px 24px;
  min-height: 40px;
  flex-shrink: 0;
  white-space: nowrap;
  overflow-x: auto;
}


.control-group {
  margin-right: 20px;
  margin-bottom: 3px;
  display: inline-block;
  vertical-align: top;
}

.control-group div {
  display: inline-block;
}

.control-btn {
  margin-right: 4px;
}

.zoom-controls {
  min-height: 1px;
  width: 160px;
  text-align: center;
  color: #3e464e;
  background-color: #eff3f6;
  border-radius: 3px;
  transition: all 300ms ease;
}

.zoom-controls .arrow-div {
  width: 40px;
  height: 38px;
}

.zoom-controls .arrow-div img {
  margin-top: 12px;
  width: 14px;
}

.zoom-controls .arrow-div:hover {
  background-color: #ced4d9;
}

.zoom-controls .number-div {
  width: 40px;
  height: 38px;
  font-size: 19px;
  padding: 5px 12px;
}

.zoom-controls-enabled {
}

.zoom-controls .zoom-controls-cover {
  position: absolute;
  top:0;
  left:0;
  width:100%;
  height:100%;
  background-color: rgba(71, 71, 71, 0.8);
  border-radius: 3px;
}

.text-details {
  font-size: 12px;
}

.section-cards-container {
  margin-top: 15px;
}

</style>
