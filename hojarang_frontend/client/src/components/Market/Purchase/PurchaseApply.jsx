import { useEffect, useState } from 'react';
import axios from 'axios';

export default function PurchaseApply({
  isAsideOpen,
  handleToggleAside,
  point,
  price,
  deliveryCharge,
  purchaseId,
  setIsPurchase,
}) {
  const [quantity, setQuantity] = useState(1); // 시작 수량을 1로 설정
  const [afterPoint, setAfterPoint] = useState(0);
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")

  useEffect(() => {
    const calculatedPoint = point - (price * quantity + deliveryCharge);
    setAfterPoint(calculatedPoint > 0 ? calculatedPoint : 0);
  }, [quantity, point, price, deliveryCharge]);

  const handleIncrement = () => {
    const calculatedPoint = afterPoint - price;
    if (calculatedPoint >= 0) {
      setAfterPoint(calculatedPoint);
      setQuantity(quantity + 1);
    }
  };

  const handleDecrement = () => {
    if (quantity > 1) {
      setQuantity(quantity - 1);
      setAfterPoint(afterPoint + price);
    }
  };

  const handleChange = (e) => {
    const inputQuantity = parseInt(e.target.value);
    if (!isNaN(inputQuantity)) {
      const calculatedPoint = point - (price * inputQuantity + deliveryCharge);
      const newQuantity = calculatedPoint >= 0 ? inputQuantity : Math.floor((point - deliveryCharge) / price);
      const newAfterPoint = calculatedPoint >= 0 ? calculatedPoint : 0;
      setAfterPoint(newAfterPoint);
      setQuantity(newQuantity >= 1 ? newQuantity : 1);
    }
  };

  const purchase = () => {
    const data = {
      joint_purchase_id : purchaseId,
      quantity : quantity,
    }
    if((point-(price * quantity + deliveryCharge))>=0){
      axios.post(`${URL}/api/v1/joint-purchases/${purchaseId}/applicants`,data,
        {
          headers:{
            "Authorization" : `Bearer ${token}`,
            "Content-Type" : "application/json"
          }
        })
        .then((res) => {
          console.log(res)
          setIsPurchase(true)
          handleToggleAside()
        })
        .catch((err) => console.log(err))
    }else{
      window.alert("포인트가 부족합니다.")
    }
  }

  return (
    <>
      {isAsideOpen && (
        <aside
          className={`${
            isAsideOpen
              ? 'fixed bottom-0 left-1/2 transform -translate-x-1/2 h-52 w-5/12 bg-white p-2 border-2'
              : 'hidden'
          }`}
        >
          <div className="flex flex-col items-center">
            <button onClick={handleToggleAside} className="my-3 hover:text-main2">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={1.5}
                stroke="currentColor"
                className="w-6 h-6"
              >
                <path strokeLinecap="round" strokeLinejoin="round" d="M19.5 8.25l-7.5 7.5-7.5-7.5" />
              </svg>
            </button>
            <div>
              <button onClick={handleDecrement} className="border px-2 font-semibold hover:border-main2 w-8 rounded-l-sm">-</button>
              <input
                type="number"
                name="quantity"
                value={quantity}
                onChange={handleChange}
                className=" w-28 text-center border-gray1 rounded-none focus:outline-none focus:border-main2 "
              />
              <button onClick={handleIncrement} className="border px-2 font-semibold hover:border-main2 w-8 rounded-r-sm">+</button>
            </div>
            <div className="my-3 w-6/12">
              <div className="flex justify-between">
                <p className="">보유 포인트</p>
                <p className="">{point}P</p>
              </div>
                {/* <p className="">차감 포인트 : {price * quantity + deliveryCharge}P</p> */}
              <div className="flex justify-between">
                <p className="">차감 후 포인트</p>
                <p className="">{afterPoint}P</p>
              </div>
            </div>
            <button type="button" className="main1-full-button w-32 mb-3" onClick={purchase}>
              구매하기
            </button>
          </div>
        </aside>
      )}
    </>
  );
}
