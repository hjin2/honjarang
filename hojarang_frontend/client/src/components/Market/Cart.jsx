import React, { useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';

export default function Cart({ selectedMenu, detail, modalState, setModalState }) {
  
  const onClickCloseButton = (()=>{
    setModalState(false)
  })

  // 수량 
  const [quantity, setQuantity] = useState(1)
  // 수량 변경
  const handleQuantityChange = (e) => {
    if(!isNaN(e) && e >= 1){
      setQuantity(e)
    }
    else{
      setQuantity(1)
    }
  }
  const handleIncrement = () => {
    console.log('플')
    setQuantity(prevQuantity => prevQuantity + 1)
  }  
  const handleDecrement = () => {
    if(quantity > 1){
      console.log('마')
      setQuantity(prevQuantity => prevQuantity - 1);
    }
    else{
      setQuantity(1)
    }
  }
 

  // selectedMenu props에 대한 validation 설정
  // Cart.propTypes = {
  //   modalState: PropTypes.bool.isRequired,
  //   setModalState: PropTypes.func.isRequired,
  //   selectedMenu: PropTypes.string,
  //   selectedMenuQuantity: PropTypes.number,
  //   cartItems: PropTypes.func.isRequired,
  // };
  const token = localStorage.getItem("access_token")
  const URL = import.meta.env.VITE_APP_API

  const createCart = () => {

    const headers = {'Authorization': `Bearer ${token}`}
    const data = {
      joint_delivery_id: detail.id,
      menu_id: selectedMenu.id,
      quantity: quantity
    }
    console.log(data)
    // 장바구니 추가
    axios.post(`${URL}/api/v1/joint-deliveries/${detail.id}/carts`, data, {headers})
    .then((res) => {
      console.log(res)
    })
    .catch((err) => {
      console.log(err)
    })
  }
  
  // 이미지 없을 때 대체 이미지 넣기
  const defaultImage = '/src/assets/noimage.png';

  return (
    <div className="relative bg-white m-auto border rounded-lg space-y-5 w-6/12 ">
      <h2 className="text-2xl font-semibold mb-3">장바구니</h2>
      <img src={selectedMenu.image|| defaultImage} alt={selectedMenu.name} className="w-12 h-12" />
      {selectedMenu.name}
      <div>수량:
        <button onClick={handleDecrement} className="border p-1">-</button>
          <input
            type="number"
            value={quantity}
            onChange={(e) => {
              const newValue = parseInt(e.target.value);
              if (!isNaN(newValue)) {
                setQuantity(newValue);
              }
            }}
            className="mx-2 w-16 text-center"
          />
        <button onClick={handleIncrement} className="border p-1">+</button>
        현재가격: {selectedMenu.price * quantity}
      </div>
      <div> 가격: {selectedMenu.price}/현재포인트 {detail.my_point}</div>
      <div> <button onClick={createCart}>장바구니에 담기</button></div>
      <button onClick={onClickCloseButton}>모달 닫기</button>
    </div>
  );
}

