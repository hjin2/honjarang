import ChatList from "../../components/Chatting/ChatList"
import { useState, useEffect } from "react";
import axios from 'axios'

export default function Chatting() {
  const [Lists, setLists] = useState([
    {id: 1, title: 'test'}
  ]);
  const [socket, setSocket] = useState(null);
  const [stomp, setStomp] = useState(null)

  useEffect(() => {
    const access_token = localStorage.getItem('access_token');
    axios.get('http://honjarang.kro.kr:30000/api/v1/chats', {
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
    <div className="absolute w-3/5 h-3/5">
        {Lists.length ? Lists.map(list => (
          <ChatList list={list} key={list.id} /> // ChatList에서 setKey 대신 handleChat2Open 호출
        )) : <div>아직 생성된 채팅방이 없습니다.</div>}
    </div>
  )
}
