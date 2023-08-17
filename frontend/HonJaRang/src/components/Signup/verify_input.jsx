import { useState, useEffect } from "react"
import axios from "axios"
import { API } from "@/apis/config"

export default function Verify_check({ email, ChangeEmailValid }) {
  // 인증번호 변수
  const [VerifyNum, setVerifyNum] = useState('')
  const [Check, setCheck] = useState(false)
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

  const onChange = (e) => {
    setVerifyNum(e.target.value)
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

  // 인증번호 확인 절차
  const NumberCheck = () => {
    axios.post(`${API.USER}/verify-code`, {
      email: email,
      code: VerifyNum
    })
      .then(function (res) {
        console.log(res)
        ChangeEmailValid()
        alert('인증이 완료되었습니다.')
        setCheck(true)
        clearInterval(timer); // 인증이 완료되면 타이머를 멈춤
      })
      .catch(function (err) {
        console.log(err)
        alert('인증번호가 틀렸습니다.')
      })
  }

  return (
    <div className="mb-3 mt-3">
      <div className="flex flex-col">
        <label className="font-semibold text-lg text-main2 mb-1">인증번호 입력</label>
        <div className="flex ">
          <input type="text" onChange={onChange} disabled={Check} 
          className=" border-gray2 rounded-lg block w-60 h-10 p-2 focus:outline-main2"/>
          {Check ? (
            <button className="w-32 gray3-full-button ml-2">인증완료</button>
          ):(
            <button className='w-32 h-10 main4-full-button text-base ml-2'
              onClick={NumberCheck} disabled={Check}>인증번호 확인</button>
          )}
          {Check ? (
              null
            ) : (
            <span className="font-semibold text-lg ml-2 flex items-center">{parseInt(time / 60)} : {getSeconds(time)}</span>
          )}
        </div>
      </div>
    </div>
  )
}
