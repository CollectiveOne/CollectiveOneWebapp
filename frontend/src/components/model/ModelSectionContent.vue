<template lang="html">
  <div class="section-content">
    <div class="w3-row control-row w3-border-bottom">
      <div class="w3-col m6">
        <app-section-info></app-section-info>
      </div>
      <div class="w3-col m6 section-tabs w3-center">
        <router-link :to="{ name: 'ModelSectionMessages'}"
          class="tab-btn-space">
          <div class="tab-btn noselect" :class="{'bold-text': isChat, 'button-blue': isChat}">
            <span class="w3-hide-small w3-hide-medium tab-btn-text">Chat</span>
            <span class="w3-hide-large"><i class="fa fa-home" aria-hidden="true"></i></span>
          </div>
        </router-link>
        <router-link :to="{ name: 'ModelSectionCards'}"
          class="tab-btn-space">
          <div class="tab-btn noselect" :class="{'bold-text': isCards, 'button-blue': isCards}">
            <span class="w3-hide-small w3-hide-medium tab-btn-text">Cards</span>
            <span class="w3-hide-large"><i class="fa fa-lightbulb-o" aria-hidden="true"></i></span>
          </div>
        </router-link>
      </div>
    </div>
    <div class="w3-row">
      <transition name="fadeenter">
        <router-view></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
import SectionInfo from '@/components/model/SectionInfo'

export default {

  components: {
    'app-section-info': SectionInfo
  },

  computed: {
    isChat () {
      return this.$route.name === 'ModelSectionMessages'
    },
    isCards () {
      return this.$route.name === 'ModelSectionCards' || this.$route.name === 'ModelSectionCard'
    },
    isDoc () {
      return this.$route.name === 'ModelSectionDoc'
    }
  },

  methods: {
    redirect () {
      if (this.$route.name === 'ModelSectionContent') {
        console.log('redirecting from ModelSectionContent to ModelSectionCards')
        this.$router.replace({name: 'ModelSectionCards'})
      }
    }
  },

  watch: {
    '$route' () {
      this.redirect()
    }
  },

  created () {
    this.redirect()
  }
}
</script>

<style scoped>

.w3-col {
  min-height: 1px;
}

.section-content {
}

.control-row {
  padding: 6px 18px;
}

.section-tabs {
  display: flex;
  flex-direction: row;
}

.tab-btn-space {
  display: block;
  padding: 6px 10px 0px 10px;
  width: 50%;
}

.tab-btn {
  padding: 0px 6px;
  border-radius: 6px;
}

.tab-btn-text {
  font-size: 14px;
}

.bold-text {
  font-weight: bold;
}

</style>
