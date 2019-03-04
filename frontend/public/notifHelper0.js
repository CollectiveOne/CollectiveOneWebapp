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
      swRegistration.showNotification('New updates in CollectiveOne', {
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

  var xhttp = new XMLHttpRequest();
  xhttp.open('PUT', '/1/notifications/pushed/' + notification.id, true);
  xhttp.send();
}
