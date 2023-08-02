import { useState } from "react"
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSquareCheck } from "@fortawesome/free-solid-svg-icons"

export default function WebRTCCreate() {
  const [ voiceSelect, setVoiceSelect ] = useState(true)
  const handleVoiceSelectToggle = () => {
    setVoiceSelect(!voiceSelect);
  };

  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5 space-y-5 w-5/12">
      <div>
        <div>제목</div>
        <input type="text" aria-label="category"/>
      </div>
      <div>
        <div>참여 가능 인원(최대 8명까지 입력 가능)</div>
        <input type="text" />
      </div>
      <div className="flex bg-main4 justify-between p-5 rounded-lg">
        <div>
          <div className="font-bold text-lg">only voice</div>
          <div className="text-sm">체크하면 음성만 on으로 방이 생성됩니다.</div>
        </div>
        <div className="my-auto">
        <button
            type="button"
            onClick={handleVoiceSelectToggle}
            className={voiceSelect ? "" : "w-4 h-4 rounded-sm bg-white border-blue"}
          >
            {voiceSelect ? (
              <FontAwesomeIcon icon={faSquareCheck} style={{ color: "#008b57" }} size="lg"/>
            ) : null}
          </button>
        </div>
      </div>
      <div>
        <div>썸네일 설정</div>
        <input type="file" />
      </div>
      <div>
        <div>카테고리</div>
        <form action="" value="카테고리">
          <select name="" id="" className="border">
            <option value="자유">자유</option>
            <option value="혼밥/혼술">혼밥/혼술</option>
            <option value="도와주세요">도와주세요</option>
            <option value="게임">게임</option>
          </select>
        </form>
      </div>
      <button type="button" className='main1-full-button w-20'>생성하기</button>
    </div>
  )
}
