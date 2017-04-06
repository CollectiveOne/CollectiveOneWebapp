<template lang="html">
  <div v-if="isUserLogged()" id="projectfollow_div">
    <img id="starred-img" @click="startToggle" :src="starredImage" data-toggle="tooltip" title="add to your starred items">
    <img id="watched-img" @click="watchToggle" :src="watchedImage" data-toggle="tooltip" title="follow and get notifications">
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  data () {
    projectName: '',
    isStarred: false,
    isWatched: false
  },

  computed: {
    ...mapGetters([
      'isUserLogged'
    ]),

    starredImage () {
      if (this.isStarred) return '@/assets/images/starred-yes.png'
      else '@/assets/images/starred-no.png'
    },

    watchedImage () {
      if (this.isWatched) return '/images/watched-yes.png'
      else '@/assets/images/watched-no.png'
    }
  },

  methods: {
    starredToggle () {
      this.axios.put('/1/project/' + this.projectName + '/star', {}).then((response) => {

      })
    }
  }
}
</script>

<style lang="css">
</style>
