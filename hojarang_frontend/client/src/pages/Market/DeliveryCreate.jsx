import React, { useState } from 'react'
import axios from 'axios';
import Modal from '../../components/Common/Modal';
import Store from '../../components/Market/Store';
import { useNavigate } from 'react-router-dom';


export default function DeliveryCreate() {
  
  // 가게 목록 모달
  const [modalState, setModalState] = useState(false);
  const onModalOpen = () => {
    setModalState(!modalState);
  };

  // 입력 데이터 변수
  const [content, setContent] = useState('')
  const [storeId, setStoreId] = useState('')
  const [deliveryCharge, setDeliveryCharge] = useState('')
  const [targetMinPrice, setTargetMinPrice] = useState('')
  const [deadline, setDeadline] = useState('')

  // 선택한 가게 ID 저장
  const onStoreClick = (storeId) => {
    setStoreId(storeId)
    setModalState(false)
  }

  // 마감시간 최대 30분 값 검증 및 초과하면 값 수정하도록
  const maxDeadline = 30
  const handleChange = (e) => {
    const newDeadline = parseInt(e.target.value)
    if (newDeadline > maxDeadline) {
      setDeadline(maxDeadline)
    }else {
      setDeadline(newDeadline)
    }
  }

  const navigate = useNavigate()
  const goDeliveryDetail = () => {
    navigate('market/deliverydetail/:id')
  }
  // axios
  const createDelivery = () => {
 
    const headers = {
      'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpc2NoYXJAbmF2ZXIuY29tIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE2OTEyMDU5ODcsImV4cCI6MTY5MTIwOTU4N30.Q0pILeqA0jJXwAmGBX1xIisgsE_ya5_uxVBbMZX2HkM'
    };
    const data = {
      content: content,
      store_id: storeId,
      delivery_charge: deliveryCharge,
      target_min_price: targetMinPrice,
      deadline: deadline
    };

    axios.post('http://honjarang.kro.kr:30000/api/v1/joint-deliveries', data, {headers})
    .then((res) => {
      console.log(res.data)
      goDeliveryDetail()
    })
    .catch((err) => {
      console.log(err)
    })
  };


  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5 space-y-5 ">
      {/* <div>
        <div>가게 검색</div>
        <input type="text" />
        <div type="button" onClick={onModalOpen}>
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
            <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z" />
          </svg>
        </div>
      </div> */}

      <div>
        <div>가게</div>
        <input type="number" value={storeId} onChange={(e) => setStoreId(e.target.value)} onClick={onModalOpen}/>
          {modalState && (
            <Modal modalState={modalState} setModalState={setModalState}>
              <Store modalState={modalState} setModalState={setModalState}/>
            </Modal>
          )}
      </div>
      <div>
        <div>내용</div>
        <input type="text" 
        value={content} 
        onChange={(e) => setContent(e.target.value)}/>
      </div>
      <div>
        <div>최소금액 (1,000,000원 미만의 금액만 가능)</div>
        <input type="number" 
        value={targetMinPrice} 
        onChange={(e) => setTargetMinPrice(e.target.value)}/>
      </div>
      <div>
        <div>배달비</div>
        <input type="number" 
        value={deliveryCharge} 
        onChange={(e) => setDeliveryCharge(e.target.value)}/>
      </div>
      <div>
        <div>마감시간 (최대 30분까지 가능)</div>
        <input type="number" min="1" max="30" value={deadline} 
        onChange={handleChange}/>
      </div>
      {/* <div>
        <div>메뉴선택</div>
        <form action="" value="메뉴선택">
          <select name="" id="" className="border">
            <option value="황금올리브치킨">황금올리브치킨</option>
            <option value="양념치킨">양념치킨</option>
            <option value="자메이카치킨">자메이카치킨</option>
            <option value="황올반+양념반">황올반+양념반</option>
          </select>
        </form>
      </div> */}
      {/* <div>
        <div>장바구니</div>
        <div className="border rounded-md mx-auto">
          <div className="border-0">
            <div className="w-full flex">
              <div className="w-1/6"><img src="" alt="" />이미지</div>
              <div className="w-4/6">황금올리브치킨</div>
              <div className="w-1/6">20,000원</div>
            </div>
          </div>
          <div className="border-0">
            <div className="w-full flex">
              <div className="w-1/6"><img src="" alt="" />이미지</div>
              <div className="w-4/6">황금올리브치킨</div>
              <div className="w-1/6">20,000원</div>
            </div>
          </div>
        </div>
      </div> */}
      <div>
        <button type="button" className="main1-full-button w-20" 
        onClick={createDelivery}>작성완료</button>
      </div>
    </div>
  )
}

