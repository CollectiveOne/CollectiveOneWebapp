module.exports = {
  pwa: {
      // configure the workbox plugin
      workboxPluginMode: 'InjectManifest',
      workboxOptions: {
          // swSrc is required in InjectManifest mode.
          swSrc: 'public/service-worker.js',
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
