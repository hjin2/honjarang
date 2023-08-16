import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { API } from '@/apis/config';

export default function FindPassword() {
  const [Id, setId] = useState('')
  const [Address, setAddress] = useState('');
  const [Check, setCheck] = useState(false)
  const [Number, setNumber] = useState('')

  const onChange = (e) => {
    setId(e.target.value)
  }

  const onSelect = (e) => {
    setAddress(e.target.value)
  }

  const onCode = (e) => {
    setNumber(e.target.value)
  }

  const movePage = useNavigate();
  const email = Id + '@' + Address;

  const check_number = () => {
    axios.post(`${API.USER}/verify-code`,
    {
      email: email,
      code: Number
    })
    .then((res) => {
      console.log(res.data)
      change_password()
    })
    .catch((err) => {
      console.log(err)
    })
  }

  function change_password() {
    movePage('/setnewpassword');
  }
  
  const email_code = () => {
    axios
      .post(`${API.USER}/send-verification-code`, {
        email: email,
      })
      .then(function (response) {
        console.log(response);
        alert('인증 완료 되었습니다.')
        setCheck(true)
      })
      .catch((err) => {
        console.log(err)
      });
  };

  return (
    <div>
      <div>
        <label htmlFor="email">비밀번호를 찾을 이메일</label>
        <br />
        <input type="text" name="id" onChange={onChange}/>
            @
            <select name="email" id="email" className="border-solid border border-black rounded"onChange={onSelect}>
              <option value="default">--이메일 선택--</option>
              <option value="naver.com">naver.com</option>
              <option value="gmail.com">gmail.com</option>
              <option value="nate.com">nate.com</option>
              <option value="hanmail.net">hanmail.net</option>
            </select>
        <button onClick={email_code}>인증번호 전송</button>
      </div>
      {Check ? <div>
        <input type="text"  onChange={onCode}/>
        <button onClick={check_number}>인증하기</button>
      </div> : ''}
      
    </div>
  );
}
