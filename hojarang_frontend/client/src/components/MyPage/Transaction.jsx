import { useState, useEffect } from "react"
import TransacationList from "@/components/MyPage/List/TransacationList"
import axios from "axios"
import { activetabStyles, tabStyles } from "@/components/MyPage/MypageCss"

export default function Transaction() {
	const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  const headers = {"Authorization" : `Bearer ${token}`}
	const [activeTab, setActiveTab] = useState('join-transaction')
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
        console.log(res.data)
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
    <div className="p-6">
			<div className="space-x-5 mb-5">
				<button 
					onClick={() => handleTabClick("join-transaction")}
					className={`${activeTab === "join-transaction" ? "font-semibold" : "font-normal"}`}
          style={
            activeTab === "join-transaction"
            ? activetabStyles
            : tabStyles
          }
					>참여 중고</button>
				<button 
					onClick={() => handleTabClick("write-transaction")}
					className={`${activeTab === "write-transaction" ? "font-semibold" : "font-normal"}`}
          style={
            activeTab === "write-transaction"
            ? activetabStyles
            : tabStyles
          }
					>작성 중고</button>
			</div>
			<div className="">
				{activeTab === `join-transaction`&&(
					<TransacationList
						pageSize={joinPageSize}
						transactionData={joinTransacationData}
						setCurrentPage={setJoinCurrentPage}
						currentPage={joinCurrentPage}
					/>
				)}
				{activeTab === "write-transaction"&&(
					<TransacationList
						pageSize={writePageSize}
						transactionData={writeTransacationData}
						setCurrentPage={setWriteCurrentPage}
						currentPage={writeCurrentPage}
					/>
				)}
			</div>
    </div>
  )
}
