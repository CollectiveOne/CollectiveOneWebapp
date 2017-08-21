<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="div-close-modal w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times fa-close-modal" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>{{ section.title }} section</h2>
        </div>

        <div v-if="section" class="w3-container div-modal-content">
          <label class=""><b>Section description:</b></label>
          <div class="w3-padding light-grey">
            {{ section.description }}
          </div>

          <br>
          <label class=""><b>Contents:</b></label>
          <ul class="light-grey">
            <li>{{ section.nCards }} card.</li>
            <li>{{ section.nSubsections }} subsections</li>
            <li>{{ section.nCardsSubsections }} cards within those subsections.</li>
          </ul>

          <br>
          <label class=""><b>Activity:</b></label>
          <br>
          <button class="w3-button app-button">comments</button>
          <button class="w3-button app-button">editions</button>
          <div v-for="comment in section.comments">
              <div class="w3-panel">
                {{ comment.user }}
                <br>
                {{ comment.text }}
              </div>
          </div>

        </div>

      </div>
    </div>
  </div>
</template>

<script>
export default {

  props: {
    sectionId: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      section: null
    }
  },

  computed: {
  },

  methods: {
    updateSectionData () {
      // this.axios.get('/1/secured/token/' + this.assetId).then((response) => {
      /* TODO: update backend to return section data */
      this.section = {
        id: '1010',
        title: 'Vision',
        description: 'Describes the vision of CollectiveOne in the long-term',
        nCards: 1,
        nCardsSubsections: 15,
        nSubsections: 3,
        comments: [
          {
            user: 'user 1',
            text: 'Lorem ipsum dolor sit amet, no novum viris tempor vim. Id mel omnis animal epicuri, wisi philosophia vel eu. Ius iuvaret abhorreant ad. Vix dolor civibus ex, aeque persecuti constituam sed in. Vis id alteru'
          },
          {
            user: 'user 2',
            text: 'Sapientem corrumpit forensibus usu at. Te sed dolorem maluisset, vix stet audiam neglegentur ex. Vim id minim iudico platonem. No quaeque perpetua sententiae per, id congue veniam vix. Pro erant regione ea.'
          },
          {
            user: 'user 3',
            text: 'Clita voluptua mel no. Enim ceteros ut mea, mei percipit invenire cu. Fabulas efficiantur signiferumque vel eu. Viris tacimates id sed. Ea nec eirmod volumus.'
          }
        ]
      }
      // })
    },
    closeThis () {
      this.$emit('close')
    }
  },

  mounted () {
    this.updateSectionData()
  }
}
</script>

<style scoped>

</style>
