/* ---------------------------------------------------------- */
/* LEVELS HELP ENG */
/* ---------------------------------------------------------- */
const LEVELS_DOWN_VERB = 'This will <b>reduce</b> the depth of subsections that are aggregated in this view by one level less.<br><br>'
const LEVELS_UP_VERB = 'This will <b>increase</b> the depth of subsections that are aggregated in this view by one level more.<br><br>'
const LEVELS_ALL_VERB = 'This will aggregate the cards from <b>all subsections</b> of this section.<br><br>'

const LEVELS_BASE = 'Selected subsections are highlighted in blue in the navigation panel on the left.<br><br>- When levels is 1, you will only see the cards in this section. <br><br>- When levels is 2, you will see the cards of this section, followed by the cards of all its immediate subsections. <br><br>- When levels is 3, you will see the cards of the subsections of the subsections tooand so on. <br><br>- ... and so on ...'

const LEVELS_DOWN = LEVELS_DOWN_VERB + LEVELS_BASE
const LEVELS_UP = LEVELS_UP_VERB + LEVELS_BASE
const LEVELS_ALL = LEVELS_ALL_VERB + LEVELS_BASE

/* ---------------------------------------------------------- */
/* TRANSLATIONS */
/* ---------------------------------------------------------- */
const translations = {
  en: {
    general: {
      'SHOW': 'show',
      'HIDE': 'hide'
    },
    help: {
      'MESSAGES-TAB-TT': 'messages',
      'MESSAGES-TAB-DET': 'Here you will find a list of all messages and events that were sent or occurred in this section. The list will also include all the messages and events of the all the subsections',

      'CARDS-SUMMARY-TAB-TT': 'cards summary',
      'CARDS-SUMMARY-TAB-DET': 'Here you will find a <b>condensed</b> list of all the cards in this section (and its subsections if selected levels is greater than 2)',

      'CARDS-TAB-TT': 'cards',
      'CARDS-TAB-DET': 'Here you will find a list of all the cards in this section (and its subsections if selected levels is greater than 2)',

      'CARDS-DOC-VIEW-TT': 'document view',
      'CARDS-DOC-VIEW-DET': 'Here you will see the contents of all the cards in this section (and its subsections if selected levels is greater than 2) displayed as a document',

      'REDUCE-LEVELS-TT': 'see less levels',
      'REDUCE-LEVELS-DET': LEVELS_DOWN,

      'INCREASE-LEVELS-TT': 'see more levels',
      'INCREASE-LEVELS-DET': LEVELS_UP,

      'SEE-ALL-LEVELS-TT': 'see all levels',
      'SEE-ALL-LEVELS-DET': LEVELS_ALL,

      'PRIVATE-CARDS-TT': 'private cards',
      'PRIVATE-CARDS-DET': 'Private cards are only visible to the user who adds them to a section.'
    }
  }
}

export {
  translations
}
