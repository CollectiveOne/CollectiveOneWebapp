<template lang="html">
  <div class="w3-center">
    <div class="w3-row top-div light-grey">
      <div class="top-div-top-div">
          <img class="logo" src="../../assets/logo-white.png">
          <div class="section-tabs">
            <div @click="goToPrinciples()" class="section-tab">{{ $t('landing.PRINCIPLES') }}</div>
            <div @click="goToFeatures()" class="section-tab">{{ $t('landing.FEATURES') }}</div>
            <div @click="goToDemos()" class="section-tab">{{ $t('landing.DEMOS') }}</div>
            <div @click="goToParticipate()" class="section-tab">{{ $t('landing.PARTICIPATE') }}</div>
          </div>
      </div>
      <div class="top-div-middle-div">
        <h1>{{ $t('landing.WELCOME') }} <br>COLLECTIVEONE</h1>
        <div class="text-div-text">
          <h5 v-html="$t('landing.BRIEF')"></h5>
        </div>
        <button class="w3-button dark-gray w3-margin-top" @click="login()">{{ $t('general.LOGIN_SIGNUP') }}</button>
        <button class="w3-button dark-gray w3-margin-top" @click="explore()">{{ $t('landing.EXPLORE') }}</button>
      </div>
    </div>

    <div id="principles" class="w3-row principles-div w3-padding-large gray-2-color">
      <h1>{{ $t('landing.PRINCIPLES') }}</h1>
      <div class="w3-row-padding principles-row">
        <div class="w3-col l4 principle">
          <img src="../../assets/icon-open.png" alt="">
          <h5><b>{{ $t('landing.OPEN_COLLAB') }}</b></h5>
          <p>{{ $t('landing.OPEN_COLLAB_CONTENT') }}</p>
        </div>
        <div class="w3-col l4 principle">
          <img src="../../assets/icon-governance.png" alt="">
          <h5><b>{{ $t('landing.DIST_GOV') }}</b></h5>
          <p>{{ $t('landing.DIST_GOV_CONTENT') }}</p>
        </div>
        <div class="w3-col l4 principle">
          <img src="../../assets/icon-record.png" alt="">
          <h5><b>{{ $t('landing.CONTRIB_REC') }}</b></h5>
          <p>{{ $t('landing.CONTRIB_REC_CONTENT') }}</p>
        </div>
      </div>
      <div class="w3-row-padding principles-row">
        <div class="w3-col m2"></div>
        <div class="w3-col m8">
          <div class="w3-row-padding">
            <div class="w3-col l6 principle">
              <img src="../../assets/icon-transparency.png" alt="">
              <h5><b>{{ $t('landing.TRANSP') }}</b></h5>
              <p>{{ $t('landing.TRANSP_CONTENTS') }}</p>
            </div>
            <div class="w3-col l6 principle">
              <img src="../../assets/icon-liquid.png" alt="">
              <h5><b>{{ $t('landing.LIQUID_OWNERSHIP') }}</b></h5>
              <p>{{ $t('landing.LIQUID_OWNERSHIP_CONTENT') }}</p>
            </div>
          </div>
        </div>
        <div class="w3-col m2"></div>
      </div>
    </div>

    <div id="features" class="w3-row vision-div w3-padding-large roadmap-div gray-2-color light-grey">
      <h1>{{ $t('landing.FEATURES') }}</h1>
      <div class="w3-row-padding">
        <div class="w3-col l3">
          <div class="title-container flex-vert">
            <h5><b>{{ $t('landing.COLL_VISION') }}</b></h5>
          </div>
          <div class="text-container">
            <p>{{ $t('landing.COLL_VISION_CONTENT') }}</p>
          </div>
        </div>
        <div class="w3-col l3">
          <div class="title-container flex-vert">
            <h5><b>{{ $t('landing.VALUE_ACC') }}</b></h5>
          </div>
          <div class="text-container">
            <p>{{ $t('landing.VALUE_ACC_CONTENT') }}</p>
          </div>
        </div>
        <div class="w3-col l3">
          <div class="title-container flex-vert">
            <h5><b>{{ $t('landing.DISTR_GOB') }}</b></h5>
          </div>
          <div class="text-container">
            <p>{{ $t('landing.DISTR_GOB_CONTENT') }}</p>
          </div>
        </div>
        <div class="w3-col l3">
          <div class="title-container flex-vert">
            <h5><b>{{ $t('landing.TASK_MAN') }}</b></h5>
          </div>
          <div class="text-container">
            <p>{{ $t('landing.TASK_MAN_CONTENT') }}</p>
          </div>
        </div>
      </div>
    </div>

    <div id="demos" class="w3-row tutorial-videos-header-container">
      <h1 class="gray-1-color">{{ $t('landing.DEMOS')}}</h1>
    </div>

    <div class="w3-cell-row tutorial-video-row">
      <div class="w3-cell w3-cell-middle w3-mobile tutorial-title-container gray-2-color">
        <h2>{{ $t('landing.FLEXIBLE_CHANNELS') }}</h2>
        <p>{{ $t('landing.FLEXIBLE_CHANNELS_CONTENTS') }}</p>
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
        <h2>{{ $t('landing.PERSONALIZED_CHANNELS') }}</h2>
        <p>{{ $t('landing.PERSONALIZED_CHANNELS_CONTENT') }}</p>
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
        <h2>{{ $t('landing.PERSONALIZED_CHANNELS') }}</h2>
        <p>{{ $t('landing.PERSONALIZED_CHANNELS_CONTENTS') }}</p>
      </div>
    </div>

    <div class="w3-row tutorial-video-row tutorial-video-last-row w3-border-top w3-border-bottom">
      <div class="w3-cell w3-cell-middle w3-mobile tutorial-title-container gray-2-color">
        <h2>{{ $t('landing.CHANNEL_CONTENTS') }}</h2>
        <p>{{ $t('landing.CHANNEL_CONTENTS_CONTENTS') }}</p>
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
        <h1 class="gray-2-color">{{ $t('landing.PARTICIPATE') }}</h1>
        <div class="w3-row-padding participate-row">
          <h5 v-html="$t('landing.PARTICIPATE_TEXT')"></h5>
        </div>
      </div>
    </div>

    <div class="w3-row bottom-div">
      <div class="w3-row icons-row dark-gray w3-center">
        <p><b>{{ $t('landing.FOLLOW_US') }}</b></p>
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
      document.getElementById('principles').scrollIntoView({behavior: 'smooth', block: 'start'})
    },
    goToFeatures () {
      document.getElementById('features').scrollIntoView({behavior: 'smooth', block: 'start'})
    },
    goToDemos () {
      document.getElementById('demos').scrollIntoView({behavior: 'smooth', block: 'start'})
    },
    goToParticipate () {
      document.getElementById('participate').scrollIntoView({behavior: 'smooth', block: 'start'})
    }
  },

  mounted () {
    if (this.$route.query.demos) {
      this.goToDemos()
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
  padding-top: 35px;
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
