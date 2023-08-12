import Rooms from "../Rooms"
import DeliveryRoom from "./DeliveryRoom"
import { useEffect, useState } from "react"
import axios from "axios"
import Pagination from 'react-js-pagination'
import "../pagination.css"

export default function DeliveryList() {
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem('access_token')
  const headers = {"Authorization" : `Bearer ${token}`}
  const [deliveryData, setDeliveryData] = useState([])
  const [pageSize, setPageSize] = useState(0)
  const [currentPage, setCurrentPage] = useState(1)
  useEffect(() => {
    axios.get(`${URL}/api/v1/joint-deliveries/page`,{params:{size:12},headers})
    .then((res)=>{
      console.log(res.data)
      setPageSize(res.data)
    })
    .catch((err)=>{
      console.log(err)
    })
  },[])

  useEffect(() => {
    axios.get(`${URL}/api/v1/joint-deliveries`,{params:{page:currentPage, size:12}, headers})
    .then((res)=>{
      console.log(res.data)
      setDeliveryData(res.data)
    })
    .catch((err)=>{
      console.log(err)
    })
  },[currentPage])

  const setPage = (error) => {
    setCurrentPage(error);
  };
  return (
    <div>
      <Rooms roomsData={deliveryData} component={DeliveryRoom}/>
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
  )
}
