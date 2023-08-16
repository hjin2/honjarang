// import React from 'react'
import { useParams } from 'react-router-dom';
import UserInfo from '@/components/MyPage/UserInfo';
import SideBar from '@/components/MyPage/SideBar';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { useDispatch } from 'react-redux';
import { Userinfo } from '@/redux/slice/UserInfoSlice';
import { initializeApp } from 'firebase/app';
import {
  getMessaging,
  getToken,
  onMessage,
} from 'firebase/messaging';
import { API } from '@/apis/config';


export default function MyPage() {

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
  const LoginId = localStorage.getItem("user_id")
  const [isMe, setIsMe] = useState(false)
  useEffect(() => {
    requestPermission();
    if(LoginId == id){
      setIsMe(true)
    }else{
      setIsMe(false)
    }
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

  useEffect(() => {
    PushTest()
  },[])


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


// 여기까지 firebase 임시

  const { id } = useParams();
  const dispatch = useDispatch()
  const token = localStorage.getItem("access_token")
  useEffect(() => {
    console.log(id)
    console.log(token)
    axios.get(`${API.USER}/info`,
      {
        params : {id : id},
        headers : {'Authorization' : `Bearer ${token}`}
      },
      )
      .then(function(response){
        console.log(response.data)
        dispatch(Userinfo(response.data))
      })
      .catch(function(error){
        console.log(error)
      })
  },[id, token]);

  return (
    <div className="my-page space-y-5">
      <UserInfo id={id} isMe={isMe}/>
      <div>
       <SideBar id={id} isMe={isMe}/>
      </div>
    </div>
  );
}
