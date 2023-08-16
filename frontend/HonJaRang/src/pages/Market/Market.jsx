import { useState } from 'react';
import SideTab from '@/components/Common/SideTab'
import Content from '@/components/Common/Content';
import PurchaseList from '@/components/Market/Purchase/PurchaseList';
import DeliveryList from '@/components/Market/Delivery/DeliveryList';
import TransactionList from '@/components/Market/Transaction/TransactionList';
import { Link } from 'react-router-dom';



export default function Market() {
  const [activeTabIndex, setActiveTabIndex] = useState(0)

  const tabs = [
    {
      title: '공동구매',
      content: <PurchaseList/>,
      recruit: <Link to="/market/purchase/create">모집하기</Link>,
    },
    {
      title: '공동배달',
      content: <DeliveryList/>,
      recruit: <Link to="/market/delivery/create">모집하기</Link>,
    },
    {
      title: '중고거래',
      content: <TransactionList/>,
      recruit: <Link to="/market/transaction/create">모집하기</Link>,
    },
  ]


  return (
    <div className="flex space-x-14 mx-auto h-full">
      <div className="basis-1/6 text-center">
        <SideTab 
          tabs = {tabs}
          activeTabIndex = {activeTabIndex}
          setActiveTabIndex = {setActiveTabIndex}
          />
        <button 
          className="main3-full-button w-28 mb-10"
        >
          {tabs[activeTabIndex].recruit}
        </button>
      </div>
      <div className="basis-5/6 h-full">
        <Content
          tabs = {tabs}
          activeTabIndex = {activeTabIndex}
          setActiveTabIndex = {setActiveTabIndex}
          />
      </div>
    </div>
  );
}
