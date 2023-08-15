import axios from "axios";
import React, {useState, useRef, useEffect} from "react";


function Talks({messages, id, Nickname}) {
  const [isLoading, setIsLoading] = useState(false);
  const [pages, setPages] = useState(1);
  const [msg, setMsg] = useState([]);

  const token = localStorage.getItem('access_token');
  const chatAreaRef = useRef(null);
  const isInitialLoad = useRef(true);
  const Key = id

  
  
  useEffect(() => {
    getChats()
  },[])
 
  const getChats = () => {
    axios.get(`${import.meta.env.VITE_APP_API}/api/v1/chats/${Key}/page`, {
      params: {
        size: 30
      },
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
    .then((res) => {
      console.log(res.data)
      setPages(res.data);
      loadChats(res.data);
    });
  };
  
  const loadChats = (p) => {
    const page = p
    axios.get(`${import.meta.env.VITE_APP_API}/api/v1/chats/${Key}`, {
      params: {
        page: page,
        size: 30
      },
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
    .then((res) => {
      console.log(res.data);
      setMsg(res.data);
      if (res.data.length < 30) {
        console.log(pages)
        if (p >1) {
          loadMoreChat2(p)
        }
      }
      
      if (isInitialLoad.current) {
        isInitialLoad.current = false;
        // Scroll to bottom only on initial load
        chatAreaRef.current.scrollTop = chatAreaRef.current.scrollHeight;
      }
    });
  };
  
  useEffect(() => {
    
    if (chatAreaRef.current) {
      chatAreaRef.current.addEventListener('scroll', handleChatAreaScroll);
    }
    
    return () => {
    if (chatAreaRef.current) {
      chatAreaRef.current.removeEventListener('scroll', handleChatAreaScroll);
    }
  };
})



const loadMoreChat = () => {
  
  setIsLoading(true);
  const currentPage = pages;
  axios.get(`${import.meta.env.VITE_APP_API}/api/v1/chats/${Key}`, {
    params: {
      page: currentPage - 1,
      size: 30
    },
    headers: {
      "Authorization": `Bearer ${token}`
    }
  })
  .then((res) => {
    if (res.data.length > 0) {
      console.log(res.data)
        setMsg((prevMsgs) => [...res.data, ...prevMsgs]);
        setPages(currentPage - 1);
      }
    })
    .finally(() => {
      setIsLoading(false);
    });
  };
  
  
  const loadMoreChat2 = (p) => {
    
    const currentPage = p;
    axios.get(`${import.meta.env.VITE_APP_API}/api/v1/chats/${Key}`, {
      params: {
        page: currentPage - 1,
        size: 30
      },
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
    .then((res) => {
      if (res.data.length > 0) {
        console.log(res.data)
        setMsg((prevMsgs) => [...res.data, ...prevMsgs]);
        setPages(currentPage - 1);
      }
    })
  };
  
  
  const handleChatAreaScroll = () => {
    const chatArea = chatAreaRef.current;
    
    // Calculate how far the user has scrolled from the top
    const scrolledFromTop = chatArea.scrollTop;
    
    // Check if the user has scrolled to the very top
    if (scrolledFromTop >= 0 && !isLoading && !isInitialLoad.current && pages !== 1) {
      loadMoreChat();
    }
  };

  useEffect(() => {
    scrollToBottomOnInitialLoad();
  }, []);

  const scrollToBottomOnInitialLoad = () => {
    if (chatAreaRef.current) {
      chatAreaRef.current.scrollTop = chatAreaRef.current.scrollHeight;
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


return(
  <div id="ChatArea" ref={chatAreaRef} className="flex flex-col overflow-y-auto flex-grow border border-gray-300 p-4">
        {/* <div className="float-right">
          <span>닉네임 들어감</span>
          <div>
            메시지 들어감
          </div>
        </div>
        <div className="left">
          <span>닉네임 들어감</span>
          <div>
            메시지 들어감
          </div>
        </div> */}
        {msg.map((ms, idx) => (
          (ms.nickname === Nickname?
            <div key={idx}>
          <div className = "float-right text-right inline-block border rounded-lg bg-main3 bg-opacity-50 px-2 py-1 m-3" >
             {ms.content}
         </div>
   
          </div>
       : 
       <div key={idx} className="m-2">
         <span>{ms.nickname}</span>
         <br />
       <div className = "inline-block text-left border rounded-lg bg-main4 bg-opacity-50 px-2 py-1 m-1" >
          {ms.content}
      </div>

       </div>
          )))}
          {messages.map((msg, index) => (
                (msg.nickname === Nickname?
                  <div key={index}>
                <div className = "float-right text-right inline-block border rounded-lg bg-main3 bg-opacity-50 px-2 py-1 m-3" >
                   {msg.content}
               </div>
         
                </div>
             : 
             <div key={index} className="m-2">
               <span>{msg.nickname}</span>
               <br />
             <div className = "inline-block text-left border rounded-lg bg-main4 bg-opacity-50 px-2 py-1 m-1" >
                {msg.content}
            </div>
      
             </div>
                )
              ))}
  </div>
)
}

export default Talks