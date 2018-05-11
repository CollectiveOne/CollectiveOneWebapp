import { store } from '../store/store'
export default (to, from, next) => {
  /** adhock logic to handle route transitions so that the sub-section is kept
  when switching among initiatives, and the animation is selected accordingly */
  console.log('from: ' + from.name + '-' + JSON.stringify(from.params))
  console.log('to: ' + to.name + '-' + JSON.stringify(to.params))

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

    next()
  }

  var toModelSection = false
  for (var ix in to.matched) {
    if (to.matched[ix].name === 'ModelSectionContent') {
      toModelSection = true
    }
  }

  if (toModelSection) {
    /* keep the levels and cards url parameters when switching among views. */
    var fromLevels = from.query.levels ? from.query.levels : '1'
    var fromCardsType = from.query.cardsType ? from.query.cardsType : 'card'

    var toLevels = to.query.levels ? to.query.levels : fromLevels
    var toCardsType = to.query.cardsType ? to.query.cardsType : fromCardsType

    if (to.name === 'ModelSectionContent') {
      /* just keep the current tab when switching among sections */
      switch (from.name) {
        case 'ModelSectionMessages':
        next({
            name: 'ModelSectionMessages',
            params: {
              sectionId: to.params.sectionId
            },
            query: {
              levels: toLevels,
              cardsType: toCardsType
            },
            replace: true
          })
          break

        case 'ModelSectionCards':
        case 'ModelSectionCard':
          next({
            name: 'ModelSectionCards',
            params: {
              sectionId: to.params.sectionId
            },
            query: {
              levels: toLevels,
              cardsType: toCardsType
            },
            replace: true})
          break

        default:
          next()
          break
      }
    } else {
      if (
        (toLevels !== fromLevels) ||
        (toCardsType !== fromCardsType) ||
        (to.name !== from.name)) {
        next({
          name: to.name,
          params: {
            sectionId: to.params.sectionId
          },
          query: {
            levels: toLevels,
            cardsType: toCardsType
          },
          replace: true})
      } else {
        next()
      }
    }
  }
}
