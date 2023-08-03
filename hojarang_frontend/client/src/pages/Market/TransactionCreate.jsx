import React from 'react'

export default function TransactionCreate() {
  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5 space-y-5 ">
      <div>
        <div>상품명</div>
        <input type="text" />
      </div>
      <div>
        <div>상품 가격</div>
        <input type="number" />
      </div>
      <div>
        <div>상품 소개</div>
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
