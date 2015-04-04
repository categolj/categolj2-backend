self.addEventListener('push', function (event) {
    console.log(event);
    event.waitUntil(
        self.registration.showNotification('News', {
            body: 'New Entry is created!!'
        })
    );
});

self.addEventListener('notificationclick', function (event) {
    event.notification.close();
    event.waitUntil(
        clients.openWindow('/')
    );
});