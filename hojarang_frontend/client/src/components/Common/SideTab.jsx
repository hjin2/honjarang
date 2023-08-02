import React, { useState } from 'react'
import Content from './Content'

export default function SideTab(props){
  const tabs = props.tabs
  const [activeTabIndex, setActiveTabIndex] = useState(0)

  return (
    <div>
      <Content
        tabs = {tabs}
        activeTabIndex = {activeTabIndex}
        setActiveTabIndex = {setActiveTabIndex}
      />
    </div>
  )
}
