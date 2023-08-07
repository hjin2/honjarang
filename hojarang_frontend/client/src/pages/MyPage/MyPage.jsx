// import React from 'react'
import { useParams } from 'react-router-dom';
import UserInfo from '../../components/MyPage/UserInfo';
import SideBar from '../../components/MyPage/SideBar';
import { useEffect } from 'react';
import axios from 'axios';
import { useDispatch } from 'react-redux';
import { Userinfo } from '../../redux/slice/UserInfoSlice';
import { useSelector } from 'react-redux';

export default function MyPage() {
  const nickname = useSelector((state) => state.userinfo.nickname)
  const { id } = useParams();
  const dispatch = useDispatch()
  const token = localStorage.getItem("access_token")
  useEffect(() => {
    console.log(id)
    console.log(token)
    axios.get('http://honjarang.kro.kr:30000/api/v1/users/info',
      {
        params : {id : id},
        headers : {'Authorization' : `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpc2NoYXJAbmF2ZXIuY29tIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE2OTEzNzExMTUsImV4cCI6MTY5MTM3NDcxNX0.O4wh73mcwHA7pediKrye_sUMe1IVSo7Et7y7-m8A_g4`}
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
