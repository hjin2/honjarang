import React from 'react'

export default function Content({ tabs, activeTabIndex, setActiveTabIndex }) {
  return (
    <div>
      {tabs[activeTabIndex].content}
    </div>
  )
}
