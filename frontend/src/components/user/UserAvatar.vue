<template lang="html">
  <div class="w3-cell-row">
    <div class="w3-cell w3-cell-middle" style="width:30px">
      <img class="" style="width:40px" :src="userData.pictureUrl"/>
    </div>
    <div class="name-container w3-cell w3-cell-middle">
      <h5 class="w3-padding"><b>{{ userData.nickname }}</b></h5>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    userData: {
      type: Object,
      default: () => {
        return {
          c1Id: 'id',
          nickname: 'nickname',
          pictureUrl: require('../../assets/male-avatar-50x50.png')
        }
      }
    }
  }
}
</script>

<style scoped>

.name-container {
  text-align: left;
}

</style>
