import ImageInput from './ImageInput';
import { Link } from 'react-router-dom';
import { useState } from 'react';
import Address from './Address';
import Nickname from './NickName';

export default function Edit({ modalState, setModalState }) {
  const [NickName, setNickname] = useState('')
  const [Adress, setAddress] = useState('')

  const [isNicknameModifed, setisNicknameModified] = useState(false)
  const [isAddressModified, setisAddressModified] = useState(false)

  const [NicknameValid, setNicknameValid] = useState(false)
  const [AddressValid, setAddressValid] = useState(false)

  const handleNickname = (() => {
    setisNicknameModified(!isNicknameModifed)
  })
  const handleAddress = (() => {
    setisAddressModified(!isAddressModified)
  })
  const onClickCloseButton = (() => {
    setModalState(!modalState);
  });

  return (
    <div className="relative border bg-white m-auto rounded-lg w-4/12">
      <div className="p-14">
        <div className="space-y-4">
          <div className="flex justify-between mb-4">
            <div className="font-bold">회원정보 수정</div>
            <button type="button" onClick={onClickCloseButton}>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={1.5}
                stroke="currentColor"
                className="w-6 h-6"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            </button>
          </div>
          <ImageInput />
          <div className="flex justify-center">
            <button type="button" className="main1-button w-48">
              프로필 사진 변경하기
            </button>
          </div>
          <div className="flex justify-between">
            <div className="font-bold">이메일</div>
            <div className="flex space-x-2">
              <div>samsung@ssafy.com</div>
              <div className="w-24"></div>
            </div>
          </div>
          <div className="flex justify-between">
            <div className="font-bold">닉네임</div>
            <div className="flex space-x-2">
              {isNicknameModifed ? (
                <Nickname 
                  Nickname={NickName}
                  setNickname={setNickname}
                  setNicknameValid={setNicknameValid}
                  handleNickname={handleNickname}  
                />
              ) : 
              (<div>SSAFY01</div>)
             }
              {isNicknameModifed ? (
                null
              ):(
                <button type="button" className="" onClick={handleNickname}>
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L6.832 19.82a4.5 4.5 0 01-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 011.13-1.897L16.863 4.487zm0 0L19.5 7.125" />
                  </svg>
                </button>
              )}

            </div>
          </div>
          <div className="flex justify-between">
            <div className="font-bold">주소</div>
            <div className="flex space-x-2">
              {isAddressModified ? (
                <Address handleAddress={handleAddress}/>
              ):(
                <div>경북 구미시 3공단 3로 302</div>
              )}
              {isAddressModified ? (
                null
              ):(
                <button type="button" className="" onClick={handleAddress}>
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L6.832 19.82a4.5 4.5 0 01-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 011.13-1.897L16.863 4.487zm0 0L19.5 7.125" />
                  </svg>
                </button>
              )}
            </div>
          </div>
        </div>
        <div className="mt-10 space-y-2">
          <div className="cursor-pointer text-xs text-main1">변경사항 저장</div>
          <div className="cursor-pointer text-xs text-main3">
            <Link to="/findpassword/changepassword">
              비밀번호 변경
            </Link>
          </div>
          <div className="cursor-pointer text-xs text-main5">회원 탈퇴</div>
        </div>
      </div>
    </div>
  );
}
