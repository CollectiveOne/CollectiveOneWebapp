import Vue from 'vue'
import Router from 'vue-router'

import InitiativesView from '../components/InitiativesView.vue'
import InitiativesContent from '../components/initiative/InitiativesContent.vue'
import InitiativeOverview from '../components/initiative/InitiativeOverview.vue'

Vue.use(Router)

export default new Router({
  routes: [
    { path: '/', redirect: '/inits' },
    { path: '/inits',
      name: 'Initiatives',
      component: InitiativesView,
      children: [
        { path: ':id',
          name: 'Initiative',
          component: InitiativesContent,
          children: [
            { path: 'overview',
              name: 'InitiativeOverview',
              component: InitiativeOverview
            }
          ]}
      ]}
  ]
})
