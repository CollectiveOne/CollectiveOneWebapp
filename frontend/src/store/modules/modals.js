const state = {
  showEditInitiativeModal: false,
  showEditNotificationsModal: false,
  showNewTokenMintModal: false,
  showNewInitiativeTransferModal: false,
  showNewAssignationModal: false,
  showNewInitiativeModal: false,
  showNewSubInitiativeModal: false,
  parentInitiativeIdForModal: ''
}

const getters = {
}

const mutations = {
  showEditInitiativeModal: (state, payload) => {
    state.showEditInitiativeModal = payload
  },
  showEditNotificationsModal: (state, payload) => {
    state.showEditNotificationsModal = payload
  },
  showNewTokenMintModal: (state, payload) => {
    state.showNewTokenMintModal = payload
  },
  showNewInitiativeTransferModal: (state, payload) => {
    state.showNewInitiativeTransferModal = payload
  },
  showNewAssignationModal: (state, payload) => {
    state.showNewAssignationModal = payload
  },
  showNewInitiativeModal: (state, payload) => {
    state.showNewInitiativeModal = payload
  },
  showNewSubInitiativeModal: (state, payload) => {
    state.parentInitiativeIdForModal = payload.parentId
    state.showNewSubInitiativeModal = payload.show
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
