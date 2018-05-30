import Vue from 'vue'
import Router from 'vue-router'

const RootView = () => import('@/components/RootView.vue')
const LandingView = () => import('@/components/LandingView.vue')
const AppView = () => import('@/components/AppView.vue')

const InitiativesView = () => import('@/components/InitiativesView.vue')
const InitiativesHome = () => import('@/components/InitiativesHome.vue')
const InitiativesContent = () => import('@/components/initiative/InitiativeContent.vue')
const Unsubscribe = () => import('@/components/user/Unsubscribe.vue')
const ModelSectionRead = () => import('@/components/model/ModelSectionRead.vue')

const OverviewSection = () => import('@/components/initiative/OverviewSection.vue')
const PeopleSection = () => import('@/components/initiative/PeopleSection.vue')
const TransfersSection = () => import('@/components/initiative/TransfersSection.vue')

const ModelSectionTab = () => import('@/components/initiative/ModelSectionTab.vue')
const ModelSectionContent = () => import('@/components/model/ModelSectionContent.vue')
const ModelSectionElements = () => import('@/components/model/ModelSectionElements.vue')

const UserProfilePage = () => import('@/components/UserProfilePage.vue')
const WSWebSocketDebugPage = () => import('@/components/WebSocketDebugPage.vue')

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
          path: '/section/:sectionId',
          name: 'ModelSectionRead',
          component: ModelSectionRead
        },
        {
          path: '/section/:sectionId/card/:cardId',
          name: 'ModelSectionReadCard',
          component: ModelSectionRead
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
                {
                  path: 'overview',
                  name: 'InitiativeOverview',
                  component: OverviewSection,
                  meta: {'column': 1} },
                {
                  path: 'model',
                  component: ModelSectionTab,
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
                        { path: 'messages', name: 'ModelSectionMessages', component: ModelSectionElements, meta: {'column': 3} },
                        { path: 'cards', name: 'ModelSectionCards', component: ModelSectionElements, meta: {'column': 3} },
                        { path: 'cards/:cardId', name: 'ModelSectionCard', component: ModelSectionElements, meta: {'column': 3} }
                      ]
                    },
                    {
                      path: '/card',
                      name: 'ModelCardAlone',
                      component: ModelSectionElements,
                      meta: {'column': 3}
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
