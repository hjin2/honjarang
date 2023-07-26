import { Link } from 'react-router-dom';

export default function Header() {
  return (
    <nav className="py-6 px-12">
      <div className="flex items-center justify-between">
        <Link to="/" className="text-2xl text-main2 hover:text-main1">혼자랑</Link>
        <div className="flex space-x-12">
          <Link to='/board' className="text-gray3 hover:text-gray5">게시판</Link>
          <Link to='/market' className="text-gray3 hover:text-gray5">장터</Link>
          <Link to='/webrtc' className="text-gray3 hover:text-gray5">화상</Link>
          <Link to='/map' className="text-gray3 hover:text-gray5">지도</Link>
          <Link to='/chatting' className="text-gray3 hover:text-gray5">채팅</Link>
        </div>
        <Link to='/login' className="text-gray3 hover:text-gray5">Login</Link>
      </div>
    </nav>
  )
}
