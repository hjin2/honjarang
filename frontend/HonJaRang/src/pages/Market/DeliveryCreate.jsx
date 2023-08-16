import React, { useState } from 'react'
import axios from 'axios';
import Modal from '../../components/Common/Modal';
import Stores from '../../components/Market/Delivery/Stores';
import { useNavigate } from 'react-router-dom';
import { API } from '@/apis/config';

export default function DeliveryCreate() {

  const token = localStorage.getItem("access_token")
  const URL = import.meta.env.VITE_APP_API
  
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
  const [selectedStore, setSelectedStore] = useState('')
  

  // 선택한 가게 ID 저장
  const onStoreClick = (store) => {
    setSelectedStore(store)
    // setStoreId(store.id)
    setModalState(false)
    
  }

  // 선택한 가게 전달
  const handleStoreClick = (store) => {
    onStoreClick(store);
    setModalState(false)
  }

  // 금액 1,000,000원 이하
  const handleTargetMinPrice = (e) => {
    const price = e.target.value
    const newPrice = Math.min(price, 1000000)
    setTargetMinPrice(newPrice)
  }

  // 하는 중......
  // 마감시간 최대 3시간, 초과하면 값 수정하도록
  // const deadlineChange = (e) => {
  //   const selectedDeadline = new Date(e.target.value);
  //   console.log(selectedDeadline)
  //   console.log(maxDeadline)
  //   if (selectedDeadline <= maxDeadline) {
  //     const formattedDeadline = selectedDeadline.toISOString().slice(0,10) + " " + selectedDeadline.toISOString().slice(11, 16) + ":00";
  //     setDeadline(formattedDeadline);
  //     // console.log(selectedDeadline.toISOString().slice(0,10) + " " + selectedDeadline.slice(16,24))
  //   }
  //   else{
  //     setDeadline(maxDeadline);
  //   }
  // };
  

  const navigate = useNavigate()
  // axios
  const createDelivery = () => {

    const headers = {'Authorization': `Bearer ${token}`};
    const data = {
      content: content,
      store_id: selectedStore.id,
      delivery_charge: deliveryCharge,
      target_min_price: targetMinPrice,
      deadline: deadline.slice(0,10)+" "+deadline.slice(11,16)+":00"
    };

    console.log(data)
    axios.post(`${API.DELIVERIES}`, data, {headers})
      .then((res) => {
        console.log(res.data)
        navigate(`/market/deliverydetail/${res.data}`)
      })
      .catch((err) => {
        console.log(data)
        console.log(err)
      })
  };


  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5 space-y-5 ">
      <div>
        <div className="text-lg mb-1">가게명</div>  
        <input 
        type="text" onClick={onModalOpen} 
        value={selectedStore ? selectedStore.name : ''} 
        onChange={(e) => setStoreId(e.target.value)}
        className="border border-gray2 focus:outline-main2 h-8 p-1 w-60"/>
          {modalState && (
            <Modal modalState={modalState} setModalState={setModalState}>
              <Stores 
              modalState={modalState} 
              setModalState={setModalState}
              onStoreClick={handleStoreClick}/>
            </Modal>
          )}
        {/* <input type="number" value={storeId} 
        onChange={(e) => setStoreId(e.target.value)}/> */}
      </div>
      <div>
        <div className="text-lg mb-1">내용</div>
        <textarea
          value={content}
          onChange={(e) => setContent(e.target.value)}
          className="border border-gray2 rounded p-2 w-full resize-none h-48 focus:outline-main2"
          rows="4"  // 원하는 높이 조절
        ></textarea>
        {/* <input type="text" 
        value={content} 
        onChange={(e) => setContent(e.target.value)}/> */}
      </div>
      <div>
        <div className="flex flex-row mb-1">
          <p className="text-lg">최소 금액</p>
          <p className="ml-1 text-gray4 text-sm flex items-end mb-1">(1,000,000원 미만의 금액만 가능)</p>
        </div>
        <input type="number" 
        value={targetMinPrice} 
        onChange={handleTargetMinPrice}
        className="border border-gray2 focus:outline-main2 h-8 p-1 w-60"/>
      </div>
      <div>
        <div className="text-lg mb-1">배달비</div>
        <input type="number" 
        value={deliveryCharge} 
        onChange={(e) => setDeliveryCharge(e.target.value)}
        className="border border-gray2 focus:outline-main2 h-8 p-1 w-60"/>
      </div>
      <div>
        <div className="flex flex-row mb-1">
          <p className="text-lg">마감 시간</p>
          <p className="ml-1 text-gray4 text-sm flex items-end mb-1">(최대 3시간)</p>
        </div>
        <input 
        type="datetime-local" 
        value={deadline} 
        onChange={(e) => setDeadline(e.target.value)}
        className="border border-gray2 focus:outline-main2 h-8 p-1 w-60"
        />
      </div>
      <div>
        <button type="button" className="main1-full-button w-32" 
        onClick={createDelivery}>작성완료</button>
        <p className="text-gray3 text-xs mt-2">모집글 생성시 보증금 1,000포인트가 차감됩니다.</p>
      </div>
    </div>
  )
}

