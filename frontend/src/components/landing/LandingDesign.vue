<template lang="html">
  <div class="w3-center">
    <div class="top-div light-grey">
      <div class="centered-block">
        <h1 >Welcome to CollectiveOne</h1>
        <button class="w3-button app-button w3-margin-bottom" @click="login()">login / signup</button>
        <div class="text-div-text">
          <h5 >
            CollectiveOne is a method (and a platform) to develop open,
            decentralized and collaborative initiatives: initiatives to which anyone can,
            potentially, contribute, and which are collectively owned and self-governed
            by their contributors.
          </h5>
        </div>
      </div>
    </div>
    <div class="content-div w3-padding-large dark-gray">
      <div class="centered-block">
        <h1><b>Principles</b></h1>
        <div class="w3-row-padding principles-row">
          <div class="w3-col m4">
            <h3><b>Open collaboration</b></h3>
            Like open-source projects, entry barriers are kept low to allow anyone to
            contribute to an initiative in similar conditions as those of previous contributors.
          </div>
          <div class="w3-col m4">
            <h3><b>Contribution record and value</b></h3>
            Contributions are recognized and valued relative to each other, using project-specific
            tokens.
          </div>
          <div class="w3-col m4">
            <h3><b>Liquid ownership</b></h3>
            The ownership of each project is linked to contributions, and, therefore, to tokens.
            The more you contribute, the more youn should own.
          </div>
        </div>
        <div class="w3-row-padding">
          <div class="w3-col m4">
            <h3><b>Distributed governance</b></h3>
            Configurable governance at each level of an initiative enables decisions to be taken openly
            and collectively.
          </div>
          <div class="w3-col m4">
          </div>
          <div class="w3-col m4">
            <h3><b>Transparency</b></h3>
            The information of an initiative can be kept public as a mean to attract and enable people
            to contribute.
          </div>
        </div>
      </div>
    </div>

    <div class="w3-row bottom-div w3-padding-large light-grey">
      <div class="centered-block">
        <div class="w3-row">
          <a class="w3-button w3-col s3 slack-link" href="http://old.collectiveone.org/v/slack">
            <img src="../../assets/slack-512.png"></img>
    			</a>
    			<a class="w3-button w3-col s3 email-link" href="mailto:contact@collectiveone.org">
    				<img src="../../assets/iconmonstr-email-4-icon-27.png"></img>
    			</a>
    			<a class="w3-button w3-col s3 twitter-link" href="https://twitter.com/collectiveonepr" target='_blank'>
    				<img src="../../assets/43-twitter-512.png"></img>
    			</a>
    			<a class="w3-button w3-col s3 github-link" href="https://github.com/CollectiveOne" target='_blank'>
    				<img src="../../assets/GitHub-Mark-120px-plus.png"></img>
    			</a>
        </div>
      </div>
		</div>
  </div>
</template>

<script>
export default {
  methods: {
    login () {
      this.$store.state.user.lock.show()
    }
  }
}
</script>

<style scoped>

.w3-col {
  min-height: 5px;
}

.centered-block {
  max-width: 800px;
  margin: 0 auto;
}

.top-div {
  height: 600px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.top-div button {
  width: 150px;
}

.text-div-text {
  padding-left: 30px;
  padding-right: 30px;
}

.content-div {
  padding-top: 100px !important;
  padding-bottom: 100px !important;
}

.principles-row {
  margin-bottom: 50px;
}

.bottom-div img {
  width: 30px;
}

</style>
