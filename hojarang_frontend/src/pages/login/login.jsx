import React from 'react';
import { Link } from 'react-router-dom';


export default function login() {
  return (
    <div style={{
      justifyContent: 'center', 
      width: '100%', height: '100vh'
    }}>
      <form >
        <label htmlFor="email">이메일</label>
        <input type="email" />
        <label htmlFor="password">비밀번호</label>
        <input type="empassword" />
      </form>
      <button>로그인</button>
      <div>
        <Link to='/'>비밀번호 찾기</Link> | <Link to='/signup'>회원가입</Link>
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
}
