import UserVideoComponent from "@/components/WebRTC/UserVideoComponent"
import { useState, useRef, useCallback, useEffect } from "react"
import { OpenVidu } from "openvidu-browser";
import { createSession, createToken } from "@/components/WebRTC/Util";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useParams } from "react-router-dom";
import { handleSession } from "@/redux/slice/sessionSlice";

export default function FreeChat() {
  const [session, setSession] = useState(undefined)
  const [subscribers, setSubscribers] = useState([])
  const [currentVideoDevice, setCurrentVideoDevice] = useState(null);
  const [mySessionId, setMySessionId] = useState(useParams().sessionid)
  const [publisher, setPublisher] = useState(undefined);
  const nickname = useSelector((state) => state.userinfo.nickname)
  const navigate = useNavigate()
  const OV = useRef(new OpenVidu)
  const asession = useSelector((state) => state.session.session)
  const [publishVideo, setPublishVideo] = useState(true)
  const [publishAudio, setPublishAudio] = useState(true)
  const [chatMessages, setChatMessages] = useState([]);
  const [isChatOpen, setIsChatOpen] = useState(false)

  const handleChatting = () =>{
    setIsChatOpen(!isChatOpen)
  }

  const leaveSession = useCallback(() => {
    // Leave the session
    if (session) {
      session.disconnect();
    }

    // Reset all states and OpenVidu object
    OV.current = new OpenVidu();
    setSession(undefined);
    setSubscribers([]);
    setPublisher(undefined);
    navigate('/webrtc')
  }, [session]);


  const getToken = useCallback(async () => {
    return createSession(mySessionId).then(sessionId =>
        createToken(sessionId),
    );
  }, [mySessionId]);
  
  const toggleAudio= () => {
    setPublishAudio(!publishAudio)
    publisher.publishAudio(!publishAudio)
  }

  const toggleVideo= () => {
    console.log(publishVideo) 
    setPublishVideo(!publishVideo)
    publisher.publishVideo(!publishVideo)
  }

  const sendChatMessage = (message) => {
    const newMessage = {
      text: message,
      sender: nickname,
    };
  
    // Push the new message to the chatMessages state
    setChatMessages((prevMessages) => [...prevMessages, newMessage]);
    console.log(chatMessages)
    
    // Broadcast the message to all subscribers
    session.signal({
      type: 'chat',
      data: JSON.stringify(newMessage),
    });
  };

  const joinSession = useCallback(() => {
    const mySession = OV.current.initSession();

    mySession.on('streamCreated', (event) => {
      const subscriber = mySession.subscribe(event.stream, undefined);
      setSubscribers((subscribers) => [...subscribers, subscriber]);
    });

    mySession.on('streamDestroyed', (event) => {
      deleteSubscriber(event.stream.streamManager);
    });

    mySession.on('exception', (exception) => {
      console.warn(exception);
    });

    mySession.on('signal:chat', (event) => {
      const newMessage = JSON.parse(event.data);
      setChatMessages((prevMessages) => [...prevMessages, newMessage]);
    });

    setSession(mySession);
  }, []);
//   useEffect(() => {
//     const listenBackEvent = () => {
//       console.log(1)
//       console.log(session)
//     };

//     const unlistenHistoryEvent = history.listen(({ action }) => {
//       if (action === "POP") {
//         listenBackEvent();
//       }
//     });

//     return unlistenHistoryEvent;
//   }, [
//   // effect에서 사용하는 state를 추가
// ]);
  // useEffect(() => {
  //   const handleBackButton = (event) => {
  //     console.log('뒤로가기 버튼이 눌렸습니다.');
  //     leaveSession()
  //   };

  //   window.onpopstate = handleBackButton()

  //   return () => {
  //     // 컴포넌트가 언마운트될 때 이벤트 리스너 해제
  //     // window.removeEventListener('popstate', handleBackButton);
  //   };
  // }, [leaveSession]);

  useEffect(() => {
    joinSession()
  },[])
  
  useEffect(()=>{
    handleSession(session)

  },[session])

  useEffect(() => {
    if (session) {
        // Get a token from the OpenVidu deployment
      getToken().then(async (token) => {
        try {
          await session.connect(token, { clientData: nickname });

          let publisher = await OV.current.initPublisherAsync(undefined, {
            audioSource: undefined,
            videoSource: undefined,
            publishAudio: publishAudio,
            publishVideo: publishVideo,
            resolution: '640x480',
            frameRate: 30,
            insertMode: 'APPEND',
            mirror: true,
          });

          session.publish(publisher);

          const devices = await OV.current.getDevices();
          const videoDevices = devices.filter(device => device.kind === 'videoinput');
          const currentVideoDeviceId = publisher.stream.getMediaStream().getVideoTracks()[0].getSettings().deviceId;
          const currentVideoDevice = videoDevices.find(device => device.deviceId === currentVideoDeviceId);

          setPublisher(publisher);
          setCurrentVideoDevice(currentVideoDevice);
        } catch (error) {
          console.log('There was an error connecting to the session:', error.code, error.message);
        }
      });
    }
  }, [session, nickname]);

  const deleteSubscriber = useCallback((streamManager) => {
    setSubscribers((prevSubscribers) => {
      const index = prevSubscribers.indexOf(streamManager);
      if (index > -1) {
        const newSubscribers = [...prevSubscribers];
        newSubscribers.splice(index, 1);
        return newSubscribers;
      } else {
        return prevSubscribers;
      }
    });
  }, []);

  useEffect(() => {
    const handleBeforeUnload = (event) => {
      leaveSession();
    };
    window.addEventListener('beforeunload', handleBeforeUnload);

    return () => {
        window.removeEventListener('beforeunload', handleBeforeUnload);
    };
  }, [leaveSession]);

  const containerRef = useRef(null)

  const handleScroll = () =>{
    setChatMessages((prevMessages) => [...prevMessages]);
  }


  return (
    <div id="session" className="h-screen p-6">
      <div className="text-center text-5xl text-main1 font-bold" style={{height : "10%"}}>혼자랑</div>
      <div className="flex space-x-5" style={{height : "80%"}}>
      <div id="video-container" className="grid grid-cols-4 gap-4">
        {publisher !== undefined ? (
          <div className="">
            <UserVideoComponent
              streamManager={publisher} />
          </div>
        ) : null}
        {subscribers.map((sub, i) => (
          <div key={sub.id} className="">
            <span>{sub.id}</span>
            <UserVideoComponent streamManager={sub} />
          </div>
        ))}
      </div>
      {isChatOpen ? (
        <div 
          className="chat-container p-6 w-6/12 h-full border-2 rounded-lg"
        >
          <div className="chat-messages"
            ref={containerRef}
            onScroll={handleScroll}
            style={{
              height : "90%",
              overflow : "auto",
            }}
          >
            {chatMessages.map((message, index) => (
              <div key={index} className="chat-message">
                <strong>{message.sender}:</strong> {message.text}
              </div>
            ))}
          </div>
          <div className="chat-input mt-5">
            <input
              className="border-none"
              type="text"
              placeholder="채팅..."
              onKeyDown={(e) => {
                if (e.key === 'Enter') {
                  sendChatMessage(e.target.value);
                  e.target.value = '';
                }
              }}
            />
          </div>
        </div>
      ):(null)}
      </div>
    <div className="flex justify-center space-x-5 mt-5">
      <button className="flex h-8 w-20 bg-main1 rounded-lg text-white justify-center items-center" onClick={toggleAudio}>
        {publishAudio === true ? (
          <div className="flex">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 18.75a6 6 0 006-6v-1.5m-6 7.5a6 6 0 01-6-6v-1.5m6 7.5v3.75m-3.75 0h7.5M12 15.75a3 3 0 01-3-3V4.5a3 3 0 116 0v8.25a3 3 0 01-3 3z" />
            </svg>
            Mute
          </div>
        ):(
          <div>UnMute</div>
        )}
      </button>
      <button className="flex h-8 w-32 bg-main1 rounded-lg text-white justify-center items-center" onClick={toggleVideo}>
        {publishVideo === true ? (
          <div className="flex">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
              <path strokeLinecap="round" d="M15.75 10.5l4.72-4.72a.75.75 0 011.28.53v11.38a.75.75 0 01-1.28.53l-4.72-4.72M4.5 18.75h9a2.25 2.25 0 002.25-2.25v-9a2.25 2.25 0 00-2.25-2.25h-9A2.25 2.25 0 002.25 7.5v9a2.25 2.25 0 002.25 2.25z" />
            </svg>
            Stop Video
          </div>
        ):(
          <div>
            Start Video
          </div>
        )}
      </button>
      <button onClick={handleChatting} className="bg-main1 text-white w-10 rounded-lg flex justify-center items-center">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
          <path strokeLinecap="round" strokeLinejoin="round" d="M8.625 9.75a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0H8.25m4.125 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0H12m4.125 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0h-.375m-13.5 3.01c0 1.6 1.123 2.994 2.707 3.227 1.087.16 2.185.283 3.293.369V21l4.184-4.183a1.14 1.14 0 01.778-.332 48.294 48.294 0 005.83-.498c1.585-.233 2.708-1.626 2.708-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0012 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018z" />
        </svg>
      </button>
      <button 
        onClick={leaveSession}
        className="w-8 h-8 bg-main5 rounded-full justify-center items-center flex"
      >
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6 text-white">
          <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>
    </div>
  </div>
  )
}
