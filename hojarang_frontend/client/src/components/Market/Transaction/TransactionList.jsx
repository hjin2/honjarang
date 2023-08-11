import { useEffect, useState } from "react"
import Rooms from "../Rooms"
import Pagination from "react-js-pagination"
import axios from "axios"
import TransactionRoom from "./TransactionRoom"
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';

export default function TransactionList() {
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem('access_token')
  const headers = {"Authorization" : `Bearer ${token}`}
  const [transactionData, setTransactionData] = useState([])
  const [pageSize, setPageSize] = useState(1)
  const [currentPage, setCurrentPage] = useState(1)
  const [keyword, setKeyword] = useState('')

  const fetchTransacationData = () =>{
    axios.get(`${URL}/api/v1/secondhand-transactions`,{params:{page:currentPage, size:12, keyword: keyword}, headers})
    .then((res)=>{
      console.log(res.data)
      setTransactionData(res.data)
    })
    .catch((err)=>console.log(err))
  }

  useEffect(() => {
    axios.get(`${URL}/api/v1/secondhand-transactions/page`,{params:{size:12}, headers})
      .then((res) => {
        console.log(res.data)
        setPageSize(res.data)
      })
      .catch((err) => console.log(err))
  },[])

  useEffect(()=>{
    fetchTransacationData()
  },[currentPage])

  const setPage = (error) => {
    setCurrentPage(error)
  }

  const search = (e) =>{
    e.preventDefault()
    fetchTransacationData()
  }

  const handleKeyword = (e) =>{
    setKeyword(e.target.value)
  }

  return (
    <div className="h-full">
      <div>
        <div className="flex justify-end">
          <form action="" className="space-x-2" onSubmit={search}>
            <input type="text" placeholder="검색어" onChange={handleKeyword}/>
            <button>
              <FontAwesomeIcon icon={faMagnifyingGlass} style={{color: "#008b28",}} />
            </button>
          </form>
        </div>
        <Rooms roomsData={transactionData} component={TransactionRoom}/>
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
