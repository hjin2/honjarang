import React, { useState } from 'react';

export default function Cart({ modalState, setModalState, selectedMenu, selectedMenuQuantity, cartItems }) {
  
  const [quantity, setQuantity] = useState(selectedMenuQuantity || 1)
  // 수량 변경
  const handleQuantityChange = (e) => {
    const newQuantity = parseInt(e.target.vlaue)
    setQuantity(newQuantity)
  }
  // 수량 저장 후 콜백
  const handleCartSubmit = () => {
    // 선택한 메뉴 아이템과 수량을 전달하여 cartItems 콜백 함수 호출
    cartItems(selectedMenu, quantity);
    
  };
  
  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 p-5 space-y-5">
      <p>클릭아 되어라</p>
      <h2 className="text-2xl font-semibold mb-3">장바구니</h2>
      {selectedMenu && (
        <div className="flex items-center space-x-4">
          <p>{selectedMenu.id}</p>
          <img src={selectedMenu.image} alt={selectedMenu.name} className="w-12 h-12" />
          <div>
            <p className="font-semibold">{selectedMenu.name}</p>
            <p>가격: {selectedMenu.price}</p>
            <input type="number" value={quantity} onChange={handleQuantityChange}
            min="1" className="border rounded-md"/>
            <button onClick={handleCartSubmit}>수량 저장</button>
          </div>
          <div>
            수량에 따른 가격
            <p>총 가격 : {selectedMenu.price * quantity}</p>
          </div>
        </div>
      )}
      <div>
        {/* <button onClick={handleCartSubmit}>구매하기</button> */}
      </div>
    </div>
  );
}

