<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container  w3-border-bottom">
          <h2>Edit Initiative</h2>
        </div>

        <div class="w3-container form-container">

          <div class="w3-row">
            <label class=""><b>Name</b></label>
            <input v-model="newInitiative.name"
              class="w3-input w3-hover-light-grey"
              :class="{ 'error-input' : nameErrorShow }"
              type="text">
            <app-error-panel
              :show="nameEmptyShow"
              message="please set the new name of the initiative">
            </app-error-panel>
            <app-error-panel
              :show="nameTooLarge"
              message="name too long">
            </app-error-panel>
          </div>
          <br>

          <div class="w3-row">
            <label class=""><b>Driver</b></label>
            <textarea v-model="newInitiative.driver" rows="5" class="w3-input w3-border w3-round w3-hover-light-grey"></textarea>
            <app-error-panel
              :show="driverEmptyShow"
              message="please set a driver for the initiative">
            </app-error-panel>
          </div>
          <br>

          <div class="w3-row">
            <label class=""><b>Color</b></label>
            <div class="colors-container w3-center">
              <div
                v-for="color in colors"
                class="color-picker w3-circle cursor-pointer"
                :class="{'color-picked': newInitiative.color === color}"
                :style="{'background-color': color}"
                @click="newInitiative.color = color">
              </div>
            </div>
          </div>

          <hr>
          <div class="bottom-btns-row w3-row-padding">
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button-light" @click="closeThis()">Cancel</button>
            </div>
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button" @click="accept()">Accept</button>
            </div>
          </div>

          <hr>
          <div class="w3-row w3-center w3-margin-bottom delete-row">
            <button type="button" class="w3-left w3-button app-button-danger" @click="deleteInitiativeSelected = true">Delete Initiative</button>
          </div>
          <div class="slider-container">
            <transition name="slideDownUp">
              <div v-if="deleteInitiativeSelected" class="w3-row tags-row w3-center">
                <div class="w3-padding w3-round light-grey w3-margin-bottom">
                  <p>
                    <b>Warning:</b> This will delete this initiative and all its
                    subinitiatives and, if there is a parent initiative, transfer all
                    its assets back to it. Please confirm.
                  </p>
                </div>

                <button
                  class="w3-button app-button-light button-pair"
                  @click="deleteInitiativeSelected = false">cancel
                </button>
                <button
                  class="w3-button app-button-danger button-pair"
                  @click="deleteInitiative()">confirm
                </button>
              </div>
            </transition>
          </div>

        </div>

      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      newInitiative: {
        name: '',
        driver: '',
        color: ''
      },
      colors: [
        '#009ee3',
        '#e5007d',
        '#ffed00',
        '#68b628',
        '#ff9100',
        '#e40000',
        '#00a8a8'
      ],
      nameEmptyError: false,
      driverEmptyError: false,
      deleteInitiativeSelected: false
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    nameErrorShow () {
      return this.nameEmptyShow || this.nameTooLarge
    },
    nameEmptyShow () {
      return this.nameEmptyError && this.newInitiative.name === ''
    },
    nameTooLarge () {
      return this.newInitiative.name.length > 30
    },
    driverEmptyShow () {
      return this.driverEmptyError && this.newInitiative.driver === ''
    }
  },

  mounted () {
    this.newInitiative.name = this.initiative.meta.name
    this.newInitiative.driver = this.initiative.meta.driver
    this.newInitiative.color = this.initiative.meta.color
  },

  methods: {
    closeThis () {
      this.$store.commit('showEditInitiativeModal', false)
    },
    accept () {
      var ok = true

      if (this.newInitiative.name === '') {
        this.nameEmptyError = true
        ok = false
      }

      if (this.nameTooLarge) {
        ok = false
      }

      if (this.newInitiative.driver === '') {
        this.driverEmptyError = true
        ok = false
      }

      if (ok) {
        this.axios.put('/1/secured/initiative/' + this.initiative.id, this.newInitiative).then((response) => {
          this.closeThis()
          this.$store.dispatch('refreshInitiative')
          this.$store.dispatch('updatedMyInitiatives')
        })
      }
    },
    deleteInitiative () {
      this.axios.delete('/1/secured/initiative/' + this.initiative.id).then((response) => {
        this.$store.dispatch('refreshInitiative')
        this.$store.dispatch('updatedMyInitiatives')
        window.location.href = '/'
      })
    }
  }
}
</script>

<style scoped>

.w3-modal {
  display: block;
}

.close-div {
  width: 70px;
  height: 70px;
  cursor: pointer;
  text-align: right;
}

.fa-times {
  margin-right: 20px;
  margin-top: 20px;
}

.form-container {
  padding-top: 20px;
  padding-bottom: 35px;
}

.bottom-btns-row button {
  width: 100%;
}

.colors-container {
  margin-top: 15px;
}

.color-picker {
  width: 55px;
  height: 55px;
  display: inline-block;
  margin-left: 15px;
}

.color-picker:hover {
  border-style: solid;
  border-width: 5px;
  border-color: black;
}

.color-picked {
  border-style: solid;
  border-width: 5px;
  border-color: black;
}

.delete-row {
  margin-top: 50px;
}

</style>
