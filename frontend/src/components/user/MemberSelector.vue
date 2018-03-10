<template>
  <div class="w3-row-padding autocomplete-wrapper">
    <div v-if="selected !== null" class="w3-col s2">
      <div class="w3-cell w3-cell-middle" style="width:30px">
        <img class="" style="width:35px" :src="selected.pictureUrl"/>
      </div>
    </div>
    <div class="w3-col s10">
      <input  type="text"
              id="T_selectMemberTransferModal" 
              class="w3-input w3-hover-light-grey autocomplete-inputs"
              placeholder="select member"
              v-model="type"
              @input="input(type)"
              @dblclick="showAll"
              @blur="hideAll"
              @keydown="keydown"
              @focus="focus"
              autocomplete="off" />
    </div>

    <div class="w3-row autocomplete transition autocomplete-list" v-show="showList">
      <ul>
        <li v-for="(data, i) in json"
            transition="showAll"
            :class="activeClass(i)">

          <a  href="#"
              @click.prevent="selectList(data)"
              @mousemove="mousemove(i)">
            <app-user-avatar :user="data"></app-user-avatar>
          </a>

        </li>
      </ul>
    </div>
  </div>
</template>


<script>
import UserAvatar from './UserAvatar.vue'

export default {
  name: 'member-selector',

  components: {
    'app-user-avatar': UserAvatar
  },

  props: {
    members: Array,

    resetAfterSelect: {
      type: Boolean,
      default: false
    },

    // Intial Value
    init: {
      type: Object,
      default: () => { return null }
    },

    // Anchor of list
    anchor: {
      type: String,
      default: 'c1Id'
    },

    // Callback
    onInput: Function,
    onShow: Function,
    onBlur: Function,
    onHide: Function,
    onFocus: Function,
    onBeforeRequest: Function,
    onRequestLoaded: Function
  },

  data () {
    return {
      showList: false,
      type: '',
      focusList: '',
      label: 'nickname'
    }
  },

  computed: {
    json () {
      return this.members
    },
    selected () {
      if (this.type !== '') {
        if ((this.json.length > 0)) {
          return this.json[this.focusList]
        }
      }

      return null
    }
  },

  methods: {

    // Netralize Autocomplete
    clearInput () {
      this.showList = false
      this.type = ''
      this.focusList = ''
    },

    // Get the original data
    cleanUp (data) {
      return JSON.parse(JSON.stringify(data))
    },

    input (val) {
      if (this.json.length > 0) {
        this.showList = true
      } else {
        this.hideAll()
      }

      // Callback Event
      this.onInput ? this.onInput(val) : null
    },

    showAll () {
      // Callback Event
      this.onShow ? this.onShow() : null
      this.showList = true
    },

    hideAll (e) {
      // Callback Event
      this.onBlur ? this.onBlur(e) : null

      setTimeout(() => {
        // Callback Event
        this.onHide ? this.onHide() : null

        this.showList = false
      }, 250)
    },

    focus (e) {
      this.focusList = 0

      // Callback Event
      this.onFocus ? this.onFocus(e) : null

      // Show when seleceted
      this.showAll()
    },

    mousemove (i) {
      this.focusList = i
    },

    keydown (e) {
      let key = e.keyCode
      // Disable when list isn't showing up
      if (!this.showList) return

      switch (key) {
        case 40: // down
          this.focusList++
          break
        case 38: // up
          this.focusList--
          break
        case 13: // enter
          this.selectList(this.json[this.focusList])
          this.showList = false
          break
        case 27: // esc
          this.showList = false
          break
      }

      // When cursor out of range
      let listLength = this.json.length - 1
      this.focusList = this.focusList > listLength ? 0 : this.focusList < 0 ? listLength : this.focusList
    },

    activeClass (i) {
      return {
        'focus-list': i === this.focusList
      }
    },

    selectList (data) {
      let clean = this.cleanUp(data)
      // Put the selected data to type (model)
      if (this.resetAfterSelect) {
        this.clearInput()
      } else {
        this.type = clean[this.label]
      }

      this.showList = false
      this.$emit('select', clean)
    }
  },

  created () {
    // Sync parent model with initValue Props
    this.json = this.init ? [this.init] : []
    this.type = this.init ? this.init.nickname : ''
  }
}
</script>

<style scoped>

.autocomplete-wrapper input {
  font-size: 18px;
  padding-top: 10.3px;
  padding-bottom: 0px;
}

.transition, .autocomplete, .showAll-transition, .autocomplete ul, .autocomplete ul li a{
  transition:all 0.3s ease-out;
  -moz-transition:all 0.3s ease-out;
  -webkit-transition:all 0.3s ease-out;
  -o-transition:all 0.3s ease-out;
}

.autocomplete-list ul {
  max-height: 250px;
  width: 60%;
  overflow-y: auto;
  z-index: 1100;
}

.autocomplete ul{
  font-family: sans-serif;
  position: absolute;
  list-style: none;
  background: #f8f8f8;
  padding: 10px 0;
  margin: 0;
  display: inline-block;
  min-width: 15%;
  margin-top: 10px;
}

.autocomplete ul:before{
  content: "";
  display: block;
  position: absolute;
  height: 0;
  width: 0;
  border: 10px solid transparent;
  border-bottom: 10px solid #f8f8f8;
  left: 46%;
  top: -20px
}

.autocomplete ul li a{
  text-decoration: none;
  display: block;
  background: #f8f8f8;
  color: #2b2b2b;
  padding: 5px;
  padding-left: 10px;
}

.autocomplete ul li a:hover, .autocomplete ul li.focus-list a{
  color: white;
  background: #2F9AF7;
}

.autocomplete ul li a span{
  display: block;
  margin-top: 3px;
  color: grey;
  font-size: 13px;
}

.autocomplete ul li a:hover span, .autocomplete ul li.focus-list a span{
  color: white;
}

.showAll-transition{
  opacity: 1;
  height: 50px;
  overflow: hidden;
}

.showAll-enter{
  opacity: 0.3;
  height: 0;
}

.showAll-leave{
  display: none;
}

</style>
