// import React from 'react'
import { useParams } from 'react-router-dom';
import UserInfo from '../../components/MyPage/UserInfo';
import SideBar from '../../components/MyPage/SideBar';
import { useEffect } from 'react';
import axios from 'axios';
import { useDispatch } from 'react-redux';
import { Userinfo } from '../../redux/slice/UserInfoSlice';

export default function MyPage() {
  const { id } = useParams();
  const dispatch = useDispatch()
  useEffect(() => {
    axios.get('http://honjarang.kro.kr:30000/api/v1/users/info',
      {
        params : {id : 2},
        headers : {'Authorization' : 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpc2NoYXJAbmF2ZXIuY29tIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE2OTExMzQzMjgsImV4cCI6MTY5MTEzNzkyOH0.0i3JMQhK-UcRuOnnUEuvULq38zcVdQpknNskd-9Lvwc'}
      },
      )
      .then(function(response){
        console.log(response.data)
        dispatch(Userinfo(response.data))
      })
      .catch(function(error){
        console.log(error)
      })
  },[]);

  return (
    <div className="my-page space-y-5">
      <UserInfo />
      <div className='h-96'>
       <SideBar />
      </div>
    </div>
  );
}
