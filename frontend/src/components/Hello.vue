<template>
  <div>
    <div class="preferred_browser_div cp_box_gray_slim">
      The preferred browsers for visiting this page are Google
      Chrome or Mozilla Firefox
    </div>

    <div class="presentation_container">
      <div class="video_container">
        <iframe class="video_iframe" src="https://www.youtube.com/embed/qka1zP_GQaI" frameborder="0" allowfullscreen="true"></iframe>
      </div>
    </div>

    <div class="under_presentation">
      <div class="info col-md-12">
        <h1>Welcome to CollectiveOne (Beta)</h1>
        <p>
          CollectiveOne is a method (and a platform) to develop open,
          decentralized and collaborative projects: projects to which anyone can,
          potentially, contribute, and which are collectively owned and self-governed
          by their contributors.
        </p>
        <p>
          If you are interested in setting up a project and testing the method and the tool, please get in contact with us.
        </p>
        <br />
        <div class="principles">
          <h2>Principles</h2>
          <div class="row">
            <div class="principle col-md-4">
              <h3>Open collaboration</h3>
              Like open-source projects, entry barriers are kept low to allow anyone to contribute to a project in similar conditions as those of previous contributors.
            </div>
            <div class="principle col-md-4">
              <h3>Contribution record and value</h3>
              Individual contributions are recognized and valued relative to each other, using a project specific token called <i>participation points</i>.
            </div>
            <div class="principle col-md-4">
              <h3>Liquid ownership</h3>
              The ownership of each project is linked to contributions, and, therefore, to participation points. The more you contribute, the more you own.
            </div>
          </div>
          <div class="row">
            <div class="principle col-md-6">
              <h3>Efficient decentralized decision-making</h3>
              Decisions are taken openly, letting any contributor to take/influence any decision. At the same time, it is not required, nor promoted, that all contributors influence all decisions, and voting weights can be different among contributors.
            </div>
            <div class="principle col-md-6">
              <h3>Transparency and open data</h3>
              Projects data and communication channels are as open as possible, a necessary condition to balance the opportunity to participate. Moreover, transparency in CollectiveOne could reach <i>hardcore</i> levels, since a detailed record of all the contributions and decisions within a project may also become publicly available.
            </div>
          </div>
        </div>
        <br />
        <h2>Roadmap</h2>
        <p>
          This website includes a basic prototype of the platform and is being actively used for the
          development of CollectiveOne itself. Other projects are also starting to use it. If you are interested in
          testing the method and the tool, please get in contact with us.
        </p>
        <p>
          CollectiveOne wants to become, itself, a decentralized project, and be
          developed openly with contributions from new participants.
          The development pace of CollectiveOne will depend on the amount of contributors.
          It wants to be a product-driven project. The more contributors, the faster it should go.
        </p>
        <br />
        <br />
        <p>
          <a class="learnmore" href="https://drive.google.com/open?id=1thbrickvboN7umt339GfKDfb3Jk29crhmkS1-swC61ZtwdyNrl3dOsY5A43pohgcBrxPYRKoFYwXSayA">...learn more</a>
          or
          <a href="/v/slack"> participate!</a>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'hello',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  },
  head: {
    title: {
      inner: 'This is a title'
    },
    meta: [
      {property: 'og:title', content: 'Welcome'},
      {property: 'og:description" content="CollectiveOne is a method (and a platform) to develop open, decentralized and collaborative projects: projects to which anyone can, potentially, contribute, and which are collectively owned and self-governed by their contributors'},
      {property: 'og:type" content="website'},
      {property: 'og:url" content="http://www.collectiveone.org/'},
      {property: 'og:image" content="http://www.collectiveone.org/images/Logo.png'}
    ]
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.preferred_browser_div {
	text-align: center;
	width: 90%;
	clear: both;
	margin-bottom: 10px;
	margin: 0 auto;
	max-width: 720px;
	margin-top: 10px;
}

.presentation_container {
	width:90%;
	max-width: 720px;
	clear: both;
	margin: 0 auto;
	margin-top: 10px;
}

.video_container {
	position:relative;
	padding-bottom:56.25%;
	padding-top:0px;
	height:0;
	overflow:hidden;
}

.video_iframe {
	position:absolute;
	top:0;
	left:0;
	width:100%;
	height:100%;
	max-width:720px;
}

.under_presentation {
	text-align: center;
	display: block;
	margin: 0 auto;
	margin-top: 20px;
	overflow: auto;
}

.under_presentation .info {
}

.under_presentation .principles {
}

.under_presentation .principle {
}

.under_presentation .roadmap {

}
</style>
