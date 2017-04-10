<template lang="html">

  <div class="col cb-container card">

    <div class="header">

      <div class="badge badge-default state" :class="{'state-open': isOpen, 'state-assigned': isAssigned}">{{cbtion.state}}</div>

      <div class="promotion">
        <div class="arrow">&#x25B2;</div>
        <div class="relevance">{{ cbtion.relevance }}</div>
        <div class="arrow">&#x25BC;</div>
      </div>

      <div class="location">
        <a href="#">{{cbtion.projectName}}</a>
        <a class="" href="#">+{{cbtion.goalTag}}</a>
        <div class="parent-goals">
          <span v-if="cbtion.parentGoalsTags.length > 0">in</span>
          <a class="" href="#" v-for="parent in cbtion.parentGoalsTags"> +{{parent}}</a>
        </div>
      </div>

    </div>

    <div class="row">

      <div class="col card-block">

        <h5 class="card-title"><a href="#">
          {{ cbtion.title }}
        </a></h5>

        <p :id="'cbtion-' + cbtion.id + '-text'" class="card-text card-text-hidden" :class="{'card-text-expanded': descriptionExpand}">
          <vue-markdown>{{ cbtion.description }}</vue-markdown>
        </p>

        <img src="../assets/images/arrow-down.png" class="expand-icon" @click="descriptionExpand = true" v-if="showTextExpand && !descriptionExpand"></img>
        <img src="../assets/images/arrow-up.png" class="expand-icon" @click="descriptionExpand = false" v-if="descriptionExpand"></img>

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
      showTextExpand: false,
      descriptionExpand: false
    }
  },

  computed: {
    isOpen () {
      return this.cbtion.state === 'OPEN'
    },

    isAssigned () {
      return this.cbtion.state === 'ASSIGNED'
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

    // parent goals ordered from goal to super goals
    this.cbtion.parentGoalsTags = this.cbtion.parentGoalsTags.reverse()
  }
}
</script>

<style scoped>

.state {
  position: absolute;
  margin-left: auto;
  right: 15px;
}

.state-open {
  background-color: rgb(107, 184, 149)
}

.state-assigned {
  background-color: rgb(159, 119, 0)
}

.promotion {
  color: #9d9d9d;
  width: 10%;
  float: left;
  text-align: center;
  padding-top: 3px;
}

.promotion .arrow {
  height: 15px;
  line-height: 15px;
  padding: 0;
  cursor: pointer;
}

.arrow:hover {
  color: rgb(66, 190, 190)
}


.promotion .relevance {
  font-size: 10px;
  height: 10px;
  line-height: 10px;
  padding-top: 1px;
}

.location {
  width: 90% ;
  float: left;
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

.card {
  padding-top: 10px;
}

.card-block {
  padding-top: 10px;
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

.card-text-expanded {
  max-height: none;
}

.expand-icon {
  cursor: pointer;
  width: 20px;
  position: relative;
  float: right;
  margin-top: -20px;
  margin-right: 0px;
}

</style>
