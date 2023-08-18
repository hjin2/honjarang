import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Cart from '../../components/Market/Delivery/Cart';
import Modal from '../../components/Common/Modal';
import axios from 'axios';
import CartList from '../../components/Market/Delivery/CartList';
import { API } from '@/apis/config';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faComments } from '@fortawesome/free-regular-svg-icons';
import DefaultMenu from '@/assets/default_menu.jpg';

export default function DeliveryDetail() {
  const navigate = useNavigate();
  const id = useParams().id;
  const token = localStorage.getItem('access_token');
  const headers = { Authorization: `Bearer ${token}` };

  const [isWriter, setIsWriter] = useState(false); // 작성자 확인
  const loginId = localStorage.getItem('user_id');
  const [detail, setDetail] = useState({}); // 가게 상세
  const [menuList, setMenuList] = useState([]); // 메뉴 리스트
  const [isAdd, setIsAdd] = useState(false);
  const [cart, setCart] = useState([]);
  const [isPurchase, setIsPurchase] = useState(false);

  const enter = () => {
    navigate(`/chatting/${detail.chat_room_id}`, {
      state: { title: `${detail.store_name} 공동배달 참여방` },
    });
  };

  useEffect(() => {
    // 공동 배달 상세
    axios
      .get(`${API.DELIVERIES}/${id}`, { headers })
      .then((res) => {
        setDetail(res.data);
        console.log(res.data);
        if (Number(loginId) === Number(res.data.user_id)) {
          setIsWriter(true);
        } else {
          setIsWriter(false);
        }
        fetchCart();
      })
      .catch((err) => {
        console.log(err);
      });
    // 메뉴 목록 조회
    axios
      .get(`${API.DELIVERIES}/${id}/menus`, { headers })
      .then((res) => {
        setMenuList(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [id]);

  // 모달 관련 상태
  const [modalState, setModalState] = useState(false); // 모달 열림, 닫힘
  const [selectedMenu, setSelectedMenu] = useState(''); // 선택된 메뉴

  // 모달 열어서 선택된 메뉴 정보 설정
  const onModalOpen = (menu) => {
    setSelectedMenu(menu);
    setModalState(true);
  };

  // 마감시간 나타내기
  const [currentTime, setCurrentTime] = useState(new Date());
  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000); // 1초마다 업데이트

    return () => {
      clearInterval(interval);
    };
  }, []);

  const deleteDelivery = () => {
    axios
      .delete(`${API.DELIVERIES}/${id}`, { headers })
      .then((res) => {
        console.log(res);
        navigate('/market');
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 수령 확인
  // const [received, setReceived] = useState(false)

  const handleCheck = () => {
    console.log(id);
    axios
      .put(`${API.DELIVERIES}/${id}/receive`, [], { headers })
      .then((res) => {
        console.log(res);
        // setReceived(true)
        window.alert('수령하셨습니다');
      })
      .catch((err) => console.log(err));
  };

  const clickUser = () => {
    navigate(`/mypage/${detail.user_id}`);
  };
  const userId = localStorage.getItem('user_id');
  const fetchCart = () => {
    axios
      .get(`${API.DELIVERIES}/${id}/carts`, { headers })
      .then((res) => {
        console.log(res.data);
        setCart(res.data);
        console.log(cart);
        for (let i = 0; i < res.data.length; i++) {
          if (res.data[i].user_id == userId) {
            setIsPurchase(true);
            break;
          }
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  useEffect(() => {
    const calculatedGroupedCart = calculateGroupedCart(cart);
    setGroupedCart(calculatedGroupedCart);
  }, [cart]);

  const [showCartList, setShowCartList] = useState(false);
  const onClick = () => {
    setShowCartList(!showCartList);
  };

  const calculateGroupedCart = (cart) => {
    return Object.entries(
      cart.reduce((acc, menu) => {
        if (!acc[menu.user_nickname]) {
          acc[menu.user_nickname] = [];
        }
        acc[menu.user_nickname].push(menu);
        return acc;
      }, {}),
    );
  };

  const [groupedCart, setGroupedCart] = useState([]);

  const deadline = new Date(detail.deadline);
  const timeDiff = deadline - currentTime;
  const days = Math.floor(timeDiff / (1000 * 60 * 60 * 24));
  const hours = Math.floor(
    (timeDiff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60),
  );
  const minutes = Math.floor((timeDiff % (1000 * 60 * 60)) / (1000 * 60));
  const seconds = Math.floor((timeDiff % (1000 * 60)) / 1000);

  return (
    <div>
      <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5 space-y-5 ">
        <div className="flex justify-between">
          <div>
            <div className="font-bold text-3xl flex items-end">
              {detail.store_name}
            </div>
          </div>
          <div>
            <div
              className="font-semibold text-right cursor-pointer"
              onClick={clickUser}
            >
              {detail.nickname}
            </div>
            <div className="text-right ">
              {detail.created_at?.slice(0, 16)}{' '}
            </div>
          </div>
        </div>
        {isPurchase && (
          <div className="space-x-2 cursor-pointer w-fit flex" onClick={enter}>
            <FontAwesomeIcon icon={faComments} style={{ color: '#008b57' }} />
            <div className="text-sm text-main1 font-bold">채팅</div>
          </div>
        )}
        <hr />
        <div className="m-2 whitespace-pre-line">{detail.content}</div>
        <hr />
        {/* 메뉴 목록 */}
        <div className="menu-container overflow-y-scroll max-h-96 border rounded-md px-10">
          {menuList.map((menu) => (
            <div
              key={menu.id}
              className="flex justify-between items-center space-x-4 my-2"
            >
              <img
                src={menu.image || DefaultMenu}
                alt={menu.name}
                className="w-16 h-16"
              />
              <div className="text-center">
                <p className="font-semibold">{menu.name}</p>
                <p>{menu.price.toLocaleString()}원</p>
              </div>
              {timeDiff > 0 ? (
                <div>
                  <button
                    onClick={() => onModalOpen(menu)}
                    className="hover:text-main2"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                      strokeWidth={1.5}
                      stroke="currentColor"
                      className="w-6 h-6"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        d="M20.25 7.5l-.625 10.632a2.25 2.25 0 01-2.247 2.118H6.622a2.25 2.25 0 01-2.247-2.118L3.75 7.5m8.25 3v6.75m0 0l-3-3m3 3l3-3M3.375 7.5h17.25c.621 0 1.125-.504 1.125-1.125v-1.5c0-.621-.504-1.125-1.125-1.125H3.375c-.621 0-1.125.504-1.125 1.125v1.5c0 .621.504 1.125 1.125 1.125z"
                      />
                    </svg>
                  </button>
                </div>
              ) : (
                ''
              )}
            </div>
          ))}
          {modalState && selectedMenu && (
            <Modal modalState={modalState} setModalState={setModalState}>
              <Cart
                selectedMenu={selectedMenu}
                detail={detail}
                modalState={modalState}
                setModalState={setModalState}
                setIsAdd={setIsAdd}
                cart={cart}
                isWriter={isWriter}
                setCart={setCart}
                setGroupedCart={setGroupedCart}
                calculateGroupedCart={calculateGroupedCart}
              />
            </Modal>
          )}
        </div>
      </div>
      <div className="border rounded-lg max-w-2xl mx-auto mt-5 mb-10 pb-3 p-5 space-y-5 flex flex-col items-center text-center">
        {timeDiff > 0 ? (
          <div>
            <div className="text-main5">
              마감까지 남은 시간 : {days}일 {hours}시간 {minutes}분 {seconds}초
            </div>
            <div className="text-main2">
              목표까지{' '}
              {detail.target_min_price - detail.current_total_price > 0
                ? (
                    detail.target_min_price - detail.current_total_price
                  ).toLocaleString()
                : 0}
              원
            </div>
          </div>
        ) : (
          <div className="space-y-5">
            <div className="text-main5 flex justify-center">모집 마감</div>
            {detail.target_min_price - detail.current_total_price <= 0 ? (
              // <button onClick={handleCheck}
              //   className={`main1-full-button w-40 ${received ? 'main3-full-button' : ''}`}
              // >{received ? '수령 완료' : '수령 확인'}
              <button onClick={handleCheck} className="main1-full-button w-40">
                수령확인
              </button>
            ) : (
              ''
            )}
          </div>
        )}
        {/* 메뉴를 담으면 장바구니 버튼 활성화 */}
        {isPurchase && (
          <button className="main2-button w-40 mt-3" onClick={onClick}>
            장바구니 목록
          </button>
        )}
        <CartList
          id={id}
          loginId={loginId}
          cart={cart}
          setCart={setCart}
          showCartList={showCartList}
          setShowCartList={setShowCartList}
          setIsPurchase={setIsPurchase}
          setGroupedCart={setGroupedCart}
          groupedCart={groupedCart}
          calculateGroupedCart={calculateGroupedCart}
        />
        {isWriter && timeDiff > 0 ? (
          <button
            className="main5-full-button w-40 mt-3"
            onClick={deleteDelivery}
          >
            모집 취소
          </button>
        ) : null}
      </div>
    </div>
  );
}
