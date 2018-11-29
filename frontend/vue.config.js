module.exports = {
  pwa: {
    name: 'CollectiveOne',
    themeColor: '#eff3f6',
    msTileColor: '#000000',
    appleMobileWebAppCapable: 'yes',
    appleMobileWebAppStatusBarStyle: 'black',

    // configure the workbox plugin
    workboxPluginMode: 'GenerateSW',
    workboxOptions: {}
  },

  devServer: {
    proxy: {
      '/1': {
        target: 'http://localhost:3000',
        changeOrigin: true
      }
    }
  }
}
