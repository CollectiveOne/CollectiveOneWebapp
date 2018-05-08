<template lang="html">
  <div class="w3-cell-row editor-container">
    <div v-if="mentionedUsers.length > 0" class="w3-row mentions-row">
      <span>Mentions :</span>
      <span class="w3-tag mentionedUsers light-grey"
        v-for="(user, i) in mentionedUsers">@{{ user.nickname }}
        <i class="fa fa-close mentionedUsersClose cursor-pointer" @click="removeUserSelected(i)"></i>
      </span>
    </div>
    <div class="w3-row">
      <div v-if="mentioning" class="dropup">
        <div class="dropup-content">
          <div v-for="user in userSuggestions"
            @click="userSelected(user)">
            <app-user-avatar :user="user" :small="true"></app-user-avatar>
          </div>
        </div>
      </div>
      <div class="w3-row">

        <textarea
          v-if="!preview"
          class="this-textarea"
          :class="textareaClasses"
          ref="mdArea"
          @focus="atFocus($event)"
          @blur="$emit('c-blur', $event)"
          @keyup="checkMentions()"
          @click="checkMentions()"
          v-model="text"
          :placeholder="placeholder">
        </textarea>
        <div v-if="preview || sideBySide" class="this-markdown" :class="markdownClasses">
          <vue-markdown class="marked-text" :source="text"></vue-markdown>
        </div>
      </div>

    </div>
    <div class="w3-cell buttons-column w3-cell-top">
      <div @click="togglePreview()"
        class="w3-tag cursor-pointer a-button" :class="{'gray-1': !preview, 'button-blue': preview}">
        <i class="fa fa-eye" aria-hidden="true"></i>
      </div>
      <div @click="toggleSideBySide()"
        class="w3-tag gray-1 cursor-pointer a-button" :class="{'gray-1': !sideBySide, 'button-blue': sideBySide}">
        <i class="fa fa-columns" aria-hidden="true"></i>
      </div>
      <a href="https://en.support.wordpress.com/markdown-quick-reference/" target="_blank"
        class="w3-tag gray-1 cursor-pointer a-button">
        <i class="fa fa-question-circle" aria-hidden="true"></i>
      </a>
    </div>
    <div v-if="showSend" class="w3-cell send-button-column w3-cell-top">
      <div class="send-button-container">
        <button class="w3-button app-button" name="button"
          @click="sendData()">
          <i class="fa fa-paper-plane" aria-hidden="true"></i>
          <br>
          <small>ctr + &crarr;</small>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { spliceString } from '@/lib/common.js'

export default {

  props: {
    value: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: ''
    },
    showSend: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      text: '',
      preview: false,
      sideBySide: false,
      mentioningQuery: '',
      userSuggestions: [],
      mentionedUsers: [],
      lastMentionIx: 0
    }
  },

  watch: {
    value () {
      this.text = this.value
      setTimeout(() => { this.checkHeight() }, 500)
    },
    text () {
      this.$emit('input', this.text)
      this.checkHeight()
    },
    mentioningQuery () {
      this.updateMentionSuggestions()
    }
  },

  computed: {
    textareaClasses () {
      if (!this.sideBySide) {
        return {
          'w3-input': true,
          'w3-border': true
        }
      } else {
        return {
          'w3-input': true,
          'w3-border': true,
          'w3-col': true,
          'm6': true
        }
      }
    },
    markdownClasses () {
      if (!this.sideBySide) {
        return {
          'w3-input': true,
          'w3-border': true
        }
      } else {
        return {
          'w3-border': true,
          'w3-col': true,
          'm6': true
        }
      }
    },
    mentioning () {
      return this.mentioningQuery !== ''
    },
    initiativeId () {
      return this.$route.params.initiativeId
    }
  },

  methods: {
    removeUserSelected (index) {
      let user = this.mentionedUsers[index]
      /* remove string */
      let ixFound = this.text.search('@' + user.nickname)
      if (ixFound !== -1) {
        /* remove the name with the @ too */
        this.text = spliceString(this.text, ixFound - 1, user.nickname.length + 1, '')
      }
      this.mentionedUsers.splice(index, 1)
    },

    getMentions () {
      let re = /@\w*/g
      let mentions = []
      let m

      /* get mentions and their indexes */
      do {
        m = re.exec(this.text)
        if (m && (this.text.charAt(m.index - 1) === ' ' || this.text.length)) {
          mentions.push(m)
        }
      } while (m)

      return mentions
    },

    checkMentions () {
      /* regular expression of @word */
      let mentions = this.getMentions()

      /* check if cursor is at one mention */
      let selectedMention = []
      for (let ix = 0; ix < mentions.length; ix++) {
        let e = mentions[ix]
        if ((e.index <= this.$refs.mdArea.selectionStart) &&
          (this.$refs.mdArea.selectionStart <= (e.index + e[0].length))) {
          selectedMention = e
          /* need to remember ix to know which mention to update after user select */
          this.lastMentionIx = ix
        }
      }

      // console.log(selectedMention)
      if (selectedMention.length > 0) {
        /* remove the at and store in mentioning */
        this.mentioningQuery = selectedMention[0].substr(0)
      } else {
        this.mentioningQuery = ''
      }

      /* check mentioned user are still mentioned */
      for (let ix = 0; ix < this.mentionedUsers.length; ix++) {
        if (this.text.search(this.mentionedUsers[ix].nickname) === -1) {
          this.removeUserSelected(ix)
        }
      }
    },

    updateMentionSuggestions () {
      this.axios.get('/1/initiative/' + this.initiativeId + '/members/suggestions?q=' + this.mentioningQuery.substr(1)).then((response) => {
        if (response.data.result === 'success') {
          this.userSuggestions = this.process ? self.process(response.data) : response.data.data
        }
      })
    },

    userSelected (user) {
      let mentions = this.getMentions()
      let mention = mentions[this.lastMentionIx]

      /* remove mention */
      this.text = spliceString(this.text, mention.index + 1, mention[0].length - 1, user.nickname)
      this.$refs.mdArea.focus()
      this.$refs.mdArea.selectionStart = mention.index + user.nickname.length

      /* add mentioned user if not already added */
      if (this.mentionedUsers.filter(e => e.c1Id === user.c1Id).length === 0) {
        this.mentionedUsers.push(user)
      }
      this.mentioningQuery = ''
    },

    atFocus (event) {
      this.$emit('c-focus', event)
      this.checkHeight()
    },

    checkHeight () {
      if (this.$refs.mdArea) {
        if (this.text !== '') {
          /* resize text area */
          this.$refs.mdArea.style.height = (this.$refs.mdArea.scrollHeight) + 'px'
        } else {
          this.$refs.mdArea.style.height = '0px'
        }
      }
    },

    togglePreview () {
      this.sideBySide = false
      this.preview = !this.preview
    },

    toggleSideBySide () {
      this.preview = false
      this.sideBySide = !this.sideBySide
    },

    atKeydown (e) {
      /* ctr + enter */
      if (e.keyCode === 13 && e.ctrlKey) {
        e.preventDefault()
        this.sendData()
      }
    },

    sendData () {
      var info = {
        message: this.value,
        mentions: this.mentionedUsers.map(e => e.c1Id)
      }
      this.$emit('send', info)
      this.userSuggestions = []
      this.mentionedUsers = []
      this.preview = false
      this.sideBySide = false
    }
  },

  mounted () {
    this.text = this.value
    window.addEventListener('keydown', this.atKeydown)

    /* autoresize textarea */
    this.$refs.mdArea.setAttribute('style', 'height:' + (this.$refs.mdArea.scrollHeight) + 'px;overflow-y:hidden;')

    setTimeout(() => {
      this.checkHeight()
    }, 500)
  },

  destroyed () {
    window.removeEventListener('keydown', this.atKeydown)
  }
}
</script>

<style scoped>

.this-textarea {
  max-height: 50vh;
  overflow-y: auto !important;
}

.w3-cell-top {
  vertical-align: bottom;
}

.this-textarea, .this-markdown {
  min-height: 68px;
}

.buttons-column {
  width: 32px;
  height: 100%;
  padding: 0px !important;
}

.a-button {
  width: 32px;
  border-top-right-radius: 3px;
  border-bottom-right-radius: 3px;
  margin-bottom: 1px;
}

.send-button-column {
  width: 60px;
  height: 100%;
}

.send-button-container {
  height: 100%;
  padding-left: 10px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}

.editor-container {
  position: relative;
}

.mentions-row {
  font-size: 12px;
}

.dropup {
  position: relative;
  width: 100%
}

.dropup-content {
  display: none;
  position: absolute;
  background-color: #f1f1f1;
  width: 790px;
  bottom: 0px;
  z-index: 1;
}

.dropup-content div {
  color: black;
  padding: 6px 16px;
  text-decoration: none;
  display: block;
}

.dropup-content div:hover {
  background-color: #ccc
}

.dropup .dropup-content {
    display: block; /*triggers to showList*/
}

.mentionedUsers {
   padding: 0px 10px !important;
   margin-left: 5px;
   border-radius: 3px;
}

.mentionedUsersClose {
   padding: 0px 10px !important;
}

.mentionedUsersClose:hover {
  color: #C0C0C0;
}
</style>
