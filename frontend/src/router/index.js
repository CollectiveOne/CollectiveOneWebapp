import Vue from 'vue'
import Router from 'vue-router'

import InitiativesView from '../components/InitiativesView.vue'
import InitiativesContent from '../components/initiative/InitiativesContent.vue'

import InitiativeOverview from '../components/initiative/InitiativeOverview.vue'
import InitiativePeople from '../components/initiative/InitiativePeople.vue'
import InitiativeAssignations from '../components/initiative/InitiativeAssignations.vue'
import InitiativeAssignationsBrowser from '../components/initiative/InitiativeAssignationsBrowser.vue'
import InitiativeAssignationPage from '../components/initiative/InitiativeAssignationPage.vue'

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
            { path: 'assignations',
              component: InitiativeAssignations,
              meta: {'column': 3},
              children: [
                { path: '/', name: 'InitiativeAssignations', component: InitiativeAssignationsBrowser, meta: {'column': 3} },
                { path: ':assignationId', name: 'InitiativeAssignationPage', component: InitiativeAssignationPage, meta: {'column': 3} }
              ]}
          ]}
      ]
    }
  ]
})
