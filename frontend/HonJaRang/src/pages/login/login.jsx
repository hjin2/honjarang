import { useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { setLoginStatus } from '../../redux/slice/loginSlice';
import Logo from '@/assets/2.png'
import { API } from '@/apis/config';
import { useEffect } from 'react';

export default function Login() {
  const [Email, setEmail] = useState('')
  const [Pwd, setPwd] = useState('')
  const [IdMsg, setIdMsg] = useState('')
  const [PwdMsg, setPwdMsg] = useState('')
  const isLogged = useSelector((state) => {state.login.isLogged})
  const dispatch = useDispatch()

  const navigate = useNavigate()
  const goMarket = () => {
    navigate(`/`)
  };
  const login = (e) => {
    e.preventDefault()

    if (Email === '') {
      setIdMsg('이메일을 입력해주세요.')
    }
    else if (Pwd === '') {
      setPwdMsg('비밀번호를 입력해주세요.')
    }

    else {
    axios.post(`${API.USER}/login`, {
      email: Email,
      password: Pwd
    },
    )
    .then((res)=>{
      console.log(res.data)
      localStorage.setItem('access_token', res.data.access_token)
      localStorage.setItem('refresh_token', res.data.refresh_token)
      localStorage.setItem('user_id', res.data.user_id)
      dispatch(setLoginStatus(true))
      goMarket()
    })
    .catch((err) => {
      console.log(err)
      if (err.response.status === 400) {
        setPwdMsg('비밀번호가 틀립니다.')
        alert('비밀번호가 틀립니다.')
      }
      else if (err.response.status === 404) {
        setIdMsg('존재하지 않는 아이디입니다.')
        alert('존재하지 않는 아이디입니다.')
      }
    })
  }}

  useEffect(() => {
    setIdMsg('')
    setPwdMsg('')
  },[Email, Pwd])
  return (
    <div className="flex flex-col items-center justify-center h-screen" >
      <img src={Logo} alt="" className='mx-auto w-2/12 mb-8'/>
      <form className="space-y-4 md:space-y-6" onSubmit={login}>
        <div>
          <label htmlFor="email" className="block mb-1 font-semibold text-lg text-main2">
            이메일
          </label>
          <input
            type="text"
            className="border-gray2 rounded-lg block w-72 h-10 text-base p-2 focus:outline-main2"
            onChange = {e => setEmail(e.target.value)}/>
            <span className="font-semibold text-bs text-main5">{IdMsg}</span>
        </div>
        <div>
          <label htmlFor="password" className="block mb-1 font-semibold text-lg text-main2">
            비밀번호
          </label>
          <input
            type="password"
            className="border-gray2 rounded-lg block w-72 h-10 text-base p-2 focus:outline-main2 "
            onChange = {e => setPwd(e.target.value)}/>
            <span className="font-semibold text-bs text-main5">{PwdMsg}</span>
        </div>
        <button
          type="submit"
          className="w-72 h-10 main1-full-button my-10 text-base"
        >
          로그인
        </button>
      </form>
      <div className="w-72 flex justify-around p-1 text-gray4 mt-4">
        <Link to="/signup">회원가입</Link>
        <Link to="/findpassword">비밀번호 찾기</Link> 
      </div>
      {/* <div>
        <span>카카오로 시작하기</span>
      </div>
      <div>
        <span>네이버로 시작하기</span>
      </div>
      <div>
        <span>구글로 시작하기</span>
      </div> */}
    </div>
  );
}
