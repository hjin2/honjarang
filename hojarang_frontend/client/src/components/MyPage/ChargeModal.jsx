import React, { useState } from 'react';
import { Link } from 'react-router-dom';

export default function ChargeModal({ modalState, setModalState }) {
  const onClickCloseButton = () => {
    setModalState(!modalState);
  };
  const [activeRadio, setActiveRadio] = useState('5000');
  const handleClickRadioButton = (e) => {
    console.log(e.target.value);
    setActiveRadio(e.target.value);
  };
  const tabs = [
    {
      title: '5,000P',
      price: '5000',
    },
    {
      title: '10,000P',
      price: '10000',
    },
    {
      title: '30,000P',
      price: '30000',
    },
    {
      title: '50,000P',
      price: '50000',
    },
  ];
  return (
    <div className="fixed top-0 right-0 bottom-0 left-0 flex items-center justify-center">
      <div
        className="bg-gray5 opacity-70 fixed top-0 right-0 bottom-0 left-0"
        onClick={onClickCloseButton}
      ></div>
      <div className="relative border bg-white m-auto rounded-lg w-2/6">
        <div className="m-4">
          <div className="flex justify-between">
            <div>포인트 결제</div>
            <div>포인트 환급</div>
          </div>
          {tabs.map((tab, index) => {
            return (
              <div className="flex items-center m" key={index}>
                <input
                  type="radio"
                  className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500"
                  value={tab.price}
                  checked={activeRadio === `${tab.price}`}
                  onChange={handleClickRadioButton}
                />
                <label
                  htmlFor={`radio-${index}`}
                  className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                >
                  {tab.title}
                </label>
              </div>
            );
          })}
          <hr />
          <div>현재 포인트 결제할 포인트 결제 후 포인트</div>
          <Link
            to="/checkout"
            state={{
              price: { activeRadio },
              nickName: '김싸피',
            }}
          >
            <button className="main1-button w-24">결제하기</button>
          </Link>
        </div>
      </div>
    </div>
  );
}
