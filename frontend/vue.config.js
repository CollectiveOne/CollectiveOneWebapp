module.exports = {
  pwa: {
    name: 'CollectiveOne',
    themeColor: '#eff3f6',
    msTileColor: '#000000',
    appleMobileWebAppCapable: 'yes',
    appleMobileWebAppStatusBarStyle: 'black',

    iconPaths: {
      favicon32: 'img/icons/icon-32x32.png',
      favicon16: 'img/icons/icon-16x16.png',
      appleTouchIcon: 'img/icons/icon-152x152.png',
      maskIcon: 'img/icons/icon-32x32.png',
      msTileImage: 'img/icons/icon-144x144.png'
    },

    // configure the workbox plugin
    workboxPluginMode: 'InjectManifest',
    workboxOptions: {
      // swSrc is required in InjectManifest mode.
      swSrc: 'public/service-worker.js'
      // ...other Workbox options...
    }
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
