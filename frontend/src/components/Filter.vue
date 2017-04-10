<template lang="html">
  <div class="col">

    <div class="row header">

      <div class="col-6 left-btns">
        <div>
          <b-button variant="primary" v-if="showFilterBtn" @click="toggleFilter"> {{ this.show ? 'hide and update' : 'show filters' }} </b-button>
          <b-button variant="primary" v-if="showNewBtn">new element</b-button>
        </div>
      </div>

      <div class="col-6 rigth-btns">
        <b-button variant="primary" class="last" @click="nextPage">next</b-button>
        <b-button variant="primary" @click="prevPage">prev</b-button>
        <div class="badge badge-primary res-set" v-if="resSet.length > 0">
          {{ this.resSet[0] + '-' + this.resSet[1] + ' of ' + this.resSet[2] }}
        </div>

        <select class="form-control" v-model="sortBy" @change="updateData()">
          <option v-for="order in orderByOptions" :value="order.value">{{ order.text}}</option>
        </select>
      </div>

    </div>

    <transition
       appear
       enter-class=""
       enter-active-class="animated flipInX"
       leave-class=""
       leave-active-class="animated flipOutX">

       <form @submit.prevent v-if="show" class="jumbotron body">
        <div class="row inputs">
          <div class="col projects-col">
            <p>projects:</p>
            <app-badge-selectable v-for="name in projectNameOptions" :key="name"
              :selectedInit="projectNamesSelected.indexOf(name) !== -1"
              class="badgeSelectable"
              :id="name" @selected="projectNameSelected">
              {{ name }}
            </app-badge-selectable>
          </div>

          <div class="col states-col">
            <p>state:</p>
            <app-badge-selectable v-for="state in stateOptions" :key="state"
              :selectedInit="stateNames.indexOf(state) !== -1"
              class="badgeSelectable"
              :id="state" @selected="stateSelected">
              {{ state }}
            </app-badge-selectable>
          </div>

          <div class="col gen-inputs-col">
            <div class="form-group">
              <label>keyword:</label>
              <input v-model="keyw" class="form-control" placeholder="keyword"></input>
            </div>
            <div class="form-group">
              <label>creator:</label>
              <app-input-autocomplete
                :initValue="creatorUsernames[0]"
                :onSelect="(val) => {creatorUsernames = [val.anchor]}"
                anchor="anchor" placeholder="creator"
                url="/1/users/suggestions">
              </app-input-autocomplete>
            </div>
          </div>

          <div class="col cbtion-inputs-col" v-if="type === 'cbtions'">
            <div class="form-group">
              <label>goal:</label>
              <app-input-autocomplete
                :initValue="goalTag"
                :onSelect="(val) => {goalTag = val.anchor}"
                :onFocus="() => { projectNamesSelected.length != 1 ? goalSelectorError = true :  goalSelectorError = false }"
                anchor="anchor" placeholder="goal-tag"
                url="/1/goals/suggestions" :customParams="{projectName: projectNamesSelected[0]}">
              </app-input-autocomplete>
              <b-alert variant="danger" :show="goalSelectorError">select one project</b-alert>
            </div>
            <div class="form-group">
              <app-badge-selectable class="badgeSelectable"
                :selectedInit="goalSubgoalsFlag"
                @selected="(data) => { goalSubgoalsFlag = data.selected }" >
                and subgoals
              </app-badge-selectable>
              <br>
            </div>
            <div class="form-group">
              <label>contributor:</label>
              <app-input-autocomplete
                :initValue="contributorUsername"
                :onSelect="(val) => {contributorUsername = val.anchor}"
                anchor="anchor" placeholder="contributor"
                url="/1/users/suggestions">
              </app-input-autocomplete>
            </div>
            <div class="form-group">
              <label>assignee:</label>
              <app-input-autocomplete
                :initValue="assigneeUsername"
                :onSelect="assigneeSelected"
                anchor="anchor" placeholder="assignee"
                url="/1/users/suggestions">
              </app-input-autocomplete>
            </div>
          </div>

          <div class="col decisions-input-col" v-if="type === 'decisions'">
            <div class="checkbox">
              <app-badge-selectable
                :selectedInit: "goalSubgoalsFlag"
                class="badgeSelectable">
                show automatic decisions
              </app-badge-selectable>
            </div>
          </div>

        </div>

        <div class="row bottom-btns">
          <div class="col">
            <b-button variant="primary" @click="toggleFilter">update</b-button>
          </div>
        </div>

        <div v-if="loading" class="row loading">
          <img src="../assets/images/ajax-loader.gif">
        </div>
      </form>
    </transition>
  </div>

</template>

<script>
import Autocomplete from '@/components/external/Autocomplete.vue'

import BadgeSelectable from '@/components/BadgeSelectable.vue'
import { remove } from '@/services/common'

export default {
  props: {
    showFilterBtn: {
      type: Boolean,
      default: true
    },

    showNewBtn: {
      type: Boolean,
      default: true
    },

    newBtnModal: {
      type: String,
      default: ''
    },

    type: {
      type: String,
      default: 'cbtions'
    },

    orderByOptions: {
      type: Array,
      default: () => {
        return [
          {
            text: 'New First',
            value: 'NEWFIRST'
          },
          {
            text: 'Old First',
            value: 'OLDFIRST'
          }
        ]
      }
    },

    orderBySelected: {
      type: String,
      default: 'CREATIONDATEDESC'
    },

    stateOptions: {
      type: Array,
      default: () => {
        return ['State1', 'State2']
      }
    },

    statesSelected: {
      type: Array,
      default: () => {
        return ['State1']
      }
    },

    projectNameOptions: {
      type: Array,
      default: () => {
        return ['Project1', 'Project2']
      }
    },

    resSet: {
      type: Array,
      default: () => {
        return []
      }
    },

    loading: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      show: false,
      sortBy: 'CREATIONDATEDESC',
      page: 1,
      nperpage: 15,
      projectNamesSelected: [],
      keyw: '',
      stateNames: [],
      creatorUsernames: [],
      goalTag: '',
      goalSubgoalsFlag: true,
      goalSelectorError: false,
      contributorUsername: '',
      assigneeUsername: ''
    }
  },

  methods: {
    toggleFilter () {
      if (this.show) {
        this.updateData()
      }

      this.show = !this.show
    },

    updateData () {
      this.$emit('updateData', {
        sortBy: this.sortBy,
        page: this.page,
        nperpage: this.nperpage,
        projectNames: this.projectNamesSelected,
        stateNames: this.stateNames,
        creatorUsernames: this.creatorUsernames,
        contributorUsername: this.contributorUsername,
        assigneeUsernam: this.assigneeUsername,
        keyw: this.keyw,
        goalTag: this.goalTag,
        goalSubgoalsFlag: this.goalSubgoalsFlag,
        showInternalDecisions: this.showInternalDecisions
      })
    },

    projectNameSelected (data) {
      if (data.selected) {
        if (!this.projectNamesSelected.includes(data.id)) {
          this.projectNamesSelected.push(data.id)
        }
      } else {
        if (this.projectNamesSelected.includes(data.id)) {
          remove(this.projectNamesSelected, data.id)
        }
      }
    },

    stateSelected (data) {
      if (data.selected) {
        if (!this.stateNames.includes(data.id)) {
          this.stateNames.push(data.id)
        }
      } else {
        if (this.stateNames.includes(data.id)) {
          remove(this.stateNames, data.id)
        }
      }
    },

    assigneeSelected (val) {
      if (!this.stateNames.includes('ASSIGNED')) {
        this.stateNames.push('ASSIGNED')
      }
      this.contributorUsername = val.anchor
    },

    nextPage () {
      this.page++
      var pageMax = Math.ceil(this.resSet[2] / this.nperpage)
      if (this.page > pageMax) {
        this.page = pageMax
      }

      this.updateData()
    },

    prevPage () {
      this.page--
      if (this.page < 1) {
        this.page = 1
      }

      this.updateData()
    }

  },

  components: {
    AppBadgeSelectable: BadgeSelectable,
    AppInputAutocomplete: Autocomplete
  },

  created () {
    this.sortBy = this.orderBySelected
    this.stateNames = this.statesSelected

    this.updateData()
  }
}
</script>

<style scoped>

@media (max-width: 768px) {
  .left-btns button {
    margin-bottom: 10px;
  }

  .rigth-btns button,select,.badge {
    margin-bottom: 10px;
  }
}

.left-btns button {
  width: 150px;
}

.rigth-btns button,select {
  float: right;
  margin-right: 5px;
}

.rigth-btns select {
  width: 150px;
}

.rigth-btns .last {
  margin-right: 0px;
}

.res-set {
  width: 100px;
  height: 36.9px;
  float: right;
  margin-right: 5px;
  padding-top: 14px;
}

.badgeSelectable {
  width: 100%;
  margin-bottom: 10px;
  font-size: 14px;
}

.header {
  overflow: auto;
}

.body {
  margin-top: 10px;
  font-size: 12px;
  -webkit-animation-duration: 0.8s;
}

.bottom-btns {
  text-align: center;
}

.bottom-btns button {
  margin-top: 20px;
  width: 200px;
}

</style>
