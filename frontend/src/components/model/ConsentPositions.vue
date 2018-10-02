<template lang="html">
  <div class="w3-row positions-container">

    <popper trigger="click" :options="popperOptions" :toggleShow="toggleShowPopper" :disabled="disabled" class="w3-left">
      <div class="position-selector w3-card w3-white">
        <div class="w3-row">
          {{ $t('model.MY_POSITION_ON_CARD') }}:
        </div>
        <div class="w3-row w3-margin-top">
          <div class="w3-left position-tag position-tag-green tag-margin"
            @click="setOwnPosition('GREEN')">
            {{ $t('model.SUPPORT') }}
          </div>
          <div class="w3-left position-tag position-tag-yellow tag-margin"
            @click="setOwnPosition('YELLOW')">
            {{ $t('model.UNDECIDED') }}
          </div>
          <div class="w3-left position-tag position-tag-red"
            @click="setOwnPosition('RED')">
            {{ $t('model.OBJECT') }}
          </div>
        </div>
      </div>

      <div slot="reference" class="cursor-pointer avatar-position" :class="getBorderClass(ownPosition)">
        <app-user-avatar
          :user="$store.state.user.profile"
          :showName="false" :small="true"
          :enableHover="false">
        </app-user-avatar>
      </div>

    </popper>

    <div v-if="othersPositions.length > 0" class="vertical-separator w3-left">

    </div>

    <div v-for="position in othersPositions"
      :key="position.id"
      class="w3-left avatar-position"
      :class="getBorderClass(position.positionColor)">
      <app-user-avatar
        :user="position.author"
        :showName="false" :small="true">
      </app-user-avatar>
    </div>

    <div v-if="disabled" class="cover-div cover-div-closed">
      <b>{{ $t('model.CLOSED') }}</b>
    </div>
    <div v-else class="cover-div cover-div-open">
      <b>{{ $t('model.OPEN') }}</b>
    </div>

  </div>
</template>

<script>
export default {
  props: {
    elementId: {
      type: String
    },
    ownPosition: {
      type: String,
      default: null
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      positions: [],
      toggleShowPopper: false
    }
  },

  computed: {
    hasOwnPosition () {
      return this.ownPosition != null
    },
    othersPositions () {
      return this.positions.filter((position) => {
        return position.author.c1Id !== this.$store.state.user.profile.c1Id
      })
    },
    popperOptions () {
      return {
        placement: 'right',
        modifiers: {
          preventOverflow: {
            enabled: true,
            boundariesElement: 'viewport'
          }
        }
      }
    }
  },

  methods: {
    getBorderClass (position) {
      switch (position) {
        case 'GREEN':
          return {'avatar-border-green': true}

        case 'YELLOW':
          return {'avatar-border-yellow': true}

        case 'RED':
          return {'avatar-border-red': true}
      }
    },
    updatePositions () {
      this.axios.get('/1/model/cardAddition/' + this.elementId + '/consentPositions', {
        params: {
          elementType: 'CARD_WRAPPER_ADDITION'
        }
      }).then((response) => {
        this.positions = response.data.data
      })
    },
    setOwnPosition (value) {
      this.toggleShowPopper = !this.toggleShowPopper
      this.axios.put('/1/model/cardAddition/' + this.elementId + '/consentPosition', {}, {
        params: {
          consentPosition: value,
          elementType: 'CARD_WRAPPER_ADDITION'
        }
      }).then((response) => {
        this.$emit('update')
      })
    }
  },

  mounted () {
    this.updatePositions()
  }
}
</script>

<style scoped>

.positions-container {
  padding: 9px 3px 0px 3px;
  position: relative;
}

.cover-div {
  position: absolute;
  top: 0px;
  right: 0px;
  font-size: 12px;
  text-align: right;
  padding: 3px 6px;
}

.cover-div-closed {
  height: 100%;
  width: 100%;
  background-color: rgba(100, 100, 100, 0.3);
}

.own-avatar-container {
  position: relative;
}

.position-selector {
  width: 410px;
  padding: 8px 16px 16px 16px;
  z-index: 10;
}

.position-tag {
  border: 3px solid;
  padding: 3px 6px;
  width: 120px;
  text-align: center;
  border-radius: 6px;
  cursor: pointer;
  transition: all 200ms ease;
}

.tag-margin {
  margin-right: 6px;
}

.vertical-separator {
  height: 34px;
  width: 3px;
  border-left-style: solid;
  border-width: 1px;
  margin: 0px 6px;
  border-color: #cbcfd2;
}

.position-tag-green {
  border-color: #40a36d;
  color: #40a36d;
}

.avatar-border-green {
  border-color: #40a36d;
}

.position-tag-green:hover {
  color: white;
  background-color: #40a36d;
}

.position-tag-yellow {
  border-color: #c8bf42;
  color: #c8bf42;
}

.avatar-border-yellow {
  border-color: #c8bf42;
}

.position-tag-yellow:hover {
  color: white;
  background-color: #c8bf42;
}

.position-tag-red {
  border-color: #c84242;
  color: #c84242;
}

.avatar-border-red {
  border-color: #c84242;
}

.position-tag-red:hover {
  color: white;
  background-color: #c84242;
}

.avatars-container {
  width: 100%;
}

.avatars-container-red {
  text-align: left;
}

.avatars-container-yellow {
  text-align: center;
}

.avatars-container-green {
  text-align: right;
}

.avatar-position {
  height: 33px;
  width: 33px;
  border-radius: 16px;
  border-width: 3px;
  display: inline-block;
  text-align: center;
  margin: 1px 1px;
  overflow: hidden;
  border-style: solid;
}

.border-transparent {
  border-color: transparent;
}

.w3-col {
  min-height: 1px;
}

</style>
