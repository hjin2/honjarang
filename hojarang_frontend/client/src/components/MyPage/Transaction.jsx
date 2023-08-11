import { useState, useEffect } from "react"
import TransacationList from "@/components/MyPage/List/TransacationList"
import axios from "axios"

export default function Transaction() {
	const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  const headers = {"Authorization" : `Bearer ${token}`}
	const [activeTab, setActiveTab] = useState('join-delivery')
	const [joinPageSize, setJoinPageSize] = useState(1)
	const [writePageSize, setWritePageSize] = useState(1)
	const [joinCurrentPage, setJoinCurrentPage] = useState(1)
	const [writeCurrentPage, setWriteCurrentPage] = useState(1)
	const [joinTransacationData, setJoinTransacationData] = useState([])
	const [writeTransacationData, setWriteTransacationData] = useState([])
	const handleTabClick = (tab) => {
		setActiveTab(tab)
	}
	useEffect(()=>{
    axios.get(`${URL}/api/v1/users/page-joined-transaction`, {params:{size:4}, headers})
      .then((res)=>{
        console.log(res.data)
        setJoinPageSize(res.data)
      })
      .catch((err)=> console.log(err))
  },[])

	useEffect(()=>{
    axios.get(`${URL}/api/v1/users/transaction-participating`, {params:{page:joinCurrentPage, size:4},headers})
      .then((res) => {
        console.log(res)
        setJoinTransacationData(res.data)
      })
      .catch((err) => console.log(err))
  },[joinCurrentPage])

  useEffect(() => {
    axios.get(`${URL}/api/v1/users/page-transaction`,{params:{size:4},headers})
      .then((res) =>{
        console.log(res.data)
        setWritePageSize(res.data)
      })
      .catch((err)=>{
        console.log(err)
      })
  },[])

	useEffect(() => {
    axios.get(`${URL}/api/v1/users/transaction-writer`,{params:{size:4,page:writeCurrentPage},headers})
      .then((res) => {
        console.log(res.data)
        setWriteTransacationData(res.data)
      })
      .catch((err)=>{
        console.log(err)
      })
  },[writeCurrentPage])

  return (
    <div>
			<div className="text-center space-x-5 mt-5">
				<button 
					onClick={() => handleTabClick("join-delivery")}
					className={`${activeTab === "join-delivery" ? "font-semibold" : "font-normal"}`}
					>참여 중고</button>
				<button 
					onClick={() => handleTabClick("write-delivery")}
					className={`${activeTab === "write-delivery" ? "font-semibold" : "font-normal"}`}
					>작성 중고</button>
			</div>
			<div>
				{activeTab === `join-delivery`&&(
					<TransacationList
						pageSize={joinPageSize}
						transactionData={joinTransacationData}
						setCurrentPage={setJoinCurrentPage}
						currentPage={joinCurrentPage}
					/>
				)}
				{activeTab === "write-delivery"&&(
					<TransacationList
						pageSize={writePageSize}
						transactionData={writeTransacationData}
						setCurrentPage={setWriteCurrentPage}
						currentPage={writeCurrentPage}
					/>
          // <div>Write</div>
				)}
			</div>
    </div>
  )
}
