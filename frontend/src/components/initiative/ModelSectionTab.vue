<template lang="html">

<div class="">

  <div class="w3-cell-row">
    <div class="vision-nav light-grey w3-cell">

    </div>
    <div class="vision-content w3-cell w3-container">
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
import ModelViewButton from '@/components/model/ModelViewButton.vue'

export default {
  components: {
    'app-model-view-modal': ModelViewModal,
    'app-model-card-modal': ModelCardModal,
    'app-model-view-button': ModelViewButton
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

.vision-nav {
}

.router-container {
  font-family: 'Open Sans', sans-serif !important;
}

.router-container h1 {
  font-size: 32px;
}

.router-container {
  padding-bottom: 20px;
}

.router-container .model-button {
  background-color: #eff3f6;
  padding-top: 2px !important;
  padding-bottom: 3px !important;
}

</style>
