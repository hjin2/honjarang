import { NavLink, Link } from 'react-router-dom';
// import { useState,useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { setLoginStatus } from '../../redux/slice/loginSlice';
import { useEffect } from 'react';
import axios from 'axios';
import { API } from '@/apis/config';

export default function Header() {
  const IsLogged = useSelector((state) => state.login.isLogged);
  const dispatch = useDispatch();

  useEffect(() => {
    if (localStorage.length >= 3) {
      dispatch(setLoginStatus(true));
    } else {
      dispatch(setLoginStatus(false));
    }
  });

  const Clear = () => {
    let fcm = localStorage.getItem('fcm_token');
    if (localStorage.getItem('fcm_token') === null) {
      fcm = '';
    }
    axios
      .post(
        `${API.USER}/logout`,
        { fcm_token: fcm },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('access_token')}`,
          },
        },
      )
      .then((res) => {
        console.log(fcm);
        localStorage.clear();
        dispatch(setLoginStatus(false));
        alert('로그아웃 되었습니다.');
      });
  };
  const id = localStorage.getItem('user_id');

  return (
    <div className="Header">
      <nav className="py-6">
        <div className="flex items-center justify-between text-lg">
          <Link
            to={`/`}
            className="font-ImcreSoojin text-3xl text-main2 hover:text-main1"
          >
            혼자랑
          </Link>
          <div className="flex space-x-12 font-ImcreSoojin">
            <NavLink
              to="/market"
              className={({ isActive }) =>
                isActive
                  ? 'text-gray5 font-bold'
                  : 'text-gray3 hover:text-gray5 hover:font-bold'
              }
            >
              장터
            </NavLink>
            <NavLink
              to="/board"
              className={({ isActive }) =>
                isActive
                  ? 'text-gray5 font-bold'
                  : 'text-gray3 hover:text-gray5 hover:font-bold'
              }
            >
              게시판
            </NavLink>
            <NavLink
              to="/webrtc"
              className={({ isActive }) =>
                isActive
                  ? 'text-gray5 font-bold'
                  : 'text-gray3 hover:text-gray5 hover:font-bold'
              }
            >
              화상
            </NavLink>
            <NavLink
              to="/map"
              className={({ isActive }) =>
                isActive
                  ? 'text-gray5 font-bold'
                  : 'text-gray3 hover:text-gray5 hover:font-bold'
              }
            >
              지도
            </NavLink>
            <NavLink
              to="/chatting"
              className={({ isActive }) =>
                isActive
                  ? 'text-gray5 font-bold'
                  : 'text-gray3 hover:text-gray5 hover:font-bold'
              }
            >
              채팅
            </NavLink>
          </div>
          <div className="flex space-x-4">
            <button type="button" className="">
              <Link
                to={`/mypage/${id}`}
                className="font-semibold font-ImcreSoojin text-lg"
              >
                마이페이지
              </Link>
            </button>
            {IsLogged ? (
              <button type="button" onClick={Clear}>
                <Link
                  to="/"
                  className="font-semibold font-ImcreSoojin text-lg mx-4"
                >
                  로그아웃
                </Link>
              </button>
            ) : (
              <button type="button">
                <Link
                  to="/login"
                  className="font-semibold font-ImcreSoojin text-lg mx-4"
                >
                  로그인
                </Link>
              </button>
            )}
          </div>
        </div>
      </nav>
    </div>
  );
}
