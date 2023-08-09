import axios from 'axios'
import { useEffect, useState } from 'react'
import { useParams } from 'react-router'

export default function TransactionDetail() {
  const URL = import.meta.env.VITE_APP_API
  const [ detail, setDetail ] = useState({})
  const token = localStorage.getItem('access_token')
  const headers = {"Authorization" : `Bearer ${token}`}
  const {id} = useParams()

  // useEffect(()=>{
  //   axios.get(`${URL}/api/v1/secondhand-transactions/${id}`,{headers})
  //     .then((res) => {
  //       console.log(res)
  //       setDetail(res.data)
  //     })
  //     .catch((err) => console.log(err))
  // },[id])
  return (
    <div className="w-6/12 mx-auto mt-5 border rounded-lg">
      <div className="flex justify-between">
        <div className="font-bold text-3xl">{detail.title}</div>
        <div>
          <div className="font-semibold text-right">{detail.seller_nickname}</div>
          <div>{detail.created_at?.slice(0,10)}</div>
        </div>
      </div>
      <div className="flex justify-between">
        <div className="text-main5 mt-3">{detail.price?.toLocaleString()}</div>
        <butoton className="main1-full-button">1:1 채팅</butoton>
      </div>
      <hr />
      <div className="mt-2">
        <img src={detail.image} alt="상품이미지" />
        {detail.content}
      </div>
    </div>
  )
}
