<template lang="html">
  <div>
    <div class="row header">
      <div class="col">
        <div>
          <b-button variant="info" v-if="showFilterBtn" @click="toggleFilter">show filters</b-button>
          <b-button variant="info" v-if="showNewBtn">new element</b-button>
        </div>
      </div>
  		<div class="col rigth-btns">
        <b-button variant="info" >next</b-button>
  			<b-button variant="info" >prev</b-button>
  			<b-form-select v-model="sortBy" :options="orderByOptions"></b-form-select>
  		</div>
    </div>
    <form v-if="show" class="row body jumbotron">
      <div class="col">
        <div class="row">
          <div class="col">
            <app-badge-selectable v-for="name in projectNameOptions" :key="name"
              :selectedInit="false" class="badgeSelectable">
              {{ name }}
            </app-badge-selectable>
          </div>
      		<div class="col">
            <app-badge-selectable v-for="state in stateOptions" :key="state"
              :selectedInit="false" class="badgeSelectable">
              {{ state }}
            </app-badge-selectable>
          </div>
      		<div class="col">
      			<div class="form-group">
      				<label>keyword:</label>
      				<input class="form-control" placeholder="keyword"></input>
      			</div>
      			<div class="form-group">
      				<label>creator:</label>
      				<input class="form-control" placeholder="creator"></input>
      			</div>
      		</div>
      		<div class="col" v-if="type === 'cbtions'">
      			<div class="form-group">
      				<label>goal (select one project):</label>
      				<input class="form-control" placeholder="goal-tag"></input>
      			</div>
      			<div class="form-group">
      			  	<app-badge-selectable>and subgoals</app-badge-selectable>
                <br>
      			</div>
      			<div class="form-group">
      				<label>contributor:</label>
      				<input class="form-control" placeholder="contributor"></input>
      			</div>
      			<div class="form-group">
      				<label>assignee:</label>
      				<input id=filter_assignee_input class="form-control" placeholder="assignee"></input>
      			</div>
      		</div>
      		<div class="col" v-if="type === 'decisions'">
      			<div class="checkbox">
      				<label>
                <app-badge-selectable text="hide automatic decisions"></app-badge-selectable>
      				</label>
      			</div>
      		</div>
        </div>
        <div class="row bottom-btns">
          <div class="col">
            <b-button variant="info">update</b-button>
          </div>
        </div>
      </div>
  	</form>
  	<div v-if="loading" class="loading_gif">
  		<img src="../assets/images/ajax-loader.gif">
  	</div>
  </div>
</template>

<script>
import BadgeSelectable from '@/components/BadgeSelectable.vue'

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

    stateOptions: {
      type: Array,
      default: () => {
        return ['State1', 'State2']
      }
    },

    projectNameOptions: {
      type: Array,
      default: () => {
        return ['Project1', 'Project2']
      }
    }
  },

  data () {
    return {
      show: true,
      loading: false,
      sortBy: 'NEWFIRST',
      projectsNamesOptions: [],
      filters: {}
    }
  },

  watch: {
    sortBy () {
      this.updateData()
    }
  },

  methods: {
    toggleFilter () {
      // if (this.show) {
      //   this.updateData()
      // }
      this.show = !this.show
    }
  },

  components: {
    AppBadgeSelectable: BadgeSelectable
  },

  created () {
    // $('#filter_goal_input',this.container).autocomplete({
    //   serviceUrl: '/1/goals/suggestions',
    //   minChars: 0,
    //   params: {projectName: ""},
    //   maxHeight: 100
    // })
    //
    // $('#filter_contributor_input',this.container).autocomplete({
    //   serviceUrl: '/1/users/suggestions',
    //   minChars: 0,
    //   maxHeight: 100
    // })
    //
    // $('#filter_assignee_input',this.container).autocomplete({
    //   serviceUrl: '/1/users/suggestions',
    //   minChars: 0,
    //   maxHeight: 100
    // })
  }
}
</script>

<style scoped>

.rigth-btns button,select {
  float: right;
  margin-right: 5px;
}

.badgeSelectable {
  width: 100%;
  margin-bottom: 10px;
  font-size: 16px;
}

.header {

}

.body {
  margin-top: 10px;
}

.bottom-btns {
  text-align: center;
}

.bottom-btns button {
  width: 200px;
}

</style>
