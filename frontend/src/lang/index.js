/* ---------------------------------------------------------- */
/* LEVELS HELP ENG */
/* ---------------------------------------------------------- */
const LEVELS_DOWN_VERB = 'This will <b>reduce</b> the depth of subsections that are aggregated in this view by one level less.<br><br>'
const LEVELS_UP_VERB = 'This will <b>increase</b> the depth of subsections that are aggregated in this view by one level more.<br><br>'
const LEVELS_ALL_VERB = 'This will aggregate the cards from <b>all subsections</b> (of all levels of subsections) under this section.<br><br>'

const LEVELS_BASE = 'Selected subsections are highlighted in blue in the navigation panel on the left.<br><br>- When levels is 1, you will only see the cards in this section. <br><br>- When levels is 2, you will see the cards of this section, followed by the cards of all its immediate subsections. <br><br>- When levels is 3, you will see the cards of the subsections of the subsections too. <br><br>- ... and so on ...'

const LEVELS_DOWN = LEVELS_DOWN_VERB + LEVELS_BASE
const LEVELS_UP = LEVELS_UP_VERB + LEVELS_BASE
const LEVELS_ALL = LEVELS_ALL_VERB + LEVELS_BASE

const SCOPE_CARDS_BASE = 'A card can be added in a section with different scopes:<br><br>- "PRIVATE": only the user who adds the card to that section can see it.<br><br>- "SHARED": all the members of that initiative ecosystem (all parent initiatives and their subinitiatives) can see and comment the card (but only the adder can remove, move or edit it)<br><br>- "COMMON": Visible to all initiative ecosystem members, and if the initiative is public, visible to anonymous visitors. All the editors of the initiative can edit it.'

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
      'PRIVATE-CARDS-DET': SCOPE_CARDS_BASE,

      'SHARED-CARDS-TT': 'shared cards',
      'SHARED-CARDS-DET': SCOPE_CARDS_BASE,

      'COMMON-CARDS-TT': 'common cards',
      'COMMON-CARDS-DET': SCOPE_CARDS_BASE,

      'SHOW-SECTION-ORDER-TT': 'show cards ordered by sections',
      'SHOW-SECTION-ORDER-DET': 'This view will show all the cards of the selected sections (based on the current depth level) and in their pre-specified per-section order. The number of cards shown will depend on the depth level only',

      'SEARCH-CARDS-TT': 'seach all cards under this section',
      'SEARCH-CARDS-DET': 'This will search all cards under this section (and all its subsecions) matching the input query. Top ten results will be shown by default, and you can sort the results based on different criteria.',

      'SHOW-MESSAGES-TT': 'messages',
      'SHOW-MESSAGES-DET': 'This will show or hide messages from the activity timeline',

      'SHOW-EVENTS-TT': 'other events',
      'SHOW-EVENTS-DET': 'This will show or hide events (everything that is not a message) from the activity timeline'
    }
  }
}

export {
  translations
}
