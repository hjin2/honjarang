import ChatList from "../../components/Chatting/ChatList"
import { useState, useEffect } from "react";
import axios from 'axios'
import { API } from "@/apis/config";


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
    <div className="flex w-4/5 mx-auto h-sreen bg-main4 rounded-md border-2">
      <div className="w-2/5 rounded-md  h-full">
        <div className="bg-white p-4">채팅목록</div>
          {Lists.length ? Lists.map(list => (
            <div key={list.id} className="hover:bg-main1 active:bg-main1 border border-gray1 rounded-md cursor-pointer">
              <ChatList list={list}/>
            </div>
            )) : <div>아직 생성된 채팅방이 없습니다.</div>}
      </div>
    </div>
  )
}
