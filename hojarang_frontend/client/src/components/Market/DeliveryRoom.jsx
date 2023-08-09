
import React from 'react';
import { Link } from 'react-router-dom';

export default function DeliveryRoom(roomData) {

  const { id, current_total_price, target_min_price, store_id, store_name, store_image, user_id, nickname } = roomData;
 
  // 가게명이 길면 ... 으로 보이게
  const MAX_STORE_NAME_LENGTH = 11; 
  let adjustedStoreName = store_name;
  if (store_name.length > MAX_STORE_NAME_LENGTH) {
    adjustedStoreName = store_name.substring(0, MAX_STORE_NAME_LENGTH) + '...';
  }

  return (
    <div >
      <div className="border w-52 h-52 p-2 ">
        <div className="flex justify-center relative">
          <div className="w-32 h-32">
            <img src={store_image} alt="가게 이미지" className="w-full h-full" />
          </div>
          <div className="absolute inset-0 flex items-end justify-end">
            <p className="text-sm font-bold">{current_total_price}/{target_min_price}</p>
          </div>
        </div>
        <div className="flex justify-between my-1">
          <div>{adjustedStoreName}</div>
        </div>
        <div className="flex justify-end">
          <Link to={{ pathname: `deliverydetail/${id}` }}>
            <button type="button" className="main1-full-button w-20">참여하기</button>
          </Link>
        </div>
      </div>
    </div>
  )
}