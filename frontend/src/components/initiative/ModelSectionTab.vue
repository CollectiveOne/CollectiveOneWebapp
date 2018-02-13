<template lang="html">

<div class="">

  <div class="slider-container">
    <transition name="slideDownUp">
      <app-model-view-modal
        v-if="showViewModal"
        :isNew="true"
        :initiativeId="initiative.id"
        @close="showViewModal = false">
      </app-model-view-modal>
    </transition>
  </div>

  <div class="w3-container">

    <div class="w3-row w3-margin-top">
      <div v-if="views" class="view-selector">
        <div v-for="view in views"
          class="w3-left w3-margin-right app-margin-bottom"
          draggable="true"
          @dragstart="viewDragStart($event, view.id)"
          @dragover.prevent
          @drop.prevent="viewDragDroped(view.id, $event)">

          <app-view-button :view="view" :selected="isViewSelected(view.id)">
          </app-view-button>
        </div>
        <div v-if="isLoggedAnEditor" @click="showViewModal = true" class="new-view-button w3-large w3-button w3-left app-button-color">
          <i class="fa fa-plus-circle" aria-hidden="true"></i>
        </div>
      </div>
      <div class="w3-right">
        <router-link
          :to="{ name: 'ModelSearch' }"
          tag="button"
          class="w3-button"
          :class="{'app-button-light': !isSearch, 'app-button': isSearch}"
          type="button" name="button">search
        </router-link>
      </div>
    </div>

    <div class="">
      <div class="w3-row w3-margin-top router-container">
        <transition name="fadeenter">
          <router-view>
          </router-view>
        </transition>
      </div>
    </div>

  </div>

</div>
</template>

<script>
import ModelViewModal from '@/components/model/modals/ModelViewModal.vue'
import ModelCardModal from '@/components/model/modals/ModelCardModal.vue'
import ViewButton from '@/components/model/ViewButton.vue'

export default {
  components: {
    'app-model-view-modal': ModelViewModal,
    'app-model-card-modal': ModelCardModal,
    'app-view-button': ViewButton
  },

  data () {
    return {
      showViewModal: false
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    views () {
      if (this.$store.state.initiative.initiativeModelViews) {
        return this.$store.state.initiative.initiativeModelViews.views
      } else {
        return []
      }
    },
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    searching () {
      return this.query !== ''
    },
    isSearch () {
      return this.$route.name === 'ModelSearch'
    }
  },

  methods: {
    isViewSelected (viewId) {
      if (this.$route.params.viewId) {
        if (this.$route.params.viewId === viewId) {
          return true
        }
      }
    },
    viewDragStart (event, viewId) {
      var moveViewData = {
        type: 'MOVE_VIEW',
        viewId: viewId
      }
      event.dataTransfer.setData('text/plain', JSON.stringify(moveViewData))
    },
    viewDragDroped (onViewId, event) {
      var dragData = JSON.parse(event.dataTransfer.getData('text/plain'))
      this.axios.put('/1/initiative/' + this.initiative.id +
        '/model/moveView/' + dragData.viewId, {}, {
          params: {
            onViewId: onViewId
          }
        }).then((response) => {
          this.$store.dispatch('refreshModelViews')
        })
    }
  },

  created () {
    if (!this.initiative.meta.modelEnabled) {
      /* redirect to overview if model is not enabled */
      this.$router.replace({ name: 'InitiativeOverview', params: { initiativeId: this.initiative.id } })
    } else {
      /* redirect only if no view or section or card is selected as sub-route */
      var _redirect = this.$route.matched.length === 4
      this.$store.dispatch('refreshModelViews', { router: this.$router, redirect: _redirect })
    }
  }
}
</script>

<style scoped>

.new-view-button {
  padding-top: 5px !important;
  padding-bottom: 5px !important;
  border-radius: 12px;
}

.router-container {
  font-family: 'Open Sans', sans-serif !important;
}

.router-container h1 {
  font-size: 32px;
}

.router-container {
  padding-bottom: 50px;
}

.router-container .model-button {
  background-color: #eff3f6;
  padding-top: 2px !important;
  padding-bottom: 3px !important;
}

</style>
