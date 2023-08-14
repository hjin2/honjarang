import Pagination from "react-js-pagination"
import Rooms from "@/components/Market/Rooms"
import { useEffect, useState } from "react"
import axios from "axios"
import "@/components/Market/pagination.css"
import WebRTCRoom from "@/components/WebRTC/WebRTCRoom"

export default function FreeChatList({category}) {
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem('access_token')
  const headers = {"Authorization" : `Bearer ${token}`}
  const [chatData, setChatData] = useState([])
  const [pageSize, setPageSize] = useState(0)
  const [currentPage, setCurrentPage] = useState(1)
  useEffect(() => {
    axios.get(`${URL}/api/v1`)
    .then((res) => {
      console.log(res.data)
      setPageSize(res.data)
    })
    .catch((err) => {
      console.log(err)
    })
  },[])

  useEffect(() => {
    axios.get(`${URL}/api/v1/video-room/sessions/${category}`,)
    .then((res) =>{
      console.log(res.data)
      setChatData(res.data)
    })
    .catch((err) => console.log(err))
  },[category])
  const setPage = (error) => {
    setCurrentPage(error);
  };
  return (
    <div>
      {chatData.length>0 ? (
        <div>
          <Rooms roomsData={chatData} component={WebRTCRoom}/>
          <div className="flex justify-center">
            <Pagination
              activePage={currentPage}
              itemsCountPerPage={12}
              totalItemsCount={12 * pageSize}
              pageRangeDisplayed={10}
              prevPageText={"<"}
              nextPageText={">"}
              onChange={setPage}
            />
          </div>
        </div>
      ):(
        <div>화상채팅방이 없습니다.</div>
      )}
      
    </div>
  )
}