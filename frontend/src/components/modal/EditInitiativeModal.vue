<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-theme">
          <h2>Edit Initiative</h2>
        </div>

        <form class="w3-container">

          <label class="w3-text-indigo"><b>Name</b></label>
          <input v-model="newInitiative.name" class="w3-input w3-hover-light-gray" type="text">
          <br>

          <label class="w3-text-indigo"><b>Driver</b></label>
          <textarea v-model="newInitiative.driver" rows="5" class="w3-input w3-border w3-round w3-hover-light-gray"></textarea>

          <hr>
          <div class="bottom-btns-row w3-row-padding">
            <div class="w3-col m6">
              <button type="button" class="w3-button w3-light-gray w3-round" @click="closeThis()">Cancel</button>
            </div>
            <div class="w3-col m6">
              <button type="button" class="w3-button w3-theme w3-round" @click="accept()">Accept</button>
            </div>
          </div>

        </form>

      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    initiative: {
      type: Object
    }
  },

  data () {
    return {
      newInitiative: {
        name: '',
        driver: ''
      }
    }
  },

  mounted () {
    this.newInitiative.name = this.initiative.name
    this.newInitiative.driver = this.initiative.driver
  },

  methods: {
    closeThis () {
      this.$emit('close-this')
    },
    accept () {
      this.axios.put('/1/secured/initiative/' + this.initiative.id, this.newInitiative).then((response) => {
        this.closeThis()
        this.$emit('initiative-updated')
      })
    }
  }
}
</script>

<style scoped>

.w3-modal {
  display: block;
}

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
  padding-top: 20px;
  padding-bottom: 35px;
}

.bottom-btns-row button {
  width: 100%;
}

</style>
