import { store } from '../store/store'
export default (to, from, next) => {
  /** adhock logic to handle route transitions so that the sub-section is kept
  when switching among initiatives, and the animation is selected accordingly */

  // var fromLevel = this.$store.getters.initiativeLevel(from.params.initiativeId)
  // var toLevel = this.$store.getters.initiativeLevel(to.params.initiativeId)

  if (to.name === 'Initiative') {
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
  }

  var animation = ''
  if (from.meta.column < to.meta.column) {
    animation = 'slideToLeft'
  } else {
    animation = 'slideToRight'
  }
  store.commit('setContentAnimationType', animation)

  next()
}
