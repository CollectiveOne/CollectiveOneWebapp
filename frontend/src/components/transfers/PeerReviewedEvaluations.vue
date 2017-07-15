<template lang="html">
  <div class="w3-container table-container">
    <!--  header row -->
    <div class="w3-row">
      <div class="w3-col evaluator-col w3-center">
        <h6 class="d2-color"><i class="fa fa-arrow-down" aria-hidden="true"></i> <b>evaluators</b></h6>
      </div>
      <div class="w3-rest receivers-cols">
        <div class="w3-left receiver-col" v-for="receiver in receivers">
          <app-user-avatar :user="receiver.user" :showName="false"></app-user-avatar>
        </div>
      </div>
    </div>
    <hr class="header-line">

    <!-- evaluators rows -->
    <div class="w3-row evaluator-row" v-for="evaluator in evaluators">
      <div class="w3-col s12">
        <div class="w3-row">
          <div class="w3-col evaluator-col w3-center">
            <app-user-avatar :user="evaluator.user"></app-user-avatar>
          </div>
          <div class="w3-rest receivers-cols">
            <div class="w3-left receiver-col" v-for="receiver in receivers">
              <div class="evaluation-value-div d2-color">
                {{ getEvaluationOf(evaluator, receiver) }}
              </div>
            </div>
          </div>
        </div>
        <hr class="evaluator-line">
      </div>
    </div>
  </div>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'

export default {
  components: {
    'app-user-avatar': UserAvatar
  },

  props: {
    evaluators: Array,
    receivers: Array
  },

  methods: {
    getEvaluationOf (evaluator, receiver) {
      /* Look for the evaluation of a given receiver */
      for (var ix in evaluator.grades) {
        let grade = evaluator.grades[ix]
        if (grade.receiverUser.c1Id === receiver.user.c1Id) {
          if (grade.evaluationType === 'DONT_KNOW') {
            return 'DK'
          } else {
            return grade.percent
          }
        }
      }
      return '-'
    }
  },

  mounted () {
  }
}
</script>

<style scoped>

.table-container {
  white-space: nowrap;
  overflow: auto;
}

.evaluator-col {
  width: 250px;
}

.receiver-col {
  width: 50px;
  margin-left: 10px;
}


.receivers-cols {
  display: inline-block;
}

.header-line {
  margin: 5px 0px 10px 0px !important;
}

.evaluator-line {
  margin: 5px 0px 5px 0px !important;
}

.evaluation-value-div {
  padding-top: 5px;
  margin-left: 10px;
  font-size: 18px;
}

</style>
