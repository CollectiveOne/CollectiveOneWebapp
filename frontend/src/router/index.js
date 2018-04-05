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

import ModelSectionTab from '@/components/initiative/ModelSectionTab.vue'
import ModelSectionContent from '@/components/model/ModelSectionContent.vue'

import ModelSectionMessages from '@/components/model/ModelSectionMessages.vue'
import ModelSectionCards from '@/components/model/ModelSectionCards.vue'

import UserProfilePage from '@/components/UserProfilePage.vue'

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
                {
                  path: 'model',
                  component: ModelSectionTab,
                  name: 'InitiativeModelBase',
                  meta: {'column': 3},
                  children: [
                    {
                      path: '/',
                      name: 'InitiativeModel',
                      component: ModelSectionContent,
                      meta: {'column': 3}
                    },
                    {
                      path: 'section/:sectionId',
                      name: 'ModelSectionContent',
                      component: ModelSectionContent,
                      meta: {'column': 3},
                      children: [
                        { path: 'messages', name: 'ModelSectionMessages', component: ModelSectionMessages, meta: {'column': 3} },
                        { path: 'cards', name: 'ModelSectionCards', component: ModelSectionCards, meta: {'column': 3} },
                        { path: 'cards/:cardId', name: 'ModelSectionCard', component: ModelSectionCards, meta: {'column': 3} },
                        { path: 'doc', name: 'ModelSectionDoc', component: ModelSectionCards, meta: {'column': 3} }
                      ]
                    }
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
        }
      ]
    }
  ]
})
