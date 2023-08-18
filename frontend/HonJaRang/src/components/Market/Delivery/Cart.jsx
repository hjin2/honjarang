import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { API } from '@/apis/config';
import DefaultMenu from '@/assets/default_menu.jpg';

export default function Cart({
  selectedMenu,
  detail,
  modalState,
  setModalState,
  setIsAdd,
  cart,
  isWriter,
}) {
  const [quantity, setQuantity] = useState(1);
  const [afterPoint, setAfterPoint] = useState(0);

  useEffect(() => {
    const calculatedPoint = detail.my_point - selectedMenu.price * quantity;
    setAfterPoint(calculatedPoint > 0 ? calculatedPoint : 0);
  }, [quantity, selectedMenu.price, detail.my_point]);

  const handleIncrement = () => {
    const calculatedPoint = afterPoint - selectedMenu.price;
    if (calculatedPoint >= 0) {
      setAfterPoint(calculatedPoint);
      setQuantity((prevQuantity) => prevQuantity + 1);
    }
  };
  const handleDecrement = () => {
    if (quantity > 1) {
      setAfterPoint(afterPoint + selectedMenu.price);
      setQuantity((prevQuantity) => prevQuantity - 1);
    }
  };

  const handleChange = (e) => {
    const inputQuantity = parseInt(e.target.value);
    if (!isNaN(inputQuantity)) {
      const calculatedPoint =
        detail.my_point - selectedMenu.price * inputQuantity;
      const newQuantity =
        calculatedPoint >= 0
          ? inputQuantity
          : Math.floor(detail.my_point / selectedMenu.price);
      const newAfterPoint = calculatedPoint >= 0 ? calculatedPoint : 0;
      setAfterPoint(newAfterPoint);
      setQuantity(newQuantity >= 1 ? newQuantity : 1);
    } else {
      setQuantity(1);
    }
  };

  // selectedMenu props에 대한 validation 설정
  // Cart.propTypes = {
  //   modalState: PropTypes.bool.isRequired,
  //   setModalState: PropTypes.func.isRequired,
  //   selectedMenu: PropTypes.string,
  //   selectedMenuQuantity: PropTypes.number,
  //   cartItems: PropTypes.func.isRequired,
  // };
  const token = localStorage.getItem('access_token');

  const createCart = () => {
    // const headers = {'Authorization': `Bearer ${token}`}
    const data = {
      joint_delivery_id: detail.id,
      menu_id: selectedMenu.id,
      quantity: quantity,
    };

    // 글 작성자면 포인트 차감 안 됨
    if (isWriter) {
      axios
        .post(`${API.DELIVERIES}/${detail.id}/carts`, data, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        })
        .then((res) => {
          console.log(cart);
          setIsAdd(true);
          setModalState(false);
        })
        .catch((err) => {
          console.log(err);
        });
    } else {
      if (detail.my_point - selectedMenu.price * quantity >= 0) {
        // 장바구니 추가
        axios
          .post(`${API.DELIVERIES}/${detail.id}/carts`, data, {
            headers: {
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json',
            },
          })
          .then((res) => {
            setIsAdd(true);
            setModalState(false);
          })
          .catch((err) => {
            console.log(err);
          });
      } else {
        window.alert('포인트가 부족합니다.');
      }
    }
  };

  return (
    <div className="relative bg-white m-auto border rounded-lg space-y-5 w-6/12 p-9">
      <div className="flex flex-col items-center ">
        <h2 className="text-2xl font-semibold">장바구니</h2>
      </div>
      <div className="pt-0 p-5 flex flex-col items-center">
        <div>
          <p className="font-semibold p-1">{selectedMenu.name}</p>
          <div className="flex flex-row">
            <img
              src={selectedMenu.image || DefaultMenu}
              alt={selectedMenu.name}
              className="w-36 h-36 mr-5 shadow-md shadow-gray3/30 p-1"
            />
            <div className="flex flex-col justify-around ml-3">
              {/* 가격 */}
              <div className="">
                <div>
                  <button
                    onClick={handleDecrement}
                    className="border px-2 font-semibold hover:border-main2 w-8 rounded-l-sm"
                  >
                    -
                  </button>
                  <input
                    type="number"
                    value={quantity}
                    onChange={handleChange}
                    className=" w-28 text-center border-gray1 rounded-none focus:outline-none focus:border-main2 "
                  />
                  <button
                    onClick={handleIncrement}
                    className="border px-2 font-semibold hover:border-main2 w-8 rounded-r-sm"
                  >
                    +
                  </button>
                </div>
                <div className="w-44">
                  <div className="flex justify-between">
                    <p className="mt-1">현재 가격</p>
                    <p className="mt-1">
                      {(selectedMenu.price * quantity).toLocaleString()}원
                    </p>
                  </div>
                </div>
              </div>
              {/* 작성자면 포인트 차감 안 됨 */}
              {isWriter ? (
                ''
              ) : (
                <div>
                  {/* 포인트 */}
                  {cart.length > 0 ? (
                    <div className="w-52">
                      <div className="flex justify-between">
                        <p>보유 포인트</p>
                        <p>{detail.my_point.toLocaleString()} P</p>
                      </div>
                      <div className="flex justify-between">
                        <p>차감 후 포인트</p>
                        <p>
                          {(
                            detail.my_point -
                            selectedMenu.price * quantity
                          ).toLocaleString()}{' '}
                          P
                        </p>
                      </div>
                    </div>
                  ) : (
                    <div className="w-52">
                      <div className="flex justify-between">
                        <p>보유 포인트</p>
                        <p>{detail.my_point.toLocaleString()} P</p>
                      </div>
                      <div className="flex justify-between">
                        <p>차감 후 포인트</p>
                        <p>
                          {(
                            detail.my_point -
                            selectedMenu.price * quantity -
                            1000
                          ).toLocaleString()}{' '}
                          P
                        </p>
                      </div>
                    </div>
                  )}
                </div>
              )}
            </div>
          </div>
        </div>
        <div className="mt-5">
          <button onClick={createCart} className="main1-full-button w-36">
            장바구니에 담기
          </button>
        </div>
        <p className="text-gray3 text-xs mt-2">
          첫 메뉴를 담을 때 선배달비 1,000 포인트가 차감됩니다.
        </p>
        <p className="text-gray3 text-xs">
          배달비의 차액은 수령확인 후 돌려드립니다.
        </p>
      </div>
    </div>
  );
}
