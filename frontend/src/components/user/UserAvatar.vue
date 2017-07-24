<template lang="html">
  <div class="w3-row avatar-container">
    <div class="img-div noselect"
      @mouseover="showHoverName = true"
      @mouseleave="showHoverName = false">
      <img class="w3-circle" :class="imgClass" :src="user.pictureUrl"/>
      <div class="hover-name cursor-pointer">
        <div v-if="!showName && showHoverName" class="hover-name-container w3-tag dark-gray w3-round">
          <b>{{ user.nickname }}</b>
        </div>
      </div>
    </div>
    <div v-if="showName" class="name-container">
      <b>{{ user.nickname }}</b>
    </div>
  </div>
</template>

<script>
export default {
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
      showHoverName: false
    }
  },

  computed: {
    imgClass () {
      if (this.small) {
        return { 'img-style-small': true }
      } else {
        return { 'img-style-large': true }
      }
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

.img-style-large {
  width: 40px;
}

.img-style-small {
  width: 35px;
}

</style>
