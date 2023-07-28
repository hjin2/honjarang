import { useState } from 'react'
import EditModal from './EditModal';

export default function UserInfo(props) {
  const [modalState, setModalState] = useState(false);
  const onModalOpen = () => {
    setModalState(!modalState)
  }

  return (
    <div className="flex justify-center space-x-10">
        <div className="border w-32 h-32"></div>
        <div className="space-y-2">
          <div>이메일 : 이메일</div>
          <div>닉네임 : {props.nickname}</div>
          <div>현재 포인트 : 현재포인트</div>
          <div className="space-x-5">
            <button type="button" className="main1-button w-32" onClick={onModalOpen}>회원정보 수정</button>
            <button type="button" className="main5-button w-32">회원 탈퇴</button>
          </div>
          {modalState && (
            <EditModal modalState = {modalState} setModalState = {setModalState}/>
          )}
        </div>
      </div>
  )
}
