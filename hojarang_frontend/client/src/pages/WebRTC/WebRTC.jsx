import SideTab from "@/components/Common/SideTab";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import ChatList from "@/components/WebRTC/ChatList";
import { useSelector } from "react-redux";
import { deleteSession } from "@/redux/slice/SessionSlices";

export default function WebRTC() {
  
  const [category, setCategory] = useState("free")
  const [activeTabIndex, setActiveTabIndex] = useState(0)
  const session = useSelector((state) => state.session.session)
  
  useEffect(()=>{
    console.log(session)
    if(session){
      session.disconnect()
      deleteSession()
    }
    setCategory(tabs[activeTabIndex].category)
    console.log(tabs[activeTabIndex].category)
  },[activeTabIndex])

  const tabs = [
    {
      title : "자유",
      category : "free"
    },
    {
      title : "도와주세요",
      category : "help"
    },
    {
      title : "혼밥/혼술",
      category : "honbobsul"
    },

    {
      title : "게임",
      category : "game"
    },
    {
      title : "스터디",
      category : "study"
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
        <ChatList category={category}/>
      </div>
    </div>
  );
}
