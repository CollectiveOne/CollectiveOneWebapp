<template lang="html">
  <nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse"
          data-target="#myNavbar">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <router-link to="/" class="navbar-brand">
          <img class="logo" src="../assets/images/Logo-Light.png"></img>
        </router-link>
      </div>

      <div class="collapse navbar-collapse" id="myNavbar">
        <ul class="nav navbar-nav">

          <li><router-link to="/projectList" data-toggle="tooltip" title="browse all projects">projects</router-link></li>
          <li><router-link to="/cbtionList" data-toggle="tooltip" title="browse all contribution items">contributions</router-link></li>
          <li><router-link to="/decisionList" data-toggle="tooltip" title="browse all decision items">decisions</router-link></li>
          <li><router-link to="/activityList" data-toggle="tooltip" title="browse recent activity">recent activity</router-link></li>
          <li><router-link to="/help" data-toggle="tooltip" title="help and tips">help</router-link></li>

          <li class="dropdown"><a class="dropdown-toggle"
            data-toggle="dropdown" href="#">new <span class="caret"></span></a>
            <ul class="dropdown-menu">
              <li><router-link to="/cbtionNew">Contribution</router-link></li>
              <li><router-link to="/goalNew">Goal</router-link></li>
              <li><router-link to="/decisionNew">Decision</router-link></li>
              <li><router-link to="/projectNew">Project</router-link></li>
            </ul>
          </li>
        </ul>

        <div class="navbar-text navbar-right cp_navbar-right">
          <div v-if="isUserLogged">
            Welcome username |
            <a href="./logout">logout</a>
          </div>
          <div v-else>
            <a href="./login">login</a>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  computed: {
    ...mapGetters([
      'isUserLogged'
    ])
  }
}
</script>

<style scoped>

.logo {
	width: 210px;
}

.navbar-brand {
	padding-top:0px;
	padding-bottom: 0px;
}

.navbar {
	margin-bottom: 0px;
	padding-right:10px;
}

@media (max-width: 1000px) {
  .navbar-header {
      float: none;
  }
  .navbar-left,.navbar-right {
      float: none !important;
  }
  .navbar-toggle {
      display: block;
  }
  .navbar-collapse {
      border-top: 1px solid transparent;
      box-shadow: inset 0 1px 0 rgba(255,255,255,0.1);
  }
  .navbar-fixed-top {
      top: 0;
      border-width: 0 0 1px;
  }
  .navbar-collapse.collapse {
      display: none!important;
  }
  .navbar-nav {
      float: none!important;
      margin-top: 7.5px;
  }
  .navbar-nav>li {
      float: none;
  }
  .navbar-nav>li>a {
      padding-top: 10px;
      padding-bottom: 10px;
  }
  .collapse.in{
      display:block !important;
  }
}

</style>
