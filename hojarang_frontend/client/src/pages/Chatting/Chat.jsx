import React, { useState, useRef, useEffect } from 'react';
import { useParams } from 'react-router';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { useNavigate } from 'react-router';
import axios from 'axios';
import Talks from '@/components/Chatting/Talks';

const Chat = () => {
  const params = useParams();
  const Key = params["id"];
  const navigate = useNavigate();
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([]);
  const [socket, setSocket] = useState(null);
  const [stomp, setStomp] = useState(null);

  const token = localStorage.getItem('access_token');

  const stompClientRef = useRef(null);

  const connect = () => {
    const serverAddress = `https://honjarang.kro.kr/chat`;
    const socket = new SockJS(serverAddress);
    const stompClient = Stomp.over(socket);

    stompClient.connect('guest', 'guest', (frame) => {
      stompClient.subscribe(`/topic/room.${Key}`, (message) => {
        console.log(message);
        showMessage(JSON.parse(message.body));
      });
      stompClient.send(`/app/chat/connect.${Key}`, {}, JSON.stringify({ token: token }));

      stompClientRef.current = stompClient;
      setSocket(socket);
      setStomp(stompClient);
    });
  };

  const showMessage = (message) => {
    console.log(message);
    setMessages((prevMessages) => {
      if (!prevMessages.includes(`${message.id}: ${message.content}`)) {
        return [...prevMessages, `${message.id}: ${message.content}`];
      }
      return prevMessages;
    });
  };

  const sendMessage = () => {
    const messageToSend = {
      room_id: Key,
      content: message
    };

    stomp.send(`/app/chat/message.${Key}`, {}, JSON.stringify(messageToSend));
    setMessage('');
    
    // Scroll to bottom after sending message
    scrollToBottom();
  };

  const scrollToBottom = () => {
    const chatArea = document.getElementById('ChatArea');
    chatArea.scrollTop = chatArea.scrollHeight;
  };

  const handleBack = () => {
    if (stompClientRef.current) {
      console.log('Attempting to disconnect Stomp...');
      stompClientRef.current.disconnect(() => {
        console.log('Stomp disconnected.');
        if (socket) {
          socket.onclose = (event) => {
            console.log('Socket closed.', event);
            navigate('/chatting');
          };
          socket.close();
        } else {
          navigate('/chatting');
        }
      });
    } else if (socket) {
      console.log('Closing socket...');
      socket.onclose = (event) => {
        console.log('Socket closed.', event);
        navigate('/chatting');
      };
      socket.close();
    } else {
      navigate('/chatting');
    }
  };

  useEffect(() => {
    connect();
  }, []);

  const onKeyEnter = (e) => {
    if (e.key === 'Enter') {
      sendMessage();
    }
  };

  return (
    <div className="h-screen flex flex-col">
      <Talks messages={messages} id={Key} />
      <div className="py-2 px-4 border-t border-gray-300">
        <input type="text" id="message" value={message} onChange={(e) => setMessage(e.target.value)} className="border rounded p-2 w-full" onKeyDown={onKeyEnter} />
        <button onClick={sendMessage} className="mt-2 bg-blue-500 text-white rounded px-4 py-2 hover:bg-blue-600">전송</button>
      </div>
      <button onClick={handleBack} className="bg-red-500 text-white rounded px-4 py-2 mt-2 mx-4 self-center hover:bg-red-600">뒤로가기</button>
    </div>
  );
};

export default Chat;
