const state = {
  contentAnimationType: 'slideToDown',
  triggerUpdateAssets: false,
  triggerUpdateSectionCards: false,
  triggerUpdateNotifications: false,
  userEmailNotVerified: false,
  expandNav: false,
  expandModelNav: true,
  windowIsSmall: false,
  windowIsFocus: true,
  isTouchScreen: false,
  draggingElement: null,
  triggerSectionDraggingState: false,
  triggerCardDraggingState: false,
  createNewCardLocation: null
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
  setIsTouchScreen: (state, payload) => {
    state.isTouchScreen = payload
  },
  setWindowIsSmall: (state, payload) => {
    state.windowIsSmall = payload
  },
  setWindowIsFocus: (state, payload) => {
    state.windowIsFocus = payload
  },
  setDraggingElement: (state, payload) => {
    state.draggingElement = payload
  },
  triggerSectionDraggingState: (state) => {
    state.triggerSectionDraggingState = !state.triggerSectionDraggingState
  },
  triggerCardDraggingState: (state) => {
    state.triggerCardDraggingState = !state.triggerCardDraggingState
  },
  createNewCardLocation: (state, payload) => {
    state.createNewCardLocation = payload
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
