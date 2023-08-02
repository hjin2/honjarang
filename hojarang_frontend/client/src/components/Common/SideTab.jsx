import React, { useState } from 'react'
import Content from './Content'

export default function SideTab({ tabs, activeTabIndex, setActiveTabIndex }){

  return (
    <div className='space-x-14 mx-auto'>
      <ul className="space-y-5">
        {tabs.map((tab, index) => (
          <li
            key={index}
            onClick={() => setActiveTabIndex(index)}
            className="m-auto cursor-pointer"
          >
            <div className={`${activeTabIndex === index ? 'font-bold' : ''}`}>
              {tab.title}
            </div>
          </li>
        ))}
      </ul>
    </div>
  )
}
