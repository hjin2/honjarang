import axios from 'axios';
import React, { useEffect, useState } from 'react'

export default function CartList({id, loginId, isAdd}) {

  const token = localStorage.getItem("access_token")
  const URL = import.meta.env.VITE_APP_API
  const headers = {'Authorization': `Bearer ${token}`};

  const [cart, setCart] = useState([])
  const [showCartList, setShowCartList] = useState(false)

  useEffect(() => {
    axios.get(`${URL}/api/v1/joint-deliveries/${id}/carts`, { headers })
      .then((res) => {
        setCart(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [showCartList, id, isAdd]);

  const onClick = () => {
    setShowCartList(!showCartList);
  };
  


  const deleteMenu = (menuId) => {
    console.log(menuId)
    axios.delete(`${URL}/api/v1/joint-deliveries/${id}/carts/${menuId}`, {headers})
    .then((res) => {
      console.log(res)
      const updatedCart = cart.filter(menu => menu.id !== menuId);
      setCart(updatedCart);
    })
    .catch((err) => {
      console.log(err) 
    })
  }
  // 내가 담은 메뉴가 있는지
  const hasMyMenu = cart.some(menu => menu.user_id === Number(loginId));

  return (
    <div className="flex items-center flex-col">
      {cart.length > 0 && hasMyMenu && (
        <button className="main1-full-button w-40 mt-3" onClick={onClick}>장바구니 목록</button>)}
        {showCartList  &&  (
          <div className="flex flex-col">
            {cart.map((menu) => (
              <div key={menu.id}>
                <div className="flex flex-row justify-between my-2">
                  <div>
                    {menu.user_nickname} - {menu.menu_name} ({menu.quantity}개)
                  </div>
                  {/* 로그인 한 사용자와 메뉴를 장바구니에 담은 사용자가 같을 때 메뉴 삭제 가능 */}
                  {menu.user_id === Number(loginId) ? 
                  <div className="mx-2" onClick={() => deleteMenu(menu.id)}>
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
                      <path strokeLinecap="round" strokeLinejoin="round" d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0" />
                    </svg>
                  </div>
                  : ''
                  }
                </div>
              </div>
            ))}
          </div>
        )}
    </div>
  )
}
