import axios from 'axios'
import { useState } from 'react'
import Verify_check from './verify_input'
import { API } from '@/apis/config'


function Email_verify({Email, setEmail, ChangeEmailValid}) {

  // 아이디, 이메일주소, 오류메시지, 인증번호 확인
  const [id, setInput] = useState('')
  const [address, setAddress] = useState('')
  const [emailMsg, setemailMsg] = useState('')
  const [emailCheck, setemailCheck] = useState(false)
  const [EmailDisalbed, setEmailDisalbed] = useState(false)
  const [Manual, setManual] = useState(false)

  const onSelect = (event) => {
    setAddress(event.target.value)
    if (event.target.value === 'manual') {
      setManual(true)
    }
  }

  const onChange = (event) => {
    setInput(event.target.value)
  }

  const onAddress = (e) => {
    setAddress(e.target.value)
  }
  

  const EmailDuplicate = () => {
    if (address === 'default' || address === '') {
      setemailMsg('이메일을 선택해주세요.')
    }
    else {

      axios.get(`${API.USER}/check-email`,
      {params: {
        email : email
      }})
      .then((res) => {
        console.log(res.data)
        let result = confirm('사용가능한 이메일 입니다. \n해당 이메일을 사용하시겠습니까?')
        if (result) {
          setEmailDisalbed(true)
          setemailMsg('')
  
        }
        else {
          setemailMsg('')
        }
        axios.post(`${API.USER}/send-verification-code`,
        {
          email: email
        })
        .then(function (response) {
          console.log(response)
          setemailMsg('')
          setemailCheck(true)
          setEmail(email)
        alert('인증번호를 전송했습니다!')
      })
  
        .catch(function (error) {
          console.log(error)
        })
      }    
      )
      .catch((err) => {
        console.log(err)
        setemailMsg('사용할 수 없는 이메일입니다.')
  
      })
    }
  }



  const email = id + '@' + address

  
  
return (
    <div className='mb-2'>
      <div>
      <label htmlFor="email" className="font-semibold text-lg text-main2 mb-1">이메일</label>
      <div className="flex">
        <input type="text" name="id" onChange={onChange} value={id} disabled={EmailDisalbed}
        className=" border-gray2 rounded-lg block w-60 h-10 p-2 focus:outline-main2 m-0"/>
        <span className='ml-2 mr-2 text-lg'>@</span>
        {Manual ?  
        (<input type="text" onChange={onAddress} disabled={EmailDisalbed} 
        className=" border-gray2 rounded-lg block w-60 h-10 text-base p-2 m-0 focus:outline-main2"/>)
        : 
        (<select name="email" id="email" className=" border border-gray2 rounded-lg block w-60 h-10 p-2 m-0 focus:outline-main2" 
        value={address} onChange={onSelect} disabled={EmailDisalbed}
        >
          <option value="default" >--이메일 선택--</option>
          <option value="naver.com">naver.com</option>
          <option value="gmail.com">gmail.com</option>
          <option value="nate.com"> nate.com</option>
          <option value="hanmail.net" > hanmail.net</option>
          <option value="manual">직접 입력</option>
        </select>)}
        {EmailDisalbed ? (
          <button className="gray3-full-button w-32 ml-2">전송완료</button>
        ):(
          <button className='w-32 h-10 main4-full-button text-base ml-2'
          onClick={EmailDuplicate} disabled={EmailDisalbed}>이메일 중복 확인</button>
        )}
      </div>
      <span className="font-semibold text-lg text-red-600">{emailMsg}</span>
      {emailCheck && <Verify_check email={email} ChangeEmailValid = {ChangeEmailValid}/>}
    </div>        
  </div>
  )

}


export default Email_verify