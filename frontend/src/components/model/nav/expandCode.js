const menuStringToArray = function (codedString) {
  if (codedString === '') return []
  let jsonStr = codedString.replace(/C/g, '[').replace(/D/g, ']').replace(/Q/g, ',')
  let arr = JSON.parse(jsonStr)
  return arr
}

const menuArrayToString = function (arr) {
  let jsonStr = JSON.stringify(arr)
  let strCoded = jsonStr.replace(/\[/g, 'C').replace(/\]/g, 'D').replace(/,/g, 'Q')
  return strCoded
}

export {
  menuStringToArray,
  menuArrayToString
}
