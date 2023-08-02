import React, { useState } from 'react'
import { Purchase } from './Purchase'
import Delivery from './Delivery'
import { Transaction } from './Transaction'
import Tab from '../MyPage/Tab'

export const MarketBar = () => {

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
    <div>
      <Tab
        tabs = {tabs}
        activeTabIndex = {activeTabIndex}
        setActiveTabIndex = {setActiveTabIndex}
      
      />
    </div>
  )
}
