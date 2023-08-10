import React, { useState } from 'react'
import axios from 'axios';
import Modal from '../../components/Common/Modal';
import Stores from '../../components/Market/Stores';

import { useNavigate } from 'react-router-dom';


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
    setStoreId(store.id)
    setModalState(false)
  }

  // 선택한 가게 전달
  const handleStoreClick = (store) => {
    onStoreClick(store);
    setModalState(false)
  }

  // 하는 중......
  // 마감시간 최대 1시간, 초과하면 값 수정하도록
  // const maxDeadline = new Date();
  // maxDeadline.setHours(maxDeadline.getHours() + 1);
  
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
      store_id: storeId,
      delivery_charge: deliveryCharge,
      target_min_price: targetMinPrice,
      deadline: deadline.slice(0,10)+" "+deadline.slice(11,16)+":00"
    };

    console.log(data)
    axios.post(`${URL}/api/v1/joint-deliveries`, data, {headers})
      .then((res) => {
        console.log(res)
        console.log(res.data)
        navigate(`/market/deliverydetail/${res.data}`)
      })
      .catch((err) => {
        console.log(err)
      })
  };


  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5 space-y-5 ">
      <div>
        <div>가게</div>
        <input type="number" value={selectedStore ? selectedStore.id : ''} readOnly onClick={onModalOpen}/>
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
        <div> 마감시간(최대 1시간) </div>
        {/* <input type="datetime-local" value={deadline} 
        onChange={deadlineChange}/> */}
        <input type="datetime-local" value={deadline} 
        onChange={(e) => setDeadline(e.target.value)}/>
      </div>
      <div>
        <button type="button" className="main1-full-button w-20" 
        onClick={createDelivery}>작성완료</button>
      </div>
    </div>
  )
}

