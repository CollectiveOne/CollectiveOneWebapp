<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>Exchange or Convert tokens</h2>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
export default {

  data () {
    return {
    }
  },

  computed: {
  },

  methods: {
  },

  mounted () {
  }
}
</script>

<style scoped>

.form-container {
  padding-top: 0px;
  padding-bottom: 35px;
}

.fa-percent {
  font-size: 22px;
  margin-top: 7.5px;
  margin-left: 5px;
}

.amount-container {
  height: 150px;
}

.fa-certificate {
  font-size: 100px;
}


</style>
