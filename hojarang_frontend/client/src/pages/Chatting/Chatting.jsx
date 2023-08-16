import ChatList from "../../components/Chatting/ChatList"
import Chat from "@/components/Chatting/ChatChat";
import { useState, useEffect } from "react";
import axios from 'axios'


export default function Chatting() {
  const [Lists, setLists] = useState([]);
  const [chatId, setChatId] = useState(0)
  const [title, setTitle] = useState("")



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

  useEffect(() => {
    if(chatId!==0){
      let firstTitle = ""
      for(let i = 0 ; i<Lists.length; i++){
        if(Lists[i].id == chatId){
          firstTitle=Lists[i].name
        }
      }
      if(firstTitle.includes("공동")){
        setTitle(firstTitle)
      }
      else{
        const splitTitle = firstTitle.split('&')[1]
        setTitle(splitTitle.split("1")[0])
      }
    }
  },[chatId])


  return (
    <div className="flex w-4/5 mx-auto h-sreen bg-main4 rounded-md">
      <div className="w-2/5 border border-solid border-black  border-r-0 rounded-md h-full">
          {Lists.length ? Lists.map(list => (
            <div key={list.id} className="hover:bg-main1 active:bg-main1 border-b border-solid border-gray-300 cursor-pointer">
              <ChatList list={list}  setChatId={setChatId}/>
            </div>
            )) : <div>아직 생성된 채팅방이 없습니다.</div>}
      </div>
      {chatId !== 0 ? (
        <Chat chatId={chatId} setChatId={setChatId} title={title}/>
      ):(
        <div className="w-3/5 h-12/12 flex flex-col border-2 rounded-lg bg-white"></div>
      )}
    </div>
  )
}
