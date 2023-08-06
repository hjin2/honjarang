// import React from 'react'

// export default function PurchaseApply() {
//   c
//   return (
//     <div>


//     </div>
//   )
// }

import React from 'react';


const PurchaseApply = ({
  isAsideOpen,
  handleToggleAside,
  quantity,
  handleIncrement,
  handleDecrement,
  handleChange,
  detail,
}) => {
  return (
    <>
      {isAsideOpen && (
        <aside
          className={`${
            isAsideOpen
              ? 'fixed bottom-0 left-1/2 transform -translate-x-1/2 h-48 w-4/5 bg-white border p-4 rounded-t-lg'
              : 'hidden'
          }`}
        >
          <div className="flex flex-col items-center">
            <button onClick={handleToggleAside}>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={1.5}
                stroke="currentColor"
                className="w-6 h-6"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M19.5 8.25l-7.5 7.5-7.5-7.5"
                />
              </svg>
            </button>
            <div>
              <button onClick={handleDecrement} data-quantity="minus">
                -
              </button>
              <input
                type="number"
                name="quantity"
                value={quantity}
                onChange={handleChange}
              />
              <button onClick={handleIncrement} data-quantity="plus">
                +
              </button>
            </div>
            <h2 className="">개수</h2>
            <p className="">현재 포인트</p>
            <p className="">차감 포인트</p>
            <p className="">차감 후 포인트</p>
            <button type="button" className="main1-full-button w-20">
              구매하기
            </button>
          </div>
        </aside>
      )}
    </>
  );
};

export default PurchaseApply;

