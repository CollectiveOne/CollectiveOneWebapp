const floatToChar = function (x, ndec) {
  return parseFloat(Math.round(x * Math.pow(10, ndec)) / Math.pow(10, ndec)).toFixed(ndec)
}

const getNiceTimeStr = function (dtsec) {
  var strout = ''
  if (Math.abs(dtsec) < 60) {
    strout = floatToChar(dtsec, 0) + ' sec'
  } else if (Math.abs(dtsec) < 60 * 60) {
    strout = floatToChar(dtsec / 60, 0) + ' min'
  } else if (Math.abs(dtsec) < 60 * 60 * 24) {
    strout = floatToChar(dtsec / (60 * 60), 0) + ' hr'
  } else {
    strout = floatToChar(dtsec / (60 * 60 * 24), 0) + ' days'
  }

  return strout
}

const getSecondsSince = function (timestamp) {
  var nowms = new Date().getTime()
  var dtsec = (nowms - timestamp) / 1000
  return dtsec
}

const getTimeStrUntil = function (timestamp) {
  return getNiceTimeStr(-getSecondsSince(timestamp))
}

const getTimeStrSince = function (timestamp) {
  return getNiceTimeStr(getSecondsSince(timestamp))
}

const dateString = function (timestamp) {
  var date = new Date(timestamp)
  var monthNames = [
    'Jan', 'Feb', 'Mar',
    'Apr', 'May', 'Jun', 'Jul',
    'Aug', 'Sep', 'Oct',
    'Nov', 'Dec'
  ]

  var day = date.getDate()
  var monthIndex = date.getMonth()
  var year = date.getFullYear()

  return monthNames[monthIndex] + ' ' + day + ', ' + year
}

const tokensString = function (v, n, x) {
  if (typeof v !== 'undefined') {
    var re = '\\d(?=(\\d{' + (x || 3) + '})+' + (n > 0 ? '\\.' : '$') + ')'
    return v.toFixed(Math.max(0, ~~n)).replace(new RegExp(re, 'g'), '$&,')
  } else {
    return ''
  }
}

const percStr = function (v, n, x) {
  var re = '\\d(?=(\\d{' + (x || 3) + '})+' + (n > 0 ? '\\.' : '$') + ')'
  return v.toFixed(Math.max(0, ~~n)).replace(new RegExp(re, 'g'), '$&,')
}

const amountAndPerc = function (val, tot) {
  return tot > 0 ? tokensString(val, 1) + ' / ' + percStr(val / tot * 100, 1) + '%' : '0 / 0%'
}

/* splice for strings */
const spliceString = function (string, idx, rem, str) {
  return string.slice(0, idx) + str + string.slice(idx + Math.abs(rem))
}

const urlB64ToUint8Array = function (base64String) {
  const padding = '='.repeat((4 - base64String.length % 4) % 4)
  const base64 = (base64String + padding)
    .replace(/-/g, '+')
    .replace(/_/g, '/')

  const rawData = window.atob(base64)
  const outputArray = new Uint8Array(rawData.length)

  for (let i = 0; i < rawData.length; ++i) {
    outputArray[i] = rawData.charCodeAt(i)
  }
  return outputArray
}

export {
  floatToChar,
  getTimeStrUntil,
  getTimeStrSince,
  dateString,
  tokensString,
  percStr,
  amountAndPerc,
  spliceString,
  urlB64ToUint8Array
}
