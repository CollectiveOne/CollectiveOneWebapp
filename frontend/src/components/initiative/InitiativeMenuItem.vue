<template lang="html">
  <div :class="topClass" class="d2-color">
    <div class="w3-row this-element" :class="{'selected': isSelected, 'w3-theme-l3': isSelected, 'd2-color': isSelected}">
      <div v-if="this.level > 0" class="space-col" :class="leftSpaceClass">
        x
      </div>
      <div class="w3-col s2 d2-color" :class="{'w3-button': hasSubinitiatives, 'w3-padding': !hasSubinitiatives}"
        @click="showSubinitiatives = !showSubinitiatives">

        <div v-if="hasSubinitiatives">
          <i v-if="!showSubinitiatives"class="fa fa-chevron-circle-right" aria-hidden="true"></i>
          <i v-else class="fa fa-chevron-circle-down" aria-hidden="true"></i>
        </div>
        <div v-else>
          <i class="fa fa-minus" aria-hidden="true"></i>
        </div>
      </div>
      <div class="w3-col name-col" :class="nameSpaceClass" :style="nameColFontSize" @click="$emit('initiative-clicked')">
        <router-link :to="'/inits/'+initiative.id+'/overview'" class="w3-left">
          {{ initiative.name }}
        </router-link>
      </div>
      <div v-if="false" class="w3-col s2 d2-color w3-button" @click="$emit('new-subinitiative', initiative.id)">
        <i class="fa fa-plus l1-color" aria-hidden="true"></i>
      </div>
    </div>
    <div class="w3-row" v-if="showSubinitiatives" v-for="subinitiative in initiative.subInitiatives">
      <app-initiative-menu-item
        class="sub-initiative-element" :initiative="subinitiative" :key="subinitiative.id"
        :level="level + 1"
        @initiative-clicked="$emit('initiative-clicked')"
        @new-subinitiative="$emit('new-subinitiative', $event)">
      </app-initiative-menu-item>
    </div>
  </div>
</template>

<script>
export default {
  name: 'app-initiative-menu-item',

  props: {
    initiative: {
      type: Object,
      default: () => {
        return {
          name: '',
          driver: '',
          subInitiatives: []
        }
      }
    },
    level: {
      type: Number
    }
  },

  computed: {
    isSelected () {
      return this.initiative.id === this.$route.params.initiativeId
    },
    topClass () {
      if (this.level === 0) {
        return {
          'w3-card-2': true
        }
      } else {
        return {}
      }
    },
    leftSpaceClass () {
      if (this.level > 0) {
        var name = 's' + (this.level < 5 ? this.level : 5)
        var classObject = {}
        classObject[name] = true
        classObject['w3-col'] = true
        return classObject
      } else {
        return {}
      }
    },
    nameSpaceClass () {
      var leftSpace = this.level < 5 ? this.level : 5
      var name = 's' + (8 - leftSpace)
      var classObject = {}
      classObject[name] = true
      return classObject
    },
    nameColFontSize () {
      var fontsize = this.level < 5 ? 16 - this.level : 11
      return {'font-size': fontsize + 'px'}
    },
    hasSubinitiatives () {
      return this.initiative.subInitiatives.length > 0
    }
  },

  data () {
    return {
      showSubinitiatives: false
    }
  }
}
</script>

<style scoped>

.space-col {
  visibility: hidden;
}

.name-col {
  padding-left: 5px;
  padding-right: 5px;
  padding-top: 8px;
}

.this-element {
  padding-bottom: 5px;
}

.selected {
  font-weight: bold;
}

</style>
