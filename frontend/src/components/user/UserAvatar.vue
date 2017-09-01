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
        <img class="w3-circle" :class="imgClass" :src="user.pictureUrl"/>
        <div v-if="!showName && showHoverName" class="w3-container w3-padding hover-name-container w3-tag dark-gray w3-round">
          <div
            @click="showProfileModal = true"
            class="w3-row cursor-pointer">
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
      <div v-if="showName" class="name-container">
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
    small: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      showHoverName: false,
      showProfileModal: false
    }
  },

  computed: {
    imgClass () {
      if (this.small) {
        return { 'img-style-small': true }
      } else {
        return { 'img-style-large': true }
      }
    },
    hasUsername () {
      if (this.user.username) {
        if (this.user.username.length > 0) {
          return true
        }
      }
      return false
    }
  }
}
</script>

<style scoped>

.avatar-container {
  white-space: nowrap;
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
  font-size: 18px;
  margin-left: 15px;
  text-align: left;
  vertical-align: middle;
}

.hover-name-container {
  position: absolute;
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
}

.img-style-small {
  width: 35px;
}

</style>
