import Vue from 'vue'

const state = {
  sectionsTree: [],
  triggerUpdateExpands: false
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

const isSectionShown = function (subTree, sectionId) {
  for (let ix in subTree) {
    if (subTree[ix].section.id === sectionId) {
      return true
    } else {
      /* recursively call this method in the subsectionsData if they
      are expanded */
      if (subTree[ix].expand) {
        return isSectionShown(subTree[ix].subsectionsData, sectionId)
      }
    }
  }
  /* if tree is empty or not found in the for above */
  return false
}

const getters = {
  getSectionDataAtCoord: (state) => (coord) => {
    return getSectionDataAtCoord(state.sectionsTree, coord)
  },
  isSectionShown: (state) => (sectionId) => {
    return isSectionShown(state.sectionsTree, sectionId)
  }
}

const mutations = {
  triggerUpdateExpands: (state, payload) => {
    state.triggerUpdateExpands = !state.triggerUpdateExpands
  },
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
    context.dispatch('appendSectionData', {sectionId: payload.baseSectionId, coord: [0]}).then(() => {
      context.dispatch('updateCurrentSection', payload.currentSectionId)
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

    if (payload.expandIds.length === 0) {
      /* return if not nextIds to expand */
      /* finisihed expanding the tree in the sectionsTree global state, now
         now force subsections to be reactive. */
      context.commit('triggerUpdateExpands')
      return
    }

    /* find next section to be expanded from the subsections */
    let ixNext = 0
    for (let ix = 0; ix < sectionData.subsectionsData.length; ix++) {
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
        } else {

        }
      })
  },
  expandSubsection: (context, coord) => {
    let sectionData = getSectionDataAtCoord(state.sectionsTree, coord)
    sectionData.expand = true

    /* force subsections to be appended */
    if (!sectionData.subsectionsDataSet) {
      context.dispatch('appendSectionData', { sectionId: sectionData.section.id, coord: coord })
    }

    /* besides expanding, preload the subsections of each subsection */
    for (let ix = 0; ix < sectionData.subsectionsData.length; ix++) {
      if (!sectionData.subsectionsData[ix].subsectionsDataSet) {
        context.dispatch('appendSectionData', { sectionId: sectionData.subsectionsData[ix].section.id, coord: coord.concat(ix) })
      }
    }
  },
  autoExpandSectionsTree: (context, payload) => {
    if (context.state.sectionsTree.length === 0) {
      /* only check if the section tree has been already initialized */
      return
    }

    if (!context.getters.isSectionShown(payload.currentSectionId)) {
      /* if section is not currently shown, autoexpand all paths that reach to it */
      for (let ix in payload.currentSectionPaths) {
        let thisPath = payload.currentSectionPaths[ix]
        /* make sure this path starts at the current initiative */
        if (thisPath.length > 0) {
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
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
