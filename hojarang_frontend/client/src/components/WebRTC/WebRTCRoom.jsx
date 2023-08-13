import React from 'react';
import { useNavigate } from 'react-router-dom';

export default function WebRTCRoom(roomData) {
  const navigate = useNavigate()
  const { sessionId, category } = roomData;
  const onClick = () =>{
    console.log(roomData)
    navigate(`/webrtc/${sessionId}`)
  }
  return (
    <div >
      <div className="border-2 border-gray1 p-2 rounded-lg">
        {/* 이미지와 그 위 텍스트 */}
          <div className="flex justify-center">
            <div className="h-32">
              <img alt="썸네일" className="w-full h-full" />
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
