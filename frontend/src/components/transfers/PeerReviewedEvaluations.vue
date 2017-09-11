<template lang="html">
  <div class="w3-container table-container">
    <table class="w3-table w3-striped w3-bordered w3-centered table-element">
      <thead>
        <tr class="top-row">
          <th colspan="2"></th>
          <th :colspan="receivers.length">receivers</th>
        </tr>
        <tr class="evaluators-row">
          <th colspan="2"><i class="fa fa-arrow-down" aria-hidden="true"></i> evaluators</th>
          <th v-for="receiver in receivers">
            <app-user-avatar :user="receiver.user" :showName="false"></app-user-avatar>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="evaluator in evaluators">
          <td class="avatar-col">
            <app-user-avatar :user="evaluator.user" class="user-container" :showName="false"></app-user-avatar>
          </td>
          <td>
            {{ evaluator.user.nickname }}
          </td>
          <td v-for="receiver in receivers" class="w3-large">
            {{ getEvaluationOf(evaluator, receiver) }}
          </td>
        </tr>
      </tbody>
    </table>
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
          if (grade.state === 'DONE') {
            if (grade.type === 'DONT_KNOW') {
              return 'DK'
            } else {
              return grade.percent.toFixed(0) + ' %'
            }
          } else {
            return '-'
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

.top-row {
  border-style: none !important;
}

.evaluators-row th {
  vertical-align: bottom !important;
}
</style>
