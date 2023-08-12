import { useState, useEffect } from "react"
import DeliveryList from "@/components/MyPage/List/DeliveryList"
import axios from "axios"
import { activetabStyles, tabStyles } from "@/components/MyPage/MypageCss"


export default function Delivery() {
	const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  const headers = {"Authorization" : `Bearer ${token}`}
	const [activeTab, setActiveTab] = useState('join-delivery')
	const [joinPageSize, setJoinPageSize] = useState(0)
	const [writePageSize, setWritePageSize] = useState(0)
	const [joinCurrentPage, setJoinCurrentPage] = useState(1)
	const [writeCurrentPage, setWriteCurrentPage] = useState(1)
	const [joinDeliveryData, setJoinDeliveryData] = useState([])
	const [writeDeliveryData, setWriteDeliveryData] = useState([])
	const handleTabClick = (tab) => {
		setActiveTab(tab)
	}
	useEffect(()=>{
    axios.get(`${URL}/api/v1/users/page-join`, {params:{size:4}, headers})
      .then((res)=>{
        console.log(res.data)
        setJoinPageSize(res.data)
      })
      .catch((err)=> console.log(err))
  },[])

	useEffect(()=>{
    axios.get(`${URL}/api/v1/users/joint-deliveries-participating`, {params:{page:joinCurrentPage, size:4},headers})
      .then((res) => {
        console.log(res)
        setJoinDeliveryData(res.data)
      })
      .catch((err) => console.log(err))
  },[joinCurrentPage])

  useEffect(() => {
    axios.get(`${URL}/api/v1/users/page-writing`,{params:{size:4},headers})
      .then((res) =>{
        console.log(res.data)
        setWritePageSize(res.data)
      })
      .catch((err)=>{
        console.log(err)
      })
  },[])

	useEffect(() => {
    axios.get(`${URL}/api/v1/users/joint-deliveries-writer`,{params:{size:4,page:writeCurrentPage},headers})
      .then((res) => {
        console.log(res.data)
        setWriteDeliveryData(res.data)
      })
      .catch((err)=>{
        console.log(err)
      })
  },[writeCurrentPage])

  return (
    <div className="p-6">
			<div className="space-x-5 mb-5">
				<button 
					onClick={() => handleTabClick("join-delivery")}
					className={`${activeTab === "join-delivery" ? "font-semibold" : "font-normal"}`}
					style={
						activeTab === "join-delivery"
						? activetabStyles
						: tabStyles
					}	
				>참여 배달</button>
				<button 
					onClick={() => handleTabClick("write-delivery")}
					className={`${activeTab === "write-delivery" ? "font-semibold" : "font-normal"}`}
					style={
						activeTab === "write-delivery"
						? activetabStyles
						: tabStyles
					}
				>작성 배달</button>
			</div>
			<div>
				{activeTab === `join-delivery`&&(
					<DeliveryList
						pageSize={joinPageSize}
						deliveryData={joinDeliveryData}
						setCurrentPage={setJoinCurrentPage}
						currentPage={joinCurrentPage}
					/>
				)}
				{activeTab === "write-delivery"&&(
					<DeliveryList
						pageSize={writePageSize}
						deliveryData={writeDeliveryData}
						setCurrentPage={setWriteCurrentPage}
						currentPage={writeCurrentPage}
					/>
				)}
			</div>
    </div>
  )
}
