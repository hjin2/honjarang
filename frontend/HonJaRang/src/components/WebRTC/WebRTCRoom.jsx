import { useNavigate } from 'react-router-dom';

export default function WebRTCRoom(roomData) {
  const navigate = useNavigate();
  const { id, sessionId, category, thumbnail, count, title } = roomData;
  const onClick = () => {
    console.log(roomData);
    navigate(`/webrtc/${sessionId}`);
  };

  const MAX_TITLE_LENGTH = 9;
  let adjustedTitle = title;
  if (title.length > MAX_TITLE_LENGTH) {
    adjustedTitle = title.substring(0, MAX_TITLE_LENGTH) + '...';
  }

  return (
    <div>
      <div className="border-2 border-gray1 p-2 rounded-lg">
        {/* 이미지와 그 위 텍스트 */}
        <div className="flex justify-center">
          <div className="h-32">
            <img alt="썸네일" className="h-32" src={thumbnail} />
          </div>
        </div>
        <div>
          <div className="flex flex-row">
            <p className="mr-1 font-semibold">[{category}] </p>
            <p>{adjustedTitle}</p>
          </div>
          <div className="flex flex-row">
            <p
              className={`count ${
                count >= 4
                  ? `${count >= 7 ? 'text-main5' : 'text-main2'}`
                  : 'text-black'
              }`}
            >
              {count}
            </p>
            <p>명/8명</p>
          </div>
        </div>
        <button className="main1-full-button w-24" onClick={onClick}>
          참여하기
        </button>
      </div>
    </div>
  );
}
