import React from 'react'

export default function Content({ tabs, activeTabIndex, setActiveTabIndex }) {
  return (
    <div className='flex space-x-14 mx-auto'>
      <ul className="basis-1/6 space-y-5">
        <input type="text" />
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
      <div className="basis-5/6">
        {tabs[activeTabIndex].content}
      </div>
    </div>
  )
}
