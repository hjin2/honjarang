import ChatList from "../../components/Chatting/ChatList"
import { useState, useEffect } from "react";
import axios from 'axios'
import { API } from "@/apis/config";
import highlight from "@/assets/Highlight_green.png"
import { useRef } from "react";
import sytles from "@/pages/Chatting/scrollbar.module.css"


export default function Chatting() {
  const [Lists, setLists] = useState([]);
  const [showScrollbar, setShowScrollbar] = useState(false);
  const [prevScrollY, setPrevScrollY] = useState(0);

  const handleScroll = () => {
    const currentScrollY = window.scrollY;

    // 스크롤 방향을 확인하여 스크롤바 보이기/숨기기 조절
    setShowScrollbar(currentScrollY <= prevScrollY);
    setPrevScrollY(currentScrollY);
  };
  const listRef = useRef(null)
  useEffect(() => {
    const access_token = localStorage.getItem('access_token');
    axios.get(`${API.CHATS}`, {
      headers: {
        'Authorization' : `Bearer ${access_token}`
      }
    })
    .then((res) => {
      console.log(res.data);
      setLists(res.data);
    });
    listRef.current?.addEventListener('scroll', handleScroll);
    return () => {
      listRef.current?.removeEventListener('scroll', handleScroll);
    };
    
  }, []);
  



  return (
    <div className="w-6/12 mx-auto mt-4 h-[82vh]">
      <div className="text-lg p-4 font-bold w-24 text-center mb-4" style={{backgroundImage : `url(${highlight})`, backgroundSize: "cover"}}>채팅</div>
        <div className={`mx-auto rounded-lg border-4 border-main4 overflow-y-scroll ${showScrollbar ? '' : 'scrollbar-hide'}`} style={{height:"85%"}} ref={listRef}>
          {Lists.length ? Lists.map(list => (
            <div key={list.id} className="hover:bg-main4 p-1">
              <ChatList list={list}/>
        </div>
            )) : <div>아직 생성된 채팅방이 없습니다.</div>}
        </div>
    </div>
  )
}
