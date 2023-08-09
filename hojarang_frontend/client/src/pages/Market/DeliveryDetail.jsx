import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Cart from '../../components/Market/Cart';
import Modal from '../../components/Common/Modal';
import axios from 'axios';

export default function DeliveryDetail() {

  const {id} = useParams();
  const token = localStorage.getItem("access_token")
  const URL = import.meta.env.VITE_APP_API
  const headers = {'Authorization': `Bearer ${token}`};

  // 공동 배달 상세
  const [detail, setDetail] = useState([]);
  useEffect(() => {
    axios.get(`${URL}/api/v1/joint-deliveries/${id}`,  {headers})
    .then((res) => {
      setDetail(res.data)
      console.log(res.data)
    })
    .catch((err) => {
      console.log(err)
    })
  }, [id]);

  // 메뉴 목록 조회
  const [menu, setMenu] = useState([]);
  useEffect(() => {
    axios.get(`${URL}/api/v1/joint-deliveries/${id}/menus`, {headers})
    .then((res) => {
      setMenu(res.data)
    })
    .catch((err) => {
      console.log(err)
    })
  })

  // if (!detail) {
  //   return <div>Loading</div>
  // }

  // 장바구니에 추가된 메뉴들
  const [cart, setCart] = useState([])

  // 메뉴 담기
  const addToCart = (menuItem) => {
    // 메뉴 중복 확인
    const isMenuInCart = cart.some((item) => item.id === menuItem.id)
    if(!isMenuInCart) {
      setCart([...cart, menuItem])
    }
  }
  
  const [selectedMenu, setSelectedMenu] = useState('')
  const [selectedMenuQuantity, setSelectedMenuQuantity] = useState(1);
  
  // 장바구니에 메뉴 담아서 보여주는 모달
  const [modalState, setModalState] = useState('');
  const onModalOpen = (menuItem) => {
    // addToCart(menuItem)
    setSelectedMenu(menuItem)
    setModalState(!modalState);
    console.log(menuItem)
  };

  // 모달 내부에서 선택된 메뉴와 수량을 조정하고, 
  // 수량 저장 버튼을 클릭하면 해당 정보가 menuInCart 함수를 통해 전달
  const menuInCart = (quantity) => {
    // setSelectedMenu(cart)
    setSelectedMenuQuantity(quantity);
    setModalState(false)
  }


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
        <div className="m-2">
          {detail.content}
        </div>
        <hr />
        {/* 메뉴 목록 */}
        <div className="menu-container overflow-y-scroll max-h-96 border rounded-md px-4">
          {menu.map((menuItem) => (
            <div key={menuItem.id} className="flex justify-between items-center space-x-4 my-2">
              <img src={menuItem.image} alt={menuItem.name} className="w-16 h-16" />
              <div className="text-center">
                <p className="font-semibold">{menuItem.name}</p>
                <p>가격: {menuItem.price} {menuItem.id}</p>
              </div>
              <div>
                {/* <button onClick={() => addToCart(menuItem)}> */}
                <button onClick={() => onModalOpen(menuItem)}>
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M20.25 7.5l-.625 10.632a2.25 2.25 0 01-2.247 2.118H6.622a2.25 2.25 0 01-2.247-2.118L3.75 7.5m8.25 3v6.75m0 0l-3-3m3 3l3-3M3.375 7.5h17.25c.621 0 1.125-.504 1.125-1.125v-1.5c0-.621-.504-1.125-1.125-1.125H3.375c-.621 0-1.125.504-1.125 1.125v1.5c0 .621.504 1.125 1.125 1.125z" />
                  </svg>
                </button>
                {modalState &&  (
                  <Modal modalState={modalState} setModalState={setModalState}>
                    <Cart 
                    modalState={modalState} 
                    setModalState={setModalState} 
                    selectedMenu={selectedMenu}
                    selectedMenuQuantity={selectedMenuQuantity}
                    cartItems={(quantity) => menuInCart(quantity)} />
                  </Modal>
                )}
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
        {/* <Cart cartItems={cart} /> */}
      </div>
    </div>
    
  );
}


