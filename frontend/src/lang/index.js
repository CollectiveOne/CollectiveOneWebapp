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

/* ---------------------------------------------------------- */
/* SCOPES HELP ENG */
/* ---------------------------------------------------------- */

const SCOPE_COMMON = '<br><br>- "PRIVATE": only the user who adds the element to that section can see it.<br><br>- "SHARED": all the members of that initiative ecosystem (all parent initiatives and their subinitiatives) can see it  (but only the user who added the card can remove, move or edit it)<br><br>- "COMMON": Visible to all initiative ecosystem members, and if the initiative is public, visible to non-members too. Only EDITORS or ADMINS of the initiative can edit COMMON cards.<br><br>- <b>Note:</b> The same element can have different scopes on different sections.'

const SCOPE_CARDS_BASE = 'A card can be added in a section with different scopes:' + SCOPE_COMMON

const SCOPE_SUBSECTIONS_BASE = 'A subsection can be added in a section with different scopes:' + SCOPE_COMMON

/* ---------------------------------------------------------- */
/* DRAG AND DROP HELP ENG */
/* ---------------------------------------------------------- */

const ENABLE_DD_CARDS = 'This will enable/disable Drag and Drop of cards, allowing you to<br><br>- Reorder cards in this section.<br><br>- Moving cards to other sections by dropping them over an existing section on the navigation panel on the left.<br><br>- Copying (adding) cards to another section by dropping them on an existing section on the navigation panel on the left while holding the CTRL key.<br><br><b>Note:</b> PRIVATE and SHARED cards can only be placed <i>after</i> a COMMON card, they cannot be placed <i>before</i> one.'

const ENABLE_DD_SECTIONS = 'This will enable/disable Drag and Drop of sections, allowing you to<br><br>- Rorder subsections in a given section.<br><br>- Moving subsections to other sections by dropping them over an existing section (dropping them in the left half of the target section will add the dragged section at the <i>same level</i>, while dropping it on the right half will add it <i>as a subsection</i>).<br><br>- Copying (adding) subsections to another section by dropping them on an existing section while holding the CTRL key (same rules apply as for moving a section).<br><br><b>Note:</b> PRIVATE and SHARED sections can only be placed <i>after</i> a COMMON section, they cannot be placed <i>before</i> one.'

/* ---------------------------------------------------------- */
/* TRANSLATIONS */
/* ---------------------------------------------------------- */
const translations = {
  en: {
    general: {
      'SHOW': 'show',
      'HIDE': 'hide',
      'ENABLE': 'enable',
      'DISABLE': 'disable',
      'LOGIN_SIGNUP': 'LOG IN / SIGNUP',
      'NO_RESULTS_FOUND': 'no results found',
      'CREATE_NEW': 'create new',
      'NAME': 'Name',
      'DESCRIPTION': 'Description',
      'REQUIRED': 'required',
      'FIELD_CANNOT_BE_EMPTY': 'field cannot be empty',
      'FIELD_TOO_LONG': 'field too long',
      'FIELD_ONLY_LETTERS': 'field can only contain letters, numbers and/or spaces',
      'OK': 'ok',
      'YES': 'yes',
      'NO': 'no',
      'ACCEPT': 'Accept',
      'CANCEL': 'Cancel',
      'CONFIRM': 'Confirm',
      'ADD': 'add',
      'INHERIT': 'Inherit',
      'CUSTOM': 'Custom',
      'EDIT': 'edit',
      'DELETE': 'delete',
      'REMOVE': 'remove',
      'CREATE': 'Create',
      'SAVE': 'Save',
      'NOTIFICATIONS': 'notifications',
      'CHANGE': 'change',
      'UPLOAD_IMAGE': 'upload image',
      'NOTES': 'Notes',
      'CONFIGURATION': 'Configuration',
      'SHOW_MORE': 'show more',
      'MORE': 'more'
    },
    landing: {
      'PRINCIPLES': 'PRINCIPLES',
      'FEATURES': 'FEATURES',
      'DEMOS': 'DEMOS',
      'PARTICIPATE': 'PARTICIPATE',
      'WELCOME': 'WELCOME TO',
      'BRIEF': 'CollectiveOne is a method (and a platform) to develop open, decentralized and collaborative initiatives: initiatives to which anyone can, potentially, contribute, and which are collectively owned and self-governed by their contributors.',
      'EXPLORE': 'EXPLORE THE APP',
      'OPEN_COLLAB': 'OPEN COLLABORATION',
      'OPEN_COLLAB_CONTENT': 'Like open-source projects, entry barriers are kept low to allow anyone to contribute to an initiative in similar conditions as those of previous contributors.',
      'CONTRIB_REC': 'CONTRIBUTIONS RECORD AND VALUE',
      'CONTRIB_REC_CONTENT': 'Contributions are recognized and valued relative to each other using project-specific tokens.',
      'LIQUID_OWNERSHIP': 'LIQUID OWNERSHIP',
      'LIQUID_OWNERSHIP_CONTENT': 'The ownership of each project is linked to contributions, and, therefore, to tokens. The more you contribute, the more you should own.',
      'DIST_GOV': 'DISTRIBUTED GOVERNANCE',
      'DIST_GOV_CONTENT': 'Configurable governance at each level of an initiative enables decisions to be taken openly and collectively.',
      'TRANSP': 'TRANSPARENCY',
      'TRANSP_CONTENTS': 'The information of an initiative can be kept public as a mean to attract and enable people to contribute.',
      'VALUE_ACC': 'VALUE ACCOUNTING',
      'VALUE_ACC_CONTENT': 'Basic infrastructure for managing tokens and transferring them to subinitiatives and users.',
      'COLL_VISION': 'COLLECTIVE VISION',
      'COLL_VISION_CONTENT': 'A dedicated module that enables initiatives to collectively influence and agree on the initiatives vision and plans.',
      'TASK_MAN': 'TASK MANAGEMENT',
      'TASK_MAN_CONTENT': 'Kanban board to organize the tasks of an initiative and value these using the initiatives tokens.',
      'DISTR_GOB': 'DISTRIBUTED GOVERNANCE',
      'DISTR_GOB_CONTENT': 'A dedicated module that enables initiatives to take decisions collectively and efficiently.',
      'FLEXIBLE_CHANNELS': 'Flexibly Interconnected Channels',
      'FLEXIBLE_CHANNELS_CONTENTS': 'Collaboration is all about effective communication. In CollectiveOne channels can be flexibly nested and interconnected to better represent the real structure of your organization.',
      'PERSONALIZED_CHANNELS': 'Personal Perspectives',
      'PERSONALIZED_CHANNELS_CONTENTS': 'Each person is a world of their own. In ColllectiveOne each user can arrange the channels the way they want.',
      'CHANNEL_CONTENTS': 'Cards',
      'CHANNEL_CONTENTS_CONTENTS': 'Beside messages, channel can store cards (content that is not lost in the timeline), useful for storing relevant information, annoucements, proposals or agreements.',
      'PARTICIPATE_TEXT': 'CollectiveOne is an open project itself and is being developed using <b><a href="http://www.collectiveone.org/#/app/inits/ac119496-5e3e-1db5-815e-3f192a890001">CollectiveOne itself</a></b>. Contributions are welcome! <br><br>Get your invitation to the project slack <b><a href="http://old.collectiveone.org/v/slack" target="_blank">here</a></b> to get involved.',
      'FOLLOW_US': 'FOLLOW US!'
    },
    initiatives: {
      'MY_INITIATIVES': 'My Initiatives',
      'PUBLIC_INITIATIVES': 'Public Initiatives',
      'MY_INITS_EMPTY': 'you are not yet part of any initiative',
      'NEW_SUBINITIATIVE': 'new subinitiative',
      'CREATE_ONE': 'create one',
      'NEW': 'new initiative',
      'CURRENT_INITIATIVE': 'Current Initiative',
      'TAG': 'Tag',
      'TAGS': 'Tags',
      'FILTER_BY_TAG': 'Filter by tag',
      'SELECT_TAG': 'select tag',
      'NEW_INIT_MODAL': 'New Initiative',
      'CREATE_NEW_TOKEN_Q': 'Create a new token?',
      'ONE_ADMIN_ATLEAST': 'there must be at least one admin',
      'INITIAL_MEMBERS': 'Initial Members',
      'NEW_SUBINITIATIVE_OF': 'New subinitiative of',
      'EDIT_INITIATIVE': 'Edit Initiative',
      'COLOR': 'Color',
      'VISIBILITY': 'Visibility',
      'PRIVATE': 'private',
      'ECOSYSTEM': 'ecosystem',
      'INHERIT': 'inherit',
      'PUBLIC': 'public',
      'DELETE_INIT': 'Delete Initiative',
      'CONFIRM_DELETE_MSG': '<b>Warning:</b> This will delete this initiative and all its subinitiatives and, if there is a parent initiative, transfer all its assets back to it. Please confirm.',
      'DELETE_WARNING': '<b>Warning:</b> Are you sure you want to completely delete the initiative "{name}"? This will delete all its contents.',
      'SUBINITIATIVES': 'SUBINITIATIVES'
    },
    members: {
      'WANT_TO_CONTRIB_Q': 'Want to contribute?',
      'WANT_TO_CONTRIB_MSG': 'The administrators of this initiative will be notified of your interest and will be able to add you as a contributor to this initiative.',
      'ADMINS_NOTIFIED': 'The admins of this initiative have been notified.',
      'USER_ADDED_SUCCESS': 'User succesfully added.',
      'ERROR_NOTIFYING': ' Error notifying the admins',
      'MEMBERS_OF': 'Members of {title}',
      'MEMBERS_OF_SUBS': 'Members of subinitiatives of {title}',
      'ADD_USER': 'add user',
      'CHOOSE_ROLE': 'choose role',
      'ADMIN': 'ADMIN',
      'EDITOR': 'EDITOR',
      'MEMBER': 'MEMBER',
      'AVATAR': 'AVATAR',
      'NICKNAME': 'NICKNAME',
      'ROLE': 'ROLE',
      'DELETE': 'DELETE',
      'LEAVE': 'LEAVE'
    },
    tokens: {
      'NUMBER_OF_TOKENS': 'Number of Tokens',
      'TOKEN_NAME': 'Token Name',
      'TOKENS': 'Tokens',
      'TRANSFERS_HISTORY_FROM': 'Transfers history from {initName}',
      'TRANSFERS_HISTORY_FROM_SUBINITIATIVES_OF': 'Transfers history from subinitiatives of {initName}',
      'TRANSFER_ASSETS_Q': 'Transfer assets?',
      'NO_ASSETS_TRANSFERRED_CONF': 'warning: you have not selected any assets. Please confirm this is ok below, or specify the amount to be transferred',
      'NO_ASSETS_TRANSFERRED_MSG': 'You will not transfer assets from {initName} to this new initiative. Please confirm this is ok.',
      'TRANSFER_FROM': 'Transfer from {initName}',
      'TOKEN_TO_TRANSFER': 'Token to transfer',
      'NO_ASSETS_IN_PARENT': 'the parent initiative does not hold any tokens',
      'OF_EXISTING': 'ok existing',
      'STILL_AVAILABLE': 'Still Available',
      'UNDER_PENDING_PR': 'Under pending peer-review transfers',
      'TRANSFERRED_TO_SUBS': 'Transferred to sub-initiatives',
      'TRANSFERRED_TO_MEMS': 'Transferred to members',
      'AMOUNT_TO_TRANSF': 'Amount to be transfered',
      'AVAILABLE': 'available',
      'NO_ASSETS_HELD': 'No tokens are currently held by this initiative',
      'TRANSFER_TO_INITIATIVE': 'Transfer tokens to other initiative',
      'TRANSFER_TO': 'Transfer to ',
      'ZERO_TOKENS_ERROR': 'Please select how many tokens to transfer',
      'MOTIVE': 'Motive',
      'NO_PARENTS_ERROR': '{name} does not have parent or child initiatives',
      'TRANSFER_TO_USERS': 'Transfer tokens to users',
      'PEER_REVIEW_TITLE': 'Peer-reviewed transfer to users',
      'DIRECT_TITLE': 'Direct transfer to users',
      'DIRECT': 'Direct',
      'PEER_REVIEWED': 'Peer Reviewed',
      'NOT_ENOUGH_RECEIVERS': 'please add at least one receiver',
      'NOT_ENOUGH_EVALS': 'please add at least one evaluator',
      'NOT_ALL_DONORS': 'not all receivers can be donors',
      'PERCENTAGES_WRONG': 'percentages must sum 100%',
      'NO_ASSETS_SELECTED': 'warning: you have not selected any assets. Please confirm this is ok below, or specify the amount to be transferred',
      'INITIAL_STATE': 'Initial State',
      'ON_HOLD': 'on-hold',
      'OPEN': 'open',
      'MAX_DURATION': 'Max duration',
      'DAYS': 'day | days',
      'SELF_BIAS_VISIBLE': 'Self-bias visible',
      'ALL_EVALS_VISIBLE': 'All evaluations visible',
      'NO_ASSETS_TRASFERRED': 'You will not transfer assets to this users. Please confirm this is ok.',
      'NO_MEMBERS_SELECTED': 'no members have been selected',
      'ONE_RECEIVER': 'one receiver only',
      'MULTIPLE_RECEIVERS': 'add multiple receivers',
      'RECEIVERS': 'Receivers',
      'DONOR_WARNING': '<b>Warning:</b> {{ donorUsersStr }} will be part of the evaluation but the tokens they receive will be distributed among the rest of receivers!',
      'EVALUATORS': 'Evaluators',
      'SAME_AS_RECEIVERS': 'same as receivers',
      'DIFF_FROM_RECEIVERS_Q': 'different from receivers?',
      'DATE_UC': 'DATE',
      'FROM_UC': 'FROM',
      'VALUE_UC': 'VALUE',
      'TO_UC': 'TO',
      'MOTIVE_UC': 'MOTIVE',
      'STATUS_UC': 'STATUS',
      'TYPE_UC': 'TYPE',
      'FROM_NAME': 'from <b>{name}</b>',
      'REVERT_TX': 'revert transaction',
      'REVERT_WARNING': '<b>Warning:</b> This will order the transfer of tokens from the receivers back to the initiative. Please confirm you would like to revert this transaction.',
      'DELETE_WARNING': '<b>Warning:</b> This will delete this assignation. All evaluations made will be lost and no tokens will be transferred. Please confirm.',
      'OPEN_START_EVALS': 'open/start evaluations',
      'OPEN_WARNING': '<b>Warning:</b> This will open the peer-review process and evaluators will have {days} days to do provide their evaluations.',
      'REVERT_APPROVE_WARNING': '<b>Attention:</b> One of the admins would like to revert this transation. Do you approve this revert?',
      'REJECT': 'Reject',
      'APPROVE': 'Approve',
      'APPROVED_MSG': 'approved!',
      'PEER_REV_STATUS': '{nEvalsPending} evaluations pending closes in {timeRemaining}',
      'PEER_REV_TIP': '<b>Tip:</b> fill the values with numbers that make sense relative to each other, <b>even if they dont sum 100%</b>, and then click the "autoscale" button bellow.',
      'MY_EVAL': 'My evaluation',
      'USER_UC': 'USER',
      'DONOR_UC': 'DONOR',
      'MINE_UC': 'MINE',
      'PRE_DONOR_UC': 'PRE-DONOR',
      'FINAL_UC': 'FINAL',
      'FINAL_TOKENS_UC': 'FINAL TOKENS',
      'TOTAL_UC': 'TOTAL',
      'KNOW_DONT_UC': 'KNOW/DON\'T',
      'SELF_BIAS_UC': 'SELF-BIAS',
      'REMOVE_UC': 'REMOVE',
      'DONT_KNOW': ' don\'t know',
      'DONT_KNOW_SHORT': 'DK',
      'TOTAL_ASSIGNED': 'total assigned',
      'AUTO_ROUNDED': 'auto-rounded',
      'AUTOSCALE': 'autoscale',
      'CHANGE_MY_EVAL': 'Change my evaluation',
      'RESULTS': 'Results',
      'ALL_EVALS': 'All Evaluations'
    },
    activity: {
      'EDIT_NOTIFICATIONS_OF': 'Edit notifications for the {title} {type}',
      'EDIT_GLOBAL_NOTIFICATIONS': 'Edit your global notification preferences',
      'GENERAL_CONF': 'General configuration',
      'INHERITING_FROM': 'Inhereting notification preferences from: <br><b>{name}</b>',
      'DISABLE': 'disable',
      'ONLY_MENTIONS': 'only mentions',
      'ONLY_MESSAGES': 'only messages',
      'ALL_EVENTS': 'all events',
      'IN_APP_NOTIFICATIONS': 'In-App Notifications',
      'PUSH_NOTIFICATIONS_MSG': 'Push Notifications:<br><small>Sent only when not active in the app</small>',
      'IMMEDIATE_MAIL_MSG': 'Immediate Email:<br><small>Sent only when not active in the app</small>',
      'EMAIL_SUMMARY_MSG': 'Email Summary:<br><small>Sent only when not already read in app</small>',
      'DAILY': 'daily',
      'WEEKLY': 'weekly',
      'TITLE_AND_TYPE': '"{title}" {type}',
      'GLOBAL_NOTIFICATIONS_PREFS': 'your global notification preferences'
    },
    model: {
      'NEW_SECTION': 'New Section',
      'MODEL_SECTION': 'Model Section',
      'AS_TOP_LEVEL_IN': 'As top level section under the initiative',
      'IN_SECTION': 'In Section',
      'CREATE_NEW': 'Create New',
      'ADD_EXISTING': 'Add Existing',
      'SCOPE_IN': 'Scope (in "{title}" section)',
      'NEW_SCOPE_IN': 'New scope (in "{title}" section)',
      'PRIVATE': 'private',
      'SHARED': 'shared',
      'COMMON': 'common',
      'PRIVATE_MSG': 'Private (only I can see it)',
      'SHARED_MSG': 'Shared (only members can see it but only I can edit it)',
      'COMMON_MSG': 'Common (public if initiative is public and only members can edit it)',
      'TITLE': 'Title',
      'CONTENT': 'Content',
      'CLOSE_WARNING': 'You are currently editing this section. Are you sure you want to close it? Any changes would get lost.',
      'SEARCH_AND_SELECT_SECTION': 'search and select a section',
      'DELETE_CARD_WARNING': '<b>Warning:</b> This will delete the card from all the sections in which it is used.',
      'REMOVE_CARD_WARNING': '<b>Warning:</b> This will remove this card from the {title} section.'
    },
    help: {
      'MESSAGES-TAB-TT': 'messages',
      'MESSAGES-TAB-DET': 'Here you will find a list of all messages and events that were sent or occurred in this section. The list will also include the messages and events of all the subsections',

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
      'SHOW-SECTION-ORDER-DET': 'This view will show all the cards of the selected sections (based on the current depth level) in their pre-specified per-section order. The number of cards shown will depend on the depth level only',

      'SEARCH-CARDS-TT': 'seach all cards under this section',
      'SEARCH-CARDS-DET': 'This will search all cards under this section (and all its subsecions) matching the input query. Only the top ten cards will be initially shown.',

      'SHOW-MESSAGES-TT': 'messages',
      'SHOW-MESSAGES-DET': 'This will show or hide messages from the activity timeline',

      'SHOW-EVENTS-TT': 'other events',
      'SHOW-EVENTS-DET': 'This will show or hide events (everything that is not a message) from the activity timeline',

      'DOWNLOAD-CONTENT-TT': 'download as text file',
      'DOWNLOAD-CONTENT-DET': 'This will download the cards currenlty being shown as a marked-down text file',

      'ENABLE-DRAG-AND-DROP-TT': 'drag and drop',
      'ENABLE-DRAG-AND-DROP-CARDS-DET': ENABLE_DD_CARDS,
      'ENABLE-DRAG-AND-DROP-SECTIONS-DET': ENABLE_DD_SECTIONS,

      'PRIVATE-SECTIONS-TT': 'private sections',
      'PRIVATE-SECTIONS-DET': SCOPE_SUBSECTIONS_BASE,

      'SHARED-SECTIONS-TT': 'shared sections',
      'SHARED-SECTIONS-DET': SCOPE_SUBSECTIONS_BASE,

      'COMMON-SECTIONS-TT': 'common sections',
      'COMMON-SECTIONS-DET': SCOPE_SUBSECTIONS_BASE,

      'SECTION-DETAILS-TT': 'details',
      'SECTION-DETAILS-DET': 'One section can be a subsection of many parent sections at the same time. Here you can see a description of this section and all the places in which this section is present (those visible to you).',

      'READ-FRIENDLY-URL-TT': 'open read-only view',
      'READ-FRIENDLY-URL-DET': 'The read-only view will open a dedicated page which is perfect for sharing the contents of a section in a read-friendly version.<br><br>The view will include all COMMON cards and all COMMON subsections, at all depth levels.<br><br>You can share the link to that page with others, but access will depend on the visibility of the initiative.',

      'HOME-TAB-TT': 'initiative overview',
      'HOME-TAB-DET': 'This view shows the initiative details and a summary of the initiative assets/tokens and the transfers made to subinitiatives or members.',

      'CONTENT-TAB-TT': 'initiative content',
      'CONTENT-TAB-DET': 'This view shows  all the conversations and content of this initiative, organized in sections and cards:<br><br>- <b>A section</b> is a general purpose context, and it automatically provides a conversation space (like a #channel on a chat application), and a content space (similar to post-it boards) made out of cards.<br><br>- <b>A card</b> can contain text and (optionally) one image. Cards are designed to hold ideas, tasks, reminders or anything you want, however, because they are ordered within the section, cards can evolve to become paragraphs of a well structured section in a document.<br><br><b>Note:</b> Sections can be nested, allowing you to have nested conversation channels. Moreover, a section can be a subsection of many other sections at the same time, and a card can be included in many sections at the same time too, enabling you to flexibly aggregate the conversations and cards.',

      'MEMBERS-TAB-TT': 'initiative members',
      'MEMBERS-TAB-DET': 'This view shows all the members of this initiative and an aggregated list of the members of the subinitiatives.<br><br>Each initiative or subinitiative has its own, and independent, list of members, with their associated roles. Roles can be on of the following:<br><br>- ADMIN: who can edit the initiative details and create and transfer its assets/tokens.<br><br>- EDITOR: Who can create and edit sections and cards, but cant manipulate tokens.<br><br>- MEMBER: Who has access to the initiative (in case it is private) and can receive initiative assets.',

      'TRANSFERS-TAB-TT': 'initiative transfers',
      'TRANSFERS-TAB-DET': 'This view shows all the transfers of assets/tokens made in this initiative and an aggregated view of all the transfers made on all the subinitiatives.',

      'LANDING-BUTTON-TT': 'landing page',
      'LANDING-BUTTON-DET': 'You will find  video tutorials in the bottom of the page.',

      'HOME-BUTTON-TT': 'home page',
      'HOME-BUTTON-DET': 'In the home page you can browse public initiatives.',

      'PEER-REV-CONFIG-TT': 'Peer Review Configuration',
      'PEER-REV-CONFIG-DET': 'In a peer-review process, all the "evaluators" will be asked to propose a token distribution among the "receivers". <br><br><b>- Initial State:</b> If "on-hold", the evaluation period will not start immediately but can be started later. <br><br><b>- Max duration:</b> Once the evaluation period starts, the evaluators will have this amount of time to provide their inputs (in any case, the process is closed once all the evaluations are provided). <br><br><b>- Self-Bias Visible:</b> When enabled, the self-bias (difference between each evaluators self-evaluation and its final token attribution) will be visible<br><br><b>- All evaluations visible:</b> When enabled, the evaluations that each evaluator made on each receiver will be publicly visible.'
    }
  }
}

export {
  translations
}
