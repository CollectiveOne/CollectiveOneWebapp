import Vue from 'vue'
import { getSortedSubsections } from '@/lib/sortAlgorithm.js'

    
// ContextIds Initialization
let test0 = 'a9fe127b-64c2-1ec6-8164-c5a8837a0016'
let test1 = 'a9fe127b-64c2-1ec6-8164-c5a8baae0018'
let test11 = 'a9fe127b-64c2-1ec6-8164-c5a8ec21001e'
let test12 = 'a9fe127b-64c2-1ec6-8164-c5a903540021'
let test121 = 'a9fe127b-64c2-1ec6-8164-c5a972360024'
let test122 = 'a9fe127b-64c2-1ec6-8164-c5a993880027'
let test13 = 'a9fe127b-64c2-1ec6-8164-c5a9f979002c'
let test2 = 'a9fe127b-64c2-1ec6-8164-c5a8cfe8001b'
let test21 = test11

let parentContexts = []

let url = document.URL
let urlParameters = url.split("/")
console.log(urlParameters.length)

let centreContextId = urlParameters[6]
let currentContextId = urlParameters[7]

console.log(centreContextId, currentContextId)

let test1Context = '{"getters":{"isLoggedAnAdmin":true,"isLoggedAParentAdmin":false,"isLoggedAnEditor":true,"isLoggedAMember":true,"currentSectionPaths":[],"currentSection":null,"getActualLevels":1},"state":{"initiative":{"id":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","creator":{"c1Id":"c0a8bc01-61d7-1c46-8161-d7e3d0eb0003","auth0Ids":[],"email":null,"username":"","twitterHandle":"","facebookHandle":"","linkedinHandle":"","nickname":"philh","pictureUrl":"https://s.gravatar.com/avatar/424e9a08ab522602604502eaba602cf1?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fph.png","shortBio":"","longBio":null,"useUploadedPicture":null},"status":"ENABLED","meta":{"name":"test 0","driver":"asdsada","imageFile":null,"creationDate":1532324512601,"color":"#009ee3","modelEnabled":true,"visibility":null,"tags":[]},"ownAssetsIds":["a9fe127b-64c2-1ec6-8164-c5a883700013"],"assets":[{"assetId":"a9fe127b-64c2-1ec6-8164-c5a883700013","assetName":"tokens","totalExistingTokens":0,"holderId":null,"holderName":null,"ownedByThisHolder":0,"totalTransferredToSubinitiatives":0,"totalTransferredToUsers":0,"totalPending":0,"totalUnderThisHolder":0,"transferredToSubinitiatives":[],"transferredToUsers":[],"transfersPending":[]}],"subInitiatives":[],"parents":[],"initiativeMembers":{"initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","initiativeName":"test 0","members":[{"id":"a9fe127b-64c2-1ec6-8164-c5a883650010","user":{"c1Id":"c0a8bc01-61d7-1c46-8161-d7e3d0eb0003","auth0Ids":[],"email":null,"username":"","twitterHandle":"","facebookHandle":"","linkedinHandle":"","nickname":"philh","pictureUrl":"https://s.gravatar.com/avatar/424e9a08ab522602604502eaba602cf1?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fph.png","shortBio":"","longBio":null,"useUploadedPicture":null},"role":"ADMIN","receivedAssets":[{"assetId":"a9fe127b-64c2-1ec6-8164-c5a883700013","assetName":"tokens","totalExistingTokens":0,"holderId":"c0a8bc01-61d7-1c46-8161-d7e3d0eb0003","holderName":null,"ownedByThisHolder":0,"totalTransferredToSubinitiatives":0,"totalTransferredToUsers":0,"totalPending":0,"totalUnderThisHolder":0,"transferredToSubinitiatives":[],"transferredToUsers":[],"transfersPending":[]}]}],"subinitiativesMembers":[]},"loggedMembership":[{"id":"a9fe127b-64c2-1ec6-8164-c5a883650010","user":{"c1Id":"c0a8bc01-61d7-1c46-8161-d7e3d0eb0003","auth0Ids":[],"email":null,"username":"","twitterHandle":"","facebookHandle":"","linkedinHandle":"","nickname":"philh","pictureUrl":"https://s.gravatar.com/avatar/424e9a08ab522602604502eaba602cf1?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fph.png","shortBio":"","longBio":null,"useUploadedPicture":null},"role":"ADMIN","receivedAssets":[]},null],"topModelSection":{"id":"a9fe127b-64c2-1ec6-8164-c5a8837a0016","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 0","description":null,"newScope":null,"subElementsLoaded":null,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[],"scope":null,"beforeElementId":null,"afterElementId":null}},"initiativeLoaded":true},"rootGetters":{"isLoggedAnAdmin":true,"isLoggedAParentAdmin":false,"isLoggedAnEditor":true,"isLoggedAMember":true,"currentSectionPaths":[],"currentSection":null,"getActualLevels":1},"rootState":{"user":{"lock":{"_events":{},"validEvents":["show","hide","unrecoverable_error","authenticated","authorization_error","hash_parsed","signin ready","signup ready","socialOrPhoneNumber ready","socialOrEmail ready","vcode ready","forgot_password ready","forgot_password submit","signin submit","signup submit","signup error","socialOrPhoneNumber submit","socialOrEmail submit","vcode submit","federated login"],"id":1,"engine":{}},"authenticated":true,"auth0state":"","profile":{"c1Id":"c0a8bc01-61d7-1c46-8161-d7e3d0eb0003","auth0Ids":[],"email":null,"username":"","twitterHandle":"","facebookHandle":"","linkedinHandle":"","nickname":"philh","pictureUrl":"https://s.gravatar.com/avatar/424e9a08ab522602604502eaba602cf1?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fph.png","shortBio":"","longBio":null,"useUploadedPicture":null},"triggerUpdateNotifications":true,"intervalId":null,"intervalId2":null},"initiative":{"initiative":{"id":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","creator":{"c1Id":"c0a8bc01-61d7-1c46-8161-d7e3d0eb0003","auth0Ids":[],"email":null,"username":"","twitterHandle":"","facebookHandle":"","linkedinHandle":"","nickname":"philh","pictureUrl":"https://s.gravatar.com/avatar/424e9a08ab522602604502eaba602cf1?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fph.png","shortBio":"","longBio":null,"useUploadedPicture":null},"status":"ENABLED","meta":{"name":"test 0","driver":"asdsada","imageFile":null,"creationDate":1532324512601,"color":"#009ee3","modelEnabled":true,"visibility":null,"tags":[]},"ownAssetsIds":["a9fe127b-64c2-1ec6-8164-c5a883700013"],"assets":[{"assetId":"a9fe127b-64c2-1ec6-8164-c5a883700013","assetName":"tokens","totalExistingTokens":0,"holderId":null,"holderName":null,"ownedByThisHolder":0,"totalTransferredToSubinitiatives":0,"totalTransferredToUsers":0,"totalPending":0,"totalUnderThisHolder":0,"transferredToSubinitiatives":[],"transferredToUsers":[],"transfersPending":[]}],"subInitiatives":[],"parents":[],"initiativeMembers":{"initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","initiativeName":"test 0","members":[{"id":"a9fe127b-64c2-1ec6-8164-c5a883650010","user":{"c1Id":"c0a8bc01-61d7-1c46-8161-d7e3d0eb0003","auth0Ids":[],"email":null,"username":"","twitterHandle":"","facebookHandle":"","linkedinHandle":"","nickname":"philh","pictureUrl":"https://s.gravatar.com/avatar/424e9a08ab522602604502eaba602cf1?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fph.png","shortBio":"","longBio":null,"useUploadedPicture":null},"role":"ADMIN","receivedAssets":[{"assetId":"a9fe127b-64c2-1ec6-8164-c5a883700013","assetName":"tokens","totalExistingTokens":0,"holderId":"c0a8bc01-61d7-1c46-8161-d7e3d0eb0003","holderName":null,"ownedByThisHolder":0,"totalTransferredToSubinitiatives":0,"totalTransferredToUsers":0,"totalPending":0,"totalUnderThisHolder":0,"transferredToSubinitiatives":[],"transferredToUsers":[],"transfersPending":[]}]}],"subinitiativesMembers":[]},"loggedMembership":[{"id":"a9fe127b-64c2-1ec6-8164-c5a883650010","user":{"c1Id":"c0a8bc01-61d7-1c46-8161-d7e3d0eb0003","auth0Ids":[],"email":null,"username":"","twitterHandle":"","facebookHandle":"","linkedinHandle":"","nickname":"philh","pictureUrl":"https://s.gravatar.com/avatar/424e9a08ab522602604502eaba602cf1?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fph.png","shortBio":"","longBio":null,"useUploadedPicture":null},"role":"ADMIN","receivedAssets":[]},null],"topModelSection":{"id":"a9fe127b-64c2-1ec6-8164-c5a8837a0016","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 0","description":null,"newScope":null,"subElementsLoaded":null,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[],"scope":null,"beforeElementId":null,"afterElementId":null}},"initiativeLoaded":true},"initiativesTree":{"myInitiativesTree":[],"loadingMyInitiatives":true,"currentInitiativeTree":[]},"messages":{"outputMessage":"Output message","showOutputMessage":false},"support":{"contentAnimationType":"slideToDown","triggerUpdateAssets":false,"triggerUpdateSectionCards":false,"triggerUpdateNotifications":true,"userEmailNotVerified":false,"expandNav":false,"expandModelNav":true,"windowIsSmall":false,"draggingElement":null,"triggerSectionDraggingState":false,"triggerCardDraggingState":false,"createNewCardLocation":null},"model":{"currentSectionGenealogy":null},"sectionsTree":{"sectionsTree":[],"triggerUpdateExpands":false},"socket":{"connected":true},"pushManager":{"pushedIds":[]},"markdown":{"data":[]},"viewParameters":{"levels":1,"isInfiniteLevels":false,"cardsType":"card","showPrivate":true,"showShared":true,"showCommon":true,"showPrivateSections":true,"showSharedSections":true,"showCommonSections":true}}}'

const state = {
  sectionsTree: [],
  triggerUpdateExpands: false,
  hasParents: false
}

const getSectionDataAtCoord = function (sectionsTree, coord) {
  let subTree = sectionsTree

  /* navigate inside the tree up the penultimate coord */
  //console.log(JSON.stringify(subTree))
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
  let shownInSubsections = false
  for (let ix in subTree) {
    if (subTree[ix].section.id === sectionId) {
      return true
    } else {
      /* recursively call this method in the subsectionsData if they
      are expanded */
      if (subTree[ix].expand) {
         shownInSubsections = shownInSubsections || isSectionShown(subTree[ix].subsectionsData, sectionId)
      }
    }
  }
  /* if tree is empty or not found in the for above */
  return shownInSubsections
}

const getSectionCoordsFromId = function (sectionData, sectionId, thisCoord, foundCoords) {
  if (sectionData.section.id === sectionId) {
    foundCoords.push(thisCoord.slice())
  }
  for (let ix in sectionData.subsectionsData) {
    getSectionCoordsFromId(sectionData.subsectionsData[ix], sectionId, thisCoord.concat(ix), foundCoords)
  }
}

const getters = {
  getSectionDataAtCoord: (state) => (coord) => {
    return getSectionDataAtCoord(state.sectionsTree, coord)
  },
  isSectionShown: (state) => (sectionId) => {
    return isSectionShown(state.sectionsTree, sectionId)
  },
  getSectionCoordsFromId: (state) => (sectionId) => {
    let foundCoords = []
    /* updat foundCoords by reference */
    getSectionCoordsFromId(state.sectionsTree[0], sectionId, [0], foundCoords)
    return foundCoords
  },
  getParentsData: (state) => (contextId) => {
    return parentContexts
  }
}

const mutations = {
  triggerUpdateExpands: (state, payload) => {
    state.triggerUpdateExpands = !state.triggerUpdateExpands
  },
  hasParents: (state, payload) => {
    state.hasParents =! state.hasParents
  },
  setSectionsTree: (state, payload) => {
    state.sectionsTree = payload
  },
  appendSectionDataCommit: (state, payload) => {
    if (payload.coord.length === 1) {
      if (payload.coord[0] === 0) {
        /* initialize tree */
        let expand = false
        if(payload.parents.length){
          parentContexts = payload.parents
          expand = true
        }
        state.sectionsTree = [{
          coordinate: [0],
          inSection: null,
          section: payload.sectionData.section,
          subsectionsData: payload.sectionData.subsectionsData,
          subsectionsDataSet: true,
          expand: expand
        }]
        return
      }
    }

    //console.log(JSON.stringify(payload), 'bandapelaw', payload.sectionData.section.title)

    let sectionData = getSectionDataAtCoord(state.sectionsTree, payload.coord)

    if (sectionData && payload.sectionData) {
      if (sectionData.section && payload.sectionData.section) {
        /* this function always overwrites the sectionData so make sure you
           are overwritting the expected section */
        if (sectionData.section.id === payload.sectionData.section.id) {
          let inSectionData = null
          if (payload.coord.length > 1) {
            inSectionData = getSectionDataAtCoord(state.sectionsTree, payload.coord.slice(0, -1))
          }

          sectionData.inSection = inSectionData.section
          sectionData.section = payload.sectionData.section
          /* only update subsections they are different from the current ones */
          let newSize = payload.sectionData.subsectionsData.length
          let oldSize = sectionData.subsectionsData.length

          /* force the new list of subsections */
          for (let ix = 0; ix < newSize; ix++) {
            if (ix < sectionData.subsectionsData.length) {
              if (sectionData.subsectionsData[ix].section.id !== payload.sectionData.subsectionsData[ix].section.id) {
                sectionData.subsectionsData[ix] = payload.sectionData.subsectionsData[ix]
              }
            } else {
              sectionData.subsectionsData.push(payload.sectionData.subsectionsData[ix])
            }
          }

          /* remove excess sections */
          if (newSize < oldSize) {
            let nDeleted = oldSize - newSize
            sectionData.subsectionsData.splice(-nDeleted, nDeleted)
          }

          sectionData.subsectionsDataSet = true
        }
      }
    }
  }
}

const actions = {
  resetSectionsTree: (context, payload) => {

    context.dispatch('appendSectionData', {
      sectionId: payload.baseSectionId,
      coord: [0]
    }).then(() => {
      context.dispatch('updateCurrentSection', payload.currentSectionId)
    })
  },

  /* This will add a section and their immediate subsections at a given coordinate.
  Its an asynchronoues method that gets the data from the backend */
  appendSectionData: (context, payload) => {
    return new Promise((resolve, reject) => {
      Vue.axios.get('/1/model/section/' + payload.sectionId,
      {
        params: {
          levels: 2,
          parentSectionId: payload.parentSectionId ? payload.parentSectionId : '',
          onlySections: true
        }
      }).then((response) => {
        if (response.data.result === 'success') {

          // console.log(JSON.stringify(response))
          payload.parents = []
          // CASE 1 : Test0 is centre and Test1 is current -> http://localhost:8080/#/app/ctx/a9fe127b-64c2-1ec6-8164-c5a8837a0016/a9fe127b-64c2-1ec6-8164-c5a8baae0018/messages
          if(centreContextId == test0 && currentContextId == test1 && centreContextId == response.data.data.id){
            response = JSON.parse('{"data":{"result":"success","message":"section retrieved","data":{"id":"a9fe127b-64c2-1ec6-8164-c5a8837a0016","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 0","description":null,"newScope":null,"subElementsLoaded":true,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[{"id":"a9fe127b-64c2-1ec6-8164-c5a8baae0018","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 1","description":"","newScope":null,"subElementsLoaded":true,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[{"id":"a9fe127b-64c2-1ec6-8164-c5a8837a0016","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 0","description":null,"newScope":null,"subElementsLoaded":null,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[],"scope":null,"beforeElementId":null,"afterElementId":null}],"scope":"COMMON","beforeElementId":"a9fe127b-64c2-1ec6-8164-c5a8cfe8001b","afterElementId":null},{"id":"a9fe127b-64c2-1ec6-8164-c5a8cfe8001b","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 2","description":"","newScope":null,"subElementsLoaded":true,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[{"id":"a9fe127b-64c2-1ec6-8164-c5a8837a0016","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 0","description":null,"newScope":null,"subElementsLoaded":null,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[],"scope":null,"beforeElementId":null,"afterElementId":null}],"scope":"COMMON","beforeElementId":null,"afterElementId":"a9fe127b-64c2-1ec6-8164-c5a8baae0018"}],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[],"scope":null,"beforeElementId":null,"afterElementId":null}}}')
          }
          // CASE 2 : Test0 is the centre and Test2 is the current -> http://localhost:8080/#/app/ctx/a9fe127b-64c2-1ec6-8164-c5a8837a0016/a9fe127b-64c2-1ec6-8164-c5a8cfe8001b/messages
          else if(centreContextId == test0 && currentContextId == test2 && centreContextId == response.data.data.id){
            response = JSON.parse('{"data":{"result":"success","message":"section retrieved","data":{"id":"a9fe127b-64c2-1ec6-8164-c5a8837a0016","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 0","description":null,"newScope":null,"subElementsLoaded":true,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[{"id":"a9fe127b-64c2-1ec6-8164-c5a8baae0018","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 1","description":"","newScope":null,"subElementsLoaded":true,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[{"id":"a9fe127b-64c2-1ec6-8164-c5a8837a0016","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 0","description":null,"newScope":null,"subElementsLoaded":null,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[],"scope":null,"beforeElementId":null,"afterElementId":null}],"scope":"COMMON","beforeElementId":"a9fe127b-64c2-1ec6-8164-c5a8cfe8001b","afterElementId":null},{"id":"a9fe127b-64c2-1ec6-8164-c5a8cfe8001b","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 2","description":"","newScope":null,"subElementsLoaded":true,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[{"id":"a9fe127b-64c2-1ec6-8164-c5a8837a0016","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 0","description":null,"newScope":null,"subElementsLoaded":null,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[],"scope":null,"beforeElementId":null,"afterElementId":null}],"scope":"COMMON","beforeElementId":null,"afterElementId":"a9fe127b-64c2-1ec6-8164-c5a8baae0018"}],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[],"scope":null,"beforeElementId":null,"afterElementId":null}}}')
          }          
          // CASE 3 : Test1 is centre and Test1,1 is the current -> http://localhost:8080/#/app/ctx/a9fe127b-64c2-1ec6-8164-c5a8baae0018/a9fe127b-64c2-1ec6-8164-c5a8ec21001e/messages
         else if(centreContextId == test1 && currentContextId == test11){
            if (response.data.data.id == test0)
              response = JSON.parse('{"data":{"result":"success","message":"section retrieved","data":{"id":"a9fe127b-64c2-1ec6-8164-c5a8837a0016","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 0","description":null,"newScope":null,"subElementsLoaded":true,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[{"id":"a9fe127b-64c2-1ec6-8164-c5a8baae0018","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 1","description":"","newScope":null,"subElementsLoaded":true,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[{"id":"a9fe127b-64c2-1ec6-8164-c5a8837a0016","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 0","description":null,"newScope":null,"subElementsLoaded":null,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[],"scope":null,"beforeElementId":null,"afterElementId":null}],"scope":"COMMON","beforeElementId":null,"afterElementId":null}],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[],"scope":null,"beforeElementId":null,"afterElementId":null}},"status":200,"statusText":"OK","headers":{"pragma":"no-cache","date":"Mon, 23 Jul 2018 12:01:32 GMT","x-content-type-options":"nosniff","x-powered-by":"Express","x-frame-options":"DENY","content-type":"application/json;charset=UTF-8","cache-control":"no-cache, no-store, max-age=0, must-revalidate","transfer-encoding":"chunked","connection":"close","x-xss-protection":"1; mode=block","expires":"0"},"config":{"transformRequest":{},"transformResponse":{},"timeout":0,"xsrfCookieName":"XSRF-TOKEN","xsrfHeaderName":"X-XSRF-TOKEN","maxContentLength":-1,"headers":{"Accept":"application/json, text/plain, */*","Authorization":"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InBoaWxoQHguY29tIiwibmFtZSI6InBoaWxoQHguY29tIiwicGljdHVyZSI6Imh0dHBzOi8vcy5ncmF2YXRhci5jb20vYXZhdGFyLzQyNGU5YTA4YWI1MjI2MDI2MDQ1MDJlYWJhNjAyY2YxP3M9NDgwJnI9cGcmZD1odHRwcyUzQSUyRiUyRmNkbi5hdXRoMC5jb20lMkZhdmF0YXJzJTJGcGgucG5nIiwibmlja25hbWUiOiJwaGlsaCIsImFwcF9tZXRhZGF0YSI6eyJzY29wZSI6InJvbGVfdXNlciJ9LCJzY29wZSI6InJvbGVfdXNlciIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiY2xpZW50SUQiOiJrdURYMVpWb3JBbHk1UFlkeVY3MjF6Um9UZjBLMG9ybSIsInVwZGF0ZWRfYXQiOiIyMDE4LTA3LTIyVDE0OjI0OjQyLjUwNloiLCJ1c2VyX2lkIjoiYXV0aDB8NTllZGFjZGJjMzBhMzgwNTNhYjViYzBkIiwiaWRlbnRpdGllcyI6W3sidXNlcl9pZCI6IjU5ZWRhY2RiYzMwYTM4MDUzYWI1YmMwZCIsInByb3ZpZGVyIjoiYXV0aDAiLCJjb25uZWN0aW9uIjoidGVzdCIsImlzU29jaWFsIjpmYWxzZX1dLCJjcmVhdGVkX2F0IjoiMjAxNy0xMC0yM1QwODo0ODoyNy43ODJaIiwiaXNzIjoiaHR0cHM6Ly9jb2xsZWN0aXZlb25lLmF1dGgwLmNvbS8iLCJzdWIiOiJhdXRoMHw1OWVkYWNkYmMzMGEzODA1M2FiNWJjMGQiLCJhdWQiOiJrdURYMVpWb3JBbHk1UFlkeVY3MjF6Um9UZjBLMG9ybSIsImlhdCI6MTUzMjI3MDM2MywiZXhwIjoxNTM0ODYyMzYzfQ.BxLq1wFuG-DLGGlBCeBT4CRQmX7PeqgGnNHnuzhqJYc"},"method":"get","params":{"levels":2,"parentSectionId":"","onlySections":true},"url":"/1/model/section/a9fe127b-64c2-1ec6-8164-c5a8837a0016"},"request":{}}')
            else if(response.data.data.id == test1)
              payload.coord = [0]
              payload.parents = ["Test 0"]
              context.commit('hasParents')
          }
          // CASE 4 : Test1.1 is centre and Test1.1 is the current -> http://localhost:8080/#/app/ctx/a9fe127b-64c2-1ec6-8164-c5a8ec21001e/a9fe127b-64c2-1ec6-8164-c5a8ec21001e/messages
         else if(centreContextId == test11 && currentContextId == test11){
            if (response.data.data.id == test0)
              response = JSON.parse('{"data":{"result":"success","message":"section retrieved","data":{"id":"a9fe127b-64c2-1ec6-8164-c5a8837a0016","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 0","description":null,"newScope":null,"subElementsLoaded":true,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[{"id":"a9fe127b-64c2-1ec6-8164-c5a8baae0018","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 1","description":"","newScope":null,"subElementsLoaded":true,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[{"id":"a9fe127b-64c2-1ec6-8164-c5a8837a0016","initiativeId":"a9fe127b-64c2-1ec6-8164-c5a8835b000d","isTopModelSection":null,"isSubsection":null,"parentSectionId":null,"parentSectionTitle":null,"title":"test 0","description":null,"newScope":null,"subElementsLoaded":null,"cardsWrappersPrivate":[],"cardsWrappersShared":[],"cardsWrappersCommon":[],"subsectionsCommon":[],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[],"scope":null,"beforeElementId":null,"afterElementId":null}],"scope":"COMMON","beforeElementId":null,"afterElementId":null}],"subsectionsShared":[],"subsectionsPrivate":[],"inSections":[],"scope":null,"beforeElementId":null,"afterElementId":null}},"status":200,"statusText":"OK","headers":{"pragma":"no-cache","date":"Mon, 23 Jul 2018 12:01:32 GMT","x-content-type-options":"nosniff","x-powered-by":"Express","x-frame-options":"DENY","content-type":"application/json;charset=UTF-8","cache-control":"no-cache, no-store, max-age=0, must-revalidate","transfer-encoding":"chunked","connection":"close","x-xss-protection":"1; mode=block","expires":"0"},"config":{"transformRequest":{},"transformResponse":{},"timeout":0,"xsrfCookieName":"XSRF-TOKEN","xsrfHeaderName":"X-XSRF-TOKEN","maxContentLength":-1,"headers":{"Accept":"application/json, text/plain, */*","Authorization":"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InBoaWxoQHguY29tIiwibmFtZSI6InBoaWxoQHguY29tIiwicGljdHVyZSI6Imh0dHBzOi8vcy5ncmF2YXRhci5jb20vYXZhdGFyLzQyNGU5YTA4YWI1MjI2MDI2MDQ1MDJlYWJhNjAyY2YxP3M9NDgwJnI9cGcmZD1odHRwcyUzQSUyRiUyRmNkbi5hdXRoMC5jb20lMkZhdmF0YXJzJTJGcGgucG5nIiwibmlja25hbWUiOiJwaGlsaCIsImFwcF9tZXRhZGF0YSI6eyJzY29wZSI6InJvbGVfdXNlciJ9LCJzY29wZSI6InJvbGVfdXNlciIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiY2xpZW50SUQiOiJrdURYMVpWb3JBbHk1UFlkeVY3MjF6Um9UZjBLMG9ybSIsInVwZGF0ZWRfYXQiOiIyMDE4LTA3LTIyVDE0OjI0OjQyLjUwNloiLCJ1c2VyX2lkIjoiYXV0aDB8NTllZGFjZGJjMzBhMzgwNTNhYjViYzBkIiwiaWRlbnRpdGllcyI6W3sidXNlcl9pZCI6IjU5ZWRhY2RiYzMwYTM4MDUzYWI1YmMwZCIsInByb3ZpZGVyIjoiYXV0aDAiLCJjb25uZWN0aW9uIjoidGVzdCIsImlzU29jaWFsIjpmYWxzZX1dLCJjcmVhdGVkX2F0IjoiMjAxNy0xMC0yM1QwODo0ODoyNy43ODJaIiwiaXNzIjoiaHR0cHM6Ly9jb2xsZWN0aXZlb25lLmF1dGgwLmNvbS8iLCJzdWIiOiJhdXRoMHw1OWVkYWNkYmMzMGEzODA1M2FiNWJjMGQiLCJhdWQiOiJrdURYMVpWb3JBbHk1UFlkeVY3MjF6Um9UZjBLMG9ybSIsImlhdCI6MTUzMjI3MDM2MywiZXhwIjoxNTM0ODYyMzYzfQ.BxLq1wFuG-DLGGlBCeBT4CRQmX7PeqgGnNHnuzhqJYc"},"method":"get","params":{"levels":2,"parentSectionId":"","onlySections":true},"url":"/1/model/section/a9fe127b-64c2-1ec6-8164-c5a8837a0016"},"request":{}}')
            else if(response.data.data.id == test1)
              payload.coord = [0]
              payload.parents = [{name: "Test 0", id: test0}, {name: "Test 1", id: test1}]
              context.commit('hasParents')
          }
          let section = response.data.data
          //console.log(section.title)

          /* sort subsections for this user */
          let subsections = getSortedSubsections(section, true, true, true)
          //console.log(JSON.stringify(subsections))

          let subsectionsData = []

          for (let ix = 0; ix < subsections.length; ix++) {
            subsectionsData.push({
              coordinate: payload.coord.concat(ix),
              inSection: section,
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

          context.commit('appendSectionDataCommit', {
            sectionData: sectionData,
            coord: payload.coord,
            parents: payload.parents
          })

          resolve()
        } else {
          reject()
        }
      })
    })
  },

  collapseSubsection: (context, coord) => {
    let sectionData = getSectionDataAtCoord(state.sectionsTree, coord)
    sectionData.expand = false
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

    if (sectionData.subsectionsData.length === 0) {
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

    context.dispatch('appendSectionData', {
      sectionId: sectionData.subsectionsData[ixNext].section.id,
      parentSectionId: sectionData.section.id,
      coord: newCoord
    }).then(() => {
        /* after loading, recursive call to this same action to keep expanding the sections */
        if (payload.expandIds.length > 0) {
          payload.expandIds.shift()
          context.dispatch('expandSectionAndContinue', {
            sectionData: newSectionData,
            expandIds: payload.expandIds,
            coord: newCoord
          })
        } else {
          context.commit('triggerUpdateExpands')
        }
      })
  },

  expandSubsection: (context, coord) => {
    let sectionData = getSectionDataAtCoord(state.sectionsTree, coord)
    sectionData.expand = true

    /* force subsections to be appended */
    if (!sectionData.subsectionsDataSet) {
      context.dispatch('appendSectionData', {
        parentSectionId: sectionData.inSection.id,
        sectionId: sectionData.section.id,
        coord: coord
      })
    }

    /* besides expanding, preload the subsections of each subsection */
    for (let ix = 0; ix < sectionData.subsectionsData.length; ix++) {
      if (!sectionData.subsectionsData[ix].subsectionsDataSet) {
        context.dispatch('appendSectionData', {
          sectionId: sectionData.subsectionsData[ix].section.id,
          parentSectionId: sectionData.section.id,
          coord: coord.concat(ix)
        })
      }
    }
  },

  autoExpandSectionsTree: (context, payload) => {
    if (context.state.sectionsTree.length === 0) {
      /* only check if the section tree has been already initialized */
      return
    }

    if (!context.getters.isSectionShown(payload.currentSectionId)) {
      console.log('autoexpanding unshown section ' + payload.currentSectionId)
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
  },

  updateSectionDataInTree: (context, payload) => {
    let coords = context.getters.getSectionCoordsFromId(payload.sectionId)
    for (let ix in coords) {
      let thisCoord = coords[ix]
      let currentSectionData = context.getters.getSectionDataAtCoord(thisCoord)

      context.dispatch('appendSectionData', {
        sectionId: payload.sectionId,
        parentSectionId: currentSectionData.inSection ? currentSectionData.inSection.id : '',
        coord: thisCoord
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
