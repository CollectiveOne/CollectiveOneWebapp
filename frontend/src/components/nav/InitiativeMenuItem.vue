<template lang="html">
  <div class="white-text">
    <div class="w3-row this-element" :class="{'selected': isSelected, 'dark-gray-selected': isSelected }">
      <div v-if="this.level > 0" class="space-col" :class="leftSpaceClass">
        x
      </div>
      <div class="w3-col s2" :class="bulletClass"
        @click="showSubinitiatives = !showSubinitiatives">

        <div v-if="hasSubinitiatives">
          <i v-if="!showSubinitiatives"class="fa fa-chevron-circle-right" aria-hidden="true" :style="{'color': color}"></i>
          <i v-else class="fa fa-chevron-circle-down" aria-hidden="true" :style="{'color': color}"></i>
        </div>
        <div v-else>
          <i class="fa fa-circle" aria-hidden="true" :style="{'color': color}"></i>
        </div>
      </div>
      <div class=""  @click="$emit('initiative-selected')">
        <router-link :to="{ name: 'Initiative', params: {'initiativeId': initiative.id } }"
          class="w3-col name-col" :class="nameSpaceClass" :style="nameColFontSize">
          <div class="w3-left name-link noselect">
            {{ initiative.meta.name }}
          </div>
        </router-link>
      </div>

      <div v-if="isLoggedAnAdmin()" class="w3-col s2 w3-button" @click="newSubInitiative()">
        <i class="fa fa-plus gray-2-color" aria-hidden="true"></i>
      </div>
    </div>

    <div class="slider-container">
      <transition name="slideDownUp">
        <div class="sub-initiatives-container" v-if="showSubinitiatives" >
          <div class="w3-row" v-for="(subinitiative, ix) in initiative.subInitiatives">
            <app-initiative-menu-item
              class="sub-initiative-element" :initiative="subinitiative" :key="subinitiative.id"
              :coord="coord.concat([ix])"
              @initiative-selected="$emit('initiative-selected')">
            </app-initiative-menu-item>
          </div>
        </div>
      </transition>
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
    coord: {
      type: Array,
      default: () => { return [] }
    }
  },

  computed: {
    color () {
      return this.initiative.meta.color
    },
    level () {
      return this.coord.length - 1
    },
    isSelected () {
      return this.initiative.id === this.$route.params.initiativeId
    },
    bulletClass () {
      if (this.hasSubinitiatives) {
        return {
          'bullet-class': true,
          'w3-button': true
        }
      } else {
        return {
          'bullet-class': true,
          'w3-padding': true,
          'l3-color': true,
          'line-element': true
        }
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
  },

  methods: {
    newSubInitiative () {
      this.$store.commit('showNewSubInitiativeModal', {
        show: true,
        parentId: this.initiative.id })
    },
    isLoggedAnAdmin () {
      if (this.initiative.loggedMember) {
        return this.initiative.loggedMember.role === 'ADMIN'
      } else {
        return false
      }
    }
  },

  mounted () {
    var thisCoord = this.$store.getters.initiativeCoordinate(this.initiative.id)
    var pageCoord = this.$store.getters.initiativeCoordinate(this.$route.params.initiativeId)

    if (pageCoord.length > 0 && thisCoord.length > 0) {
      if (pageCoord[this.level] === thisCoord[this.level]) {
        this.showSubinitiatives = true
      }
    }
  }
}
</script>

<style scoped>

.space-col {
  visibility: hidden;
}

.bullet-class {
  height: 100%;
}

.name-col {
  padding-left: 5px;
  padding-right: 5px;
  padding-top: 8px;
  padding-bottom: 5px;
}

.name-link {
  width: 100%;
  height: 100%;
  cursor: pointer;
}

.line-element {
  text-align: center;
}

.selected {
  font-weight: bold;
}

</style>
