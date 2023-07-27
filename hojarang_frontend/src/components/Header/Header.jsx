import { NavLink, Link } from 'react-router-dom';

export default function Header() {
  return (
    <div className='Header'>
      <nav className="px-12 py-6">
        <div className="flex items-center justify-between">
          <Link to="/" className="text-2xl text-main2 hover:text-main1">혼자랑</Link>
          <div className="flex space-x-12">
            <NavLink 
              to='/board' 
              className={({ isActive }) => 
                isActive ? "text-gray5 font-bold":"text-gray3 hover:text-gray5 hover:font-bold"
                }>게시판
            </NavLink>
            <NavLink 
              to='/market' 
              className={({ isActive }) => 
                isActive ? "text-gray5 font-bold":"text-gray3 hover:text-gray5 hover:font-bold"
                }>장터
            </NavLink>
            <NavLink 
              to='/webrtc' 
              className={({ isActive }) => 
                isActive ? "text-gray5 font-bold":"text-gray3 hover:text-gray5 hover:font-bold"
                }>화상
            </NavLink>
            <NavLink 
              to='/map' 
              className={({ isActive }) => 
                isActive ? "text-gray5 font-bold":"text-gray3 hover:text-gray5 hover:font-bold"
                }>지도
            </NavLink>
            <NavLink 
              to='/chatting' 
              className={({ isActive }) => 
                isActive ? "text-gray5 font-bold":"text-gray3 hover:text-gray5 hover:font-bold"
                }>채팅
            </NavLink>
          </div>
          <div className='flex space-x-4'>
            <button type="button" className="">
              <Link to='/mypage/:nickname'>
                <svg className="h-8 w-8"  fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
                </svg>
              </Link>
            </button>
            <button type="button">
              <Link to='/login' className="text-sm px-4 py-2 border rounded text-white bg-black">Login</Link>
            </button>
          </div>
        </div>
      </nav>
    </div>
  )
}
