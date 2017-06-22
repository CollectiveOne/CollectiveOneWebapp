import { store } from '../store/store'
export default (to, from, next) => {
  /** adhock logic to handle route transitions so that the sub-section is kept
  when switching among initiatives, and the animation is selected accordingly */

  if (to.name === 'Initiative') {
    /** always redirect if target is base initiative */
    var subsection = 'overview'
    if (from.matched.length === 3) {
      switch (from.matched[2].name) {
        case 'InitiativeOverview':
          subsection = 'overview'
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
    var fromCoord = store.getters.initiativeCoordinate(from.params.initiativeId)
    var toCoord = store.getters.initiativeCoordinate(to.params.initiativeId)
    console.log(fromCoord)
    console.log(toCoord)
    if (fromCoord.length === toCoord.length) {
      if (from.meta.column < to.meta.column) {
        animation = 'slideToLeft'
      } else {
        animation = 'slideToRight'
      }
    }

    store.commit('setContentAnimationType', animation)

    next()
  }
}
