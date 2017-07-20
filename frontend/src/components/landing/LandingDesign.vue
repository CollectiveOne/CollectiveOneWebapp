<template lang="html">
  <div class="w3-container w3-center">
    <div class="top-div light-grey">
      <div class="">
        <h1 >Welcome to CollectiveOne</h1>
        <button class="w3-button app-button w3-margin-bottom" @click="login()">login / signup</button>
        <div class="text-div-text">
          <h5 >
            CollectiveOne is a method (and a platform) to develop open,
            decentralized and collaborative projects: projects to which anyone can,
            potentially, contribute, and which are collectively owned and self-governed
            by their contributors.
          </h5>
        </div>
      </div>
    </div>
    <div class="content-div dark-gray">
      <div>
        <h2><b>Principles</b></h2>
        <div class="w3-row-padding">
          <div class="w3-col m4">
            <h3>Open collaboration</h3>
            Like open-source projects, entry barriers are kept low to allow anyone to contribute to a project in similar conditions as those of previous contributors.
          </div>
          <div class="w3-col m4">
            <h3>Contribution record and value</h3>
            Individual contributions are recognized and valued relative to each other, using a project specific token called <i>participation points</i>.
          </div>
          <div class="w3-col m4">
            <h3>Liquid ownership</h3>
            The ownership of each project is linked to contributions, and, therefore, to participation points. The more you contribute, the more you own.
          </div>
        </div>
        <div class="w3-row-padding w3-margin-top">
          <div class="w3-col m6">
            <h3>Efficient decentralized decision-making</h3>
            Decisions are taken openly, letting any contributor to take/influence any decision. At the same time, it is not required, nor promoted, that all contributors influence all decisions, and voting weights can be different among contributors.
          </div>
          <div class="w3-col m6">
            <h3>Transparency and open data</h3>
            Projects data and communication channels are as open as possible, a necessary condition to balance the opportunity to participate. Moreover, transparency in CollectiveOne could reach <i>hardcore</i> levels, since a detailed record of all the contributions and decisions within a project may also become publicly available.
          </div>
        </div>
      </div>
    </div>

    <div class="bottom-div w3-padding-large light-grey">
      <div class="w3-row">
        <a href="http://old.collectiveone.org/v/slack">get in touch!</a>
      </div>
      <div class="w3-row">
        <a href="https://twitter.com/collectiveonepr">follow us in twitter!</a>
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
  padding: 30px 50px 50px 30px !important;
}

</style>
