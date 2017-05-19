const tokensString = function (v, n, x) {
  if (v) {
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
  return tokensString(val) + ' / ' + percStr(val / tot * 100) + '%'
}

export {
  tokensString,
  percStr,
  amountAndPerc
}
