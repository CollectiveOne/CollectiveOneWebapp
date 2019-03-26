const state = {
  contentAnimationType: 'slideToDown',
  triggerUpdateAssets: false,
  triggerUpdateSectionCards: false,
  triggerUpdateNotifications: false,
  triggerUpdateSubscriber: false,
  userEmailNotVerified: false,
  expandNav: false,
  expandModelNav: true,
  windowIsSmall: false,
  windowIsFocus: true,
  isTouchScreen: false,
  draggingElement: null,
  enableSectionDraggingState: false,
  enableCardDraggingState: false,
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
  triggerUpdateSubscriber: (state) => {
    state.triggerUpdateSubscriber = !state.triggerUpdateSubscriber
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
  setExpandModelNav: (state, payload) => {
    state.expandModelNav = payload
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
  toggleSectionDraggingState: (state) => {
    state.enableSectionDraggingState = !state.enableSectionDraggingState
  },
  setSectionDraggingState: (state, payload) => {
    state.enableSectionDraggingState = payload
  },
  toggleCardDraggingState: (state) => {
    state.enableCardDraggingState = !state.enableCardDraggingState
  },
  setCardDraggingState: (state, payload) => {
    state.enableCardDraggingState = payload
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
