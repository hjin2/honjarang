import axios from 'axios'
import { useState } from 'react'


function Email_verify() {
  const [inputs, setInputs] = useState({
    id: '',
    nickname: ''
  })

  const [address, setAddress] = useState('')

  const onSelect = (event) => {
    setAddress(event.target.value)
  }

  const {id, nickname} = inputs

  const onChange = (event) => {
    const {value, name} = event.target
    setInputs({...inputs,
    [name]: value})
  }
  
  const email = id + '@' + address
  
  const email_code = () => {
    axios.post('http://localhost:8080/users/send-verification-code',
    {
      email: email
    })
    .then(function (response) {
      console.log(response)
    })
  }
  
  let nicknameCheck = /^[A-Za-z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{2,15}$/
  const nickname_check = () => {
    if (nicknameCheck.test(nickname)) {
  axios.get('http://localhost:8080/users/check-nickname',
  {
    nickname : nickname
  })
  .then(function (response) {
    console.log(response)
  })
  .catch(function (error) {
    console.log(error)
    console.log(nickname)
  })
 }
else {
  console.log('닉네임 오류')
}}
  return (
    <div>
      <div>
            이메일
            <br />
            <input type="text" name="id" onChange={onChange} value={id}/>
            @
            <select name="email" id="email" className="border-solid border border-black rounded" value={address} onChange={onSelect}>
              <option value="default">--이메일 선택--</option>
              <option value="naver.com">naver.com</option>
              <option value="google.com">google.com</option>
              <option value="nate.com">nate.com</option>
              <option value="hanmail.net">hanmail.net</option>
            </select>
            <button className='border-solid border border-black rounded bg-gray2 ml-2'
            onClick = {email_code}
            >인증번호 전송</button>
          </div>
          <div>
            인증번호 입력
            <br />
            <input type="number" />
            <button className='border-solid border border-black rounded bg-gray2 ml-2'>인증번호 확인</button>
          </div>
          <div>
            닉네임
            <br />
            <input type="text" name="nickname" onChange={onChange} value={nickname}/>
            <button className='border-solid border border-black rounded bg-gray2 ml-2'
            onClick = { nickname_check }>중복 확인</button>
          </div>
    </div>
  )

}


export default Email_verify