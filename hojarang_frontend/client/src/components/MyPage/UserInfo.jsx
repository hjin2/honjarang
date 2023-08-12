import { useState } from 'react';
import Edit from '@/components/MyPage/Edit/Edit';
import Modal from '@/components/Common/Modal';
import { useSelector } from 'react-redux';

export default function UserInfo() {
  const [modalState, setModalState] = useState(false);
  const onModalOpen = () => {
    setModalState(!modalState);
  };
  const image = useSelector((state) => state.userinfo.profile_image);
  const nickname = useSelector((state) => state.userinfo.nickname);
  const point = useSelector((state) => state.userinfo.point)
  return (
    <div className="flex justify-center space-x-10">
      <img className="h-20 w-20 rounded-full" src={image} loading='lazy'></img>
      <div className="space-y-2">
        <div className="flex">
          <div className="font-bold text-lg mr-3">{nickname}</div>
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
        </div>
        <div className="text-xs">{point}P</div>
        {modalState && (
          <Modal modalState={modalState} setModalState={setModalState}>
            <Edit modalState={modalState} setModalState={setModalState}/>
          </Modal>
        )}
      </div>
    </div>
  );
}
