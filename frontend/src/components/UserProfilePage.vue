<template lang="html">
  <div class="">

    <app-header :inInitiative="false" :withInitiativeNav="false">
    </app-header>

    <div class="w3-row w3-center content-div">

      <div class="w3-center w3-round-large w3-padding w3-border">
        <h5>{{ $t('general.PROFILE_CC') }}</h5>

        <app-user-profile
          :userId="$route.params.userId">
        </app-user-profile>
      </div>

      <div v-if="isLoggedUser" class="w3-margin-top w3-center settings-div w3-round-large w3-border">
        <h5>{{ $t('general.SETTINGS') }}</h5>

        <div class="w3-row w3-margin-top">
          <div class="w3-col l6 div-label">
            <label for="">{{ $t('general.LANGUAGE') }}  </label>
          </div>
          <div class="w3-col l6">
            <select v-model="locale" class="w3-input" name="">
              <option value="en">English</option>
              <option value="es">Spanish</option>
            </select>
          </div>
        </div>

        <div class="w3-row w3-margin-top">
          <button @click="saveSettings()" class="w3-button app-button">{{ $t('general.SAVE') }}</button>
        </div>
      </div>

    </div>
  </div>

</template>

<script>
import Header from '@/components/Header.vue'
import UserProfile from '@/components/user/UserProfile.vue'

export default {
  components: {
    'app-header': Header,
    'app-user-profile': UserProfile
  },

  data () {
    return {
      locale: 'en'
    }
  },

  computed: {
    loggedUser () {
      if (this.$store.state.user) {
        return this.$store.state.user.profile
      }

      return null
    },

    isLoggedUser () {
      if (!this.loggedUser) {
        return false
      }
      return this.$route.params.userId === this.loggedUser.c1Id
    }
  },

  watch: {
    loggedUser () {
      this.updateLocale()
    }
  },

  methods: {
    saveSettings () {
      this.updateLanguage()
    },
    updateLanguage () {
      if (this.isLoggedUser) {
        this.axios.put('/1/user/' + this.loggedUser.c1Id + '/lang',
          {},
          { params: { locale: this.locale } })
        .then((response) => {
          this.$store.dispatch('updateProfile')
        })
      }
    },
    updateLocale () {
      if (this.loggedUser) {
        if (this.loggedUser.preferredLocale) {
          if (this.loggedUser.preferredLocale !== '') {
            this.locale = this.loggedUser.preferredLocale
          }
        }
      }
    }
  },

  created () {
    this.updateLocale()
  }
}
</script>

<style scoped>

.content-div {
  max-width: 600px;
  padding-top: 35px;
  margin: 0 auto;
}

.div-label {
  text-align: center;
  padding-top: 5px;
}

.settings-div {
  padding: 6px 12px 18px 12px;
}

</style>
