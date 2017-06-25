<template lang="html">
  <div class="w3-cell-row">
    <div class="w3-cell w3-cell-middle noselect" style="width:30px">
      <img :class="imgClass" :src="user.pictureUrl"/>
    </div>
    <div v-if="showName" class="name-container w3-cell w3-cell-middle">
      <h5 class="w3-padding"><b>{{ user.nickname }}</b></h5>
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

.name-container {
  text-align: left;
}

.img-style-large {
  width: 40px;
}

.img-style-small {
  width: 35px;
}

</style>
