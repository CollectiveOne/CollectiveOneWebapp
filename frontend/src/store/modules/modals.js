const state = {
  showEditInitiativeModal: false,
  showEditNotificationsModal: false,
  showNewTokenMintModal: false,
  assetIdForMint: '',
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
    state.assetIdForMint = payload.assetId
    state.showNewTokenMintModal = payload.show
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
