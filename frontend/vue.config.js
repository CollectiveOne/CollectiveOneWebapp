module.exports = {
  devServer: {
    proxy: {
      '/1': {
        target: 'http://localhost:3000',
        changeOrigin: true
      }
    }
  }
}
