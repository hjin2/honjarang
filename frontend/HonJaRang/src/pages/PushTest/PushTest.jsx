import React, { useEffect } from 'react';
import { initializeApp } from 'firebase/app';
import {
  getMessaging,
  getToken,
  onMessage,
} from 'firebase/messaging';
import axios from 'axios';
import { API } from '@/apis/config';

const FirebaseMessaging = () => {
  const firebaseConfig = {
    apiKey: import.meta.env.VITE_APP_FIREBASE_APIKEY,
    authDomain: import.meta.env.VITE_APP_FIREBASE_AUTHDOMAIN,
    projectId: import.meta.env.VITE_APP_FIREBASE_PROJECTID,
    storageBucket: import.meta.env.VITE_APP_FIREBASE_STORAGEBUCKET,
    messagingSenderId: import.meta.env.VITE_APP_FIREBASE_MESSAGINGSENDERID,
    appId: import.meta.env.VITE_APP_FIREBASE_APPID,
    measurementId: import.meta.env.VITE_APP_FIREBASE_MEASUREMENTID
  };
  const app = initializeApp(firebaseConfig);
  const messaging = getMessaging(app);

  useEffect(() => {
    requestPermission();

    getToken(messaging, {
      vapidKey: import.meta.env.VITE_APP_FIREBASE_VAPIDKEY,
    })
      .then((currentToken) => {
        if (currentToken) {
          localStorage.setItem('fcm_token', currentToken)
          console.log(currentToken);
        } else {
          console.log('No registration token available. Request permission to generate one.');
        }
      })
      .catch((err) => {
        console.log('An error occurred while retrieving token. ', err);
      });

    onMessage(messaging, (payload) => {
      console.log('Message received. ', payload);
      const { title, body } = payload.notification;
      const options = {
        body,
        icon: '/firebase-logo.png',
      };
      new Notification(title, options);
    });
  }, []);

  const requestPermission = () => {
    console.log('Requesting permission...');
    Notification.requestPermission().then((permission) => {
      if (permission === 'granted') {
        console.log('Notification permission granted.');
      } else {
        console.log('Unable to get permission to notify.');
      }
    });
  };

  const PushTest = () => {
    const token = localStorage.getItem('fcm_token')
    const access_token = localStorage.getItem('access_token')
    axios.post(`${API.USER}/fcm-token`,
    {fcm_token : token},
    {headers: {
      'Authorization' : `Bearer ${access_token}`
    }})
    .then((res) => {
      console.log(res)
    })
  }


  return (
    <div>
      <header></header>
      <main>푸시테스트입니다.</main>
      <button onClick={PushTest}>푸시푸시</button>
    </div>
  );
};

export default FirebaseMessaging;
