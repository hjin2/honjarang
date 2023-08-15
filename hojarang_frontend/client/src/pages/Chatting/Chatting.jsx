import ChatList from "@/components/Chatting/ChatList"
import Chat from "@/components/Chatting/Chat";
import { useState, useEffect } from "react";
import axios from 'axios'


export default function Chatting() {
  const [Lists, setLists] = useState([]);
  const [chatId, setChatId] = useState(0)


  useEffect(() => {
    const access_token = localStorage.getItem('access_token');
    axios.get(`${import.meta.env.VITE_APP_API}/api/v1/chats`, {
      headers: {
        'Authorization' : `Bearer ${access_token}`
      }
    })
    .then((res) => {
      console.log(res.data);
      setLists(res.data);
    });
    
  }, []);


  return (
    <div className="flex">
      <div className="w-3/5 border border-solid border-black rounded-md">
          {Lists.length ? Lists.map(list => (
            <ChatList list={list} key={list.id} setChatId={setChatId}/> // ChatList에서 setKey 대신 handleChat2Open 호출
            )) : <div>아직 생성된 채팅방이 없습니다.</div>}
      </div>
      {chatId !== 0 ? (
        <Chat chatId={chatId}/>
      ):(null)}
    </div>
  )
}
