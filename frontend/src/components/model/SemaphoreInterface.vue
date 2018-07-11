<template lang="html">
  <div class="w3-row semaphores-container">
    <div class="w3-col s4">
      <div class="avatars-container avatars-container-red">
        <div v-for="semaphore in redSemaphores"
          :key="semaphore.id"
          class="avatar-semaphore semaphore-red-border">
          <app-user-avatar
            :user="semaphore.author"
            :showName="false" :small="true">
          </app-user-avatar>
        </div>
      </div>
    </div>
    <div class="w3-col s4">
      <div class="avatars-container avatars-container-yellow">
        <div v-for="semaphore in yellowSemaphores"
          :key="semaphore.id"
          class="avatar-semaphore semaphore-yellow-border">
          <app-user-avatar
            :user="semaphore.author"
            :showName="false" :small="true">
          </app-user-avatar>
        </div>
      </div>
    </div>
    <div class="w3-col s4">
      <div class="avatars-container avatars-container-green">
        <div v-for="semaphore in greenSemaphores"
          :key="semaphore.id"
          class="avatar-semaphore semaphore-green-border">
          <app-user-avatar
            :user="semaphore.author"
            :showName="false" :small="true">
          </app-user-avatar>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    elementId: {
      type: String
    },
    forceUpdate: {
      type: Boolean,
      default: false
    }
  },

  watch: {
    forceUpdate () {
      this.update()
    }
  },

  data () {
    return {
      semaphores: []
    }
  },

  computed: {
    redSemaphores () {
      return this.semaphores.filter((semaphore) => {
        return semaphore.state === 'RED'
      })
    },
    yellowSemaphores () {
      return this.semaphores.filter((semaphore) => {
        return semaphore.state === 'YELLOW'
      })
    },
    greenSemaphores () {
      return this.semaphores.filter((semaphore) => {
        return semaphore.state === 'GREEN'
      })
    }
  },

  methods: {
    update () {
      this.axios.get('/1/model/cardAddition/' + this.elementId + '/semaphores', {
        params: {
          elementType: 'CARD_WRAPPER_ADDITION'
        }
      }).then((response) => {
        this.semaphores = response.data.data
      })
    }
  },

  mounted () {
    this.update()
  }
}
</script>

<style scoped>

.semaphores-container {
  padding: 9px 3px 3px 3px;
}

.avatars-container {
  width: 100%;
}

.avatars-container-red {
  text-align: left;
}

.avatars-container-yellow {
  text-align: center;
}

.avatars-container-green {
  text-align: right;
}

.avatar-semaphore {
  height: 33px;
  width: 33px;
  border-radius: 16px;
  border-width: 3px;
  border-style: solid;
  display: inline-block;
  text-align: center;
  margin: 1px 1px;
}

.w3-col {
  min-height: 1px;
}

</style>
