<template lang="html">
  <div class="w3-row section-nav-item-container">

    <div class="w3-row section-nav-item-first-row">

      <div @click="toggleSubsections()" class="circle-div cursor-pointer">
        <div v-if="hasSubsections">
          <i v-if="!showSubsections" class="fa fa-chevron-circle-right" aria-hidden="true"></i>
          <i v-else class="fa fa-chevron-circle-down" aria-hidden="true"></i>
        </div>
        <div v-else>
          <i @click="sectionSelected()" class="fa fa-circle" aria-hidden="true"></i>
        </div>
      </div>
      <div @click="sectionSelected()" @dblclick="toggleSubsections()" class="title-div cursor-pointer noselect">
        {{ sectionTitle }}
      </div>
    </div>

    <div :class="{'slider-container': animating}">
      <transition name="slideDownUp"
        @before-enter="animating = true"
        @after-enter="animating = false"
        @before-leave="animating = true"
        @after-leave="animating = false">

        <div v-if="showSubsections && subsections.length > 0" class="w3-row subsections-container">
          <app-model-section-nav-item-simple
            v-for="(subsection, ix) in subsections"
            :section="subsection" :key="subsection.id"
            class="subsection-row"
            @section-selected="$emit('section-selected', $event)">
          </app-model-section-nav-item-simple>
        </div>
      </transition>
    </div>
  </div>

</template>

<script>

export default {
  name: 'app-model-section-nav-item-simple',

  components: {
  },

  props: {
    section: {
      type: Object,
      default: null
    }
  },

  data () {
    return {
      showSubsections: true,
      animating: false
    }
  },

  watch: {
  },

  computed: {
    subsections () {
      return this.section != null ? this.section.subsections : []
    },
    highlight () {
      return this.highlightLevelUse > 0
    },
    sectionTitle () {
      if (this.section) {
        return this.section.title
      }
      return ''
    },
    hasSubsections () {
      if (this.section) {
        return this.section.nSubsections > 0
      }
      return false
    }
  },

  methods: {
    toggleSubsections () {
      this.showSubsections = !this.showSubsections
    },
    sectionSelected () {
      this.$emit('section-selected', this.section)
    }
  }

}
</script>

<style scoped>

.section-nav-item-container {
  color: white;
}

.section-nav-item-first-row {
  padding: 3px 0px;
}

.subsections-container {
  padding-left: 20px;
}

.subsection-container-selected {
  background-color: #313942;
}

.subsection-row {
}

.section-selected {
  background-color: #313942;
  transition: all 300ms ease;
  color: white;
  border-color: #3e464e;
}

.circle-div {
  width: 30px;
  float: left;
  text-align: center;
  padding: 3px 0px;
  transition: all 300ms ease;
}

.circle-div:hover {
  color: #106e87;
}

.title-div {
  width: calc(100% - 30px - 30px - 30px);
  float: left;
  padding: 3px 0px;
  transition: all 300ms ease;
}

.title-div:hover {
  color: #106e87;
}


</style>
