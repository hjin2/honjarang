import React from 'react'

export default function TransactionRoom(roomData) {

  const {id, file_url, is_complete, title, price, user_id, buyer_id, created_at} = roomData;
  return (
    <div >
      <div className="border w-52 h-52 p-2 ">
        {/* 이미지와 그 위 텍스트 */}
        <div className="flex justify-center relative">
          <div className="w-32 h-32">
            <img src={file_url} alt="상품 이미지" className="w-full h-full" />
          </div>
          <div className="absolute inset-0 flex items-end justify-end">
            <p className="text-sm font-bold">{is_complete}</p>
          </div>
        </div>
        {/* 상품 제목 */}
        <div className="flex justify-between my-1">
          <div>{title}</div>
          <div>{price} </div>
        </div>
        {/* 참여버튼 */}
        <div className="flex justify-end">
        <button type="button" className="main1-full-button w-20">채팅하기</button>
        </div>
      </div>
    </div>
  )
}
