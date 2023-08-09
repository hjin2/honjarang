import Rooms from "../Rooms"
import PurchaseRoom from "./PurchaseRoom"
import { useEffect, useState } from "react"
import axios from "axios"
import Pagination from 'react-js-pagination'
import "../pagination.css"

export default function PurchaseList() {
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem('access_token')
  const headers = {"Authorization" : `Bearer ${token}`}
  const [purchaseData, setPurchaseData] = useState([])
  const [pageSize, setPageSize] = useState(0)
  const [currentPage, setCurrentPage] = useState(1)
  useEffect(() => {
    axios.get(`${URL}/api/v1/joint-purchases/page`,{params:{size:12},headers})
    .then((res)=>{
      console.log(res.data)
      setPageSize(res.data)
    })
    .catch((err)=>{
      console.log(err)
    })
  },[])

  useEffect(() => {
    axios.get(`${URL}/api/v1/joint-purchases`,{params:{page:currentPage, size:12}, headers})
    .then((res)=>{
      console.log(res)
      setPurchaseData(res.data)
    })
    .catch((err)=>{
      console.log(err)
    })
  },[currentPage])

  const setPage = (error) => {
    setCurrentPage(error);
  };
  return (
    <div className="h-full">
      <div style={{height:"90%"}}>
        <Rooms roomsData={purchaseData} component={PurchaseRoom}/>
      </div>
      <div className="flex justify-center" style={{height:"10%"}}>
        <Pagination
          activePage={currentPage}
          itemsCountPerPage={12}
          totalItemsCount={12*pageSize}
          pageRangeDisplayed={10}
          prevPageText={"<"}
          nextPageText={">"}
          onChange={setPage}
          />
      </div>
    </div>
  )
}
