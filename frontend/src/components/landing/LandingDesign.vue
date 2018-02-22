<template lang="html">
  <div class="w3-center">
    <div class="w3-row top-div light-grey">
      <div class="top-div-top-div">
          <img class="logo" src="../../assets/logo-white.png">
          <div class="section-tabs">
            <div @click="goToPrinciples()" class="section-tab">PRINCIPLES</div>
            <div @click="goToVision()" class="section-tab">VISION</div>
            <div @click="goToRoadmap()" class="section-tab">ROADMAP</div>
            <div @click="goToParticipate()" class="section-tab">PARTICIPATE</div>
          </div>
      </div>
      <div class="top-div-middle-div">
        <h1>WELCOME TO<br>COLLECTIVEONE</h1>
        <div class="text-div-text">
          <h5>
            CollectiveOne is a method (and a platform) to develop open,
            decentralized and collaborative initiatives: initiatives to which anyone can,
            potentially, contribute, and which are collectively owned and self-governed
            by their contributors.
          </h5>
        </div>
        <button id="T_loginButton" class="w3-button dark-gray w3-margin-top" @click="login()">LOG IN / SIGNUP</button>
        <button id="T_exploreButton" class="w3-button dark-gray w3-margin-top" @click="explore()">EXPLORE THE APP</button>
      </div>
    </div>

    <div id="principles" class="w3-row principles-div w3-padding-large gray-2-color">
      <h1>PRINCIPLES</h1>
      <div class="w3-row-padding principles-row">
        <div class="w3-col l4 principle">
          <img src="../../assets/icon-open.png" alt="">
          <h5><b>OPEN COLLABORATION</b></h5>
          <p>
            Like open-source projects, entry barriers are kept low to allow anyone to
            contribute to an initiative in similar conditions as those of previous contributors.
          </p>
        </div>
        <div class="w3-col l4 principle">
          <img src="../../assets/icon-record.png" alt="">
          <h5><b>CONTRIBUTIONS RECORD AND VALUE</b></h5>
          <p>
            Contributions are recognized and valued relative to each other using project-specific
            tokens.
          </p>
        </div>
        <div class="w3-col l4 principle">
          <img src="../../assets/icon-liquid.png" alt="">
          <h5><b>LIQUID OWNERSHIP</b></h5>
          <p>
            The ownership of each project is linked to contributions, and, therefore, to tokens.
            The more you contribute, the more you should own.
          </p>
        </div>
      </div>
      <div class="w3-row-padding principles-row">
        <div class="w3-col m2"></div>
        <div class="w3-col m8">
          <div class="w3-row-padding">
            <div class="w3-col l6 principle">
              <img src="../../assets/icon-governance.png" alt="">
              <h5><b>DISTRIBUTED GOVERNANCE</b></h5>
              <p>
                Configurable governance at each level of an initiative enables decisions to be taken openly
                and collectively.
              </p>
            </div>
            <div class="w3-col l6 principle">
              <img src="../../assets/icon-transparency.png" alt="">
              <h5><b>TRANSPARENCY</b></h5>
              <p>
                The information of an initiative can be kept public as a mean to attract and enable people
                to contribute.
              </p>
            </div>
          </div>
        </div>
        <div class="w3-col m2"></div>
      </div>
    </div>

    <div id="roadmap" class="w3-row vision-div w3-padding-large roadmap-div gray-2-color light-grey">
      <h1>ROADMAP</h1>
      <div class="w3-row-padding">
        <div class="w3-col l3">
          <div class="title-container flex-vert">
            <h5><b>VALUE ACCOUNTING</b></h5>
          </div>
          <div class="text-container">
            <p>
              Basic infrastructure for managing tokens and transferring them to
              subinitiatives and users.
            </p>
          </div>
          <div class="w3-tag w3-green w3-round">
            <b>beta</b>
          </div>
        </div>
        <div class="w3-col l3">
          <div class="title-container flex-vert">
            <h5><b>COLLECTIVE VISION</b></h5>
          </div>
          <div class="text-container">
            <p>
              A dedicated module that enables initiatives to collectively influence and
              agree on the initiative's vision and plans.
            </p>
          </div>
          <div class="w3-tag w3-green w3-round">
            <b>beta</b>
          </div>
        </div>
        <div class="w3-col l3">
          <div class="title-container flex-vert">
            <h5><b>TASK MANAGEMENT</b></h5>
          </div>
          <div class="text-container">
            <p>
              Kanban board to organize the tasks of an initiative and value these
              using the initiative's tokens.
            </p>
          </div>
          <div class="w3-tag gray-1 w3-round">
            <b>soon</b>
          </div>
        </div>
        <div class="w3-col l3">
          <div class="title-container flex-vert">
            <h5><b>DISTRIBUTED GOVERNANCE</b></h5>
          </div>
          <div class="text-container">
            <p>A dedicated module that enables initiatives to take decisions
              collectively and efficiently.
            </p>
          </div>
          <div class="w3-tag gray-1 w3-round">
            <b>soon</b>
          </div>
        </div>
      </div>
    </div>

    <div class="w3-row tutorial-videos-header-container">
      <h1 class="gray-1-color">CURRENT FEATURES</h1>
    </div>

    <div class="w3-cell-row tutorial-video-row">
      <div class="w3-cell w3-cell-middle w3-mobile tutorial-title-container gray-2-color">
        <h2>Create new initiatives and preallocate the value in sub-areas.</h2>
      </div>
      <div class="w3-cell w3-mobile tutorial-video-container">
        <div class="">
          <video controls >
            <source src="https://s3.us-east-2.amazonaws.com/collectiveone-b1/Web/Video1.mp4" type="video/mp4">
            Your browser does not support the video tag.
          </video>
        </div>
      </div>
    </div>

    <div class="w3-row tutorial-video-row w3-border-top light-grey">
      <div class="w3-cell w3-cell-middle w3-mobile tutorial-title-container tutorial-top-title-container gray-2-color">
        <h2>Agree on the vision and the plan with other contributors.</h2>
      </div>
      <div class="w3-cell w3-mobile tutorial-video-container">
        <div class="w3-center">
          <video controls >
            <source src="https://s3.us-east-2.amazonaws.com/collectiveone-b1/Web/Video2.mp4" type="video/mp4">
            Your browser does not support the video tag.
          </video>
        </div>
      </div>
      <div class="w3-cell w3-cell-middle w3-mobile tutorial-title-container tutorial-bottom-title-container gray-2-color">
        <h2>Agree on the vision and the plan with other contributors.</h2>
      </div>
    </div>

    <div class="w3-row tutorial-video-row tutorial-video-last-row w3-border-top w3-border-bottom">
      <div class="w3-cell w3-cell-middle w3-mobile tutorial-title-container gray-2-color">
        <h2>Recognize contributions directly or through peer-review.</h2>
      </div>
      <div class="w3-cell w3-mobile tutorial-video-container">
        <div class="w3-center ">
          <video controls >
            <source src="https://s3.us-east-2.amazonaws.com/collectiveone-b1/Web/video3.mp4" type="video/mp4">
            Your browser does not support the video tag.
          </video>
        </div>
      </div>
    </div>

    <div id="participate" class="w3-row participate-div w3-padding-large">
      <div class="">
        <h1 class="gray-2-color">PARTICIPATE</h1>
        <div class="w3-row-padding participate-row">
          <h5>CollectiveOne is an open project itself and is being developed using
            <b><a href="http://www.collectiveone.org/#/app/inits/ac119496-5e3e-1db5-815e-3f192a890001/overview">CollectiveOne itself</a></b>.
            Contributions are welcome!
            <br>
            <br>Get your invitation to the project slack
            <b><a href="http://old.collectiveone.org/v/slack" target="_blank">here</a></b> to get involved.</h5>
        </div>
      </div>
    </div>

    <div id="vision" class="w3-row vision-div w3-padding-large dark-gray vision-div">
      <div class="centered-block gray-1-color">
        <h1>OUR VISION</h1>
        <div class="w3-row video-row">
          <div>
            <div class="video-container">
              <iframe class="video-iframe" src="https://www.youtube.com/embed/qka1zP_GQaI" frameborder="0" allowfullscreen="true"></iframe>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="w3-row bottom-div">
      <div class="w3-row icons-row dark-gray w3-center">
        <p><b>FOLLOW US!</b></p>
        <div class="icons-container">
          <div class="w3-col s1">
          </div>
          <a class="w3-button w3-col s2 slack-link" href="https://medium.com/collectiveone" target='_blank'>
            <i class="fa fa-medium" aria-hidden="true"></i>
          </a>
          <a class="w3-button w3-col s2 slack-link" href="http://old.collectiveone.org/v/slack" target='_blank'>
            <i class="fa fa-slack" aria-hidden="true"></i>
          </a>
          <a class="w3-button w3-col s2 email-link" href="mailto:contact@collectiveone.org">
            <i class="fa fa-envelope-o" aria-hidden="true"></i>
          </a>
          <a class="w3-button w3-col s2 twitter-link" href="https://twitter.com/collectiveonepr" target='_blank'>
            <i class="fa fa-twitter" aria-hidden="true"></i>
          </a>
          <a class="w3-button w3-col s2 github-link" href="https://github.com/CollectiveOne" target='_blank'>
            <i class="fa fa-github" aria-hidden="true"></i>
          </a>
        </div>
      </div>
      <div class="w3-centered copyright-row light-grey">
        <img src="../../assets/by-nc.eu.png" alt="">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    login () {
      this.$store.state.user.lock.show()
    },
    explore () {
      this.$router.push({ name: 'InitiativesHome' })
    },
    goToPrinciples () {
      window.scroll(0, this.findPos(document.getElementById('principles')))
    },
    goToVision () {
      window.scroll(0, this.findPos(document.getElementById('vision')))
    },
    goToRoadmap () {
      window.scroll(0, this.findPos(document.getElementById('roadmap')))
    },
    goToParticipate () {
      window.scroll(0, this.findPos(document.getElementById('participate')))
    },
    findPos (obj) {
      var curtop = 0
      if (obj.offsetParent) {
        do {
          curtop += obj.offsetTop
          obj = obj.offsetParent
        } while (obj.offsetParent)
        return [curtop]
      }
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
  height: 550px;
  color: white;
  background: url('../../assets/home.jpg') center left / cover no-repeat;
}

.top-div-top-div {
  position: absolute;
  height: 100px;
  padding: 30px 0px 0px 0px;
  width: 100%;
}

.top-div .top-div-middle-div {
  height: 100%;
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  justify-content: center;
  flex-direction: column;
}

.top-div-top-div img {
  float: left;
  margin-bottom: 20px;
}

.top-div-top-div .logo {
  width: 220px;
  margin-left: 30px;
}

.top-div-top-div .section-tabs {
  padding-top: 6px;
  float: right;
}

.top-div-top-div .section-tab {
  color: white;
  cursor: pointer;
  display: inline-block;
  white-space: nowrap;
  margin-right: 15px;
}

.top-div-top-div .section-tabs a {
  text-decoration: none;
}

.top-div-top-div .section-tab:hover {
  font-weight: bold;
}

.top-div-middle-div h1 {
  font-weight: bolder;
}

@media all and (max-width: 601px) {

  .top-div-middle-div h1 {
    font-size: 20px !important;
  }

  .top-div-middle-div h5 {
    font-size: 14px !important;
  }

  .top-div-top-div .section-tabs {
    width: 100%;
  }

  .top-div-top-div .section-tab {
    width: 24%;
    margin: 0px;
  }
}

.top-div-middle-div button {
  margin: 0 auto;
  width: 200px;
}

.text-div-text {
  padding-left: 30px;
  padding-right: 30px;
}

.principles-div {
  padding-top: 50px !important;
  padding-bottom: 50px !important;
  max-width: 1000px;
  margin: 0 auto;
}

.principles-div h1 {
  margin-bottom: 50px;
}

.principles-div h5 {
  color: black;
}

.principles-div img {
  height: 60px;
  margin-bottom: 10px;
}

.principles-div p {
  max-width: 400px;
  margin: 0 auto;
}

.principle {
  margin-bottom: 40px;
}

.tutorial-videos-header-container {
  padding-top: 50px;
  padding-bottom: 50px;
}

.tutorial-video-row {
  max-width: 1200px;
  margin: 0 auto;
}

.tutorial-video-last-row {
  padding-bottom: 50px;
}

@media screen and (min-width: 992px) {

  .tutorial-title-container {
    padding-left: 40px;
    padding-right: 40px;
    width: 50%;
  }

  .tutorial-top-title-container {
    display: none;
  }

  .tutorial-bottom-title-container {
    display: table-cell;
  }

  .tutorial-video-container {
    width: 50%;
  }

  .tutorial-video-container video {
    width: 100%;
  }
}

@media screen and (max-width: 991px) {
  .tutorial-title-container {
    padding-left: 10px;
    padding-right: 10px;
  }

  .tutorial-top-title-container {
    display: table-cell;
  }

  .tutorial-bottom-title-container {
    display: none;
  }

  .tutorial-video-container video {
    width: 100%;
  }
}

.vision-div {
  padding-top: 50px !important;
  padding-bottom: 75px !important;
}

.video-container {
	position:relative;
	padding-bottom:55.9%;
	padding-top:0px;
	height:0;
	overflow:hidden;
  margin-top: 20px;
}

.video-iframe {
	position:absolute;
	top:0;
	left:0;
	width:100%;
	height:100%;
	max-width:800px;
}

.roadmap-div {
  padding-top: 50px !important;
  padding-bottom: 75px !important;
  max-width: 1200px;
  margin: 0 auto;
}

.roadmap-div .title-container {
  min-height: 70px;
}

.roadmap-div h5 {
  margin: 0;
  color: black;
}

.text-container {
  max-width: 250px;
  margin: 0 auto;
  min-height: 100px;
}

.text-container p {
  margin: 0;
}

.participate-div {
  padding-top: 50px !important;
  min-height: 368px;
}

.participate-div h5 {
  max-width: 800px;
  margin: 0 auto;
}

.participate-row {
  margin-top: 30px;
}

.bottom-div .icons-row {
  padding-top: 30px;
  padding-bottom: 45px;
  color: white;
}

.icons-container {
  font-size: 22px;
  max-width: 600px;
  margin: 0 auto;
}

.copyright-row {
  padding-top: 17px;
  padding-bottom: 15px;
}

.copyright-row img {
  width: 100px;
  border-radius: 5px;
}

</style>
