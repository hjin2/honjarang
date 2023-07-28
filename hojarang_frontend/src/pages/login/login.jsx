import React from 'react';
import { Link } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import store from '../../redux/store';
import { loginAccount } from '../../redux/slice/loginSlice';



export default function Login() {
  
  // dispatch에 action 전달하면 동작 실시됨
  const dispatch = useDispatch();

  return (
    <div className="container flex flex-col items-center justify-center  mx-auto ">
      <form className="space-y-4 md:space-y-6" action="#">
        <div>
          <label htmlFor="email" className="block mb-2 ">이메일</label>
          <input type="email" />
        </div>
        <div>
          <label htmlFor="password" className="block mb-2">비밀번호</label>
          <input type="empassword"  />
        </div>
      </form>
      <button onClick={() => {dispatch(loginAccount)}} className="w-32 main1-button" >로그인</button>
      
      <div>
        <Link to='/findpassword'>비밀번호 찾기</Link> | <Link to='/signup'>회원가입</Link>
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
  )
};



