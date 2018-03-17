import { store } from '../store/store'
export default (to, from, next) => {
  /** adhock logic to handle route transitions so that the sub-section is kept
  when switching among initiatives, and the animation is selected accordingly */

  if (to.name === 'Initiative') {
    /** always redirect if target is base initiative.
     *  The path level 1 is 'inits' and the path level '3'
     *  is the initaitive section
    */

    var subsection = 'overview'
    if (from.matched[1].path === '/app/inits' && from.matched.length >= 4) {
      switch (from.matched[3].name) {
        case 'InitiativeOverview':
          subsection = 'overview'
          break

        case 'InitiativeTimeline':
          subsection = 'timeline'
          break

        case 'InitiativeModel':
          subsection = 'model'
          break

        case 'InitiativePeople':
          subsection = 'people'
          break

        case 'InitiativeAssignations':
          subsection = 'assignations'
          break

      }
    }
    next(to.path + '/' + subsection)
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
    } else {
      var fromCoord = store.getters.initiativeCoordinate(from.params.initiativeId)
      var toCoord = store.getters.initiativeCoordinate(to.params.initiativeId)

      /* if within the same level */
      var ixLevel = 0
      var moveDown = true

      while (true) {
        if (fromCoord.length < ixLevel + 1) {
          moveDown = true
          break
        }
        if (toCoord.length < ixLevel + 1) {
          moveDown = false
          break
        }
        if (fromCoord[ixLevel] !== toCoord[ixLevel]) {
          moveDown = toCoord[ixLevel] > fromCoord[ixLevel]
          break
        }
        ixLevel = ixLevel + 1
        if (ixLevel > 50) {
          break
        }
      }
      animation = moveDown ? 'slideToUp' : 'slideToDown'
    }

    store.commit('setContentAnimationType', animation)

    next()
  }
}
