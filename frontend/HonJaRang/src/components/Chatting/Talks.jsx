import React, { useState, useRef, useEffect } from 'react';
import axios from 'axios';
import { API } from '@/apis/config';

function Talks({ messages, id, Nickname, chatKey, setChatId }) {
  const token = localStorage.getItem('access_token');
  const chatAreaRef = useRef(null);
  const [isLoading, setIsLoading] = useState(false);
  const [pages, setPages] = useState(1);
  const [msg, setMsg] = useState([]);
  const [hour, setHour] = useState('');
  const [minute, setMinute] = useState('');
  const [isChat, setIsChat] = useState(false);
  const Key = id;
  const isInitialLoad = useRef(true);

  useEffect(() => {
    timeline();
  }, []);

  useEffect(() => {
    getChats();
  }, [chatKey]);

  useEffect(() => {
    if (chatAreaRef.current) {
      chatAreaRef.current.addEventListener('scroll', handleChatAreaScroll);
    }
    return () => {
      if (chatAreaRef.current) {
        chatAreaRef.current.removeEventListener('scroll', handleChatAreaScroll);
      }
    };
  }, []);

  const timeline = () => {
    const time = new Date();
    setHour(time.getHours());
    setMinute(time.getMinutes());
  };

  const getChats = () => {
    axios
      .get(`${API.CHATS}/${Key}/page`, {
        params: {
          size: 30,
        },
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        if (res.data > 0) {
          setIsChat(true);
          setPages(res.data);
          loadChats(res.data);
        } else {
          setIsChat(false);
        }
      });
  };

  const loadChats = (p) => {
    const page = p;
    axios
      .get(`${API.CHATS}/${Key}`, {
        params: {
          page: page,
          size: 30,
        },
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        setMsg(res.data);
        if (res.data.length < 30 && p > 1) {
          loadMoreChat2(p);
        }
      });
  };

  const loadMoreChat = (currentPage) => {
    setIsLoading(true);
    axios
      .get(`${API.CHATS}/${Key}`, {
        params: {
          page: currentPage,
          size: 30,
        },
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        if (res.data.length > 0) {
          setMsg((prevMsgs) => [...res.data, ...prevMsgs]);
          setPages(currentPage - 1);
        }
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  const loadMoreChat2 = (p) => {
    axios
      .get(`${API.CHATS}/${Key}`, {
        params: {
          page: p - 1,
          size: 30,
        },
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        if (res.data.length > 0) {
          setMsg((prevMsgs) => [...res.data, ...prevMsgs]);
          setPages(p - 1);
        }
      });
  };

  const handleChatAreaScroll = () => {
    const chatArea = chatAreaRef.current;
    const scrolledFromTop = chatArea.scrollTop;
    if (scrolledFromTop >= 0 && !isLoading && pages !== 1) {
      loadMoreChat(pages);
    }
  };

  useEffect(() => {
    scrollToBottomOnInitialLoad();
  }, []);

  const scrollToBottomOnInitialLoad = () => {
    if (chatAreaRef.current && isInitialLoad.current) {
      chatAreaRef.current.scrollTop = chatAreaRef.current.scrollHeight;
      isInitialLoad.current = false; // 한 번만 실행하기 위해 상태 업데이트
    }
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const scrollToBottom = () => {
    if (chatAreaRef.current) {
      chatAreaRef.current.scrollIntoView({ behavior: 'smooth', block: 'end' });
    }
  };

  return (
    <div
      id="ChatArea"
      ref={chatAreaRef}
      className="flex flex-col overflow-y-auto flex-grow p-4"
    >
      {msg.map((ms, idx) => (
        <div
          key={idx}
          className={
            ms.nickname === Nickname ? 'flex flex-row justify-end' : 'm-1'
          }
        >
          {ms.nickname === Nickname ? (
            <div key={idx} className="flex flex-row justify-end">
              <div className="flex items-end py-3 text-gray3 text-sm">
                {ms.created_at?.slice(11, 16)}
              </div>
              <div className="whitespace-pre-line float-right text-right inline-block border rounded-lg bg-main3 bg-opacity-50 px-2 py-1 m-2">
                {ms.content}
              </div>
            </div>
          ) : (
            <div key={idx} className="m-1">
              <div className="flex felx-row">
                <img src={ms.profile_image_url} alt="" className="h-10 w-10" />
                <div className="ml-1">
                  <p className="text-sm">{ms.nickname}</p>
                  <div className="flex">
                    <div className="whitespace-pre-line inline-block text-left border rounded-lg bg-main4 bg-opacity-50 px-2 py-1 m-1">
                      {ms.content}
                    </div>
                    <div className="flex items-end py-1 text-gray3 text-sm">
                      {ms.created_at?.slice(11, 16)}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          )}
        </div>
      ))}

      {messages.map((msg, idx) => (
        <div
          key={idx}
          className={
            msg.nickname === Nickname ? 'flex flex-row justify-end' : 'm-1'
          }
        >
          {msg.nickname === Nickname ? (
            <div key={idx} className="flex flex-row justify-end">
              <div className="flex items-end py-3 text-gray3 text-sm">
                {hour}:{minute}
              </div>
              <div className="whitespace-pre-line float-right text-right inline-block border rounded-lg bg-main3 bg-opacity-50 px-2 py-1 m-2">
                {msg.content}
              </div>
            </div>
          ) : (
            <div key={idx} className="m-1">
              <div className="flex felx-row">
                <img src={msg.profile_image_url} alt="" className="h-10 w-10" />
                <div className="ml-1">
                  <p className="text-sm">{msg.nickname}</p>
                  <div className="flex">
                    <div className="whitespace-pre-line inline-block text-left border rounded-lg bg-main4 bg-opacity-50 px-2 py-1 m-1">
                      {msg.content}
                    </div>
                    <div className="flex items-end py-1 text-gray3 text-sm">
                      {hour}:{minute}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          )}
        </div>
      ))}
    </div>
  );
}

export default Talks;
