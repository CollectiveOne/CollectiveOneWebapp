/* eslint-disable no-console */
import { register } from 'register-service-worker'
var swRegistration

register(`${process.env.BASE_URL}service-worker.js`, {
  ready (registration) {
    swRegistration = registration
    console.log(
      'App is being served from cache by a service worker.\n' +
      'For more details, visit https://goo.gl/AFskqB'
    )
  },
  registered (registration) {
    swRegistration = registration
    console.log('SW Registered')
  },
  cached (registration) {
    swRegistration = registration
    console.log('Content has been cached for offline use.')
  },
  updated (registration) {
    swRegistration = registration
    console.log('New content is available; please refresh.')
  },
  offline () {
    console.log('No internet connection found. App is running in offline mode.')
  },
  error (error) {
    console.error('Error during service worker registration:', error)
  }
})

export {
  swRegistration
}
