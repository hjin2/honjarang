import axios from "axios";
import { useEffect, useState } from "react";
import Rooms from "../../Market/Rooms";
import DeliveryRoom from "../../Market/DeliveryRoom"
import Pagination from "react-js-pagination";


export default function DeliveryList() {
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  const headers = {"Authorization" : `Bearer ${token}`}
  const [purchaseData, setPurchaseData] = useState([])
  const [pageSize, setPageSize] = useState(0)
  const [currentPage, setCurrentPage] = useState(1)
  useEffect(()=>{
    axios.get(`${URL}/api/v1/users/page-join`, {params:{size:10}, headers})
      .then((res)=>{
        console.log(res.data)
        setPageSize(res.data)
      })
      .catch((err)=> console.log(err))
  })

  useEffect(()=>{
    axios.get(`${URL}/api/v1/users/joint-deliveries-participating`, {params:{page:currentPage, size:10},headers})
      .then((res) => {
        console.log(res)
        setPurchaseData(res.data)
      })
      .catch((err) => console.log(err))
  },[currentPage])

  const setPage = (error) =>{
    setCurrentPage(error)
  }

  return(
    <div className="h-full p-6">
      {purchaseData.length > 0 ?(
        <div className="h-full">
        <div>
          <Rooms roomsData={purchaseData} component={DeliveryRoom}/>
        </div>
        <div className="flex justify-center">
          <Pagination
            activePage={currentPage}
            itemsCountPerPage={8}
            totalItemsCount={8*pageSize}
            pageRangeDisplayed={10}
            prevPageText={"<"}
            nextPageText={">"}
            onChange={setPage}
            />
        </div>
      </div>
      ):(
        <div>참여한 공동구매가 없습니다.</div>
      )}
    </div>
  )
}
