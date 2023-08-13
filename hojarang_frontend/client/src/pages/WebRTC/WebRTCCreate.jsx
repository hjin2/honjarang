import { useState } from "react"
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSquareCheck } from "@fortawesome/free-solid-svg-icons"
import axios from "axios"
import { useNavigate } from "react-router-dom"
import { v4 as uuidv4 } from 'uuid';


export default function WebRTCCreate() {
  const uuid = uuidv4()
  const navigate = useNavigate()
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  const headers = {
    "Authorization" : `Bearer ${token}`
    // "Content-Type" : "multipart/formed-data"
  }
  const [ voiceSelect, setVoiceSelect ] = useState(false)
  const handleVoiceSelectToggle = () => {
    setVoiceSelect(!voiceSelect);
  };
  const [title, setTitle] = useState('')
  const [numPeople, setNumPeople] = useState(0)
  const [image, setImage] = useState()
  const [category, setCategory] = useState("")
  const handleTitle = (e) =>{
    setTitle(e.target.value)
  }
  const handleNumPeople = (e) =>{
    setNumPeople(e.target.value)
  }
  const handleImage = (e) => {
    if(e.target.files[0]){
      setImage(e.target.files[0])
    }
  }
  const handleCategory = (e) =>{
    console.log(e.target.value)
    setCategory(e.target.value)
  }
  const createWebRTC = () =>{
    const formData = new FormData()
    formData.append("thumbnail_image", image)
    formData.append("customSessionId", uuid)
    formData.append("title", title)
    // formData.append("numPeople", numPeople)
    formData.append("category", category)
    formData.append("onlyVoice", voiceSelect)
    // const data = {
    //   category : category,
    //   customSessionId : uuid,
    //   title : title,
    //   onlyVoice : voiceSelect,
    // }
    // console.log(data)
    axios.post(`${URL}/api/v1/video-room/sessions`, formData,{"Content-Type" : "multipart/form-data"})
      .then((res) => {
        console.log(res.data)
        navigate(`/webrtc`, {replace:true})
      })
      .catch((err) =>{
        console.log(err)
      })
  }
  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5 space-y-5 w-5/12">
      <div>
        <div>제목</div>
        <input type="text" aria-label="title" onChange={handleTitle}/>
      </div>
      <div>
        <div>참여 가능 인원(최대 8명까지 입력 가능)</div>
        <input type="text" onChange={handleNumPeople}/>
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
        <input 
          type="file"
          accept="image/jpg,image/png,image/jpeg"
          name="thumnail_image"
          onChange={handleImage} 
        />
      </div>
      <div>
        <div>카테고리</div>
        <form action="" value="카테고리">
          <select value={category} className="border" onChange={handleCategory}>
            <option value="FREE">자유</option>
            <option value="MUKBANG">혼밥/혼술</option>
            <option value="HELP">도와주세요</option>
            <option value="GAME">게임</option>
          </select>
        </form>
      </div>
      <button type="button" className='main1-full-button w-20' onClick={createWebRTC}>생성하기</button>
    </div>
  )
}
