import Vue from 'vue'
import Router from 'vue-router'

import InitiativesView from '@/components/InitiativesView.vue'
import InitiativesContent from '@/components/initiative/InitiativeContent.vue'

import OverviewSection from '@/components/initiative/OverviewSection.vue'
import PeopleSection from '@/components/initiative/PeopleSection.vue'
import TransfersSection from '@/components/initiative/TransfersSection.vue'

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
            { path: 'overview', name: 'InitiativeOverview', component: OverviewSection, meta: {'column': 0} },
            { path: 'people', name: 'InitiativePeople', component: PeopleSection, meta: {'column': 1} },
            { path: 'assignations', name: 'InitiativeAssignations', component: TransfersSection, meta: {'column': 3} },
            { path: 'assignations/:assignationId', name: 'InitiativeAssignation', component: TransfersSection, meta: {'column': 3} }
          ]
        }
      ]
    }
  ]
})
