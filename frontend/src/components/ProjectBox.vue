<template lang="html">
  <div class="col card">

    <div class="card-block">

      <h5 class="card-title"><a href="#">
        {{ project.name }}
      </a></h5>

      <p class="card-text">
        <vue-markdown>{{ project.description }}</vue-markdown>
      </p>

    </div>

  </div>
</template>

<script>
export default {
  props: {
    project: {
      type: Object,
      default: {
        name: 'ProjectName',
        description: 'Project description'
      }
    }
  },

  data () {
    return {
    }
  }
}
</script>

<style scoped>

.card-text {
  height: 300px;
  overflow: auto;
}

</style>
