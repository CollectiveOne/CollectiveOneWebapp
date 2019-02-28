import Vue from 'vue';

const showNotification = function(swRegistration, notification) {
  let user = notification.activity.triggerUser
  swRegistration.getNotifications().then((currentNotifications) => {
    if (currentNotifications.length === 0) {
      swRegistration.showNotification(user.nickname + ' ' + notification.title, {
        body: '- ' + notification.message,
        tag: 'COLLECTIVEONE_PUSH_NOTIFICATION',
        icon: user.pictureUrl,
        data: {
          url: notification.url
        },
        badge: 'https://image.ibb.co/mgQn1a/imago_red.png',
        vibrate: [150]
      })
    } else {
      /* show only one notification, suing the same tag, the previous notification will be replaced */
      swRegistration.showNotification(i18n.t('notifications.NEW_UPDATES'), {
        body: currentNotifications[0].body + '\n- ' + notification.message,
        tag: 'COLLECTIVEONE_PUSH_NOTIFICATION',
        icon: 'https://image.ibb.co/mgQn1a/imago_red.png',
        badge: 'https://image.ibb.co/mgQn1a/imago_red.png',
        data: {
          url: notification.url
        },
        vibrate: [50]
      })
    }
  })

  Vue.axios.put('/1/notifications/pushed/' + notification.id, {}).then((response) => {
  }).catch(function (error) {
    console.log(error)
  })
}

export {
  showNotification
}
