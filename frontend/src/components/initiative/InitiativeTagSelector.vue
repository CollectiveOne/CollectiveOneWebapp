<!-- Downladed from  https://github.com/BosNaufal/vue2-autocomplete
  edited to fit the user selector of CollectiveTweet -->
<template>
  <div class="w3-row-padding autocomplete-wrapper">

    <input  type="text"
            :id="id"
            name="T_selectTagInputInitiative" 
            class="w3-input w3-hover-light-grey autocomplete-inputs"
            placeholder="select tag"
            v-model="type"
            @input="input(type)"
            @dblclick="showAll"
            @blur="hideAll"
            @keydown="keydown"
            @focus="focus"
            autocomplete="off" />


    <div class="w3-row" :class="(className ? className + '-list ' : '') + 'autocomplete transition autocomplete-list'" v-show="showList">
      <ul class="w3-border">
        <li id="T_createNewSelectTag" v-if="enableCreate" @click="$emit('create-new')" class="w3-button w3-center" style="width: 100%"><b>create new</b></li>
        <hr class="hrz-line">
        <li v-for="(data, i) in json"
            transition="showAll"
            :class="activeClass(i)">

          <a  href="#"
              @click.prevent="selectList(data)"
              @mousemove="mousemove(i)">
            <app-initiative-tag
              :tag="data"
              :showRemove="false"
              :showDescription="true">
            </app-initiative-tag>
          </a>
        </li>
        <li v-if="json.length == 0" class="w3-padding w3-center">
          <i>no matches found</i>
        </li>
      </ul>

    </div>
  </div>
</template>

<script>
import { AutocompleteMixin } from '@/lib/mixins'
import InitiativeTag from '@/components/initiative/InitiativeTag.vue'

export default {
  mixins: [AutocompleteMixin],

  props: {
    enableCreate: {
      type: Boolean,
      default: false
    },

    /* default properties overrides mixin */
    anchor: {
      default: 'c1Id'
    },
    label: {
      default: 'tagText'
    },
    url: {
      default: '/1/initiative/tags/suggestions'
    }
  },

  components: {
    'app-initiative-tag': InitiativeTag
  },

  created () {
    // Sync parent model with initValue Props
    this.json = this.init ? [this.init] : []
    this.type = this.init ? this.init.tagText : ''
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
  width: 300px;
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
  margin-top: 0px;
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

.create-button {
  width: 300px;
}

.autocomplete hr {
  margin-top: 5px !important;
  margin-bottom: 5px;
}

.autocomplete ul li a:hover, .autocomplete ul li.focus-list a {
  color: white;
  background: #e6e6e6;
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
