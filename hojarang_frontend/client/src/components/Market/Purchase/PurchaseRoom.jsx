import React from 'react';
import { Link } from 'react-router-dom';

export default function PurchaseRoom(roomData) {

  const { id, product_name, image, deadline, current_person_count, target_person_count, price } = roomData;
  const MAX_STORE_NAME_LENGTH = 7; 
  let adjustedProducteName = product_name;
  if (product_name.length > MAX_STORE_NAME_LENGTH) {
    adjustedProducteName = product_name.substring(0, MAX_STORE_NAME_LENGTH) + '...';
  }

  return (
    <div >
      <div className="border-2 border-gray1 w-52 h-60 p-2 rounded-lg">
        {/* 이미지와 그 위 텍스트 */}
        <div className="flex justify-center relative">
          <div className="w-32 h-32">
            <img src={image} alt="상품 이미지" className="w-full h-full" />
          </div>
        </div>
        {/* 상품 제목 */}
        <div className="flex justify-between my-1">
          <div className='font-bold text-lg'>{adjustedProducteName}</div>
          <div className="text-sm">{price.toLocaleString()}원</div>
        </div>
        <div className="text-sm text-main5">목표까지 {target_person_count-current_person_count}명</div>
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
