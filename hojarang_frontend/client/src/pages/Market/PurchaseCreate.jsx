import axios from 'axios'
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import PurchaseMap from '../../components/Market/PurchaseMap'

export default function PurchaseCreate() {

  const [content, setContent] = useState('')
  const [deadline, setDeadline] = useState('')
  const [targetPersonCount, setTargetPersonCount] = useState('')
  const [productName, setProductName] = useState('')
  const [price, setPrice] = useState('')
  const [deliveryCharge, setDeliveryCharge] = useState('')
  const [position, setPosition] = useState()
  const [placeName, setPlaceName] = useState()

  const navigate = useNavigate()
  const token = localStorage.getItem("access_token")
  const URL = import.meta.env.VITE_APP_API
  const createPurchase = () => {
    const headers = {
      'Authorization': `Bearer ${token}`
    };
    const data = {
      content: content,
      deadline: deadline.slice(0,10)+" "+deadline.slice(11,16)+":00",
      target_person_count: Number(targetPersonCount),
      product_name: productName,
      price: Number(price),
      delivery_charge: Number(deliveryCharge),
      place_keyword : placeName,
      latitude : Number(position.lat),
      longitude : Number(position.lng),
    };
    console.log(data)
    axios.post(`${URL}/api/v1/joint-purchases`, data, {headers})
    .then((res) => {
      console.log(res.data)
      navigate(`/market/purchasedetail/${res.data}`)
    })
    .catch((err) => {
      console.log(err)
    })

  }

  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5 space-y-5 ">
      <div>
        <div>상품명</div>
        <input type="text" 
        value={productName} 
        onChange={(e) => setProductName(e.target.value)}/>
      </div>
      <div>
        <div>상품 가격</div>
        <input type="number"
        value={price}
        onChange={(e) => setPrice(e.target.value)} />
      </div>
      <div>
        <div>배송비</div>
        <input type="number"
          value={deliveryCharge}
          onChange={(e) => setDeliveryCharge(e.target.value)} />
      </div>
      <div>
        <div>목표 인원 (최대 00명까지 입력 가능)</div>
        <input type="number"
        value={targetPersonCount}
        onChange={(e) => setTargetPersonCount(e.target.value)} />
      </div>
      <div>
        <div>마감기한 (최대 30일까지 가능)</div>
        <input type="datetime-local"
        value={deadline}
        onChange={(e) => setDeadline(e.target.value)} />
      </div>
      <div>
        <div>만남의 장소 (상품 수령지)</div>
        <PurchaseMap
          placeName={placeName}
          setPlaceName = {setPlaceName}
          position={position}
          setPosition={setPosition}
        />
        {/* <input type="text" 
        value={placeKeyword}
        onChange={(e) => setPlaceKeyword(e.target.value)}/>
        <button type="button" className="main2-button w-20  ml-2">주소검색</button> */}
      </div>
      <div>
        <div>상품소개</div>
        <textarea className="resize-none border border-black h-48 w-full "
        value={content}
        onChange={(e) => setContent(e.target.value)}
        ></textarea>
      </div>
      {/* <div>
        <div>상품사진 첨부</div>
        <input type="file" />
      </div> */}
      <div>
        <button type="button" className="main1-full-button w-20" 
        onClick={createPurchase}>작성완료</button>
      </div>
    </div>
  )
}

