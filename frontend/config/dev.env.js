var merge = require('webpack-merge')
var prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  AUTH0_CLIENT_ID: '"kuDX1ZVorAly5PYdyV721zRoTf0K0orm"',
  AUTH0_DOMAIN: '"collectiveone.auth0.com"'
})
