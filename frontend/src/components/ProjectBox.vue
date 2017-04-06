<template lang="html">
  <div id="projectBox_div" class="cp_box_gray_thin">
  	<div id="left_div">
  		<div id="left_top_div">
  			<div id="projectname_div">{{ projectData.name }}</div>
        <app-element-follow></app-element-follow>
  		</div>
  		<div id="description_div">{{ markdown.toHTML(projectData.description) }}</div>
  	</div>
  	<div id="right_div">
  	</div>
  </div>
</template>

<script>
export default {
  props: {
    projectData: {
      type: Object,
      default: {
        name: 'ProjectName',
        description: 'Project description'
      }
    }
  }
}
</script>

<style scoped>

#projectBox_div {
	height: 150px;
	width: 100%;
	max-width:450px;
	clear:none;
	margin: 0 auto;
	overflow: auto;
}

#left_div {
	width: 100%;
	height: 100%;
	display: block;
}

#projectname_div {
	font-size: 16px;
	width: calc(100% - 60px);
	float: left;
}

#projectfollow_div {
	font-size: 16px;
	width: 60px;
	float: left;
	display: none;
}

#projectfollow_div img {
	width: 20px;
	margin-right: 10px;
	float: right;
	cursor: pointer;
}

#projectfollow_div #watched-img {
	margin-top: 5px;
}

#description_div {
	height: 95px;
	overflow: auto;
	margin-top: 5px;
	float: left;
}

#right_div {
	width: 0%;
	float:left;
}

</style>
