import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { API } from '@/apis/config';
import Logo from "@/assets/2.png"

export default function FindPassword() {
  const [Id, setId] = useState('')
  const [Address, setAddress] = useState('');
  const [Check, setCheck] = useState(false)
  const [Code, setCode] = useState('')
  const [errorMessage, setErrorMessage] = useState('')
  const [time, setTime] = useState(300)
  const [timer, setTimer] = useState(null); // 타이머 상태 추가

  const getSeconds = (time) => {
    const seconds = Number(time % 60);
    if (seconds < 0) {
      return "00"
    } else if (seconds < 10) {
      return "0" + String(seconds);
    } else {
      return String(seconds);
    }
  }


  useEffect(() => {
    setTimer(
      setInterval(() => {
        setTime((prev) => prev - 1);
      }, 1000)
    );

    return () => clearInterval(timer); // cleanup 함수 내에서 clearInterval 호출
  }, []);

  useEffect(() => {
    if (time < 0) {
      clearInterval(timer);
      location.reload();
      alert("인증 시간 초과입니다");
    }
  }, [time, timer]); // timer 상태 추가


  const onChange = (e) => {
    setId(e.target.value)
  }

  const onSelect = (e) => {
    setAddress(e.target.value)
  }

  const onCode = (e) => {
    setCode(e.target.value)
  }

  const movePage = useNavigate();
  const email = Id + '@' + Address;

  const check_number = () => {
    axios.post(`${API.USER}/verify-code`,
    {
      email: email,
      code: Code
    })
    .then((res) => {
      console.log(res.data)
      change_password()
    })
    .catch((err) => {
      console.log(err)
      alert('인증번호가 틀렸습니다.')
    })
  }

  function change_password() {
    movePage('/setnewpassword',{
      replace:true,
      state:{email}
    });
  }
  
  const EmailDuplicate = () => {
    if (Address === 'default' || Address === '') {
      console.log(1)
    }
    else {

      axios.get(`${API.USER}/check-email`,
      {params: {
        email : email
      }})
      .then((res) => {
        console.log(res.data)
        setErrorMessage("존재하지 않는 이메일입니다.")
      }    
      )
      .catch((err) => {
        console.log(err)
        axios.post(`${API.USER}/send-verification-code`,
        {
          email: email
        })
        .then(function (response) {
          console.log(response)
          setErrorMessage("")
          setCheck(true)
          alert('인증번호를 전송했습니다!')
      })
  
        .catch(function (error) {
          console.log(error)
        })
  
      })
    }
  }

  return (
    <div className="flex flex-col items-center space-y-4 mt-40">
      <img src={Logo} className="w-2/12 mb-8"/>
      <div>
        <div>
          <label htmlFor="email">가입된 이메일</label>
          <br />
          <input 
            type="text" 
            name="id"
            className="border-gray2 rounded-lg w-72 h-10 text-base p-2 focus:outline-main2" 
            onChange={onChange}/>
              @
              <select name="email" id="email" className="border-solid border h-10 border-black rounded-lg" onChange={onSelect}>
                <option value="default">--이메일 선택--</option>
                <option value="naver.com">naver.com</option>
                <option value="gmail.com">gmail.com</option>
                <option value="nate.com">nate.com</option>
                <option value="hanmail.net">hanmail.net</option>
              </select>
          {Check ? (
            <button disabled={Check} className="main5-full-button ml-4 w-24 h-10">전송완료</button>
          ) : (
            <button  onClick={EmailDuplicate} className="main1-full-button ml-4 w-24 h-10">인증번호 전송</button>
          )} 
          {errorMessage ? (
            <div className="text-xs text-main5 font-semibold">{errorMessage}</div>
          ):(
            <div></div>
          )}
        </div>
        {Check ? <div>
          <div className='mt-4'>인증번호</div>
          <input 
            type="text"
            className="border-gray2 rounded-lg w-30 h-10 text-base p-2 focus:outline-main2"  
            onChange={onCode}
          />
          <button onClick={check_number} className="main1-full-button ml-4 w-24 h-10">인증하기</button>
          <span className="font-semibold text-lg ml-2 flex items-center">{parseInt(time / 60)} : {getSeconds(time)}</span>
        </div> : ''}
        
      </div>
    </div>
  );
}
