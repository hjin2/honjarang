import Delivery from '../../components/Market/Delivery';
import SideTab from '../../components/Common/SideTab'
import Purchase from '../../components/Market/Purchase';
import Transaction from '../../components/Market/Transaction';
import Content from '../../components/Common/Content';
import { useState } from 'react';

export default function Market() {
  const [activeTabIndex, setActiveTabIndex] = useState(0)
  const tabs = [
    {
      title: '공동구매',
      content:  <Purchase />,
    },
    {
      title: '공동배달',
      content:  <Delivery />,
    },
    {
      title: '중고거래',
      content:  <Transaction />,
    },
  ]
  return (
    <div className="flex space-x-14 mx-auto">
      <div className="basis-1/6 text-center">
        <input type="text" />
        <SideTab 
          tabs = {tabs}
          activeTabIndex = {activeTabIndex}
          setActiveTabIndex = {setActiveTabIndex}
          />
        <button type = "button" className="main3-full-button w-28">모집하기</button>
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
