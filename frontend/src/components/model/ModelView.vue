<template lang="html">
  <div v-if="view" class="model-view-container">
    <div class="view-title"
      @mouseover="showActionButton = true"
      @mouseleave="showActionButton = false">

      <transition name="fadeenter">
        <div v-if="showActionButton" class="w3-col w3-right buttons-container gray-1-color" style="width:100px">
          <div @click="expandViewModal()" class="w3-button model-action-button w3-right">
            <i class="fa fa-expand" aria-hidden="true"></i>
          </div>
          <div
            v-if="isLoggedAnEditor"
            @click="newSection()"
            class="w3-button model-action-button w3-right">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
          </div>
        </div>
      </transition>

      <div class="w3-rest">
        <h1 class="">{{ view.title }}</h1>
      </div>
    </div>

    <div v-for="section in view.sections"
      :key="section.id"
      class="view-sections"
      @dragover.prevent
      @drop="sectionDroped(section.id, $event)">

      <app-model-section
        :preloaded="false"
        :sectionId="section.id"
        :viewId="view.id"
        :sectionInit="section"
        :initiativeId="initiativeId"
        :level="0"
        dragType="MOVE_SECTION"
        @show-section-modal="$emit('show-section-modal', $event)"
        @show-card-modal="$emit('show-card-modal', $event)">
      </app-model-section>
    </div>

    <div class="view-sections gray-1-color w3-border">
      <button class="w3-button" style="width: 100%; text-align: left"
        @click="newSection()">
        <i class="fa fa-plus w3-margin-right" aria-hidden="true"></i>
        add section
      </button>
    </div>
  </div>
</template>

<script>
import ModelSection from '@/components/model/ModelSection.vue'
export default {
  name: 'app-model-view',

  components: {
    'app-model-section': ModelSection
  },

  data () {
    return {
      view: null,
      showActionButton: false
    }
  },

  watch: {
    '$store.state.support.triggerUpdateModel' () {
      this.update()
    }
  },

  props: {
    initiativeId: {
      type: String,
      default: ''
    },
    viewId: {
      type: String,
      default: ''
    }
  },

  computed: {
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    }
  },

  methods: {
    update () {
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/view/' + this.viewId, {
        params: {
          level: 1
        }
      }).then((response) => {
        this.view = response.data.data
      })
    },
    expandViewModal () {
      this.$emit('show-view-modal', {
        new: false,
        viewId: this.view.id,
        initiativeId: this.view.initiativeId
      })
    },
    newSection () {
      this.$emit('show-section-modal', {
        new: true,
        initiativeId: this.view.initiativeId,
        isSubsection: false,
        viewId: this.view.id,
        viewTitle: this.view.title
      })
    },
    sectionDroped (onSectionId, event) {
      var dragData = JSON.parse(event.dataTransfer.getData('text/plain'))

      console.log('sectoin dropped no test')

      if (dragData.type === 'MOVE_SECTION') {
        console.log('section dropped')

        var url = '/1/initiative/' + this.initiativeId +
        '/model/view/' + this.view.id +
        '/moveSection/' + dragData.sectionId

        this.axios.put(url, {}, {
          params: {
            onSectionId: onSectionId
          }
        }).then((response) => {
          this.$store.commit('triggerUpdateModel')
        })
      }
    }
  },

  mounted () {
    this.update()
  }
}
</script>

<style scoped>

.view-title .buttons-container {
  padding-top: 15px;
}

.view-sections {
  margin-bottom: 30px;
}

</style>
