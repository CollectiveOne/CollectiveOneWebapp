<template lang="html">
  <div v-if="user" class="user-page-container">
    <div v-if="!editing" class="w3-display-container show-container light-grey">
      <div class="edit-button w3-display-topright">
        <div @click="editing = true" class="w3-button w3-large">
          <i class="fa fa-pencil" aria-hidden="true"></i>
        </div>
      </div>
      <div class="w3-row w3-center">

        <div class="w3-row w3-margin-bottom">
          <div class="w3-display-container user-image">
            <img class="" :src="user.pictureUrl" alt="">
          </div>
        </div>
        <div class="w3-row">
          <h2>{{ user.nickname }}</h2>
        </div>

        <div class="w3-row w3-margin-bottom">
          <div v-if="user.username" class="">
            ({{ user.username }})
          </div>
        </div>

        <div v-if="user.shortBio" class="w3-row w3-margin-bottom">
          <p>{{ user.shortBio }}</p>
        </div>

        <div class="w3-row w3-margin-bottom">
          <div class="social-icons-container">

            <a v-if="user.twitterHandle" :href="user.twitterHandle" target="_blank" class="w3-button social-icon">
              <i class="fa fa-twitter" aria-hidden="true"></i>
            </a>
            <a v-if="user.facebookHandle" :href="user.facebookHandle" target="_blank" class="w3-button social-icon">
              <i class="fa fa-facebook" aria-hidden="true"></i>
            </a>
            <a v-if="user.linkedinHandle" :href="user.linkedinHandle" target="_blank" class="w3-button social-icon">
              <i class="fa fa-linkedin" aria-hidden="true"></i>
            </a>

          </div>
        </div>

      </div>
    </div>

    <div v-else class="editing-container light-grey">
      <div class="w3-center">

        <div class="w3-row w3-margin-bottom">
          <div class="w3-display-container w3-button user-image user-image-upload">
            <span class="w3-display-middle">upload image</span>
          </div>
        </div>

        <div class="w3-row w3-margin-bottom">
          <input v-model.trim="user.nickname" class="w3-border w3-round w3-hover-light-grey" placeholder="Display Name" value="">
        </div>
        <app-error-panel
          :show="nicknameTooLarge"
          message="display name too long">
        </app-error-panel>

        <div class="w3-row">
          <input v-model.trim="user.username" class="w3-border w3-round w3-hover-light-grey" placeholder="username (unique)" value="">
        </div>
        <app-error-panel
          :show="usernameExist"
          message="username already exist">
        </app-error-panel>
        <app-error-panel
          :show="usernameTooLarge"
          message="username too long">
        </app-error-panel>
        <app-error-panel
          :show="!usernameValid"
          message="invalid, please use only lowercase letters and/or numbers">
        </app-error-panel>

        <div class="w3-row w3-margin-top w3-margin-bottom">
          <textarea v-model="user.shortBio" class="w3-input w3-border w3-round w3-hover-light-grey" rows="4" placeholder="short bio"></textarea>
        </div>
        <app-error-panel
          :show="shortBioTooLarge"
          message="short bio too long">
        </app-error-panel>

        <div class="w3-row">
          <input v-model.trim="user.twitterHandle" @focusout="checkTwitterHandle()"
          class="w3-border w3-round w3-hover-light-grey" placeholder="twitter url" value="">
        </div>
        <app-error-panel
          :show="!twitterLinkOk"
          message="twitter url should follow the 'https://twitter.com/username' pattern">
        </app-error-panel>

        <div class="w3-row w3-margin-top">
          <input v-model.trim="user.facebookHandle" @focusout="checkFacebookHandle()"
          class="w3-border w3-round w3-hover-light-grey" placeholder="facebook url" value="">
        </div>
        <app-error-panel
          :show="!facebookLinkOk"
          message="facebook url should follow the 'https://www.facebook.com/username' pattern">
        </app-error-panel>

        <div class="w3-row w3-margin-top">
          <input v-model.trim="user.linkedinHandle" @focusout="checkLinkedinHandle()"
          class="w3-border w3-round w3-hover-light-grey" placeholder="linkedin url" value="">
        </div>
        <app-error-panel
          :show="!linkedinLinkOk"
          message="linkedin url should follow the 'https://www.linkedin.com/in/username' pattern">
        </app-error-panel>

      </div>

      <hr>
      <div class="bottom-btns-row w3-row-padding">
        <div class="w3-col m6">
          <button type="button" class="w3-button app-button-light" @click="cancel()">Cancel</button>
        </div>
        <div class="w3-col m6">
          <button type="button" class="w3-button app-button" @click="accept()">Accept</button>
        </div>
      </div>
    </div>

  </div>

</template>

<script>
export default {

  data () {
    return {
      user: null,
      editing: false,
      usernameExist: false,
      twitterLinkOk: true,
      facebookLinkOk: true,
      linkedinLinkOk: true
    }
  },

  computed: {
    nicknameTooLarge () {
      return this.user.nickname.length > 32
    },
    usernameTooLarge () {
      return this.user.username.length > 16
    },
    shortBioTooLarge () {
      return this.user.shortBio.length > 250
    },
    usernameValid () {
      var re = new RegExp('^[a-z0-9]*$')
      return re.test(this.user.username)
    }
  },

  watch: {
    'user.username' () {
      if (this.user.username !== this.username0) {
        this.axios.get('/1/secured/user/username/exist', {
          params: {
            username: this.user.username
          }
        }).then((response) => {
          this.usernameExist = response.data.data
        })
      }
    }
  },

  methods: {
    cancel () {
      this.update()
      this.editing = false
    },
    checkTwitterHandle () {
      if (this.user.twitterHandle) {
        if (this.user.twitterHandle !== '') {
          var re = new RegExp('^https://twitter.com/')
          this.twitterLinkOk = re.test(this.user.twitterHandle)
        } else {
          this.twitterLinkOk = true
        }
      } else {
        this.twitterLinkOk = true
      }
    },
    checkFacebookHandle () {
      if (this.user.facebookHandle) {
        if (this.user.facebookHandle !== '') {
          var re = new RegExp('^https://www.facebook.com/')
          this.facebookLinkOk = re.test(this.user.facebookHandle)
        } else {
          this.facebookLinkOk = true
        }
      } else {
        this.facebookLinkOk = true
      }
    },
    checkLinkedinHandle () {
      if (this.user.linkedinHandle) {
        if (this.user.linkedinHandle !== '') {
          var re = new RegExp('^https://www.linkedin.com/')
          this.linkedinLinkOk = re.test(this.user.linkedinHandle)
        } else {
          this.linkedinLinkOk = true
        }
      } else {
        this.linkedinLinkOk = true
      }
    },
    accept () {
      var ok = true
      if (this.usernameExist) {
        ok = false
      }

      if (!this.twitterLinkOk) {
        ok = false
      }

      if (ok) {
        this.axios.put('/1/secured/user/profile/' + this.$route.params.userId, this.user).then((response) => {
          this.update()
          this.$store.dispatch('updateProfile')
          this.editing = false
        })
      }
    },
    update () {
      this.axios.get('/1/secured/user/profile/' + this.$route.params.userId).then((response) => {
        this.user = response.data.data
        this.username0 = this.user.username
      })
    }
  },

  mounted () {
    this.update()
  }

}
</script>

<style scoped>

.user-page-container {
  padding-top: 30px;
  padding-bottom: 30px;
  max-width: 400px;
  margin: 0 auto;
}

.user-page-container input {
  padding-left: 5px;
}

.show-container, .editing-container {
  padding-top: 50px;
  padding-bottom: 20px;
  padding-left: 16px;
  padding-right: 16px;
  border-radius: 16px;
}

.show-container h2 {
  margin-bottom: 0px;
}

.social-icons-container {
  margin: 0 auto;
  font-size: 24px;
}

.social-icon {
  display: inline-block;
  width: 50px;
}

.user-image {
  width: 150px;
  height: 150px;
  margin: 0 auto;
}

.user-image-upload {
  border-style: solid;
  border-width: thin;
  border-radius: 75px;
}

.user-image img {
  width: 100%;
  height: 100%;
  border-radius: 75px;
}

.bottom-btns-row button {
  width: 100%;
}

</style>
