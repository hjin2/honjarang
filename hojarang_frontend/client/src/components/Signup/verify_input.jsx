import { useState,useEffect } from "react"
import axios from "axios"

export default function Verify_check({email, ChangeEmailValid}) {
  // 인증번호 변수
  const [VerifyNum, setVerifyNum] = useState('')
  const [Check, setCheck] = useState(false)
  const [time, setTime] = useState(300)

  const getSeconds = (time) => {
    const seconds = Number(time % 60);
    if (seconds < 0) {
      return "00"
    }
    else if(seconds < 10) {
        return "0" + String(seconds);
    } 
    else {
        return String(seconds);
    }
}

  const onChange = (e) => {
    setVerifyNum(e.target.value)
  }

  useEffect(() => {
    const timer = setInterval(() => {
        setTime((prev) => prev - 1);
    }, 1000);
    return () => clearInterval(timer);
}, [time]);

useEffect(() => {
	if(time < 0) {
      location.reload()
      alert("인증 시간 초과입니다");
    }
}, [time]);

  // 인증번호 확인 절차
  const NumberCheck = () => {
    axios.post(`${import.meta.env.VITE_APP_API}/api/v1/users/verify-code`, {
      email: email,
      code: VerifyNum
    })
    .then(function(res) {
      console.log(res)
      ChangeEmailValid()
      alert('인증이 완료되었습니다.')
      setCheck(true)
    })
    .catch(function(err) {
      console.log(err)
      alert('인증번호가 틀렸습니다.')
    })
  }


  return (
    <div>
      <div>
            인증번호 입력

            <br />
            <input type="text" onChange = {onChange} disabled={Check}/>
            <button className='border-solid border border-black rounded bg-gray2 ml-2'
            onClick={NumberCheck} disabled={Check}>인증번호 확인</button>
                <span>{parseInt(time / 60)}</span>
                <span> : </span>
                <span>{getSeconds(time)}</span>
          </div>
    </div>
  )
}