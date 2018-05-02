<template>
  <div class="container-fluid">
    <router-view></router-view>
  </div>
</template>

<script>
import Auth0Lock from 'auth0-lock'
import NewInitiativeModal from '@/components/modal/NewInitiativeModal.vue'

export default {
  name: 'app',

  methods: {
  },

  components: {
    AppNewInitiativeModal: NewInitiativeModal
  },

  created () {
    var options = {
      auth: {
        state: this.$route.path,
        responseType: 'token',
        params: {
          connectionScopes: {
            connectionName: [ 'openid', 'user_metadata', 'app_metadata', 'picture' ]
          }
        }
      },
      theme: {
        logo: 'https://image.ibb.co/bTQ9Ev/logo_color_auth0.png',
        primaryColor: '#bc1c34'
      },
      languageDictionary: {
        title: 'welcome'
      }
    }

    var lock = new Auth0Lock(process.env.AUTH0_CLIENT_ID, process.env.AUTH0_DOMAIN, options)

    lock.on('authenticated', (authResult) => {
      console.log(authResult.accessToken)
      console.log(authResult.idToken)
      console.log(authResult.state)
      // authResult.accessToken = '6x5aVjAiyOJpe-Jhy5gVrnzp9eDh6AY9'
      // authResult.idToken = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2NvbGxlY3RpdmVvbmUuYXV0aDAuY29tLyIsInN1YiI6ImF1dGgwfDU5ZWRhY2RiYzMwYTM4MDUzYWI1YmMwZCIsImF1ZCI6Imt1RFgxWlZvckFseTVQWWR5VjcyMXpSb1RmMEswb3JtIiwiaWF0IjoxNTIwNDg5MjgwLCJleHAiOjE1MjMwODEyODB9.Xa75mfxW07TcYDfkofIPAhhCq3_wqVOss-M1rzF53Lw'
      // authResult.state = '/app/inits/'
      localStorage.setItem('access_token', authResult.accessToken)
      localStorage.setItem('id_token', authResult.idToken)
      this.$store.commit('authenticate', !!localStorage.getItem('id_token'))
      this.$store.commit('setAuth0state', authResult.state)
      this.$store.dispatch('updateProfile')
      this.$router.push({ name: 'Root' })
    })

    lock.on('authorization_error', (error) => {
      console.log('error authenticating')
      console.log(error)
      this.$store.dispatch('logoutUser')
    })

    /* check if user is authenticated */
    this.$store.commit('setLock', lock)
    this.$store.commit('authenticate', !!localStorage.getItem('id_token'))
    this.$store.dispatch('updateProfile')
  }
}
</script>

<style scoped>

.container-fluid {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

@import '~simplemde/dist/simplemde.min.css';

@import 'styles/appGeneral.css';
@import 'styles/elements.css';
@import 'styles/modals.css';
@import 'styles/colors.css';
@import 'styles/appGeneral.css';
@import 'styles/animations.css';
@import 'styles/mde.css';
@import 'styles/tooltips.css';

</style>
