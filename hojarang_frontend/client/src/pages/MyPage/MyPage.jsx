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
    axios.get('http://honjarang.kro.kr:30000/api/v1/users/info',
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
