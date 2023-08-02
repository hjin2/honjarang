// import React from 'react'
import { useParams } from 'react-router-dom';
import UserInfo from '../../components/MyPage/UserInfo';
import SideBar from '../../components/MyPage/SideBar';

export default function MyPage() {
  const { nickname } = useParams();
  return (
    <div className="my-page space-y-5">
      <UserInfo nickname={nickname} />
      <div className='h-96'>
       <SideBar />
      </div>
    </div>
  );
}
