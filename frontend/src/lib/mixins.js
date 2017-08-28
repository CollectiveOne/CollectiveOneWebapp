/* eslint-disable */

/*! Copyright (c) 2016 Naufal Rabbani (http://github.com/BosNaufal)
* Licensed Under MIT (http://opensource.org/licenses/MIT)
*
* Vue 2 Autocomplete @ Version 0.0.1
*
*/

/*!
*  javascript-debounce 1.0.0
*
*  A lightweight, dependency-free JavaScript module for debouncing functions based on David Walsh's debounce function.
*
*  Source code available at: https://github.com/jgarber623/javascript-debounce
*
*  (c) 2015-present Jason Garber (http://sixtwothree.org)
*
*  javascript-debounce may be freely distributed under the MIT license.
*/

var debounce = function(callback, delay) {
  var timeout;
  return function() {
    var context = this, args = arguments;
    clearTimeout(timeout);
    timeout = setTimeout(function() {
      callback.apply(context, args);
    }, delay);
  };
};

export const AutocompleteMixin = {

  props: {
    id: String,
    className: String,
    placeholder: String,
    resetTrigger: Boolean,

    // Intial Value
    init: {
      type: Object,
      default: () => { return null }
    },

    // Anchor of list
    anchor: {
      type: String,
      required: true
    },

    // Label of list
    label: String,

    resetAfterSelect: {
      type: Boolean,
      default: false
    },

    // Debounce time
    debounce: Number,

    // axios URL will be fetched
    url: {
      type: String,
      required: true
    },

    // query param
    param: {
      type: String,
      default: 'q'
    },

    // Custom Params
    customParams: Object,

    // minimum length
    min: {
      type: Number,
      default: 0
    },

    // Process the result before retrieveng the result array.
    process: Function,

    // Callback
    onInput: Function,
    onShow: Function,
    onBlur: Function,
    onHide: Function,
    onFocus: Function,
    onBeforeRequest: Function,
    onRequestLoaded: Function,

  },

  data() {
    return {
      showList: false,
      type: "",
      json: [],
      focusList: ""
    };
  },

  computed: {
    selected () {
      if (this.type !== '') {
        if ((this.json.length > 0)) {
          return this.json[this.focusList]
        }
      }

      return null
    }
  },

  watch: {
    resetTrigger () {
      this.clearInput()
    }
  },

  methods: {

    // Netralize Autocomplete
    clearInput() {
      this.showList = false
      this.type = ""
      this.json = []
      this.focusList = ""
    },

    // Get the original data
    cleanUp(data){
      return JSON.parse(JSON.stringify(data));
    },

    input(val){
      if(this.json.length > 0) {
          this.showList = true;
      } else {
        this.hideAll()
      }

      // Callback Event
      this.onInput ? this.onInput(val) : null

      // Debounce the "getData" method.
      if(!this.debouncedGetData || this.debounce !== this.oldDebounce) {
        this.oldDebounce = this.debounce;
        this.debouncedGetData = this.debounce ? debounce(this.getData.bind(this), this.debounce) : this.getData;
      }

      // Get The Data
      this.debouncedGetData(val)
    },

    showAll(){
      // Callback Event
      this.json = [];

      this.getData("")

      // Callback Event
      this.onShow ? this.onShow() : null

      this.showList = true;
    },

    hideAll(e){
      // Callback Event
      this.onBlur ? this.onBlur(e) : null

      setTimeout(() => {

        // Callback Event
        this.onHide ? this.onHide() : null

        this.showList = false;
      },250);
    },

    focus(e){
      this.focusList = 0;

      // Callback Event
      this.onFocus ? this.onFocus(e) : null

      // Show when seleceted
      this.showAll()
    },

    mousemove(i){
      this.focusList = i;
    },

    keydown(e){
      let key = e.keyCode;

      // Disable when list isn't showing up
      if(!this.showList) return;

      switch (key) {
        case 40: //down
          this.focusList++;
        break;
        case 38: //up
          this.focusList--;
        break;
        case 13: //enter
          this.selectList(this.json[this.focusList])
          this.showList = false;
        break;
        case 27: //esc
          this.showList = false;
        break;
      }

      // When cursor out of range
      let listLength = this.json.length - 1;
      this.focusList = this.focusList > listLength ? 0 : this.focusList < 0 ? listLength : this.focusList;

    },

    activeClass(i){
      return {
        'focus-list' : i == this.focusList
      };
    },

    selectList(data){
      let clean = this.cleanUp(data);

      // Put the selected data to type (model)
      this.type = clean[this.label]

      this.showList = false;

      /**
      * Callback Event
      * Deep clone of the original object
      */
      this.$emit('select',clean)
    },

    getData(val){

      if (val.length < this.min) return;

      if(this.url != null){
        // Callback Event
        this.onBeforeRequest ? this.onBeforeRequest(val) : null

        var params = {}
        params[this.param] = val

        this.axios.get(this.url,{
          params: params
        }).then((response) => {
          this.onRequestLoaded ? this.onRequestLoaded(response.data.data) : null
          this.json = this.process ? self.process(response.data) : response.data.data;
        })
      }
    }
  }
}
