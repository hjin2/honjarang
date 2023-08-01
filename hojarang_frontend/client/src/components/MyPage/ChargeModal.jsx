import { useState } from 'react';
import Refund from './Refund'
import Charge from './Charge';

export default function ChargeModal({ modalState, setModalState }) {
  const onClickCloseButton = () => {
    setModalState(!modalState);
  };
  const [activeTabIndex, setActiveTabIndex] = useState(0);
  const tabs = [
    {
      title: '포인트 결제',
      content : <Charge/>
    },
    {
      title: '포인트 환급',
      content : <Refund/>
    }
  ]
  return (
    <div className="fixed top-0 right-0 bottom-0 left-0 flex items-center justify-center">
      <div
        className="bg-gray5 opacity-70 fixed top-0 right-0 bottom-0 left-0"
        onClick={onClickCloseButton}
      ></div>
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
    </div>
  );
}
