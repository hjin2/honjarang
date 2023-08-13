import Rooms from "@/components/Market/Rooms"
import PurchaseRoom from "@/components/Market/Purchase/PurchaseRoom"
import { useEffect, useState } from "react"
import axios from "axios"
import Pagination from 'react-js-pagination'
import "../pagination.css"
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';

export default function PurchaseList() {
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem('access_token')
  const headers = {"Authorization" : `Bearer ${token}`}
  const [purchaseData, setPurchaseData] = useState([])
  const [pageSize, setPageSize] = useState(0)
  const [currentPage, setCurrentPage] = useState(1)
  const [keyword, setKeyword] = useState('')
  const fetchPurChaseData = () =>{
    axios.get(`${URL}/api/v1/joint-purchases`,{params:{page:currentPage, size:12, keyword:keyword}, headers})
    .then((res)=>{
      console.log(res.data)
      setPurchaseData(res.data)
    })
    .catch((err)=>{
      console.log(err)
    })
  }
  
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
    fetchPurChaseData()
  },[currentPage])

  const setPage = (error) => {
    setCurrentPage(error);
  };

  const search = (e) =>{
    e.preventDefault()
    fetchPurChaseData()
  }

  const handleKeyword = (e) =>{
    setKeyword(e.target.value)
  }
  return (
    <div className="h-full">
      <div className="flex justify-end mb-5">
        <form action="" className="space-x-2" onSubmit={search}>
          <input type="text" placeholder="검색어" onChange={handleKeyword}/>
          <button>
            <FontAwesomeIcon icon={faMagnifyingGlass} style={{color: "#008b28",}} />
          </button>
        </form>
      </div>
      <Rooms roomsData={purchaseData} component={PurchaseRoom}/>
      <div className="flex justify-center">
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
