const getArrayFromList = function (list) {
  let array = []

  /* start from first elements */
  let firstElements = list.filter((e) => { return e.afterElementId == null })
  firstElements.forEach((firstEl) => {
    /* add this first element */
    array.push(firstEl)
    removeFromList(list, firstEl)

    /* add all connected elements */
    let next = list.find((e) => { return e.afterElementId === firstEl.id })
    while (next != null) {
      array.push(next)
      removeFromList(list, next)
      next = list.find((e) => { return e.afterElementId === next.id })
    }
  })

  while (list.length > 0) {
    let firstEl = list.shift()
    /* add this first element */
    array.push(firstEl)
    removeFromList(list, firstEl)

    /* add all connected elements */
    let next = list.find((e) => { return e.afterElementId === firstEl.id })
    while (next != null) {
      array.push(next)
      removeFromList(list, next)
      next = list.find((e) => { return e.afterElementId === next.id })
    }
  }

  return array
}

const removeFromList = function (list, el) {
  let ix = list.findIndex((e) => el.id === e.id)
  if (ix !== -1) {
    return list.splice(ix, 1)
  }
  return null
}

const appendElementsToBase = function (baseList, toAddList) {
  let relativeToBase = toAddList.filter((elementToAdd) => {
    let ixInBase = baseList.findIndex((elementInBase) => {
      return (elementToAdd.beforeElementId === elementInBase.id) ||
        (elementToAdd.afterElementId === elementInBase.id)
    })

    return ixInBase !== -1
  })

  /* add those relative to base list  */
  relativeToBase.forEach((elementRelativeToBase) => {
    let nextElement = elementRelativeToBase

    do {
      let ixInBase = baseList.findIndex((elementInBase) => {
        return (nextElement.beforeElementId === elementInBase.id) ||
          (nextElement.afterElementId === elementInBase.id)
      })

      if (nextElement.beforeElementId === baseList[ixInBase].id) {
        baseList.splice(ixInBase, 0, nextElement)
      } else {
        baseList.splice(ixInBase + 1, 0, nextElement)
      }
      removeFromList(toAddList, nextElement)

      nextElement = toAddList.find((e) => { return e.afterElementId === nextElement.id })
    } while (nextElement != null)
  })

  /* append all remaining */
  baseList = baseList.concat(getArrayFromList(toAddList))

  return baseList
}

const getSortedCardWrappers = function (section, showCommon, showShared, showPrivate) {
  let allCardWrappers = []

  if (showCommon) {
    allCardWrappers = getArrayFromList(section.cardsWrappersCommon.slice())
  }

  let nonCommonCards = []
  if (showPrivate) {
    nonCommonCards = nonCommonCards.concat(section.cardsWrappersPrivate.slice())
  }

  if (showShared) {
    nonCommonCards = nonCommonCards.concat(section.cardsWrappersShared.slice())
  }

  allCardWrappers = appendElementsToBase(allCardWrappers, nonCommonCards)

  return allCardWrappers
}

const getSortedSubsections = function (section, showCommon, showShared, showPrivate) {
  let allSubsections = []

  if (showCommon) {
    allSubsections = getArrayFromList(section.subsectionsCommon.slice())
  }

  let nonCommonSubsections = []
  if (showPrivate) {
    nonCommonSubsections = nonCommonSubsections.concat(section.subsectionsPrivate.slice())
  }

  if (showShared) {
    nonCommonSubsections = nonCommonSubsections.concat(section.subsectionsShared.slice())
  }

  return appendElementsToBase(allSubsections, nonCommonSubsections)
}

export {
  getSortedCardWrappers,
  getSortedSubsections,
  getArrayFromList
}
