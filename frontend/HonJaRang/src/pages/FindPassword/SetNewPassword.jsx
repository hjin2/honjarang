import { useState,useEffect } from "react";
import axios from "axios";
import { API } from "@/apis/config";
import { useLocation, useNavigate } from "react-router-dom";
import Logo from "@/assets/2.png"

export default function SetNewPassword() {
  const navigate = useNavigate()
  // 비밀번호, 비밀번호 확인, 오류메시지
  const [password, setPassword] = useState('')
  const [password_cfm, setPassword_cfm] = useState('')
  const [pwdMsg, setpwdMsg] = useState('')
  const [pwdcfmMsg, setpwdcfmMsg] = useState('')
  const [Valid, setValid] = useState(true)
  const location = useLocation()
  const email = location.state.email
  
  // 비밀번호 유효성 검사(8~15자, 한글/영어/숫자 포함 가능)
  let pwdCheck = /^[A-Za-z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{8,15}$/
  const pwd_check = (pwd) => {
    if (pwdCheck.test(pwd)) {
      setpwdMsg('')
    }
    else {
      setpwdMsg('사용할 수 없는 비밀번호입니다.')
    }}


  const pwd_cfm_check = (pwd_cfm) => {
    if (pwd_cfm === '') {
      setpwdcfmMsg('')
      setValid(true)
      
    }
    else if (pwd_cfm !== password) {
      setpwdcfmMsg('비밀번호가 일치하지 않습니다.')
      setValid(true)
    }
    else if(pwd_cfm.length >= 8 && pwd_cfm === password){
      setValid(false)
      setpwdcfmMsg('')
    }}

  useEffect(() => {
    pwd_cfm_check(password_cfm)
  })
    
  const onChange_password = (event) => {
    setPassword(event.currentTarget.value)
    pwd_check(event.currentTarget.value)
  }
  
  const onChange_password_cfm = (event) => {
    setPassword_cfm(event.currentTarget.value)
    pwd_cfm_check(event.currentTarget.value)
  }

  const Password_Change = () => {
    const data = {
      new_password : password,
      email : email
    }
    console.log(data)
    axios.post(`${API.USER}/set-new-password`,data)
    .then((res) => {
      console.log(res.data)
      navigate("/login")
    })
    .catch((err) => {
      console.log(err)
    })
  }
    
    
  return (
    <div className="flex flex-col items-center space-y-4 mt-40">
      <img src={Logo} className="w-2/12 mb-8"/>
      <div>
        비밀번호
        <br />
        <input 
          type="password" 
          onChange = {onChange_password} 
          maxLength="15"
          className="border-gray2 rounded-lg w-72 h-10 text-base p-2 focus:outline-main2" 
        />
        <br />
        <span className="text-xs font-semibold text-main5">{pwdMsg}</span>
      </div>
      <div className="mt-4">
        비밀번호 확인
        <br />
        <input 
          type="password" 
          onChange = {onChange_password_cfm} 
          maxLength="15"
          className="border-gray2 rounded-lg w-72 h-10 text-base p-2 focus:outline-main2"   
        />
        <br />
        <span className="text-xs font-semibold text-main5 h-10">{pwdcfmMsg}</span>
      </div>
      <button disabled={Valid} onClick={Password_Change} className="main1-full-button w-72">비밀번호 변경</button>
    </div>
  )
}