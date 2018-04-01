import Vue from 'vue'
import Router from 'vue-router'

import RootView from '@/components/RootView.vue'
import LandingView from '@/components/LandingView.vue'

import AppView from '@/components/AppView.vue'
import InitiativesView from '@/components/InitiativesView.vue'
import InitiativesHome from '@/components/InitiativesHome.vue'
import InitiativesContent from '@/components/initiative/InitiativeContent.vue'
import Unsubscribe from '@/components/user/Unsubscribe.vue'

import OverviewSection from '@/components/initiative/OverviewSection.vue'
import PeopleSection from '@/components/initiative/PeopleSection.vue'
import TransfersSection from '@/components/initiative/TransfersSection.vue'
import TimelineSection from '@/components/initiative/TimelineSection.vue'

import ModelSectionTab from '@/components/initiative/ModelSectionTab.vue'
import ModelViewPlacer from '@/components/model/ModelViewPlacer.vue'
import ModelSectionPlacer from '@/components/model/ModelSectionPlacer.vue'
import ModelCardWrapperPlacer from '@/components/model/ModelCardWrapperPlacer.vue'
import ModelSearchContainer from '@/components/model/ModelSearchContainer.vue'

import UserProfilePage from '@/components/UserProfilePage.vue'
import WSWebSocketDebugPage from '@/components/WebSocketDebugPage.vue'

Vue.use(Router)

export default new Router({
  routes:
  [
    {
      path: '/',
      name: 'Root',
      component: RootView
    },
    {
      path: '/landing',
      name: 'Landing',
      component: LandingView
    },
    {
      path: '/app',
      component: AppView,
      children:
      [
        {
          path: '/',
          name: 'AppView',
          redirect: '/app/inits'
        },
        {
          path: 'inits',
          component: InitiativesView,
          children: [
            {
              path: '/',
              name: 'InitiativesHome',
              component: InitiativesHome,
              children: [
                { path: 'unsubscribe', name: 'Unsubscribe', component: Unsubscribe }
              ]
            },
            {
              path: ':initiativeId',
              name: 'Initiative',
              component: InitiativesContent,
              children: [
                { path: 'overview', name: 'InitiativeOverview', component: OverviewSection, meta: {'column': 1} },
                { path: 'timeline', name: 'InitiativeTimeline', component: TimelineSection, meta: {'column': 2} },
                {
                  path: 'model',
                  name: 'InitiativeModel',
                  component: ModelSectionTab,
                  meta: {'column': 3},
                  children: [
                    { path: 'view/:viewId', name: 'ModelView', component: ModelViewPlacer, meta: {'column': 3} },
                    {
                      path: 'section/:sectionId',
                      name: 'ModelSection',
                      component: ModelSectionPlacer,
                      meta: {'column': 2},
                      children: [
                        { path: 'card/:cardId', name: 'ModelCardInSection', meta: {'column': 3} }
                      ]
                    },
                    { path: 'card/:cardWrapperId', name: 'ModelCardAlone', component: ModelCardWrapperPlacer, meta: {'column': 3} },
                    { path: 'search', name: 'ModelSearch', component: ModelSearchContainer, meta: {'column': 3} }
                  ]
                },
                { path: 'people', name: 'InitiativePeople', component: PeopleSection, meta: {'column': 4} },
                { path: 'people/addMember/:userId', name: 'InitiativePeopleAddMember', component: PeopleSection, meta: {'column': 4} },
                { path: 'assignations', name: 'InitiativeAssignations', component: TransfersSection, meta: {'column': 5} },
                { path: 'assignations/:assignationId', name: 'InitiativeAssignation', component: TransfersSection, meta: {'column': 5} }
              ]
            }
          ]
        },
        {
          path: 'user/:userId',
          name: 'UserProfilePage',
          component: UserProfilePage
        },
        {
          path: 'websocket',
          name: 'WebSocketDebugPage',
          component: WSWebSocketDebugPage
        }
      ]
    }
  ]
})
