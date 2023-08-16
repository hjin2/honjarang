import { useState } from 'react';
import Edit from '@/components/MyPage/Edit/Edit';
import Modal from '@/components/Common/Modal';
import { useSelector } from 'react-redux';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { API } from '@/apis/config';

export default function UserInfo({id, isMe}) {
  const navigate = useNavigate()
  const [modalState, setModalState] = useState(false);
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  const onModalOpen = () => {
    setModalState(!modalState);
  };
  const image = useSelector((state) => state.userinfo.profile_image);
  const nickname = useSelector((state) => state.userinfo.nickname);
  const point = useSelector((state) => state.userinfo.point)
  const startChat = () => {
    const data = {target_id : Number(id)}
    axios.post(`${API.CHATS}/one-to-one`,data,{ headers:{'Authorization': `Bearer ${token}`, "Content-Type":"Application/json"}})
      .then((res) => {
        console.log(res)
        navigate(`/chatting/${res.data}`)
      })
      .catch((err) => console.log(err))
  }

  return (
    <div className="flex justify-center space-x-10">
      <img className="h-20 w-20 rounded-full" src={image} loading='lazy'></img>
      <div className="space-y-2">
        <div className="flex">
          <div className="font-bold text-lg mr-3">{nickname}</div>
          {isMe ? (
            <button type="button" className="" onClick={onModalOpen}>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={1.5}
                stroke="currentColor"
                className="w-4 h-4"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L10.582 16.07a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0115.75 21H5.25A2.25 2.25 0 013 18.75V8.25A2.25 2.25 0 015.25 6H10"
                />
              </svg>
            </button>
          ):(null)}
        </div>
        <div className='flex items-baseline space-x-4'>
          <div className='text-sm text-gray3 font-semibold'>포인트</div>
          <div className="text-lg text-gray5 font-semibold">{point}P</div>
        </div>
        {isMe ? (null):(
          <button className="main1-full-button w-24" onClick={startChat}>1:1 채팅하기</button>
        )}
        {modalState && (
          <Modal modalState={modalState} setModalState={setModalState}>
            <Edit modalState={modalState} setModalState={setModalState}/>
          </Modal>
        )}
      </div>
    </div>
  );
}
