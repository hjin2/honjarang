import SideTab from "@/components/Common/SideTab";
import Content from "@/components/Common/Content";
import { useEffect, useState, useCallback } from "react";
import { Link } from "react-router-dom";
import RTC from "@/components/WebRTC/RTC"
import { useSelector } from "react-redux";
import { handleSession } from "@/redux/slice/sessionSlice";
import FreeChatList from "@/components/WebRTC/FreeChatList";

export default function WebRTC() {
  const session = useSelector((state) => state.session.session)
  const leaveSession = useCallback(() => {
    // Leave the session
    if (session) {
      session.disconnect();
    }
  })
  useEffect(()=>{
    console.log(session)
    if(session){
      leaveSession()
      handleSession(undefined)
    }
  },[session])
  const [activeTabIndex, setActiveTabIndex] = useState(0)
  const tabs = [
    {
      title : "자유",
      content : <FreeChatList/>
    },
    {
      title : "도와주세요",
      content : <RTC/>
    },
    {
      title : "혼밥/혼술",
      content : "g"
    },

    {
      title : "게임",
      content:"hi"
    }
  ]
  return(
    <div className="flex space-x-14 mx-auto">
      <div className="basis-1/6 text-center">
        <SideTab 
          tabs = {tabs}
          activeTabIndex = {activeTabIndex}
          setActiveTabIndex = {setActiveTabIndex}
          />
        <button type = "button" className="main3-full-button w-28">
          <Link to="/webrtc/create">
            화상 채팅 만들기
          </Link>
        </button>
      </div>
      <div className="basis-5/6">
        <Content
          tabs = {tabs}
          activeTabIndex = {activeTabIndex}
          setActiveTabIndex = {setActiveTabIndex}
          />
      </div>
    </div>
  );
}
