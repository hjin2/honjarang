import axios from 'axios'
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function PurchaseCreate() {

  const [content, setContent] = useState('')
  const [deadline, setDeadline] = useState('')
  const [targetPersonCount, setTargetPersonCount] = useState('')
  const [productName, setProductName] = useState('')
  const [price, setPrice] = useState('')
  const [deliveryCharge, setDeliveryCharge] = useState('')
  const [placeKeyword, setPlaceKeyword] = useState('')
  
  const navigate = useNavigate()
  const goPurchaseDetail = () => {
    navigate('/market/purchasedetail/:id')
  }

  const createPurchase = () => {
    const headers = {
      'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpc2NoYXJAbmF2ZXIuY29tIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE2OTEyMDU5ODcsImV4cCI6MTY5MTIwOTU4N30.Q0pILeqA0jJXwAmGBX1xIisgsE_ya5_uxVBbMZX2HkM'
    };
    const data = {
      content: content,
      deadline: deadline,
      target_person_count: targetPersonCount,
      product_name: productName,
      price: price,
      delivery_charge: deliveryCharge,
      place_keyword: placeKeyword
    };

    axios.post('http://honjarang.kro.kr:30000/api/v1/joint-purchases', data, {headers})
    .then((res) => {
      goPurchaseDetail()
      console.log(res.data)
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
        <input type="text"
        value={price}
        onChange={(e) => setPrice(e.target.value)} />
      </div>
      <div>
        <div>배송비</div>
        <input type="text"
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
        <input type="date"
        value={deadline}
        onChange={(e) => setDeadline(e.target.value)} />
      </div>
      <div>
        <div>만남의 장소 (상품 수령지)</div>
        <input type="text" 
        value={placeKeyword}
        onChange={(e) => setPlaceKeyword(e.target.value)}/>
        <button type="button" className="main2-button w-20 ml-2">주소검색</button>
      </div>
      <div>
        <div>상품소개</div>
        <textarea className="resize-none border border-black h-48 w-full "
        value={content}
        onChange={(e) => setContent(e.target.value)}
        ></textarea>
      </div>
      <div>
        <div>상품사진 첨부</div>
        <input type="file" />
      </div>
      <div>
        <button type="button" className="main1-full-button w-20" 
        onClick={createPurchase}>작성완료</button>
      </div>
    </div>
  )
}

