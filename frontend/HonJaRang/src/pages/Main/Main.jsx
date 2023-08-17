import { Link } from 'react-router-dom'
import Logo from '@/assets/2.png'
import JointLocation from '@/assets/main/joint_location.png'
import JointPoint from '@/assets/main/joint_point.png'
import JointChat from '@/assets/main/joint_chat.png'
import VideoPeople from '@/assets/main/video_people.png'
import VideoAll from '@/assets/main/video_all.png'
import FacilityFood from '@/assets/main/facility_food.png'
import Header from '@/components/Header/Header'
import { useSelector } from 'react-redux'
import axios from 'axios'
import { initializeApp } from 'firebase/app';
import {
  getMessaging,
  getToken,
  onMessage,
} from 'firebase/messaging';
import { useEffect } from 'react'
import { API } from '@/apis/config'

export default function Main() {
  const isLogged = useSelector((state) => state.login.isLogged)

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
  const token = localStorage.getItem("access_token")
  useEffect(() => {
    if(token){
      requestPermission();
      getToken(messaging, {
        vapidKey: import.meta.env.VITE_APP_FIREBASE_VAPIDKEY,
      })
      .then((currentToken) => {
        if (currentToken) {
          localStorage.setItem('fcm_token', currentToken)
          Push(currentToken)
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
    }
  }, []);

  const requestPermission = () => {
    if(token){
      console.log('Requesting permission...');
      Notification.requestPermission().then((permission) => {
        if (permission === 'granted') {
          console.log('Notification permission granted.');
        } else {
          console.log('Unable to get permission to notify.');
        }
      });
    }
  };


  const Push = (currentToken) => {
    if(token){
      const token = currentToken
      const access_token = localStorage.getItem('access_token')
      axios.post(`${API.USER}/fcm-token`,
      {fcm_token : token},
      {headers: {
        'Authorization' : `Bearer ${access_token}`
      }})
        .then((res) => {
          console.log(res)
        })
      .catch((err) => {
        console.log(err)
      })
    }
  }
  return (
    <div className=" h-screen">
      {isLogged ? (
        <div className="bg-white w-full h-20 fixed">
          <div className='w-5/6 mx-auto'>
            <Header/>
          </div>
        </div>
      ):(
      <div className="flex m-4 w-5/6 mx-auto pb-5 justify-between">
        <img src={Logo} alt="" className="h-10"/>
        <div>
          <Link to="/login">
            <button className="font-semibold font-ImcreSoojin text-lg my-2 mx-4">로그인</button>
          </Link>        
          <Link to="/signup">
            <button className="font-semibold font-ImcreSoojin text-lg my-2 mx-4">회원가입</button>
          </Link>
        </div>
      </div>
      )}
      {isLogged ? (
        <div className='h-10'></div>
      ):(
        null
      )}
      {/* 공동배달, 공동구매 */}
      <div className="flex p-10 py-32 my-5 flex-row bg-main4 bg-opacity-75 justify-around">
        <div>
          <p className="text-5xl font-ImcreSoojin m-1">공동배달</p>
          <p className="text-5xl font-ImcreSoojin m-1">공동구매</p>
          <p className="text-xl text-gray4 mt-4">근처 자취생들과 함께 이용하며<br/>다양한 선택과 절약으로 더욱 풍성한<br/>자취 생활을 만들어 보아요.</p>
        </div>
        <div className="flex items-end">
          <div className="mx-4">
            <p>반경 10km 내<br/>사람들과 함께</p>
            <img src={JointLocation} alt="" 
            className="h-16 w-16 rounded-full p-3 bg-white bg-opacity-50 mt-2"/>
          </div>
          <div className="mx-4">
            <p>포인트 제도로<br/>자동 정산</p>
            <img src={JointPoint} alt="" 
            className="h-16 w-16 rounded-full p-3 bg-white bg-opacity-50 mt-2"/>
          </div>
          <div className="mx-4">
            <p>단체 채팅으로<br/>정보 공유</p>
            <img src={JointChat} alt="" 
            className="h-16 w-16 rounded-full p-3 bg-white bg-opacity-50 mt-2"/>
          </div>
        </div>
      </div>
      <div className="h-16"></div>
      {/* 중고거래 */}
      {/* <div className="flex p-10 py-20 my-5 flex-row bg-main4 bg-opacity-75 justify-around">
        <div className="flex items-end">
          <div className="mx-4">
            <p>판매자와 구매자 간<br />1 : 1 채팅</p>
            <img src={UsedChat} alt="" 
            className="h-16 w-16 rounded-full p-3 bg-main3 bg-opacity-50"/>
          </div>
          <div className="mx-4">
            <p>무슨 말 적지;</p>
            <img src={JointChat} alt="" 
            className="h-16 w-16 rounded-full p-3 bg-main3 bg-opacity-50"/>
          </div>
        </div>
        <div>
          <p className="text-5xl font-ImcreSoojin m-1">중고거래</p>
          <p className="text-xl text-gray4 mt-4"></p>
        </div>
      </div>
      <div className="h-28"></div> */}
      {/* 화상채팅 */}
      <div className="flex p-10 py-20 my-5 flex-row bg-white bg-opacity-75 justify-around">
        <div className="flex items-end">
          <div className="mx-4">
            <p>여러 카테고리로<br/>다양한 사람들과의 만남</p>
            <img src={VideoPeople} alt="" 
            className="h-16 w-16 rounded-full p-3 bg-main3 bg-opacity-50 mt-2"/>
          </div>
          <div className="mx-4">
            <p>영상, 음성, 채팅<br/>모두 한번에</p>
            <img src={VideoAll} alt="" 
            className="h-16 w-16 rounded-full p-3 bg-main3 bg-opacity-50 mt-2"/>
          </div>
        </div>
        <div>
          <p className="text-5xl font-ImcreSoojin m-1">화상채팅</p>
          <p className="text-xl text-gray4 mt-4">시간과 거리의 제약없이<br />소통의 즐거움을 느껴보세요.</p>
        </div>
      </div>
      <div className="h-16"></div>
      {/* 편의시설 조회 */}
      <div className="flex p-10 py-32 my-5 flex-row bg-main4 bg-opacity-75 justify-around">
        <div>
          <p className="text-5xl font-ImcreSoojin m-1">편의시설 조회</p>
          <p className="text-xl text-gray4 mt-4">집 근처에 있는 편의 시설을 한눈에<br/>확인하세요</p>
        </div>
        <div className="flex items-end">
          <div className="mx-4">
            <p>은행부터 카페까지</p>
            <img src={FacilityFood} alt="" 
            className="h-16 w-16 rounded-full p-3 bg-white bg-opacity-50 mt-2"/>
          </div>
        </div>
      </div>
      <div className="h-16"></div>
      <div className="text-gray2 text-sm"> Icons made by <a href="https://www.flaticon.com/authors/catkuro" title="캣쿠로"> 캣쿠로 </a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com'</a></div>
    </div>
  )
}
