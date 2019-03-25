<template lang="html">
  <div class="notifications-container">

    <popper :append-to-body="true" trigger="click" :options="popperOptions" :toggleShow="toggleShow" class="">
      <div class="notifications-list-container w3-white w3-card-4 w3-bar-block noselect">

        <div class="close-div w3-xlarge cursor-pointer gray-1-color"
          @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-row w3-border-bottom notifications-header">
          <div class="w3-row title-row">
            <b v-if="this.contextType != 'COLLECTIVEONE'">{{ $t('notifications.YOUR_NOTIFICATIONS_UNDER', {'elementName': elementTitle}) }}</b>
            <b v-else>{{ $t('notifications.YOUR_NOTIFICATIONS_IN_COLLECTIVEONE') }}</b>
          </div>
          <div class="w3-row-padding notif-control-row">
            <div class="w3-col s4"
              @click="toggleShowAll()">
              <button class="w3-button app-button"
                :class="{'app-button': !showingAll, 'app-button-light': showingAll}">
                {{ totalUnread }} {{ $t('notifications.UNREAD')}}
              </button>
            </div>
            <div class=" w3-col" :class="{'s6': shouldHaveSubscriber, 's8': !shouldHaveSubscriber}"
              @click="allNotificationsRead()">
              <button class="w3-button app-button-green">
                {{ $t('notifications.MARK_AS_READ') }}
              </button>
            </div>
            <div v-if="shouldHaveSubscriber" class="w3-col s2 config-button"
              @click="toggleConfiguration()">
              <span v-if="notsDisabled">
                 <img v-if="isInheriting" src="./../../assets/disable-light-inh.png" alt="">
                 <img v-else src="./../../assets/disable-light.png" alt="">
              </span>
              <span v-if="notsOnApp">
                <img v-if="isInheriting" src="./../../assets/onapp-light-inh.png" alt="">
                <img v-else src="./../../assets/onapp-light.png" alt="">
              </span>
              <span class="config-button-push" v-if="notsPush">
                 <img v-if="isInheriting" src="./../../assets/push-light-inh.png" alt="">
                 <img v-else src="./../../assets/push-light.png" alt="">
              </span>
            </div>
          </div>
        </div>

        <div class="slider-container">
          <transition name="slideDownUp">
            <div v-if="showConfig" class="w3-row config-table-row light-grey w3-center">
              <div v-if="!isInheriting" class="">
                <table class="config-table">
                  <tr v-if="contextType != 'COLLECTIVEONE'" @click="setInherited()">
                    <td class="icon-column">
                      <img v-if="inheritedFromIsNotsDisabled" src="./../../assets/disable-light-inh.png" alt="">
                      <img v-if="inheritedFromIsNotsOnApp" src="./../../assets/onapp.png" alt="">
                      <img v-if="inheritedFromIsNotsPush" src="./../../assets/push.png" alt="">
                    </td>
                    <td>{{ $t('notifications.INHERIT_DESC') }}</td>
                  </tr>
                  <tr @click="setDisabled()" :class="{'app-button-bg': notsDisabled}">
                    <td class="icon-column"><img src="./../../assets/disable.png" alt=""></td>
                    <td>{{ $t('notifications.DISABLE_DESC') }}</td>
                  </tr>
                  <tr @click="setOnApp()" :class="{'app-button-bg': notsOnApp}">
                    <td class="icon-column"><img src="./../../assets/onapp.png" alt=""></td>
                    <td>{{ $t('notifications.ONAPP_DESC') }}</td>
                  </tr>
                  <tr @click="setPush()" :class="{'app-button-bg': notsPush}">
                    <td class="icon-column icon-push"><img src="./../../assets/push.png" alt=""></td>
                    <td>{{ $t('notifications.PUSH_DESC') }}</td>
                  </tr>
                </table>
              </div>
              <div v-else class="">
                <div class="w3-row w3-padding"
                  v-html="$t('activity.INHERITING_FROM', { name: applicableSubscriberElementName })">
                </div>
                <div class="w3-row inherit-description">
                  <div class="w3-row">
                    <div class="w3-col s4">
                      <span v-if="inheritedFromIsNotsDisabled">
                        <img src="./../../assets/disable.png">
                      </span>
                      <span v-if="inheritedFromIsNotsOnApp">
                        <img src="./../../assets/onapp.png">
                      </span>
                      <span v-if="inheritedFromIsNotsPush">
                        <img src="./../../assets/push.png">
                      </span>
                    </div>
                    <div class="w3-col s8 w3-padding">
                      <span v-if="inheritedFromIsNotsDisabled">
                        <div>{{ $t('notifications.DISABLE_DESC') }}</div>
                      </span>
                      <span v-if="inheritedFromIsNotsOnApp">
                        <div>{{ $t('notifications.ONAPP_DESC') }}</div>
                      </span>
                      <span v-if="inheritedFromIsNotsPush">
                        <div>{{ $t('notifications.PUSH_DESC') }}</div>
                      </span>
                    </div>
                  </div>
                </div>
                <div class="w3-row w3-center w3-margin-top  w3-margin-bottom">
                  <button @click="setCustom()"
                    class="w3-button app-button">
                      {{ $t('notifications.CHANGE') }}
                  </button>
                </div>
              </div>
              <div v-if="savingSubscriber" class="saving-subscriber">
                <img src="./../../assets/loading.gif" alt="">
              </div>
            </div>
          </transition>
        </div>

        <div v-if="!loading" class="">
          <app-activity-table
            v-if="activities.length > 0"
            :activities="activities"
            :addInAppState="true"
            :fullWidthCard="true"
            :showCardsPreview="contextType === 'MODEL_SECTION' || contextType === 'INITIATIVE'">
          </app-activity-table>
          <div v-else class="w3-padding w3-center">
            <i>{{ $t('general.NO_RESULTS_FOUND') }}</i>
          </div>
        </div>
        <div v-else class="w3-row w3-center loader-gif-container">
          <img class="loader-gif" src="../../assets/loading.gif" alt="">
        </div>

        <div class="w3-row w3-center">
          <button v-if="!allShown"
            @click="showMore()"
            class="w3-margin-top w3-margin-bottom w3-button app-button-light" type="button" name="button">
            show more...
          </button>
        </div>
      </div>

      <div slot="reference" class="cursor-pointer w3-display-container"
        :class="{'icon-button': !largeIcon, 'icon-button-large': largeIcon}"
        @click="showNotificationsClicked()">
        <span v-if="!loadedAll">
          <img v-if="isDarkBg" src="./../../assets/onapp.png" alt="">
          <img v-else src="./../../assets/onapp-light.png" alt="">
        </span>
        <span v-else>
          <span v-if="totalUnread === 0">
            <span v-if="notsDisabled">
              <img v-if="isDarkBg && isInheriting" src="./../../assets/disable-inh.png" alt="">
              <img v-if="!isDarkBg && isInheriting" src="./../../assets/disable-light-inh.png" alt="">
              <img v-if="isDarkBg && !isInheriting" src="./../../assets/disable.png" alt="">
              <img v-if="!isDarkBg && !isInheriting" src="./../../assets/disable-light.png" alt="">
            </span>
            <span v-if="notsOnApp">
              <img v-if="isDarkBg && isInheriting" src="./../../assets/onapp-inh.png" alt="">
              <img v-if="!isDarkBg && isInheriting" src="./../../assets/onapp-light-inh.png" alt="">
              <img v-if="isDarkBg && !isInheriting" src="./../../assets/onapp.png" alt="">
              <img v-if="!isDarkBg && !isInheriting" src="./../../assets/onapp-light.png" alt="">
            </span>
            <span class="icon-button-push" v-if="notsPush">
              <img v-if="isDarkBg && isInheriting" src="./../../assets/push-inh.png" alt="">
              <img v-if="!isDarkBg && isInheriting" src="./../../assets/push-light-inh.png" alt="">
              <img v-if="isDarkBg && !isInheriting" src="./../../assets/push.png" alt="">
              <img v-if="!isDarkBg && !isInheriting" src="./../../assets/push-light.png" alt="">
            </span>
            <span v-if="!shouldHaveSubscriber">
              <img src="./../../assets/onapp-light.png" alt="">
            </span>
          </span>
          <span v-else>
            <span v-if="onlyNotificationsUnder">
              <span v-if="notsDisabled">
                 <img v-if="!isInheriting" src="./../../assets/disable.png" alt="">
                 <img v-if="isInheriting" src="./../../assets/disable-inh.png" alt="">
              </span>
              <span v-if="notsOnApp">
                <img v-if="!isInheriting" src="./../../assets/onapp-under.png" alt="">
                <img v-if="isInheriting" src="./../../assets/onapp-under-inh.png" alt="">
              </span>
              <span class="icon-button-push" v-if="notsPush">
                <img v-if="!isInheriting" src="./../../assets/push-under.png" alt="">
                <img v-if="isInheriting" src="./../../assets/push-under-inh.png" alt="">
              </span>
              <span v-if="!shouldHaveSubscriber">
                <img src="./../../assets/onapp-under.png" alt="">
              </span>
            </span>
            <span v-else>
              <span v-if="notsDisabled">
                <img v-if="!isInheriting" src="./../../assets/disable.png" alt="">
                <img v-if="isInheriting" src="./../../assets/disable-inh.png" alt="">
              </span>
              <span v-if="notsOnApp">
                <img v-if="!isInheriting" src="./../../assets/onapp-on.png" alt="">
                <img v-if="isInheriting" src="./../../assets/onapp-on-inh.png" alt="">
              </span>
              <span class="icon-button-push" v-if="notsPush">
                <img v-if="!isInheriting" src="./../../assets/push-on.png" alt="">
                <img v-if="isInheriting" src="./../../assets/push-on-inh.png" alt="">
              </span>
              <span v-if="!shouldHaveSubscriber">
                <img src="./../../assets/onapp-on.png" alt="">
              </span>
            </span>
          </span>
        </span>
      </div>
    </popper>

  </div>
</template>

<script>
import ActivityTable from './ActivityTable.vue'
import { swRegistration } from '@/registerServiceWorker.js'
import { urlB64ToUint8Array } from '@/lib/common.js'
export default {

  components: {
    'app-activity-table': ActivityTable
  },

  props: {
    element: {
      type: Object,
      default: null
    },
    contextType: {
      type: String,
      default: 'MODEL_SECTION'
    },
    isSelected: {
      type: Boolean,
      default: false
    },
    parentIsSelected: {
      type: Boolean,
      default: false
    },
    largeIcon: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      showTable: false,
      loading: false,
      notifications: [],
      totalUnread: 0,
      currentPage: 0,
      showingAll: true,
      showingMoreNotifications: false,
      allShown: false,
      toggleShow: false,
      ws_subscription: null,
      subscriber: null,
      inheritedFrom: null,
      gettingConfiguration: true,
      showConfig: false,
      savingSubscriber: false
    }
  },

  watch: {
    triggerUpdateNotifications () {
      /* only update if there is notifications to be removed */
      if (this.notifications.length > 0) {
        // console.log('updating notifications in ' + this.url + ' due to triggerUpdateNotifications watch')
        this.updateNotifications(false, 5000)
      }
    },
    '$store.state.socket.connected' () {
      this.handleSocket()
    },
    triggerUpdateSubscriber () {
      console.log('updatins subscriber due to triggerUpdateSubscriber')
      if (this.isInheriting) {
        this.updateSubscriber()
      }
    }
  },

  computed: {
    shouldHaveSubscriber () {
      return this.contextType !== 'MODEL_CARD'
    },

    isDarkBg () {
      return this.parentIsSelected || this.isSelected
    },

    isInheriting () {
      if (this.subscriber) {
        return this.subscriber.inheritConfig === 'INHERIT'
      }
      return false
    },
    loadedAll () {
      return this.applicableSubscriber != null
    },

    applicableSubscriber () {
      if (this.subscriber) {
        if (this.subscriber.inheritConfig === 'CUSTOM') {
          return this.subscriber
        } else {
          return this.inheritedFrom
        }
      }
      return null
    },
    applicableSubscriberElementName () {
      if (this.inheritedFrom) {
        switch (this.inheritedFrom.elementType) {
          case 'SECTION':
            return this.$t('activity.TITLE_AND_TYPE', { title: this.inheritedFrom.section.title, type: this.$t('model.SECTION') })

          case 'INITIATIVE':
            return this.$t('activity.TITLE_AND_TYPE', { title: this.inheritedFrom.initiative.meta.name, type: this.$t('initiatives.INITIATIVE') })

          case 'COLLECTIVEONE':
            return this.$t('activity.GLOBAL_NOTIFICATIONS_PREFS')
        }
      }
      return ''
    },

    subscriptionType () {
      switch (this.contextType) {
        case 'MODEL_SECTION':
          return 'SECTION'

        default:
          return this.contextType
      }
    },

    notsDisabledProto () {
      return {
        inAppConfig: 'DISABLED',
        pushConfig: 'DISABLED',
        emailNowConfig: 'DISABLED',
        emailSummaryConfig: 'DISABLED',
        emailSummaryPeriodConfig: 'WEEKLY'
      }
    },

    notsOnAppProto () {
      return {
        inAppConfig: 'ALL_EVENTS',
        pushConfig: 'ONLY_MENTIONS',
        emailNowConfig: 'ONLY_MENTIONS',
        emailSummaryConfig: 'ALL_EVENTS',
        emailSummaryPeriodConfig: 'WEEKLY'
      }
    },

    notsPushProto () {
      return {
        inAppConfig: 'ALL_EVENTS',
        pushConfig: 'ALL_EVENTS',
        emailNowConfig: 'ALL_EVENTS',
        emailSummaryConfig: 'ALL_EVENTS',
        emailSummaryPeriodConfig: 'DAILY'
      }
    },

    notsDisabled () {
      if (this.applicableSubscriber && this.notsDisabledProto) {
        return this.compareSubscriptions(this.applicableSubscriber, this.notsDisabledProto)
      }
      return false
    },

    notsOnApp () {
      if (this.applicableSubscriber && this.notsOnAppProto) {
        return this.compareSubscriptions(this.applicableSubscriber, this.notsOnAppProto)
      }
      return false
    },

    notsPush () {
      if (this.applicableSubscriber && this.notsPushProto) {
        return this.compareSubscriptions(this.applicableSubscriber, this.notsPushProto)
      }
      return false
    },

    inheritedFromIsNotsDisabled () {
      if (this.inheritedFrom && this.notsDisabledProto) {
        return this.compareSubscriptions(this.inheritedFrom, this.notsDisabledProto)
      }
      return false
    },

    inheritedFromIsNotsOnApp () {
      if (this.inheritedFrom && this.notsOnAppProto) {
        return this.compareSubscriptions(this.inheritedFrom, this.notsOnAppProto)
      }
      return false
    },

    inheritedFromIsNotsPush () {
      if (this.inheritedFrom && this.notsPushProto) {
        return this.compareSubscriptions(this.inheritedFrom, this.notsPushProto)
      }
      return false
    },

    elementTitle () {
      switch (this.contextType) {
        case 'MODEL_CARD':
          return 'this card'

        case 'MODEL_SECTION':
          return this.element.title

        case 'INITIATIVE':
          return this.element.meta.name
      }

      return 'this element'
    },

    contextElementId () {
      return this.element ? this.element.id : '00000000-0000-0000-0000-000000000000'
    },

    activities () {
      let activities = []
      this.notifications.forEach((notification) => {
        let activity = notification.activity
        activity.inAppState = notification.inAppState
        activities.push(activity)
      })
      return activities
    },

    url () {
      return '/1/notifications/' + this.contextType + '/' + this.contextElementId
    },

    triggerUpdateNotifications () {
      return this.$store.state.support.triggerUpdateNotifications
    },

    triggerUpdateSubscriber () {
      return this.$store.state.support.triggerUpdateSubscriber
    },

    notificationsHere () {
      let notificationsHere = []

      switch (this.contextType) {
        case 'MODEL_CARD':
          for (let ix in this.notifications) {
            if (this.notifications[ix].activity.modelCardWrapper) {
              if (this.notifications[ix].activity.modelCardWrapper.id === this.contextElementId) {
                notificationsHere.push(this.notifications[ix])
              }
            }
          }
          break

        case 'MODEL_SECTION':
          for (let ix in this.notifications) {
            if (this.notifications[ix].activity.modelSection) {
              if (this.notifications[ix].activity.modelSection.id === this.contextElementId) {
                notificationsHere.push(this.notifications[ix])
              }
            }
          }
          break

        case 'INITIATIVE':
          for (let ix in this.notifications) {
            if (this.notifications[ix].activity.initiative) {
              if (this.notifications[ix].activity.initiative.id === this.contextElementId) {
                notificationsHere.push(this.notifications[ix])
              }
            }
          }
      }

      return notificationsHere
    },

    notificationsHereMessages () {
      return this.notificationsHere.filter((e) => { return e.activity.type === 'MESSAGE_POSTED' })
    },

    onlyNotificationsUnder () {
      if (this.notifications.length > 0) {
        return this.notificationsHere.length === 0
      }
      return false
    },

    popperOptions () {
      return {
        placement: 'right',
        modifiers: {
          preventOverflow: {
            enabled: true,
            boundariesElement: 'viewport'
          }
        }
      }
    }
  },

  methods: {

    setCustom () {
      this.subscriber.inheritConfig = 'CUSTOM'
      /* initialize with the inherited configuration */
      this.copySubscriberConfig(this.subscriber, this.inheritedFrom)
      this.saveSubscriber()
    },

    setInherited () {
      this.subscriber.inheritConfig = 'INHERIT'
      this.saveSubscriber()
      this.updateInheritedFrom()
    },

    setDisabled () {
      this.copySubscriberConfig(this.subscriber, this.notsDisabledProto)
      this.saveSubscriber()
    },

    setOnApp () {
      this.copySubscriberConfig(this.subscriber, this.notsOnAppProto)
      this.saveSubscriber()
    },

    setPush () {
      this.checkAndSubscribeToPushNots()
      this.copySubscriberConfig(this.subscriber, this.notsPushProto)
      this.saveSubscriber()
    },

    saveSubscriber () {
      this.savingSubscriber = true
      this.axios.put('/1/notifications/subscriber/' + this.subscriptionType + '/' + this.contextElementId, this.subscriber)
        .then((response) => {
          this.savingSubscriber = false
          this.$store.commit('triggerUpdateSubscriber')
        })
        .catch((error) => {
          this.savingSubscriber = false
          console.error(error)
          this.updateSubscriber()
        })
    },

    compareSubscriptions (a, b) {
      return a.inAppConfig === b.inAppConfig &&
        a.pushConfig === b.pushConfig &&
        a.emailNowConfig === b.emailNowConfig &&
        a.emailSummaryConfig === b.emailSummaryConfig &&
        a.emailSummaryPeriodConfig === b.emailSummaryPeriodConfig
    },

    copySubscriberConfig (a, b) {
      a.inAppConfig = b.inAppConfig
      a.pushConfig = b.pushConfig
      a.emailNowConfig = b.emailNowConfig
      a.emailSummaryConfig = b.emailSummaryConfig
      a.emailSummaryPeriodConfig = b.emailSummaryPeriodConfig
    },

    toggleShowAll () {
      if (this.showingAll) {
        this.showUnreadOnly()
      } else {
        this.showAll()
      }
    },

    toggleConfiguration () {
      this.showConfig = !this.showConfig
      if (this.inheritedFrom == null) {
        this.updateInheritedFrom()
      }
    },

    showAll () {
      this.showingAll = true
      this.updateNotifications(false, 1)
    },
    showUnreadOnly () {
      this.showingAll = false
      this.updateNotifications(false, 1)
    },
    updateNotifications (markMessagesRead, delay, showLoading) {
      markMessagesRead = markMessagesRead || false
      delay = delay || 5000
      showLoading = (typeof showLoading === 'undefined') ? true : showLoading

      if (!this.showingMoreNotifications) {
        /* dont update if the user is scrolling down de notifications */
        /* delay the update to let other requests go before (like adding a card and so on) */
        setTimeout(() => {
          this.updateNotificationsDelayed(markMessagesRead, showLoading)
        }, delay)
      }
    },

    updateNotificationsDelayed (markMessagesRead, showLoading) {
      if (showLoading) {
        this.loading = true
      }
      this.axios.get(this.url, {
        params: {
          page: 0,
          size: 10,
          onlyUnread: !this.showingAll
        }
      }).then((response) => {
        this.loading = false
        /* check that new notifications arrived */
        this.notifications = response.data.data.notifications
        this.totalUnread = response.data.data.totalUnread

        this.allShown = this.notifications.length < 10

        if (markMessagesRead) {
          if (this.isSelected && this.$route.name === 'ModelSectionMessages') {
            /* autoread message notifications of this section */
            this.messageNotificationsRead()
          }
        }

        /* push all notifications */
        // console.log('pushing notifications ' + this.notifications.length + ' from ' + this.url)
        this.$store.dispatch('addPushNotifications', this.notifications)
      }).catch(function (error) {
        console.log(error)
      })
    },

    addNotifications () {
      this.showingMoreNotifications = true
      this.axios.get(this.url, {
        params: {
          page: this.currentPage,
          size: 10,
          onlyUnread: false
        }
      }).then((response) => {
        /* check that new notifications arrived */
        if (response.data.data.length < 10) {
          this.allShown = true
        }

        this.notifications = this.notifications.concat(response.data.data.notifications)
        this.totalUnread = response.data.data.totalUnread
      }).catch(function (error) {
        console.log(error)
      })
    },

    allNotificationsRead () {
      this.axios.put(this.url + '/read', {}).then((response) => {
        /* check that new notifications arrived */
        this.toggleShow = !this.toggleShow
        this.$store.commit('triggerUpdateNotifications')
        // console.log('updating notifications in ' + this.url + ' due to allNotificationsRead response')
        this.updateNotifications(false, 1)
        this.hide()
      }).catch(function (error) {
        console.log(error)
      })
    },

    messageNotificationsRead () {
      let idsList = this.notificationsHereMessages.map((e) => e.id)
      if (idsList.length > 0) {
        this.axios.put('/1/notifications/read', idsList).then((response) => {
          /* check that new notifications arrived */
          this.$store.commit('triggerUpdateNotifications')
          // console.log('updating notifications in ' + this.url + ' due to messageNotificationsRead response')
          this.updateNotifications(false, 1)
        }).catch(function (error) {
          console.log(error)
        })
      }
    },

    showMore () {
      this.currentPage += 1
      this.addNotifications()
    },

    clickOutsideNotifications () {
      // console.log('clickOutsideNotifications')
      if (!this.preventClickOutside) {
        if (this.showTable) {
          this.hide()
        }
      }
    },

    showNotificationsClicked () {
      if (!this.showTable) {
        this.show()
        this.preventClickOutside = true
        setTimeout(() => {
          this.preventClickOutside = false
        }, 500)
      } else {
        this.hide()
      }
    },

    hide () {
      this.showTable = false
      this.showingMoreNotifications = false
    },

    show () {
      this.showTable = true
    },

    closeThis () {
      this.toggleShow = !this.toggleShow
    },

    handleSocket () {
      let url = ''
      switch (this.contextType) {
        case 'MODEL_CARD':
          url = '/channel/activity/model/card/' + this.contextElementId
          break

        case 'MODEL_SECTION':
          url = '/channel/activity/model/section/' + this.contextElementId
          break

        case 'INITIATIVE':
          url = '/channel/activity/model/initiative/' + this.contextElementId
          break

        case 'COLLECTIVEONE':
          url = '/channel/activity/user/' + this.$store.state.user.profile.c1Id
          break
      }

      this.ws_subscription = this.$store.dispatch('subscribe', {
        url: url,
        onMessage: (tick) => {
          // console.log(tick)
          var message = tick.body
          if (message === 'UPDATE') {
            // console.log('updating notifications in ' + this.url + ' due to socket message')
            this.updateNotifications(true)
          }
        }
      })
    },

    updateInheritedFrom () {
      /* update only the inherited from subscriber, needed when user switching to inherited */
      this.axios.get('/1/notifications/subscriber/' + this.subscriptionType + '/' + this.contextElementId + '/inheritFrom').then((response) => {
        this.inheritedFrom = response.data.data
      })
    },

    updateSubscriber () {
      if (!this.shouldHaveSubscriber) {
        this.gettingConfiguration = false
        return
      }

      this.gettingConfiguration = true
      this.axios.get('/1/notifications/subscriber/' + this.subscriptionType + '/' + this.contextElementId).then((response) => {
        this.gettingConfiguration = false
        this.subscriber = response.data.data
        /* initialize the inherited from subscriber if available */
        if (this.subscriber.inheritConfig === 'INHERIT') {
          this.inheritedFrom = this.subscriber.applicableSubscriber
        }
      })
    },

    checkAndSubscribeToPushNots () {
      swRegistration.pushManager.getSubscription().then((subscription) => {
        if (subscription == null) {
          let applicationServerKey = urlB64ToUint8Array(process.env.VUE_APP_SEVER_PUBLIC_KEY)

          console.log('subscribing')
          swRegistration.pushManager.subscribe({
            userVisibleOnly: true,
            applicationServerKey: applicationServerKey
          }).then((pushSubscription) => {
            this.$store.dispatch('updateSubscription')
          }, (error) => {
            console.log('pushManager.subscribe Error:' + error)
          })
        } else {
          console.log(subscription)
        }
      }).catch((error) => {
        console.log('error getting subscription')
        console.log(error)
      })
    }
  },

  created () {
    // console.log('updating notifications in ' + this.url + ' due to component creation')
    this.updateNotifications(true, 1)
    this.handleSocket()
    this.updateSubscriber()
  },

  beforeDestroy () {
    this.$store.dispatch('unsubscribe', this.ws_subscription)
  }
}
</script>

<style scoped>

.close-div {
  position: absolute;
  top: 0px;
  right: 0px;
  width: 35px;
  height: 35px;
  transition:all 0.3s ease-out;
}

.close-div:hover {
  color: #15a5cc !important;
}

.notif-control-row button {
  width: 100%;
}

.notifications-header {
  padding: 6px 12px 8px 12px;
  background-color: #313942;
  color: white;
}

.notifications-header .title-row {
  margin-bottom: 6px;
}

.icon-button {
  width: 30px;
  text-align: center;
}

.icon-button img {
  width: 13px;
}

.icon-button-push img {
  width: 18px;
}

.inherit-description {
  height: 60px;
  border-top: 1px solid #6f8294;
}

.inherit-description img {
  width: 22px;
  margin-top: 15px;
}

.icon-button-large {
  width: 50px;
  text-align: center;
  padding-top: 12px;
}

.icon-button-large img {
  width: 22px;
}

.icon-button-large .icon-button-push img {
  width: 30px;
}

.inherit-div {
  position: absolute;
  top: 0;
  width: 100%;
  text-align: center;
}

.inherit-div img {
  width: 8px;
}

.config-button {
  text-align: center;
  height: 32px;
  border-radius: 12px;
  cursor: pointer;
  background-color: #eff3f6;
  transition:all 0.3s ease-out;
}

.config-button img {
  width: 20px;
  margin-top: 4px;
}

.config-button-push img {
  width: 27px;
}

.config-table-row {
  border-bottom: 2px solid #313942;
  margin-bottom: 20px;
  position: relative;
}

.saving-subscriber {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
}

.saving-subscriber img {
  position: absolute;
  width: 60px;
  left: calc(50% - 30px);
  top: calc(50% - 30px);
}

.config-table {
  background-color: #eff3f6;
  width: 100%;
  font-size: 13px;
  border-collapse: collapse;
  text-align: left;
}

.config-table .icon-column {
  width: 50px;
  text-align: center;
}

.config-table tr {
  height: 45px;
  cursor: pointer;
}

.config-table td {
  border-bottom: 1px solid #6f8294;
}

.config-table img {
  width: 20px;
}

.config-table .icon-push img {
  width: 27px;
}

.config-button:hover {
  background-color: #808080;
}

.notifications-container {
}

@media screen and (min-width: 600px) {
  .notifications-list-container {
    width: 400px;
  }
}

@media screen and (max-width: 599px) {
  .notifications-list-container {
    width: 98vw;
  }
}

.notifications-list-container {
  max-height: 60vh;
  min-height: 40vh;
  overflow: auto;
  z-index: 20;
}

.compact .notifications-container {
  width: 292px;
  left: -32px;
}

</style>
