import React, { useState, useRef, useEffect } from 'react';
import { useParams } from 'react-router';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { useNavigate } from 'react-router';
import axios from 'axios';
import Talks from '@/components/Chatting/Talks';
import { API } from '@/apis/config';
import { useLocation } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowLeft, faPaperPlane } from '@fortawesome/free-solid-svg-icons';


const Chat = () => {
  const location = useLocation()
  const [Nickname, setNickname] = useState('')
  const params = useParams();
  const Key = params["id"];
  const navigate = useNavigate();
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([]);
  const [socket, setSocket] = useState(null);
  const [stomp, setStomp] = useState(null);
  const [image, setImage] = useState("")
  const title =location.state.title
  const token = localStorage.getItem('access_token');

  const stompClientRef = useRef(null);

  const connect = () => {
    console.log(title)
    const serverAddress = 'https://honjarang.kro.kr/chat';
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
      if (!prevMessages.includes(message)) {
        return [...prevMessages, message];
        
      }

      return prevMessages;
    });
  };

  const sendMessage = () => {
    const messageToSend = {
      room_id: Key,
      content: message,
      nickname: Nickname,
      profile_image_url : image
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
    const cleanup = () => {
      if (stompClientRef.current) {
        console.log('Attempting to disconnect Stomp...');
        stompClientRef.current.disconnect(() => {
          console.log('Stomp disconnected.');
          if (socket) {
            socket.onclose = (event) => {
              console.log('Socket closed.', event);
              // 원하는 함수 실행
            };
            socket.close();
          } else {
            // 원하는 함수 실행
          }
        });
      } else if (socket) {
        console.log('Closing socket...');
        socket.onclose = (event) => {
          console.log('Socket closed.', event);
          // 원하는 함수 실행
        };
        socket.close();
      } else {
        // 원하는 함수 실행
      }
    };

    // 컴포넌트가 언마운트될 때 실행될 함수 등록
    return cleanup;
  }, []);


  useEffect(() => {
    connect();
  }, []);

  const onKeyEnter = (e) => {
    if (e.key === 'Enter') {
      sendMessage();
    }
  };

  useEffect(() => {
    const id = localStorage.getItem('user_id')
    axios.get(`${API.USER}/info`,
      {
        params : {id : id},
        headers : {'Authorization' : `Bearer ${token}`}
      },
      )
      .then(function(response){
        console.log(response.data)
        setNickname(response.data.nickname)
        setImage(response.data.profile_image)
      })
      .catch(function(error){
        console.log(error)
      })
  },[]);

  return (
    <div className="w-5/12 h-screen flex flex-col m-auto">
        <div className='flex space-x-5 mb-5'>
          <button onClick={handleBack}>
            <FontAwesomeIcon icon={faArrowLeft} style={{color: "#000000",}} />
          </button>
          <div className="text-lg ">{title}</div>
        </div>  
        <Talks messages={messages} id={Key} Nickname = {Nickname}/>
        <div className="py-2 px-4 border-t border-gray-300 flex justify-around">
          <input type="text" id="message" placeholder="채팅..." value={message} onChange={(e) => setMessage(e.target.value)} className="border rounded p-2 w-10/12 focus:outline-main2" onKeyDown={onKeyEnter} />
          <button onClick={sendMessage} className="bg-main1 w-10 rounded-lg">
            <FontAwesomeIcon icon={faPaperPlane} style={{color: "#000000",}} />
          </button>
        </div>
    </div>
  );
};

export default Chat;
