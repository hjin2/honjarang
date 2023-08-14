import axios from 'axios'
import { useState } from 'react'
import Verify_check from './verify_input'


function Email_verify({Email, setEmail, ChangeEmailValid}) {

  // 아이디, 이메일주소, 오류메시지, 인증번호 확인
  const [id, setInput] = useState('')
  const [address, setAddress] = useState('')
  const [emailMsg, setemailMsg] = useState('')
  const [emailCheck, setemailCheck] = useState(false)
  const [EmailDisalbed, setEmailDisalbed] = useState(false)
  const [VerifyDisalbed, setVerifyDisalbed] = useState(false)
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
    axios.get(`${import.meta.env.VITE_APP_API}/api/v1/users/check-email`,
    {params: {
      email : email
    }})
    .then((res) => {
      console.log(res.data)
      let result = confirm('해당 이메일을 사용하시겠습니까?')
      if (result) {
        setEmailDisalbed(true)
        setemailMsg('')

      }
      else {
        setemailMsg('')
      }
    })
    .catch((err) => {
      console.log(err)
      setemailMsg('사용할 수 없는 이메일입니다.')

    })
  }



  const email = id + '@' + address

  // 인증번호 전송 처리
  const email_code = () => {
    if (address === '' || address === 'default') {
      setemailMsg('이메일을 선택해주세요')
    }
    else {
      axios.post(`${import.meta.env.VITE_APP_API}/api/v1/users/send-verification-code`,
      {
        email: email
      })
      .then(function (response) {
        console.log(response)
        setemailMsg('')
        setemailCheck(true)
        setEmail(email)
        setVerifyDisalbed(true)
      alert('인증번호를 전송했습니다!')
    })

      .catch(function (error) {
        console.log(error)
      })
    }    
  }
  
return (
    <div>
      <div>
            이메일
            <br />
            <input type="text" name="id" onChange={onChange} value={id} disabled={EmailDisalbed}/>
            @
            {Manual ?  <input type="text" onChange={onAddress} disabled={EmailDisalbed}/>
            : <select name="email" id="email" className="border-solid border border-black rounded" value={address} onChange={onSelect} disabled={EmailDisalbed}>
            <option value="default">--이메일 선택--</option>
            <option value="naver.com">naver.com</option>
            <option value="gmail.com">gmail.com</option>
            <option value="nate.com">nate.com</option>
            <option value="hanmail.net">hanmail.net</option>
            <option value="manual"><input type="text" />직접 입력</option>
          </select>}
            <button className='border-solid border border-black rounded bg-gray2 ml-2'
            onClick={EmailDuplicate}>이메일 중복 확인</button>
            <button className='border-solid border border-black rounded bg-gray2 ml-2'
            onClick = {email_code} disabled={VerifyDisalbed}
            >인증번호 전송</button>
            <br />
            <span>{emailMsg}</span>
            {emailCheck && <Verify_check email={email} ChangeEmailValid = {ChangeEmailValid}/>}
          </div>
          
          
    </div>
  )

}


export default Email_verify