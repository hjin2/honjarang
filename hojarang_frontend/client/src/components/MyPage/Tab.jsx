import React from 'react';
import Highlight from '@/assets/Highlight.png'

const Tab = ({ tabs, activeTabIndex, setActiveTabIndex }) => {
  const activetabStyles = {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundImage: `url(${Highlight})`, // 배경 이미지 경로 설정
    backgroundSize: 'cover',
    minHeight: '80px', // 원하는 높이 조정
    padding: '10px',
    cursor: 'pointer',
    transition: 'background-color 0.3s',
    fontWeight : "900"
  };
  const tabStyles = {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    minHeight: '80px', // 원하는 높이 조정
    padding: '10px',
    cursor: 'pointer',
    transition: 'background-color 0.3s',
    color : "#888888",
    fontWeight : "bold"
  }


  return (
    <div className="flex space-x-14 mx-auto">
      <ul className=" basis-1/6 grid" style={{ height: '400px' }}>
        {tabs.map((tab, index) => (
          <li
            key={index}
            onClick={() => setActiveTabIndex(index)}
            style={
              activeTabIndex === index
                ? { ...activetabStyles, fontWeight: 'bold' }
                : tabStyles
            }
          >
            {tab.title}
          </li>
        ))}
      </ul>
      <div
        className="border-2 border-main2 rounded-lg basis-5/6"
        style={{ minHeight: '400px'}}
      >
        {tabs[activeTabIndex].content}
      </div>
    </div>
  );
};

export default Tab;
