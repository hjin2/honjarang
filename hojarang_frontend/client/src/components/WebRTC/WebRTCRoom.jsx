import React from 'react';
import { useNavigate } from 'react-router-dom';

export default function WebRTCRoom(roomData) {
  const navigate = useNavigate()
  const { id, product_name, image, deadline, current_person_count, target_person_count, price } = roomData;
  const MAX_STORE_NAME_LENGTH = 7; 
  let adjustedProducteName = product_name;
  if (product_name?.length > MAX_STORE_NAME_LENGTH) {
    adjustedProducteName = product_name.substring(0, MAX_STORE_NAME_LENGTH) + '...';
  }
  const onClick = () =>{
    navigate(`/webrtc/${id}`)
  }
  return (
    <div >
      <div className="border-2 border-gray1 w-52 h-60 p-2 rounded-lg">
        {/* 이미지와 그 위 텍스트 */}
          <div className="flex justify-center">
            <div className="w-32 h-32">
              <img src={image} alt="썸네일" className="w-full h-full" />
            </div>
          </div>
          <div>
            <div>[카테고리] 제목</div>
            <div>1명/6명</div>
          </div>
          <button className='main1-full-button w-24' onClick={onClick}>참여하기</button>
        </div>
    </div>
  )

}
