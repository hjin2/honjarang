// import React from 'react'
import { useParams } from 'react-router-dom';
import UserInfo from '@/components/MyPage/UserInfo';
import SideBar from '@/components/MyPage/SideBar';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { useDispatch } from 'react-redux';
import { Userinfo } from '@/redux/slice/UserInfoSlice';
import { API } from '@/apis/config';

export default function MyPage() {
  const LoginId = localStorage.getItem('user_id');
  const [isMe, setIsMe] = useState(false);

  useEffect(() => {
    if (LoginId == id) {
      setIsMe(true);
    } else {
      setIsMe(false);
    }
  }, []);

  const { id } = useParams();
  const dispatch = useDispatch();
  const token = localStorage.getItem('access_token');
  useEffect(() => {
    console.log(id);
    console.log(token);
    axios
      .get(`${API.USER}/info`, {
        params: { id: id },
        headers: { Authorization: `Bearer ${token}` },
      })
      .then(function (response) {
        console.log(response.data);
        dispatch(Userinfo(response.data));
      })
      .catch(function (error) {
        console.log(error);
      });
  }, [id, token]);

  return (
    <div className="my-page space-y-5">
      <UserInfo id={id} isMe={isMe} />
      <div>
        <SideBar id={id} isMe={isMe} />
      </div>
    </div>
  );
}
