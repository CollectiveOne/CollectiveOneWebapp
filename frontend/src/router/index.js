import Vue from 'vue'
import Router from 'vue-router'

import InitiativesView from '../components/InitiativesView.vue'
import InitiativesContent from '../components/initiative/InitiativesContent.vue'

import InitiativeOverview from '../components/initiative/InitiativeOverview.vue'
import InitiativePeople from '../components/initiative/InitiativePeople.vue'
import InitiativeAssignations from '../components/initiative/InitiativeAssignations.vue'

Vue.use(Router)

export default new Router({
  routes: [
    { path: '/', redirect: '/inits' },
    { path: '/inits',
      name: 'Initiatives',
      component: InitiativesView,
      children: [
        { path: ':initiativeId',
          name: 'Initiative',
          component: InitiativesContent,
          children: [
            { path: 'overview', name: 'InitiativeOverview', component: InitiativeOverview, meta: {'column': 0} },
            { path: 'people', name: 'InitiativePeople', component: InitiativePeople, meta: {'column': 1} },
            { path: 'assignations', name: 'InitiativeAssignations', component: InitiativeAssignations, meta: {'column': 3} }
          ]}
      ]
    }
  ]
})
