const state = {
  contentAnimationType: 'slideToDown',
  triggerUpdateAssets: false,
  triggerUpdateSectionCards: false,
  triggerUpdateNotifications: false,
  userEmailNotVerified: false,
  expandNav: false,
  expandModelNav: true,
  windowIsSmall: false,
  draggingElement: null,
  triggerSectionDraggingState: false,
  triggerCardDraggingState: false
}

const getters = {
}

const mutations = {
  setContentAnimationType: (state, payload) => {
    state.contentAnimationType = payload
  },
  triggerUpdateAssets: (state) => {
    state.triggerUpdateAssets = !state.triggerUpdateAssets
  },
  triggerUpdateSectionCards: (state) => {
    state.triggerUpdateSectionCards = !state.triggerUpdateSectionCards
  },
  triggerUpdateNotifications: (state) => {
    state.triggerUpdateNotifications = !state.triggerUpdateNotifications
  },
  setUserEmailNotVerified: (state, payload) => {
    state.userEmailNotVerified = payload
  },
  setExpandNav: (state, payload) => {
    state.expandNav = payload
  },
  toggleExpandNav: (state) => {
    state.expandNav = !state.expandNav
  },
  toggleExpandModelNav: (state) => {
    state.expandModelNav = !state.expandModelNav
  },
  setWindowIsSmall: (state, payload) => {
    state.windowIsSmall = payload
  },
  setDraggingElement: (state, payload) => {
    state.draggingElement = payload
  },
  triggerSectionDraggingState: (state) => {
    state.triggerSectionDraggingState = !state.triggerSectionDraggingState
  },
  triggerCardDraggingState: (state) => {
    state.triggerCardDraggingState = !state.triggerCardDraggingState
  }
}

const actions = {
}

export default {
  state,
  getters,
  mutations,
  actions
}
