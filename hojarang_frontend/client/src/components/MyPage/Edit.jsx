import ImageInput from './ImageInput';
import { Link } from 'react-router-dom';
import { useState } from 'react';
import Address from './Address';
import Nickname from './Nickname';
import axios from 'axios';
import { useSelector } from 'react-redux';

export default function Edit({ modalState, setModalState }) {
  const nickname = useSelector((state) => state.userinfo.nickname)
  const email = useSelector((state) => state.userinfo.email)
  const address = useSelector((state) => state.userinfo.address)

  const [isNicknameModifed, setisNicknameModified] = useState(false)
  const [isAddressModified, setisAddressModified] = useState(false)

  const [NicknameValid, setNicknameValid] = useState(false)
  const [AddressValid, setAddressValid] = useState(false)

  const [latitude, setLatitude] = useState(0)
  const [longitude, setLongitude] = useState(0)
  const token = localStorage.getItem("access_token")

  const editUserInfo = () => {
    console.log(latitude,longitude,nickname,address)
    const headers = {
      'Authorization': `Bearer ${token}`
    };
  
    const data = {
      nickname: nickname,
      address: address,
      latitude: latitude,
      longitude: longitude
    };
    console.log(data)
  
    axios.put('http://honjarang.kro.kr:30000/api/v1/users/users', data, { headers })
      .then(function(response) {
        console.log(response);
        setModalState(!modalState)
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const handleNickname = (() => {
    setisNicknameModified(!isNicknameModifed)
  })
  const handleAddress = (() => {
    setisAddressModified(!isAddressModified)
  })
  const onClickCloseButton = (() => {
    setModalState(!modalState);
  });
  const onClickWithdrawal = (() => {
    axios.delete('http://honjarang.kro.kr:30000/api/v1/users')
  })

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
              <div>{email}</div>
            </div>
          </div>
          <div className="flex justify-between">
            <div className="font-bold">닉네임</div>
            <div className="flex space-x-2">
              {isNicknameModifed ? (
                <Nickname
                  setNicknameValid={setNicknameValid}
                  handleNickname={handleNickname}  
                />
              ) : 
              (<div>{nickname}</div>)
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
                <Address
                  setLatitude = {setLatitude}
                  setLongitude = {setLongitude}
                  setAddressValid = {setAddressValid}
                  handleAddress={handleAddress}
                />
              ):(
                <div>{address}</div>
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
          <div 
            className="cursor-pointer text-xs text-main1"
            onClick={editUserInfo}
          >변경사항 저장</div>
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
