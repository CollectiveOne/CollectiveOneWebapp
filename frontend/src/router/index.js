import Vue from 'vue'
import Router from 'vue-router'

import InitiativesView from '../components/InitiativesView.vue'
import InitiativesContent from '../components/initiative/InitiativesContent.vue'

import InitiativeOverview from '../components/initiative/InitiativeOverview.vue'
import InitiativePeople from '../components/initiative/InitiativePeople.vue'
import InitiativeAssignations from '../components/initiative/InitiativeAssignations.vue'

import InitiativeAssignation from '../components/initiative/InitiativeAssignation.vue'

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
            { path: 'overview', name: 'InitiativeOverview', component: InitiativeOverview },
            { path: 'people', name: 'InitiativePeople', component: InitiativePeople },
            { path: 'assignations', name: 'InitiativeAssignations', component: InitiativeAssignations },
            { path: 'assignation/:assignationId', name: 'InitiativeAssignation', component: InitiativeAssignation }
          ]}
      ]}
  ]
})
