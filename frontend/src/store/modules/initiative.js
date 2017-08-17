import Vue from 'vue'

const state = {
  initiative: null,
  initiativeTransfers: null,
  initiativeAssignations: null
}

const getters = {
  initiativeMembersUsers: (state) => () => {
    let members = state.initiative.initiativeMembers.members
    var memberUsers = []
    for (var ix in members) {
      memberUsers.push(members[ix].user)
    }
    return memberUsers
  },
  isLoggedAnAdmin: (state) => {
    if (state.initiative) {
      return state.initiative.loggedMember.role === 'ADMIN'
    } else {
      false
    }
  },
  isLoggedAMember: (state) => {
    if (state.initiative) {
      return state.initiative.loggedMember.role === 'MEMBER'
    } else {
      return false
    }
  }
}

const mutations = {
  setInitiative: (state, payload) => {
    state.initiative = payload
  },
  setTransfers: (state, payload) => {
    state.initiativeTransfers = payload
  },
  setAssignations: (state, payload) => {
    state.initiativeAssignations = payload
  }
}

const actions = {

  updateInitiative: (context, id) => {
    Vue.axios.get('/1/secured/initiative/' + id, {
      params: {
        addAssets: true,
        addSubinitiatives: true,
        addParents: true,
        addMembers: true,
        addLoggedUser: true
      }
    }).then((response) => {
      /* TODO: temporary simulation of backend */
      var initiativeModel = {
        views: [
          {
            id: '10',
            title: 'General View',
            sections: [
              {
                id: '1010',
                title: 'Vision',
                description: 'Describes the vision of CollectiveOne in the long-term',
                cards: [
                  {
                    id: '101010',
                    title: '',
                    text: 'CollectiveOne will enable individuals to coordinate among themselves and combine their efforts into collective endeavors in an open, participatory and transparent way, while fairly recognizing contributions and translating these into ownership and control.'
                  }
                ],
                subsections: [
                  {
                    id: '20',
                    title: 'Objectives',
                    description: 'Lists the key high-level objectives of CollectiveOne',
                    cards: [
                      {
                        id: '2010',
                        title: 'Openness',
                        text: 'CollectiveOne enables the development of initiatives to which anyone can contribute. Such equality in the opportunity to contribute should be independent of the background of the contributor, such as his/her seniority in the project, roles, previous experiences, or his/her level of commitment to the initiative.'
                      },
                      {
                        id: '2020',
                        title: 'Value Tracking',
                        text: 'Individual or team contributions, tangible or intangible, to an initiative should be identified and valued relative to the rest of contributions in that initiative. Information about the "relative value" should then serve as input to determine the distribution of absolute value (revenue or others) that is created as a result of the initiative'
                      },
                      {
                        id: '2030',
                        title: 'Interoperability',
                        text: 'Initiatives in CollectiveOne should be able to interact and transact in the real world with other organizations (governments, companies, and NGOs), dispose of the value and intellectual property they create at their will, and provide services to customers or the population in general and receive income from it.'
                      },
                      {
                        id: '2040',
                        title: 'Collective Gobernance',
                        text: 'CollectiveOne should enable the contributors of an initiative to collectively control it through open, transparent, inclusive and effective decision-making processes. These processes should be compatible both with high-level strategic decisions, as long as for lower-level, every-day matters.'
                      }
                    ],
                    subsections: []
                  },
                  {
                    id: '30',
                    title: 'Key Risks',
                    description: 'Lists the key high-level objectives of CollectiveOne',
                    cards: [
                      {
                        id: '3010',
                        title: 'Early stage incentives',
                        text: 'Individuals may not have sufficient incentive to contribute at early stages'
                      },
                      {
                        id: '3020',
                        title: 'Unclear Direction',
                        text: 'Lacking standard leadership and hierarchies, initiatives may find very hard to agree on and share a common view of what they want to achieve. This would be mostly within the "leaders head" on a standard organization. How could leaders (more active participants) share this vision with the rest? How could this vision incorporate inputs from new participants?'
                      }
                    ],
                    subsections: []
                  }
                ]
              },
              {
                id: '40',
                title: 'Implementation',
                description: 'Summarize how will the vision of CollectiveOne be implemented.',
                cards: [
                  {
                    id: '401010',
                    title: '',
                    text: 'CollectiveOne will include a web application that will enable its users to follow the CollectiveOne method to develop collaborative projects.'
                  }
                ],
                subsections: [
                  {
                    id: '',
                    title: 'Basic Module',
                    description: 'Specification of the value accounting base module',
                    cards: [],
                    subsections: []
                  }
                ]
              }
            ]
          }
        ]
      }

      response.data.data.initiativeModel = initiativeModel
      context.commit('setInitiative', response.data.data)
    }).catch((error) => {
      console.log(error)
    })
  },

  refreshInitiative: (context) => {
    if (context.state.initiative) {
      context.dispatch('updateInitiative', context.state.initiative.id)
    }
  },

  refreshTransfers: (context) => {
    if (context.state.initiative) {
      Vue.axios.get('/1/secured/initiative/' + context.state.initiative.id + '/transfersToInitiatives').then((response) => {
        context.commit('setTransfers', response.data.data)
      })

      Vue.axios.get('/1/secured/initiative/' + context.state.initiative.id + '/assignations').then((response) => {
        context.commit('setAssignations', response.data.data)
      })
    }
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
