import React, { useState, useRef, useEffect } from 'react';
import { useParams } from 'react-router';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { useNavigate } from 'react-router';
import axios from 'axios';

const Chat = () => {
  const params = useParams()
  const Key = params["id"]
  const navigate = useNavigate()
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([]);
  const [socket, setSocket] = useState(null);
  const [stomp, setStomp] = useState(null)
  const [pages, setPages] = useState(0)
  const [msg, setMsg] = useState([])
  const token = localStorage.getItem('access_token')

  const stompClientRef = useRef(null);
  useEffect(() => {
    connect()
    getChats()
    getChat()
  },[])

  const connect = () => {
    const serverAddress = 'http://honjarang.kro.kr:30000/chat';
    const socket = new SockJS(serverAddress);
    stompClientRef.current = Stomp.over(socket);

    setSocket(socket)
    setStomp(stompClientRef)

    stompClientRef.current.connect('guest', 'guest', (frame) => {
      stompClientRef.current.subscribe(`/topic/room.${Key}`, (message) => {
        console.log(message);
        showMessage(JSON.parse(message.body));
      });
      stompClientRef.current.send(`/app/chat/connect.${Key}`, {}, JSON.stringify({ token: token }));
    });
  }

  const showMessage = (message) => {
    console.log(message)
    setMessages((prevMessages) => [...prevMessages, `${message.id}: ${message.content}`]);
  };

  const sendMessage = () => {
    const messageToSend = {
      room_id: Key,
      content: message
    };

    stompClientRef.current.send(`/app/chat/message.${Key}`, {}, JSON.stringify(messageToSend));
    setMessage('');
  };


  const getChats = () => {
    axios.get(`http://honjarang.kro.kr:30000/api/v1/chats/${Key}/page`,{
      params: {
        size:10
      },
      headers: {
        "Authorization" : `Bearer ${token}` 
      }
    })
    .then((res) => {
      console.log(res.data)
      setPages(res.data)
    })
  }

  const getChat = () => {
    axios.get(`http://honjarang.kro.kr:30000/api/v1/chats/${Key}`,{
      params: {
        page:1,
        size:10
      },
      headers: {
        "Authorization" : `Bearer ${token}` 
      }
    })
    .then((res) => {
      console.log(res.data)
      setMsg(res.data)
    })
  }
  
  const getMoreChat = () => {
    let cnt = 0
    cnt ++ 
    let page = pages - cnt
    
    axios.get(`http://honjarang.kro.kr:30000/api/v1/chats/${Key}`,{
      params: {
        page: pages,
        size:10
      },
      headers: {
        "Authorization" : `Bearer ${token}` 
      }
    })
    .then((res) => {
      console.log(res.data)
      setMsg(res.data)
    })
  }
  
  const Back = () => {
  if (stomp && stomp.current) {
    stomp.current.unsubscribe(`/topic/room.${Key}`);
    stomp.current.disconnect();
  }
  
  if (socket) {
    socket.close();
  }

  navigate('/chatting');
};
  return (
    <div>
      <div id="chatArea">
        {msg.map((ms, idx) => (
          <div key={idx}>{ms.nickname} : {ms.content}</div>
        ))}
        {messages.map((msg, index) => (
          <div key={index}>{msg.content}</div>
        ))}
      </div>
      <div>
        <input type="text" id="message" value={message} onChange={(e) => setMessage(e.target.value)} />
        <button onClick={sendMessage}>전송</button>
      </div>
      <button onClick={Back}>뒤로가기</button>
    </div>
  );
};

export default Chat;
