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

    store.commit('setCurrentSection', null)

    next({
      name: 'InitiativeModel',
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

      let nextPars = {
        name: toName,
        params: {
          sectionId: to.params.sectionId,
          initiativeId: to.params.initiativeId
        },
        replace: true
      }
      next(nextPars)
      return
    }

    next()
    return
  }

  next()
}
