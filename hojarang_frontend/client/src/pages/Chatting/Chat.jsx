import React, { useState, useRef, useEffect } from 'react';
import { useParams } from 'react-router';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { useNavigate } from 'react-router';
import axios from 'axios';

const Chat = () => {
  const params = useParams();
  const Key = params["id"];
  const navigate = useNavigate();
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([]);
  const [socket, setSocket] = useState(null);
  const [stomp, setStomp] = useState(null);
  const [pages, setPages] = useState(1);
  const [msg, setMsg] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const token = localStorage.getItem('access_token');

  const stompClientRef = useRef(null);
  const chatAreaRef = useRef(null);

  const connect = () => {
    const serverAddress = 'http://honjarang.kro.kr:30000/chat';
    const socket = new SockJS(serverAddress);
    stompClientRef.current = Stomp.over(socket);

    setSocket(socket);
    setStomp(stompClientRef);

    stompClientRef.current.connect('guest', 'guest', (frame) => {
      stompClientRef.current.subscribe(`/topic/room.${Key}`, (message) => {
        console.log(message);
        showMessage(JSON.parse(message.body));
      });
      stompClientRef.current.send(`/app/chat/connect.${Key}`, {}, JSON.stringify({ token: token }));
    });
  };
  const showMessage = (message) => {
    console.log(message);
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
    axios.get(`http://honjarang.kro.kr:30000/api/v1/chats/${Key}/page`, {
      params: {
        size: 30
      },
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
      .then((res) => {
        console.log(res.data);
        setPages(res.data);
      })
      .then(() => {
        axios.get(`http://honjarang.kro.kr:30000/api/v1/chats/${Key}`, {
          params: {
            page: pages,
            size: 30
          },
          headers: {
            "Authorization": `Bearer ${token}`
          }
        })
          .then((res) => {
            console.log(res.data);
            setMsg(res.data);

            // Scroll to bottom after loading messages
            chatAreaRef.current.scrollTop = chatAreaRef.current.scrollHeight;
          });
      });
  };

  const loadMoreChat = () => {
    setIsLoading(true);
    const currentPage = pages;
    axios.get(`http://honjarang.kro.kr:30000/api/v1/chats/${Key}`, {
      params: {
        page: currentPage + 1,
        size: 30
      },
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
      .then((res) => {
        if (res.data.length > 0) {
          setMsg((prevMsgs) => [...prevMsgs, ...res.data]);
          setPages(currentPage + 1);
        }
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

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

  useEffect(() => {
    connect();
    getChats();
    // getChat();
  }, []);

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);

    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, [msg]);

  const handleScroll = () => {
    const isScrollNearTop = window.scrollY <= 100;
    if (isScrollNearTop && !isLoading) {
      loadMoreChat();
    }
  };

  return (
    <div className="h-screen flex flex-col">
      <div id="chatArea" ref={chatAreaRef} className="overflow-y-auto flex-grow border border-gray-300 p-4">
        {messages.map((msg, index) => (
          <div key={index}>{msg.content}</div>
        ))}
        {msg.map((ms, idx) => (
          <div key={idx}>{ms.nickname} : {ms.content}</div>
        ))}
      </div>
      <div className="py-2 px-4 border-t border-gray-300">
        <input type="text" id="message" value={message} onChange={(e) => setMessage(e.target.value)} className="border rounded p-2 w-full" />
        <button onClick={sendMessage} className="mt-2 bg-blue-500 text-white rounded px-4 py-2 hover:bg-blue-600">전송</button>
      </div>
      <button onClick={Back} className="bg-red-500 text-white rounded px-4 py-2 mt-2 mx-4 self-center hover:bg-red-600">뒤로가기</button>
    </div>
  );
};

export default Chat;
