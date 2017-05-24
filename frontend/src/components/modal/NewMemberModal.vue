<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-theme">
          <h2>New Member</h2>
        </div>

        <form class="w3-container">

          <label class="w3-text-indigo"><b>{{ asSubinitiative ? 'Subinitiative' : 'Initiative'}} Member</b></label>
          <input v-model="name" class="w3-input w3-hover-light-gray" type="text">
          <br>

          <label class="w3-text-indigo"><b>{{ asSubinitiative ? 'Subinitiative' : 'Initiative'}} Role</b></label>
          <textarea v-model="driver" class="w3-input w3-border w3-round w3-hover-light-gray"></textarea>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    parentInitId: {
      type: String,
      default: ''
    }
  },

  components: {
  },

  data () {
    return {
    }
  },

  computed: {
  },

  watch: {
  },

  methods: {
    closeThis () {
      this.$emit('close-this')
    }
  },

  mounted () {
  }
}
</script>

<style scoped>

.close-div {
  width: 70px;
  height: 70px;
  cursor: pointer;
  text-align: right;
}

.fa-times {
  color: rgb(255, 255, 255);
  margin-right: 20px;
  margin-top: 20px;
}

form {
  padding-top: 0px;
  padding-bottom: 35px;
}

.token-type-tabs {
  text-align: center;
  user-select: none;
  cursor: pointer;
}

.bold-text {
  font-weight: bold;
}

.assets-selector-div {
}

.contributors-container {
  margin-top: 10px;
  padding-bottom: 20px !important;
}

.new-contr-row {
  margin-top: 20px;
}

.fa-times-circle-o {
  color: #607d8b;
}

.add-btn {
  width: 100%;
}

.bottom-btns-row button {
  width: 100%;
}

.w3-modal {
  display: block;
}

</style>
