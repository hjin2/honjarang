importScripts('https://www.gstatic.com/firebasejs/7.6.1/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/7.6.1/firebase-messaging.js');

// Initialize the Firebase app in the service worker by passing in the
// messagingSenderId.
const firebaseConfig = {
    apiKey: "AIzaSyBg4MfGm1Wm4JRG9jq9MF9pHo7DuD5TLOc",
    authDomain: "honjarang-92544.firebaseapp.com",
    projectId: "honjarang-92544",
    storageBucket: "honjarang-92544.appspot.com",
    messagingSenderId: "712559736953",
    appId: "1:712559736953:web:cc25175e7bea9c4c73bca8",
    measurementId: "G-TVQ8EDTV0D"
};
const app = firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();

// 백그라운드 상태에서 받은 알림 처리
messaging.setBackgroundMessageHandler((payload) => {
    console.log('[firebase-messaging-sw.js] Received background message ', payload)
    // Customize notification here
    const notificationTitle = 'Background Message Title'
    const notificationOptions = {
        body: 'Background Message body.',
        icon: '/firebase-logo.png'
    }

    return self.registration.showNotification(notificationTitle,
        notificationOptions)
})