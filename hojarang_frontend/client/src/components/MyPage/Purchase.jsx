import { useState, useEffect } from "react"
import PurchaseList from "@/components/MyPage/List/PurchaseList"
import axios from "axios"

export default function Purchase() {
	const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  const headers = {"Authorization" : `Bearer ${token}`}
	const [activeTab, setActiveTab] = useState('join-purchase')
	const [joinPageSize, setJoinPageSize] = useState(0)
	const [writePageSize, setWritePageSize] = useState(0)
	const [joinCurrentPage, setJoinCurrentPage] = useState(1)
	const [writeCurrentPage, setWriteCurrentPage] = useState(1)
	const [joinPurchaseData, setJoinPurchaseData] = useState([])
	const [writePurchaseData, setWritePurchaseData] = useState([])
	const handleTabClick = (tab) => {
		setActiveTab(tab)
	}
	useEffect(()=>{
    axios.get(`${URL}/api/v1/users/page-joined-purchase`, {params:{size:4}, headers})
      .then((res)=>{
        console.log(res.data)
        setJoinPageSize(res.data)
      })
      .catch((err)=> console.log(err))
  },[])

	useEffect(()=>{
    axios.get(`${URL}/api/v1/users/jointpurchase-participating`, {params:{page:joinCurrentPage, size:4},headers})
      .then((res) => {
        console.log(res)
        setJoinPurchaseData(res.data)
      })
      .catch((err) => console.log(err))
  },[joinCurrentPage])

  useEffect(() => {
    axios.get(`${URL}/api/v1/users/page-joint-purchase`,{params:{size:4},headers})
      .then((res) =>{
        console.log(res.data)
        setWritePageSize(res.data)
      })
      .catch((err)=>{
        console.log(err)
      })
  },[])

	useEffect(() => {
    axios.get(`${URL}/api/v1/users/jointpurchase-writer`,{params:{size:4,page:writeCurrentPage},headers})
      .then((res) => {
        console.log(res.data)
        setWritePurchaseData(res.data)
      })
      .catch((err)=>{
        console.log(err)
      })
  },[writeCurrentPage])

  return (
    <div>
			<div className="text-center space-x-5 mt-5">
				<button 
					onClick={() => handleTabClick("join-purchase")}
					className={`${activeTab === "join-purchase" ? "font-semibold" : "font-normal"}`}
					>참여 배달</button>
				<button 
					onClick={() => handleTabClick("write-purchase")}
					className={`${activeTab === "write-purchase" ? "font-semibold" : "font-normal"}`}
					>작성 배달</button>
			</div>
			<div>
				{activeTab === `join-purchase`&&(
					<PurchaseList
						pageSize={joinPageSize}
						purchaseData={joinPurchaseData}
						setCurrentPage={setJoinCurrentPage}
						currentPage={joinCurrentPage}
					/>
				)}
				{activeTab === "write-purchase"&&(
					<PurchaseList
						pageSize={writePageSize}
						purchaseData={writePurchaseData}
						setCurrentPage={setWriteCurrentPage}
						currentPage={writeCurrentPage}
					/>
				)}
			</div>
    </div>
  )
}
