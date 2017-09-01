<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>User Profile</h2>
        </div>

        <div class="w3-container">
          <app-user-profile
            :userId="userId">
          </app-user-profile>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import UserProfile from '@/components/user/UserProfile.vue'

export default {

  components: {
    'app-user-profile': UserProfile
  },

  props: {
    userId: {
      type: String,
      default: null
    }
  },

  data () {
    return {
    }
  },

  computed: {
  },

  methods: {
    closeThis () {
      this.$emit('close')
    }
  },

  mounted () {
  }
}
</script>

<style scoped>

</style>
