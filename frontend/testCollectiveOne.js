module.exports = {
  'Demo test Collective One' : function (browser) {
    browser
      .url('localhost:8080')
      .waitForElementVisible('body', 1000)
      .waitForElementVisible('button[id=T_loginButton]', 2000)
      .click('button[id=T_loginButton]')
      .pause(2000)
      .end();
  }
};


// module.exports = {
//   'Demo test Google' : function (browser) {
//     browser
//       .url('http://www.google.co.in')
//       .waitForElementVisible('body', 1000)
//       .setValue('input[type=text]', ['vassaunt', browser.Keys.ENTER])
//       .pause(1000)
//       .assert.containsText('#main', 'vssut')
//       .end();
//   }
// };