const menuStringToArray = function (codedString) {
  console.log(codedString)
  if (codedString === '') return []

  let jsonStr = codedString.replace(/C/g, '[').replace(/D/g, ']').replace(/Q/g, ',')
  console.log(jsonStr)
  let arr = JSON.parse(jsonStr)
  console.log(arr)
  return arr
}

const menuArrayToString = function (arr) {
  console.log('menuArrayToString')
  console.log(arr)
  if (arr.length === 0) {
    return ''
  }

  let str

  if (arr.length > 0) {
    str = 'C' + arr[0]
  }

  if (arr.length > 1) {
    let subsectionsExpands = arr[1]
    str += 'QC'
    for (let ix = 0; ix < subsectionsExpands.length; ix++) {
      str += menuArrayToString(subsectionsExpands[ix])
      if (ix < subsectionsExpands.length - 1) {
        /* add a comma to separate elements */
        str += 'Q'
      }
    }
    str += 'D'
  }

  str += 'D'
  console.log(str)

  return str
}

export {
  menuStringToArray,
  menuArrayToString
}
