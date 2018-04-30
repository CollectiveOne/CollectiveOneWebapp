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
  resetSectionsTree: (context, id) => {
    context.dispatch('appendSectionData', {sectionId: id, coord: [0]})
  },
  appendSectionData: (context, payload) => {
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
      }
    })
  },
  toogleExpandSubsection: (context, coord) => {
    let sectionData = getSectionDataAtCoord(state.sectionsTree, coord)
    sectionData.expand = !sectionData.expand
    /* besides expanding, preload the subsections of each subsection */
    if (sectionData.expand) {
      for (let ix = 0; ix < sectionData.subsectionsData.length; ix++) {
        if (!sectionData.subsectionsData[ix].subsectionsDataSet) {
          context.dispatch('appendSectionData', { sectionId: sectionData.subsectionsData[ix].section.id, coord: coord.concat(ix) })
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
