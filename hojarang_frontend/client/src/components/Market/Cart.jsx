import React from 'react';

export default function Cart({ cartItems }) {
  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 p-5 space-y-5">
      <h2 className="text-2xl font-semibold mb-3">장바구니</h2>
      {cartItems.map((item) => (
        <div key={item.id} className="flex items-center space-x-4">
          <img src={item.image} alt={item.name} className="w-12 h-12" />
          <div>
            <p className="font-semibold">{item.name}</p>
            <p>가격: {item.price}</p>
          </div>
          <div>
            수량
          </div>
        </div>
      ))}
      <div>
        <button>구매하기</button>
      </div>
    </div>
  );
}

