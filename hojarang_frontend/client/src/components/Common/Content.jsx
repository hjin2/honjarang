import React from 'react'

export default function Content({ tabs, activeTabIndex, setActiveTabIndex }) {
  return (
    <>
      {tabs[activeTabIndex].content}
    </>
  )
}
