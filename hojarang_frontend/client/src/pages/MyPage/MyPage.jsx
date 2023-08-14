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


export default function MyPage() {

  const firebaseConfig = {
    apiKey: "AIzaSyBg4MfGm1Wm4JRG9jq9MF9pHo7DuD5TLOc",
    authDomain: "honjarang-92544.firebaseapp.com",
    projectId: "honjarang-92544",
    storageBucket: "honjarang-92544.appspot.com",
    messagingSenderId: "712559736953",
    appId: "1:712559736953:web:cc25175e7bea9c4c73bca8",
    measurementId: "G-TVQ8EDTV0D"
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
      vapidKey: 'BKA1sedlFM56zEjILf13NHe4a-ovGw9B4z7VH3mSKulo-QnELaPWD8Uei_Y8b9yi_H4UfVSIE50DgM2ydgfcKd8',
    })
      .then((currentToken) => {
        if (currentToken) {
          localStorage.setItem('fcm_token', currentToken)
          console.log(currentToken);
          axios.post(``)
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
    axios.post(`${import.meta.env.VITE_APP_API}/api/v1/users/fcm-token`,
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
  const URL = import.meta.env.VITE_APP_API
  useEffect(() => {
    console.log(id)
    console.log(token)
    axios.get(`${URL}/api/v1/users/info`,
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
