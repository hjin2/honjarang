import { useState } from 'react'
import axios from 'axios'
import './signup.css'
import Email_verify from '../../components/Signup/email_verify'
import Nickname_check from '../../components/Signup/nickname_check'
import Address_check from '../../components/Signup/address_check'
import Password_check from '../../components/Signup/Password_check'
import { useNavigate } from 'react-router-dom'


export default function Signup() {
  // 유효성 검사 목록
  const [EmailValid, setEmailValid] = useState(false)
  const [NicknameValid, setNicknameValid] = useState(false)
  const [PwdValid, setPwdValid] = useState(false)
  const [AddressValid, setAddressValid] = useState(false)

  // 데이터 변수 목록
  const [Email, setEmail] = useState('')
  const [Nickname, setNickname] = useState('')
  const [Pwd, setPwd] = useState('')
  const [Address, setAddress] = useState('')
  const [Latitude, setLatitude] = useState(0)
  const [Longitude, setLongitude] = useState(0)

  // 유효성 검사 통과 함수
  const ChangeEmailValid = () => {
    setEmailValid(true)
  }

  const ChangeNicknameValid = () => {
    setNicknameValid(true)
  }
  const ChangePwdValid = () => {
    setPwdValid(true)
  }
  const ChangeAddressValid = () => {
    setAddressValid(true)
  }

  const SignupValid =  (EmailValid&&NicknameValid&&PwdValid&&AddressValid ? false:true)
  
  // 회원가입 성공 시 로그인 페이지로 이동하도록 구현
  const navigate = useNavigate()
  const SignupSuccess = () => {
    navigate('/login')
  }
  
  const onClick = () => {
    axios.post(`${import.meta.env.VITE_APP_API}/api/v1/users/signup`,
    {
      email: Email,
      password: Pwd,
      nickname: Nickname,
      address: Address,
      latitude: Latitude,
      longitude: Longitude
    })
    .then(function(res) {
      console.log(res.data)
      SignupSuccess()
    })
    .catch(function(err) {
      console.log(err)
    })
  }
  return (
    <div className="m-auto w-4/5 h-fit border border-solid border-black rounded
    flex flex-col items-start justify-around p-2">
          <h1 className='text-3xl'>회원가입</h1>
          <Email_verify Email = {Email} setEmail = {setEmail} ChangeEmailValid = {ChangeEmailValid}/>
          <Nickname_check Nickname = {Nickname} setNickname = {setNickname} ChangeNicknameValid = {ChangeNicknameValid}/>
          <Password_check Pwd = {Pwd} setPwd = {setPwd} setPwdValid={setPwdValid}/>
          <Address_check Address = {Address} setAddress = {setAddress}
          Latitude = {Latitude} setLatitude = {setLatitude}
          Longitude = {Longitude} setLongitude = {setLongitude}
          ChangeAddressValid = {ChangeAddressValid}/>
          <button disabled = {SignupValid} className='border-solid border border-black rounded bg-main4 mt-2 place-self-center' 
          onClick={onClick} >회원가입</button>
    </div>
  )
}