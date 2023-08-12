import ChatList from "../../components/Chatting/ChatList"
import { useState, useEffect } from "react";
import axios from 'axios'
import { useNavigate } from "react-router";

export default function Chatting() {
  const [Lists, setLists] = useState([]);
  const [socket, setSocket] = useState(null);
  const [stomp, setStomp] = useState(null)

  const navigate = useNavigate()

  const access_token = localStorage.getItem('access_token')

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

  const CreateChat = () => {
    axios.post('http://honjarang.kro.kr:30000/api/v1/chats/one-to-one',
    {target_id: 5},
    {headers: {
      'Authorization': `Bearer ${access_token}`
    }})
    .then((res) => {
      console.log(res)
    })
  }

  const onPush = () => {
    navigate('/push')
  }

  return (
    <div className="absolute w-3/5 h-3/5">
        {Lists.length ? Lists.map(list => (
          <ChatList list={list} key={list.id} /> // ChatList에서 setKey 대신 handleChat2Open 호출
        )) : <div>아직 생성된 채팅방이 없습니다.</div>}
        <button onClick={CreateChat}>채팅 만들기</button>
        <br />
        <button onClick={onPush}>푸시알림 확인하기</button>
    </div>
  )
}
