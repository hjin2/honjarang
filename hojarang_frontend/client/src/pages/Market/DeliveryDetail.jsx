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

  const [detail, setDetail] = useState([]); // 가게 상세
  const [menuList, setMenuList] = useState([]); // 메뉴 리스트
  const [cart, setCart] = useState([]); // 장바구니 목록
  useEffect(() => {
    // 공동 배달 상세
    axios.get(`${URL}/api/v1/joint-deliveries/${id}`,  {headers})
    .then((res) => {
      setDetail(res.data)
      console.log(res.data)
    })
    .catch((err) => {
      console.log(err)
    })
    // 메뉴 목록 조회
    axios.get(`${URL}/api/v1/joint-deliveries/${id}/menus`, {headers})
    .then((res) => {
      setMenuList(res.data)
    })
    .catch((err) => {
      console.log(err)
    })
    // 장바구니 목록 조회
    axios.get(`${URL}/api/v1/joint-deliveries/${id}/carts`, {headers})
    .then((res) => {
      setCart(res.data)
      console.log(res.data)
    })
    .catch((err) => {
      console.log(err)
    })
  }, [id]);
 

  // 모달 관련 상태
  const [modalState, setModalState] = useState(false); // 모달 열림, 닫힘
  const [selectedMenu, setSelectedMenu] = useState('') // 선택된 메뉴
  
  // 모달 열어서 선택된 메뉴 정보 설정
  const onModalOpen = (menu) => {
    setSelectedMenu(menu)
    setModalState(true)
  }

  const [showCartList, setShowCartList] = useState(false)
  // 장바구니 목록 열기
  const onCartListOpen = () => {
    setShowCartList(!showCartList)
  }

  // 이미지 없을 때 대체 이미지 넣기
  const defaultImage = '/src/assets/noimage.png';

  return (
    <div>
      <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5 space-y-5 ">
        <div className="flex justify-between">
          <div>
            <div className="font-bold text-3xl flex items-end">{detail.store_name}</div>
          </div>
          <div>
            <div className="font-semibold text-right">{detail.nickname}</div>
            <div className="text-right ">{detail.created_at} </div>
          </div>
        </div>
        <hr />
        <div className="m-2">
          {detail.content}
        </div>
        <hr />
        {/* 메뉴 목록 */}
        <div className="menu-container overflow-y-scroll max-h-96 border rounded-md px-4">
          {menuList.map((menu) => (
            <div key={menu.id} className="flex justify-between items-center space-x-4 my-2">
              <img src={menu.image || defaultImage} alt={menu.name} className="w-16 h-16" />
              <div className="text-center">
                <p className="font-semibold">{menu.name}</p>
                <p>가격: {menu.price}</p>
              </div>
              <div>
                <button onClick={() => onModalOpen(menu)}>
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M20.25 7.5l-.625 10.632a2.25 2.25 0 01-2.247 2.118H6.622a2.25 2.25 0 01-2.247-2.118L3.75 7.5m8.25 3v6.75m0 0l-3-3m3 3l3-3M3.375 7.5h17.25c.621 0 1.125-.504 1.125-1.125v-1.5c0-.621-.504-1.125-1.125-1.125H3.375c-.621 0-1.125.504-1.125 1.125v1.5c0 .621.504 1.125 1.125 1.125z" />
                  </svg>
                </button>
                {modalState && selectedMenu && (
                  <Modal modalState={modalState} setModalState={setModalState}>
                    <Cart 
                    selectedMenu={selectedMenu}
                    detail={detail}
                    modalState={modalState} setModalState={setModalState} />
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
        {cart.length > 0 && (
          <button onClick={onCartListOpen}>장바구니 목록</button>)}
          {showCartList && (
            <div>
              {cart.map((menu) => (
                <div key={menu.id}>
                  <div>
                    {menu.user_nickname} - {menu.menu_name}
                  </div>
                </div>
              ))}
            </div>
          )} 
      </div>
    </div>
  );
}


