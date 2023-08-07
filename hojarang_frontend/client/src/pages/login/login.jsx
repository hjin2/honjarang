import { useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

export default function Login() {
  const [Email, setEmail] = useState('')
  const [Pwd, setPwd] = useState('')

  const navigate = useNavigate()
  const goMypage = () => {
    navigate('/mypage/:nickname')
  };
  
  const login = () => {
    axios.post('http://honjarang.kro.kr:30000/api/v1/users/login', {
      email: Email,
      password: Pwd
    },
    )
    .then((res)=>{
      console.log(res.data)
      goMypage()
      localStorage.setItem('access_token', res.data.access_token)
      localStorage.setItem('refresh_token', res.data.refresh_token)
      localStorage.setItem('user_id', res.data.user_id)
    })
    .catch((err) => {
      console.log(err)
    })
  }


  return (
    <div className="container flex flex-col items-center justify-center mx-auto ">
      <form className="space-y-4 md:space-y-6" action="#">
        <div>
          <label htmlFor="email" className="block mb-2">
            이메일
          </label>
          <input
            type="text"
            className="border-gray3 rounded-lg block focus:outline-main2 focus:outline-2"
            onChange = {e => setEmail(e.target.value)}/>
        </div>
        <div>
          <label htmlFor="password" className="block mb-2">
            비밀번호
          </label>
          <input
            type="password"
            className="border-gray3 rounded-lg block focus:outline-main2 focus:outline-2"
            onChange = {e => setPwd(e.target.value)}/>
        </div>
      </form>
      <button
        onClick={login}
        className="w-32 main1-button my-5"
      >
        로그인
      </button>
      <div>
        <Link to="/findpassword">비밀번호 찾기</Link> |{' '}
        <Link to="/signup">회원가입</Link>
      </div>
      <div>
        <span>카카오로 시작하기</span>
      </div>
      <div>
        <span>네이버로 시작하기</span>
      </div>
      <div>
        <span>구글로 시작하기</span>
      </div>
    </div>
  );
}
