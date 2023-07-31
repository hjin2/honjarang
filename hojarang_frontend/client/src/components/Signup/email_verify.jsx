import axios from 'axios';
import { useState } from 'react';
import Verify_check from './verify_input';

function Email_verify() {
  const [id, setInput] = useState('');
  const [address, setAddress] = useState('');
  const [emailMsg, setemailMsg] = useState('');
  const [emailCheck, setemailCheck] = useState(false);

  const onSelect = (event) => {
    setAddress(event.target.value);
  };

  const onChange = (event) => {
    setInput(event.target.value);
  };

  const email = id + '@' + address;

  const email_code = () => {
    if (address === '' || address === 'default') {
      setemailMsg('이메일을 선택해주세요');
    } else {
      axios
        .post('http://localhost:8080/users/send-verification-code', {
          email: email,
        })
        .then(function (response) {
          console.log(response);
          setemailMsg('');
          setemailCheck(true);
          console.log(emailCheck);
        })
        .catch(function (error) {
          console.log(error);
          console.log(email);
          setemailCheck(true);
          alert('인증번호를 전송했습니다!');
          setemailMsg('사용할 수 없는 이메일입니다.');
        });
    }
  };

  return (
    <div>
      <div>
        이메일
        <br />
        <input type="text" name="id" onChange={onChange} value={id} />@
        <select
          name="email"
          id="email"
          className="border-solid border border-black rounded"
          value={address}
          onChange={onSelect}
        >
          <option value="default">--이메일 선택--</option>
          <option value="naver.com">naver.com</option>
          <option value="google.com">google.com</option>
          <option value="nate.com">nate.com</option>
          <option value="hanmail.net">hanmail.net</option>
        </select>
        <button
          className="border-solid border border-black rounded bg-gray2 ml-2"
          onClick={email_code}
        >
          인증번호 전송
        </button>
        <br />
        <span>
          {emailMsg} {emailCheck}
        </span>
        {emailCheck && <Verify_check />}
      </div>
    </div>
  );
}

export default Email_verify;
