import { useState } from "react";
import ArticlesList from "./ArticlesList";
import MarKetList from "./MarketList"
import { Link } from "react-router-dom"

const Tab = ({ tabs, activeTabIndex, setActiveTabIndex }) => {
  return (
    <div className="flex space-x-10 h-96">
      <ul className="border-2 border-main1 rounded-lg basis-1/6 grid">
        {tabs.map((tab, index) => (
          <li
            key={index}
            onClick={() => setActiveTabIndex(index)}
            className="m-auto content-evenly cursor-pointer"
          >
            <div className={`${
              activeTabIndex === index ? "font-bold" : ""
            }`}>
              {tab.title}
            </div>
          </li>
        ))}
      </ul>
      <div className="border border-main1 rounded-lg basis-5/6">{tabs[activeTabIndex].content}</div>
    </div>
  );
};

function SideBar() {
  const [activeTabIndex, setActiveTabIndex] = useState(0);

  const tabs = [
    {
      title: "작성글 목록",
      content: <ArticlesList/>,
    },
    {
      title: "공동구매 목록",
      content: <MarKetList/>,
    },
    {
      title: <Link to='/chatting'>채팅 확인하기</Link>,
    },
    {
      title : "포인트 충전 / 환급"
    }
  ];

  return (
    <div className="container mx-auto p-4">
      <Tab
        tabs={tabs}
        activeTabIndex={activeTabIndex}
        setActiveTabIndex={setActiveTabIndex}
      />
    </div>
  );
}

export default SideBar;
