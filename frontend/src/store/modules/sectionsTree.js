import Vue from 'vue'

const state = {
  sectionsTree: []
}

const getSectionDataAtCoord = function (sectionsTree, coord) {
  let subTree = sectionsTree

  /* navigate inside the tree up the penultimate coord */
  for (let ix = 0; ix < coord.length - 1; ix++) {
    if (subTree.length === 0) {
      return null
    }
    subTree = subTree[coord[ix]].subsectionsData
  }

  if (subTree.length === 0) {
    return null
  }

  return subTree[coord[coord.length - 1]]
}

const getters = {
  getSectionDataAtCoord: (state) => (coord) => {
    return getSectionDataAtCoord(state.sectionsTree, coord)
  }
}

const mutations = {
  setSectionsTree: (state, payload) => {
    state.sectionsTree = payload
  },
  appendSectionData: (state, payload) => {
    if (payload.coord.length === 1) {
      if (payload.coord[0] === 0) {
        /* initialize tree */
        state.sectionsTree = [{
          section: payload.sectionData.section,
          subsectionsData: payload.sectionData.subsectionsData,
          subsectionsDataSet: true,
          expand: false
        }]
        return
      }
    }

    let sectionData = getSectionDataAtCoord(state.sectionsTree, payload.coord)
    sectionData.section = payload.sectionData.section
    sectionData.subsectionsData = payload.sectionData.subsectionsData
    sectionData.subsectionsDataSet = true
  }
}

const actions = {
  resetSectionsTree: (context, payload) => {
    context.dispatch('appendSectionData', {sectionId: payload.sectionId, coord: [0]}).then(() => {
      if (payload.paths) {
        context.dispatch('expandSectionsInPaths')
      }
    })
  },
  appendSectionData: (context, payload) => {
    return new Promise((resolve, reject) => {
      Vue.axios.get('/1/model/section/' + payload.sectionId, { params: { levels: 1 } }).then((response) => {
        if (response.data.result === 'success') {
          let section = response.data.data
          let subsections = section.subsections
          let subsectionsData = []
          for (let ix in subsections) {
            subsectionsData.push({
              section: subsections[ix],
              subsectionsData: [],
              subsectionsDataSet: false,
              expand: false
            })
          }
          let sectionData = {
            section: section,
            subsectionsData: subsectionsData
          }
          context.commit('appendSectionData', { sectionData: sectionData, coord: payload.coord })
          resolve()
        } else {
          reject()
        }
      })
    })
  },
  collapseSubsection: (context, coord) => {
    let sectionData = getSectionDataAtCoord(state.sectionsTree, coord)
    sectionData.expand = true
  },
  expandSectionAndContinue: (context, payload) => {
    let sectionData = payload.sectionData
    sectionData.expand = true
    let ixNext = 0
    for (let ix = 0; ix < sectionData.subsectionsData.length; ix++) {
      /* next section to be expanded */
      if (sectionData.subsectionsData[ix].section.id === payload.expandIds[0]) {
        ixNext = ix
        break
      }
    }

    let newCoord = payload.coord.concat(ixNext)
    let newSectionData = getSectionDataAtCoord(state.sectionsTree, newCoord)

    context.dispatch('appendSectionData', { sectionId: sectionData.section.id, coord: newCoord })
      .then(() => {
        /* after loading, recursive call to this same action to keep expanding the sections */
        if (payload.expandIds.length > 1) {
          let nextIds = payload.expandIds.shift()
          context.dispatch('expandSectionAndContinue', {
            sectionData: newSectionData,
            expandIds: nextIds,
            coord: newCoord
          })
        }
      })
  },
  expandSubsection: (context, coord) => {
    let sectionData = getSectionDataAtCoord(state.sectionsTree, coord)
    sectionData.expand = true
    /* besides expanding, preload the subsections of each subsection */
    for (let ix = 0; ix < sectionData.subsectionsData.length; ix++) {
      if (!sectionData.subsectionsData[ix].subsectionsDataSet) {
        context.dispatch('appendSectionData', { sectionId: sectionData.subsectionsData[ix].section.id, coord: coord.concat(ix) })
      }
    }
  },
  expandSectionsInPaths: (context, paths) => {
    for (let ix in paths) {
      let thisPath = paths[ix]
      /* make sure this path starts at the current initiative */
      if (thisPath[0].id === context.rootState.initiative.initiative.topModelSection.id) {
        /* top model section coord = 0 */
        let sectionData = state.sectionsTree[0]
        let nextIds = thisPath.map((e) => e.id)
        nextIds.shift()
        context.dispatch('expandSectionAndContinue', {
          sectionData: sectionData,
          expandIds: nextIds,
          coord: [0]
        })
      }
    }
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
