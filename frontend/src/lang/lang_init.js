import Vue from 'vue'
import VueI18n from 'vue-i18n'
import { translations } from './index.js'

const getLang = function () {
  if (navigator.languages !== undefined) {
    return navigator.languages[0].substr(0, 2)
  } else {
    return navigator.language.substr(0, 2)
  }
}

Vue.use(VueI18n)

// Create VueI18n instance with options
export const i18n = new VueI18n({
  locale: getLang(),
  messages: translations
})
