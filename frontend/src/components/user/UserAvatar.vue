<template lang="html">
  <div class="">

    <transition name="slideDownUp">
      <app-user-profile-modal
        v-if="showProfileModal"
        :userId="user.c1Id"
        @close="showProfileModal = false">
      </app-user-profile-modal>
    </transition>

    <div class="w3-row avatar-container">
      <div class="img-div noselect"
        @mouseover="showHoverName = true"
        @mouseleave="showHoverName = false">

        <img class="w3-circle"
          :class="imgClass" :src="userPictureUrl" @error="errorOnPicture"/>

        <div v-if="!showName && showHoverName && enableHover"
          class="w3-container w3-padding hover-name-container w3-tag dark-gray w3-round">

          <div class="w3-row cursor-pointer nickname-row">
            <b>{{ user.nickname }} {{ hasUsername ? '('+user.username+')' : '' }}</b>
          </div>
          <div class="w3-row">
            {{ user.shortBio }}
          </div>
          <div class="w3-row">
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
      <div v-if="showName" class="name-container"
        :class="{'name-large': !small, 'name-small': small}">
        <b>{{ user.nickname }} {{ hasUsername ? '('+user.username+')' : '' }}</b>
      </div>
    </div>
  </div>

</template>

<script>
import UserProfileModal from '@/components/modal/UserProfileModal.vue'

export default {
  components: {
    'app-user-profile-modal': UserProfileModal
  },

  props: {
    user: {
      type: Object,
      default: () => {
        return {
          c1Id: 'id',
          nickname: 'nickname',
          pictureUrl: require('../../assets/male-avatar-50x50.png')
        }
      }
    },
    showName: {
      type: Boolean,
      default: true
    },
    enableHover: {
      type: Boolean,
      default: true
    },
    small: {
      type: Boolean,
      default: false
    },
    enableDatails: {
      type: Boolean,
      default: true
    }
  },

  data () {
    return {
      showHoverName: false,
      showProfileModal: false,
      errorOnPictureFlag: false
    }
  },

  computed: {
    imgClass () {
      let imgClass = {}
      if (this.small) {
        imgClass['img-style-small'] = true
      } else {
        imgClass['img-style-large'] = true
      }

      imgClass['cursor-pointer'] = this.enableDetails

      return imgClass
    },
    hasUsername () {
      if (this.user.username) {
        if (this.user.username.length > 0) {
          return true
        }
      }
      return false
    },
    userPictureUrl () {
      return this.errorOnPictureFlag ? 'https://image.ibb.co/mcnv8b/avatar.png' : this.user.pictureUrl
    }
  },

  methods: {
    errorOnPicture () {
      this.errorOnPictureFlag = true
    }
  }
}
</script>

<style scoped>

.nickname-row {
  white-space: nowrap;
}

.avatar-container {
  position: relative;
}

.avatar-container:before {
  content: "";
  display: inline-block;
  vertical-align: middle;
  height: 100%;
}

.img-div {
  display: inline-block;
  vertical-align: middle;
}

.name-container {
  display: inline-block;
  text-align: left;
  vertical-align: middle;
}

.name-large {
  font-size: 18px;
  margin-left: 15px;
}

.name-small {
  font-size: 12px;
  margin-left: 3px;
  padding-top: 2px;
}

.hover-name-container {
  position: absolute;
  width: 250px;
  z-index: 10;
}

.social-icons-container {
  margin: 0 auto;
  font-size: 18px;
}

.social-icon {
  display: inline-block;
  width: 50px;
}

.img-style-large {
  width: 40px;
  height: 40px;
}

.img-style-small {
  width: 25px;
  height: 25px;
}

</style>
