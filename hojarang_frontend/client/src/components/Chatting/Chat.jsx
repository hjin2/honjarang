import React, { useState, useRef, useEffect } from 'react';
import { useParams } from 'react-router';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { useNavigate } from 'react-router';
import axios from 'axios';
import Talks from '@/components/Chatting/Talks';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPaperPlane } from '@fortawesome/free-regular-svg-icons';

const Chat = ({chatId, setChatId, title}) => {
  const [Nickname, setNickname] = useState('')
  const params = useParams();
  const Key = chatId;
  const navigate = useNavigate();
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([]);
  const [socket, setSocket] = useState(null);
  const [stomp, setStomp] = useState(null);

  const onChange = (e) => {
    setMsg(e.target.value)
  }

  
  return (
    <div className="w-3/5 h-12/12 flex flex-col border-2 rounded-lg bg-white">
      <div className="p-2"style={{height:"10%"}}>{title}</div>
      <hr />
      <div style={{height:"80%"}}>
        <Talks messages={messages} id={Key} Nickname = {Nickname} chatKey={Key} setChatId={setChatId}/>
      </div>
      <div className="border-t border-gray-300 flex justify-around h-1/12 items-center bg-main4" style={{height:"10%"}}>
        <input type="text" id="message" value={message} onChange={(e) => setMessage(e.target.value)} className="h-10 border rounded-full p-2 w-10/12 focus:outline-main2" onKeyDown={onKeyEnter} />
        <button onClick={sendMessage} className="bg-main1 rounded-full w-10 h-10 flex justify-center items-center">
          <FontAwesomeIcon icon={faPaperPlane} style={{color: "#000000",}} />
        </button>
      </div>
      {/* <button onClick={handleBack} className="bg-red-500 text-white rounded px-4 py-2 mt-2 mx-4 self-center hover:bg-red-600">뒤로가기</button> */}
    </div>
  )
}

export default Chat