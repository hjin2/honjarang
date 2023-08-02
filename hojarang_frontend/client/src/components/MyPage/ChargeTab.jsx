import Charge from "./Charge"
import Refund from "./Refund"
import { useState } from "react"

export default function ChargeTab({ modalState, setModalState }) {
  const [activeTabIndex, setActiveTabIndex] = useState(0)
  const tabs = [
    {
      title: '포인트 결제',
      content : <Charge modalState={modalState} setModalState={setModalState}/>
    },
    {
      title: '포인트 환급',
      content : <Refund modalState={modalState} setModalState={setModalState}/>
    }
  ]
  return (
    <div className="relative border bg-white m-auto rounded-lg w-2/6">
    <div className="m-10 space-y-4">
      <ul className="flex justify-evenly">
        {tabs.map((tab, index) => (
          <li
            key={index}
            onClick={() => setActiveTabIndex(index)}
            className='cursor-pointer'
          >
            <div className={`${activeTabIndex === index ? 'font-bold text-lg': 'text-lg'}`}>
              {tab.title}
            </div>
          </li>
        ))}
      </ul>
      <div>
        {tabs[activeTabIndex].content}
      </div>
    </div>
  </div>
  )
}
