import { router } from '../router/router'

export const auth = {

  // User object will let us check authentication status
  user: {
    authenticated: false
  },

  // Send a request to the login URL and save the returned JWT
  login (context, creds, redirect) {
    context.$http.get('user', creds).then((data) => {
      this.user.authenticated = true
      // Redirect to a specified route
      if (redirect) {
        router.go(redirect)
      }
    })
  }
}
