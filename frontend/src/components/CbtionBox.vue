<template lang="html">

  <div class="col cb-container card">

    <div class="row">

      <div class="col-1 promotion">
        <div class="promotion_up_div">&#x25B2;</div>
        <div class="promotion_center_div"></div>
        <div class="promotion_down_div">&#x25BC;</div>
      </div>

      <div class="col-11 card-block">
        <p>
          <a href="#">{{cbtion.projectName}}</a>
          <a class="" href="#">+{{cbtion.goalTag}}</a>
          <div class="parent-goals">
            <span v-if="cbtion.parentGoalsTags.length > 0">under </span>
            <a class="" href="#" v-for="parent in cbtion.parentGoalsTags.reverse()">+{{parent}}</a>
          </div>
        </p>
        <h4 class="card-title"><a href="#">
          {{ cbtion.title }}
        </a></h4>

        <p :id="'cbtion-' + cbtion.id + '-text'" class="card-text card-text-hidden">
          <vue-markdown>{{ cbtion.description }}</vue-markdown>
        </p>

        <button v-if="showTextExpand">expand</button>

        <div class="badge badge-success">{{cbtion.state}}</div>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  props: {
    cbtion: {
      type: Object,
      default: () => {
        return {
          cbtion: {
            title: 'Title of the Contribution is long and even longer than this',
            description: ' Description of th contribution is event longer of th contribution ',
            state: 'OPEN'
          }
        }
      }
    }
  },

  data () {
    return {
      showTextExpand: false
    }
  },

  mounted () {
    var textP = document.getElementById('cbtion-' + this.cbtion.id + '-text')
    /* markdown adds a div */
    var textDiv = textP.firstElementChild
    var textStyle = window.getComputedStyle(textP)
    var maxHeight = parseInt(textStyle.getPropertyValue('max-height'), 10)

    if (textDiv.clientHeight > maxHeight) {
      this.showTextExpand = true
    }
  }
}
</script>

<style scoped>

.promotion {
  padding-top: 20px;
  color: #cccccc;
}

.parent-goals {
  font-size: 14px;
  color: #9d9d9d;
  margin-bottom: 5px;
}

.parent-goals a {
  font-size: 14px;
  color: #9d9d9d;
}

.card-block .badge {
  font-size: 14px;
}

.card-block p {
  margin-bottom: 0px;
}

.card-text-hidden {
  max-height: 50px;
  overflow: hidden;
}

</style>
