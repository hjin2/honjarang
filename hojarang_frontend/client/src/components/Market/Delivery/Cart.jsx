import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';

export default function Cart({ selectedMenu, detail, modalState, setModalState, setIsAdd }) {
  
  const [quantity, setQuantity] = useState(1)
  const [afterPoint, setAfterPoint] = useState(0);

  useEffect(() => {
    const calculatedPoint = detail.my_point - (selectedMenu.price * quantity)
    setAfterPoint(calculatedPoint > 0 ? calculatedPoint : 0)
  }, [quantity, selectedMenu.price, detail.my_point])

  const handleIncrement = () => {
    const calculatedPoint = afterPoint - selectedMenu.price
    if(calculatedPoint >= 0){
      setAfterPoint(calculatedPoint)
      setQuantity(prevQuantity => prevQuantity + 1)
      console.log('플')
    } 
  }  
  const handleDecrement = () => {
    if(quantity > 1){
      console.log('마')
      setAfterPoint(afterPoint + selectedMenu.price)
      setQuantity(prevQuantity => prevQuantity - 1);
    }
  }

  const handleChange = (e) => {
    const inputQuantity = parseInt(e.target.value);
    if (!isNaN(inputQuantity) && inputQuantity >= 1) {
      const calculatedPoint = detail.my_point - (selectedMenu.price * inputQuantity)
      const newQuantity = calculatedPoint >= 0 ? inputQuantity : Math.floor((detail.my_point) / selectedMenu.pric)
      const newAfterPoint = calculatedPoint >= 0 ? calculatedPoint : 0;
      setAfterPoint(newAfterPoint)
      setQuantity(newQuantity);
    }
    else {
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
    if((detail.my_point - (selectedMenu.price * quantity)) >= 0){
      // 장바구니 추가
      axios.post(`${URL}/api/v1/joint-deliveries/${detail.id}/carts`, data, {headers})
      .then((res) => {
        console.log(res)
        console.log(res.data)
        setIsAdd(true)
        setModalState(false)
      })
      .catch((err) => {
        console.log(err)
      })
    }
    else{
      window.alert("포인트가 부족합니다.")
    }
  }
  
  // 이미지 없을 때 대체 이미지 넣기
  const defaultImage = '/src/assets/noimage.png';

  return (
    <div className="relative bg-white m-auto border rounded-lg space-y-5 w-6/12 p-9">
      <div className="flex flex-col items-center ">
        <h2 className="text-2xl font-semibold">장바구니</h2>
      </div>
      <div className="pt-0 p-5 flex flex-col items-center">
        <div>
          <p className="font-semibold p-1">{selectedMenu.name}</p>
          <div className="flex flex-row">
            <img src={selectedMenu.image|| defaultImage} alt={selectedMenu.name} className="w-36 h-36 mr-5 shadow-md shadow-gray3/30 p-1" />
            <div className="flex flex-col justify-around ml-3">
              {/* 가격 */}
              <div className="">
                <div>
                  <button onClick={handleDecrement} className="border px-2 font-semibold hover:border-main2 w-8 rounded-l-sm">-</button>
                    <input
                      type="number"
                      value={quantity}
                      onChange={handleChange}
                      className=" w-28 text-center border-gray1 rounded-none focus:outline-none focus:border-main2 "
                      />
                  <button onClick={handleIncrement} className="border px-2 font-semibold hover:border-main2 w-8 rounded-r-sm">+</button>
                </div>
                <div className="w-44">
                  <div className="flex justify-between">
                    <p className="mt-1">현재 가격</p>
                    <p className="mt-1">{selectedMenu.price * quantity}원</p>
                  </div>
                </div>
              </div>
              {/* 포인트 */}
              <div className="w-52">
                <div className="flex justify-between">
                  <p>보유 포인트</p> 
                  <p>{detail.my_point} P</p> 
                </div>
                <div className="flex justify-between">
                  <p>차감 후 포인트</p>
                  <p>{detail.my_point - (selectedMenu.price * quantity)} P</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="mt-5">
          <button onClick={createCart} className="main1-full-button w-36">장바구니에 담기</button>
        </div>
      </div>
    </div>
  );
}

