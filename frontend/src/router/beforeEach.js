import { store } from '../store/store'

export default (to, from, next) => {
  /** adhock logic to handle route transitions so that the sub-section is kept
  when switching among initiatives, and the animation is selected accordingly */
  console.log('from ' + from.name + ' to ' + to.name)

  if (to.name === 'Initiative') {
    /** always redirect if target is base initiative.
     *  The path level 1 is 'inits' and the path level '3'
     *  is the initaitive section
    */

    next({
      name: 'InitiativeOverview',
      params: {
        initiativeId: to.params.initiativeId
      },
      replace: true
    })
    return
  } else {
    /** select animation based on column and level */
    var animation = ''

    /* moving within the same initiative */
    if (from.params.initiativeId === to.params.initiativeId) {
      if (from.meta.column < to.meta.column) {
        animation = 'slideToLeft'
      } else {
        animation = 'slideToRight'
      }
    }
    store.commit('setContentAnimationType', animation)
  }

  var toModelSection = false
  for (var ix in to.matched) {
    if (to.matched[ix].name === 'ModelSectionContent') {
      toModelSection = true
    }
  }

  if (toModelSection) {
    /* query parameters are:
      - stored in a global state
      - if the parameter is not in the "to" page, the value of the global state is used
      - if the parameter is in the "to" request, the global state is set and the value is used
      - all the values of the global state are added as query parameters, even if not used, to enable url sharing
    */

    /* keep the levels and cards url parameters when switching among views. */
    if (to.query.levels != null) store.commit('setLevels', to.query.levels)
    if (to.query.cardsType != null) store.commit('setCardsType', to.query.cardsType)
    if (to.query.showPrivate != null) store.commit('setDhowPrivate', to.query.showPrivate)
    if (to.query.showShared != null) store.commit('setShowShared', to.query.showShared)
    if (to.query.showCommon != null) store.commit('setShowCommon', to.query.showCommon)

    let toQuery = {
      toLevels: store.state.viewParameters.levels,
      toCardsType: store.state.viewParameters.cardsType,
      toShowPrivate: store.state.viewParameters.showPrivate,
      toShowShared: store.state.viewParameters.showShared,
      toShowCommon: store.state.viewParameters.showCommon
    }

    let toName = to.name
    if (toName === 'ModelSectionContent') {
      /* just keep the current tab when switching among sections */
      switch (from.name) {
        case 'ModelSectionMessages':
          toName = 'ModelSectionMessages'
          break

        case 'ModelSectionCards':
        case 'ModelSectionCard':
          toName = 'ModelSectionCards'
          break

        default:
          next()
          return
      }
    }

    if (toName !== from.name) {
      let nextPars = {
        name: toName,
        params: {
          sectionId: to.params.sectionId
        },
        query: toQuery,
        replace: true
      }
      console.log(nextPars)
      next(nextPars)
      return
    } else {
      next()
      return
    }
  }

  if (to.name === 'ModelSectionRead' || to.name === 'ModelSectionReadCard') {
    let fromCardsType = from.query.cardsType ? from.query.cardsType : 'card'
    let toCardsType = to.query.cardsType ? to.query.cardsType : fromCardsType

    next({
      name: to.name,
      params: {
        sectionId: to.params.sectionId
      },
      query: {
        cardsType: toCardsType
      },
      replace: true
    })
    return
  }

  next()
}
