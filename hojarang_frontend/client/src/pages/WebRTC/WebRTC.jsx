import SideTab from "../../components/Common/SideTab";
import Content from "../../components/Common/Content";
import { useState } from "react";
import { Link } from "react-router-dom";

export default function WebRTC() {
  const [activeTabIndex, setActiveTabIndex] = useState(0)
  const tabs = [
    {
      title : "도와주세요",
      content : "g"
    },
    {
      title : "혼밥/혼술",
      content : "g"
    },
    {
      title : "자유",
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
        <input type="text" />
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
