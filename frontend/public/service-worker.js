importScripts('./notifHelper.js')

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
  console.log(event)
  let notification = {
      url: 'http://www.google.com',
      id: '65465465465465465465',
      title: 'new event in CollectiveOne',
      message: 'new message hellow',
      activity: {
        triggerUser: {
          nickname: 'pepo dummy',
          pictureUrl: 'https://lh4.googleusercontent.com/-GklJ1T6lIjc/AAAAAAAAAAI/AAAAAAAAACY/4F11RvbtP5E/photo.jpg'
        }
      }
    }
  event.waitUntil(
    showNotification(self.registration, notification)
  );
});
