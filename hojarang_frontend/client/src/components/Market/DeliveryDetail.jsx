import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Cart from './Cart';

function DeliveryDetail() {
  const params = useParams();
  // 공동 배달 상세
  const [detail, setDetail] = useState({
    id: 1,
    content: '맛있는 치킨',
    delivery_charge: '5000',
    current_total_price: '5000',
    target_min_price: '30000',
    deadline: '2023-08-09',
    store_id: '1234',
    store_name: 'BBQ 인동점',
    store_image: '/src/assets/panda-bear.png',
    user_id: '2',
    nickname: 'ischar',
    created_at: '2023-08-07'
  });

  // 메뉴 목록 조회
  const [menu, setMenu] = useState([
    {id: 1,
    name: '황금올리브',
    price: '20000',
    image: '/src/assets/panda-bear.png',
    store_id: 1},
    {id: 2,
    name: '반반',
    price: '22000',
    image: '/src/assets/panda-bear.png',
    store_id: 1},
    {id: 3,
    name: '자메이카',
    price: '190000',
    image: '/src/assets/panda-bear.png',
    store_id: 1},
    ])

    // 장바구니에 추가된 메뉴들
    const [cart, setCart] = useState([])

    // 장바구니에 메뉴 추가
    const addToCart = (menuItem) => {
      // 메뉴 중복 확인
      const isMenuInCart = cart.some((item) => item.id === menuItem.id)
      if(!isMenuInCart) {
        setCart([...cart, menuItem])
      }

    }

  // useEffect(() => {
  //   fetch(`http://localhost8080://api/v1/joint-delivery/${params.id}`)
  //     .then((res) => res.json())
  //     .then((data) => setDetail(data));
  // });

 
  return (
    <div>
      <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5 space-y-5 ">
        <div className="flex justify-between">
          <div>
            <div className="font-bold text-3xl flex items-end">{detail.store_name}</div>
          </div>
          <div>
            <div className="font-semibold text-right">{detail.nickname}</div>
            <div className="text-right ">{detail.created_at}</div>
          </div>
        </div>
        <hr />
        {/* 메뉴 목록 */}
        <div>
          {menu.map((menuItem) => (
            <div key={menuItem.id} className="flex justify-between items-center space-x-4">
              <img src={menuItem.image} alt={menuItem.name} className="w-16 h-16" />
              <div>
                <p className="font-semibold">{menuItem.name}</p>
                <p>가격: {menuItem.price}</p>
              </div>
              <div>
                <button onClick={() => addToCart(menuItem)}>
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M20.25 7.5l-.625 10.632a2.25 2.25 0 01-2.247 2.118H6.622a2.25 2.25 0 01-2.247-2.118L3.75 7.5m8.25 3v6.75m0 0l-3-3m3 3l3-3M3.375 7.5h17.25c.621 0 1.125-.504 1.125-1.125v-1.5c0-.621-.504-1.125-1.125-1.125H3.375c-.621 0-1.125.504-1.125 1.125v1.5c0 .621.504 1.125 1.125 1.125z" />
                  </svg>
                </button>
              </div>
            </div>
          ))}
        </div>
        

      </div>
      <div className="border rounded-lg max-w-2xl mx-auto mt-5 mb-10 pb-3 p-5 space-y-5 flex flex-col items-center">
        <p>마감일자 {detail.deadline}</p>
        <p>{detail.current_total_price}/{detail.target_min_price}</p>
        {/* 메뉴를 담으면 장바구니 버튼 활성화 */}
        {cart.length > 0 && (<button>장바구니</button>)} 
        <Cart cartItems={cart} />
      </div>
    </div>
    
  );
}

export default DeliveryDetail;
