import React from 'react'
import { Link } from 'react-router-dom'

export default function Main() {
  return (
    <div className=" h-screen">
      <Link to="/login">
        <button >로그인</button>
      </Link>
      <div className="flex flex-row bg-main2">
        <img className="border  h-80" src="/src/assets/noimage.png" alt=""/>
        <div className="flex flex-col border h-96">
          <p className="text-5xl">공동배달</p>
          <p>근처 자취생들과 함께하는 공동 배달 서비스</p>
        </div>
      </div>
    </div>
  )
}
