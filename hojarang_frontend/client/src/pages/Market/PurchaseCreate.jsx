import React from 'react'

export default function PurchaseCreate() {
  
  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5 space-y-5 ">
      <div>
        <div>상품명</div>
        <input type="text" />
      </div>
      <div>
        <div>상품 가격 (배송비 포함 가격을 입력해 주세요.)</div>
        <input type="text" />
      </div>
      <div>
        <div>목표 인원 (최대 00명까지 입력 가능)</div>
        <input type="number" />
      </div>
      <div>
        <div>마감기한 (최대 30일까지 가능)</div>
        <input type="date" />
      </div>
      <div>
        <div>만남의 장소 (상품 수령지)</div>
        <input type="text" />
        <button type="button" className="main2-button w-20  ml-2">주소검색</button>
      </div>
      <div>
        <div>상품소개</div>
        <textarea className="resize-none border border-black h-48 w-full "></textarea>
      </div>
      <div>
        <div>상품사진 첨부</div>
        <input type="file" />
      </div>
      <div>
        <button type="button" className="main1-full-button w-20">작성완료</button>
      </div>
    </div>
  )
}

