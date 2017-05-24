<!-- Downladed from  https://github.com/BosNaufal/vue2-autocomplete
  edited to fit the user selector of CollectiveTweet -->

<template>
  <div class="w3-row-padding autocomplete-wrapper">
    <div v-if="selected !== null" class="w3-col s2">
      <div class="w3-cell w3-cell-middle" style="width:30px">
        <img class="" style="width:35px" :src="selected.pictureUrl"/>
      </div>
    </div>
    <div class="w3-col s10">
      <input  type="text"
              :id="id"
              class="w3-input w3-hover-light-gray autocomplete-inputs"
              placeholder="select user"
              v-model="type"
              @input="input(type)"
              @dblclick="showAll"
              @blur="hideAll"
              @keydown="keydown"
              @focus="focus"
              autocomplete="off" />
    </div>

    <div class="w3-row" :class="(className ? className + '-list ' : '') + 'autocomplete transition autocomplete-list'" v-show="showList">
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
import { AutocompleteMixin } from '../../lib/mixins'
import UserAvatar from './UserAvatar.vue'

export default {
  mixins: [AutocompleteMixin],

  components: {
    'app-user-avatar': UserAvatar
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
