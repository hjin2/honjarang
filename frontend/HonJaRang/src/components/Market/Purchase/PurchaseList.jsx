import Rooms from "@/components/Market/Rooms"
import PurchaseRoom from "@/components/Market/Purchase/PurchaseRoom"
import { useEffect, useState } from "react"
import axios from "axios"
import Pagination from 'react-js-pagination'
import "../pagination.css"
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';
import { API } from "@/apis/config"
import { initializeApp } from 'firebase/app';
import {
  getMessaging,
  getToken,
  onMessage,
} from 'firebase/messaging';


export default function PurchaseList() {
  const token = localStorage.getItem('access_token')
  const headers = {"Authorization" : `Bearer ${token}`}
  const [purchaseData, setPurchaseData] = useState([])
  const [pageSize, setPageSize] = useState(1)
  const [currentPage, setCurrentPage] = useState(1)
  const [keyword, setKeyword] = useState('')
  const fetchPurChaseData = () =>{
    axios.get(`${API.PURCHASES}`,{params:{page:currentPage, size:12, keyword:keyword}, headers})
    .then((res)=>{
      console.log(res.data)
      setPurchaseData(res.data)
    })
    .catch((err)=>{
      console.log(err)
    })
  }
  
  useEffect(() => {
    axios.get(`${API.PURCHASES}/page`,{params:{size:12, keyword:keyword},headers})
    .then((res)=>{
      console.log(res.data)
      setPageSize(res.data)
    })
    .catch((err)=>{
      console.log(err)
    })
  },[])
  
  useEffect(() => {
    fetchPurChaseData()
  },[currentPage])

  const setPage = (error) => {
    setCurrentPage(error);
  };

  const search = (e) =>{
    e.preventDefault()
    fetchPurChaseData()
  }

  const handleKeyword = (e) =>{
    setKeyword(e.target.value)
  }

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


  const Push = (currentToken) => {
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





  return (
    <div className="h-full">
      <div className="flex justify-end mb-5">
        <form action="" className="space-x-2" onSubmit={search}>
          <input className="border border-gray2 focus:outline-main2 h-10 p-2" type="text" placeholder="검색어" onChange={handleKeyword}/>
          <button>
            <FontAwesomeIcon icon={faMagnifyingGlass} style={{color: "#008b28",}} />
          </button>
        </form>
      </div>
      <Rooms roomsData={purchaseData} component={PurchaseRoom}/>
      <div className="flex justify-center">
        <Pagination
          activePage={currentPage}
          itemsCountPerPage={12}
          totalItemsCount={12*pageSize}
          pageRangeDisplayed={10}
          prevPageText={"<"}
          nextPageText={">"}
          onChange={setPage}
        />
      </div>
    </div>
  )
}
