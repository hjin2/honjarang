import React from 'react';
import { Link } from 'react-router-dom';

export default function PurchaseRoom(roomData) {

  const { id, product_name, image, deadline, current_person_count, target_person_count, price } = roomData;
  

  return (
    <div >
      <div className="border w-52 h-52 p-2 ">
        {/* 이미지와 그 위 텍스트 */}
        <div className="flex justify-center relative">
          <div className="w-32 h-32">
            <img src={image} alt="상품 이미지" className="w-full h-full" />
          </div>
          <div className="absolute inset-0 flex items-end justify-end">
            <p className="text-sm font-bold">{current_person_count}/{target_person_count}</p>
          </div>
        </div>
        {/* 상품 제목 */}
        <div className="flex justify-between my-1">
          <div>{product_name}</div>
          <div>{price} </div>
        </div>
        {/* 참여버튼 */}
        <div className="flex justify-end">
          <Link to={{ pathname: `/market/purchasedetail/${id}`}}>
            <button type="button" className="main1-full-button w-20">참여하기</button>
          </Link>
        </div>
      </div>
    </div>
  )

}
