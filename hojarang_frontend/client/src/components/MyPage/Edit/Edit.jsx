import ImageInput from '@/components/MyPage/Edit/ImageInput';
import { Link } from 'react-router-dom';
import { useState } from 'react';
import Address from '@/components/MyPage/Edit/Address';
import Nickname from '@/components/MyPage/Edit/Nickname';
import axios from 'axios';
import { useSelector } from 'react-redux';
import { setUserInfo, imageUpload } from '@/redux/slice/UserInfoSlice';
import { useDispatch } from 'react-redux';
import image from '@/assets/DefaultImage.png'

export default function Edit({ modalState, setModalState }) {
  const nickname = useSelector((state) => state.userinfo.nickname)
  const email = useSelector((state) => state.userinfo.email)
  const address = useSelector((state) => state.userinfo.address)
  const profile_image = useSelector((state) => state.userinfo.profile_image)
  const [addressInput, setAddressInput] = useState(address)
  const [isNicknameModifed, setisNicknameModified] = useState(false)
  const [isAddressModified, setisAddressModified] = useState(false)
  const [imageURL, setImageURL] = useState(profile_image)
  const [NicknameValid, setNicknameValid] = useState(false)
  const [AddressValid, setAddressValid] = useState(false)
  const [nicknameInput, setNicknameInput] = useState(nickname)
  const [latitude, setLatitude] = useState(useSelector((state) => state.userinfo.longitude))
  const [longitude, setLongitude] = useState(useSelector((state) => state.userinfo.latitude))
  const [imageInput, setImageInput] = useState(profile_image)
  
  const token = localStorage.getItem("access_token")

  const dispatch = useDispatch()
  const URL = import.meta.env.VITE_APP_API


  const editUserInfo = async() => {
    console.log(latitude,longitude,nickname,address)
    const data = {
      nickname: nicknameInput,
      address: addressInput,
      latitude: latitude,
      longitude: longitude
    };
    console.log(imageInput)
    const formData = new FormData()
    if(imageInput !=="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"){
      formData.append('profile_image', imageInput)
    }else{
      const extension = image.split('.').pop();
      const imageFile = await fetch(image).then(response => response.blob());
      // Blob 객체와 확장자를 함께 formData에 추가
      formData.append('profile_image', imageFile, `DefaultImage.${extension}`);
    }
    console.log(formData)
    console.log(data)
    axios.put(`${URL}/api/v1/users/users`, data, { headers:{'Authorization': `Bearer ${token}`, "Content-Type":"Application/json"} })
      .then((res)=>{
          console.log(res)
          dispatch(setUserInfo({
            nickname : nicknameInput,
            address : addressInput,
            latitude : latitude,
            longitude : longitude,
          }))
          setModalState(!modalState)
        })
      .catch(function(error) {
        console.log(error);
      });
    axios.post(`${URL}/api/v1/users/profile-image`,formData,{headers:{"Authorization":`Bearer ${token}`, "Content-Type":"multipart/form-data"}})
      .then((res)=>{
        console.log(res)
        dispatch(imageUpload(imageURL))
      })
      .catch((err)=>{
        console.log(err)
      })
    }


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
    axios.delete(`${URL}/api/v1/users`)
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
          <ImageInput 
            imageURL={imageURL}
            setImageURL={setImageURL}
            imageInput = {imageInput}
            setImageInput = {setImageInput}
          />
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
                  nicknameInput={nicknameInput}
                  setNicknameInput={setNicknameInput}
                />
              ) : 
              (<div>{nicknameInput}</div>)
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
                  addressInput = {addressInput}
                  setLatitude = {setLatitude}
                  setLongitude = {setLongitude}
                  setAddressInput = {setAddressInput}
                  setAddressValid = {setAddressValid}
                  handleAddress={handleAddress}
                />
              ):(
                <div>{addressInput}</div>
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
