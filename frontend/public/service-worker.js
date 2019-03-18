if (workbox) {
    console.log(`Workbox is loaded`);
    workbox.precaching.precacheAndRoute(self.__precacheManifest);
    workbox.skipWaiting();
}
else {
    console.log(`Workbox didn't load`);
}

self.addEventListener('notificationclick', function(event) {
  console.log('[Service Worker] Notification click Received.');
  console.log(event.notification);

  event.notification.close();

  event.waitUntil(
    clients.openWindow(event.notification.data.url)
  );
});

self.addEventListener('push', function(event) {
  console.log('[Service Worker] Push notification received')
  let notification = null;
  let makeDefault = true
  if (event.data) {
    let JSONstr = event.data.text()
    if (JSONstr[0] == "{") {
      notification = JSON.parse(event.data.text())
      makeDefault = false
    }
  }

  if (makeDefault) {
    notification = {
      url: 'http://www.collectiveone.org',
      id: '65465465465465465465',
      title: '',
      message: 'There has been some recent activity in CollectiveOne that can be relevant to you.',
      activity: {
        triggerUser: {
          nickname: 'CollectiveOne',
          pictureUrl: 'https://image.ibb.co/mgQn1a/imago_red.png'
        }
      }
    }
  }

  event.waitUntil(
    new Promise((resolve, reject) => {
      fetch('/1/notifications/pushed/' + notification.id);
      self.registration.getNotifications().then((currentNotifications) => {
        if (currentNotifications.length === 0) {
          self.registration.showNotification(notification.activity.triggerUser.nickname + ' ' + notification.title, {
            body: '- ' + notification.message,
            tag: 'COLLECTIVEONE_PUSH_NOTIFICATION',
            icon: notification.activity.triggerUser.pictureUrl,
            data: {
              url: notification.url
            },
            badge: 'https://image.ibb.co/mgQn1a/imago_red.png',
            vibrate: [150]
          }).then(() => {
            resolve()
          }).catch(() => {
            reject()
          })
        } else {
          /* show only one notification, suing the same tag, the previous notification will be replaced */
          self.registration.showNotification('New updates in CollectiveOne', {
            body: currentNotifications[0].body + '\n- ' + notification.message,
            tag: 'COLLECTIVEONE_PUSH_NOTIFICATION',
            icon: 'https://image.ibb.co/mgQn1a/imago_red.png',
            badge: 'https://image.ibb.co/mgQn1a/imago_red.png',
            data: {
              url: notification.url
            },
            vibrate: [50]
          }).then(() => {
            resolve()
          }).catch(() => {
            reject()
          })
        }
      }).catch(() => {
        reject()
      })
    })
  );
});
