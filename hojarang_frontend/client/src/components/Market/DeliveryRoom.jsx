
import React from 'react';

export default function DeliveryRoom(roomData) {

  const { id, current_total_price, target_min_price, store_id, store_name, store_image, user_id, nickname } = roomData;
 

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
          <div>{store_name}</div>
        </div>
        <div className="flex justify-end">
        <button type="button" className="main1-full-button w-20">참여하기</button>
        </div>
      </div>
    </div>
  )
  
}