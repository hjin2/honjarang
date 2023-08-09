import { useEffect, useState } from "react"
import Rooms from "../Rooms"
import Pagination from "react-js-pagination"
import axios from "axios"
import TransactionRoom from "./TransactionRoom"

export default function TransactionList() {
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem('access_token')
  const headers = {"Authorization" : `Bearer ${token}`}
  const [transactionData, setTransactionData] = useState([])
  const [pageSize, setPageSize] = useState(1)
  const [currentPage, setCurrentPage] = useState(1)
  useEffect(()=>{
    axios.get(`${URL}/api/v1/secondhand-transactions`,{params:{page:1, size:12}, headers})
      .then((res)=>{
        console.log(res.data)
        setTransactionData(res.data)
        console.log(transactionData)
      })
      .catch((err)=>console.log(err))
  },[])

  const setPage = (error) => {
    setCurrentPage(error)
  }
  return (
    <div className="h-full">
      <div style={{height:"90%"}}>
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
