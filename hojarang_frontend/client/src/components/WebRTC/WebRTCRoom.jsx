import { useNavigate } from 'react-router-dom';
import defaultImage from '@/assets/noimage.png'

export default function WebRTCRoom(roomData) {
  const navigate = useNavigate()
  const { id, sessionId, category, thumbnail, count, title  } = roomData;
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
              {thumbnail ? (
                <img alt="썸네일" className="h-32" src={thumbnail
                }/>
              ):(
                <img alt="썸네일" className="h-32" src={defaultImage}/>
              )}
            </div>
          </div>
          <div>
            <div>[{category}] {title}</div>
            <div>{count}명/6명</div>
          </div>
          <button className='main1-full-button w-24' onClick={onClick}>참여하기</button>
        </div>
    </div>
  )

}
