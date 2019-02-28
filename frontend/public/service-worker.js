import { showNotification } from '@/lib/notifHelper.js'

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
  event.waitUntil(
    showNotification(self, event.notification)
  );
});
