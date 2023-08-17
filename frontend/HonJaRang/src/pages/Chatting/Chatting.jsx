import ChatList from "../../components/Chatting/ChatList"
import { useState, useEffect } from "react";
import axios from 'axios'
import { API } from "@/apis/config";
import highlight from "@/assets/Highlight_green.png"


export default function Chatting() {
  const [Lists, setLists] = useState([]);



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
    
  }, []);



  return (
    <div className="w-7/12 mx-auto mt-4 h-screen">
        <div className="text-lg p-4 font-bold w-24 text-center mb-4" style={{backgroundImage : `url(${highlight})`, backgroundSize: "cover"}}>채팅</div>
        <div className="mx-auto rounded-md border-2" style={{height:"70%", overflow:"auto"}}>
          {Lists.length ? Lists.map(list => (
            <div key={list.id} className="hover:bg-main4 p-1">
              <ChatList list={list}/>
        </div>
            )) : <div>아직 생성된 채팅방이 없습니다.</div>}
        </div>
    </div>
  )
}
