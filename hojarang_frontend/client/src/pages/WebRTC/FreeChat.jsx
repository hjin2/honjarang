import UserVideoComponent from "@/components/WebRTC/UserVideoComponent"
import { useState, useRef, useCallback, useEffect } from "react"
import { OpenVidu } from "openvidu-browser";
import { createSession, createToken } from "@/components/WebRTC/Util";
import { useNavigate } from "react-router-dom";
import { useParams } from "react-router-dom";
import Chat from "@/components/WebRTC/Chat";
import axios from "axios";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faVideo } from "@fortawesome/free-solid-svg-icons";
import { faVideoSlash } from "@fortawesome/free-solid-svg-icons";
import { faMicrophone } from "@fortawesome/free-solid-svg-icons";
import { faMicrophoneSlash } from "@fortawesome/free-solid-svg-icons";
import { faComment } from "@fortawesome/free-regular-svg-icons";
import { useSelector } from "react-redux";

export default function FreeChat() {
  const [session, setSession] = useState(undefined)
  const [subscribers, setSubscribers] = useState([])
  const [currentVideoDevice, setCurrentVideoDevice] = useState(null);
  const [mySessionId, setMySessionId] = useState(useParams().sessionid)
  const [publisher, setPublisher] = useState(undefined);
  const navigate = useNavigate()
  const OV = useRef(new OpenVidu)
  const [publishVideo, setPublishVideo] = useState(true)
  const [publishAudio, setPublishAudio] = useState(true)
  const [chatMessages, setChatMessages] = useState([]);
  const [isChatOpen, setIsChatOpen] = useState(false)
  const nickname = useSelector((state) => state.userinfo.nickname)

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

  useEffect(() =>{
    joinSession()
  },[])


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

  const leaveSessionRef = useRef(leaveSession);

  useEffect(() => {
    window.addEventListener("popstate", () => {
      leaveSessionRef.current();
      window.alert("뒤로가기 누르셨습니다.")
    });
    window.addEventListener("beforeunload", () => {
      leaveSessionRef.current();
      window.alert("창닫기를 누르셨습니다.")
    });
  }, []);


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
          <Chat chatMessages={chatMessages} sendChatMessage={sendChatMessage}/>
        ):(null)}
      </div>
    <div className="flex justify-center space-x-5 mt-5">
      <button className="flex h-8 w-24 bg-main1 rounded-lg text-white justify-center items-center" onClick={toggleAudio}>
        {publishAudio === true ? (
          <div className="flex space-x-2 items-center">
            <FontAwesomeIcon icon={faMicrophoneSlash} style={{color : '#ffffff'}}/>
            <div>
              Mute
            </div>
          </div>
        ):(
          <div className="flex space-x-2 items-center">
            <FontAwesomeIcon icon={faMicrophone} style={{color : '#ffffff'}}/>
            <div>UnMute</div>
          </div>
        )}
      </button>
      <button className="flex h-8 w-32 bg-main1 rounded-lg text-white justify-center items-center" onClick={toggleVideo}>
        {publishVideo === true ? (
          <div className="flex space-x-2 items-center">
            <FontAwesomeIcon icon={faVideoSlash} style={{color:'#ffffff'}}/>
            <div>
              Stop Video
            </div>
          </div>
        ):(
          <div className="flex space-x-2 items-center">
            <FontAwesomeIcon icon={faVideo} style={{color: "#ffffff",}} />
            <div>
              Start Video
            </div>
          </div>
        )}
      </button>
      <button onClick={handleChatting} className="bg-main1 text-white w-10 rounded-full flex justify-center items-center">
        <FontAwesomeIcon icon={faComment} style={{color:"#ffffff"}}/>
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
