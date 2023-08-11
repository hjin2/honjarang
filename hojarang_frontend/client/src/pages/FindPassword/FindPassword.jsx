import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';

export default function FindPassword() {
  const input = useState({
    id: '',
  });

  const address = useState('');

  const id = input;

  const movePage = useNavigate();
  const email = id + '@' + address;

  const email_code = () => {
    axios
      .post(`${import.meta.env.VITE_APP_API}/users/send-verification-code`, {
        email: email,
      })
      .then(function (response) {
        console.log(response);
      });
  };

  function change_password() {
    movePage('/FindPassword/ChangePassword');
  }

  return (
    <div>
      <div>
        <label htmlFor="email">비밀번호를 찾을 이메일</label>
        <br />
        <input type="text" />@
        <select name="emaildetail" id="emaildetail">
          <option value="default">--이메일 선택--</option>
          <option value="naver.com">naver.com</option>
          <option value="gmail.com">gmail.com</option>
          <option value="nate.com">nate.com</option>
          <option value="hanmail.net">hanmail.net</option>
        </select>
        <button onClick={email_code}>인증번호 전송</button>
      </div>
      <div>
        <input type="number" />
        <button onClick={change_password}>인증하기</button>
      </div>
    </div>
  );
}
